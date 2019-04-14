package io.github.liamtuan.semicon.sim;


import com.sun.javaws.exceptions.InvalidArgumentException;
import io.github.liamtuan.semicon.sim.core.Analyser;
import io.github.liamtuan.semicon.sim.core.Node;
import io.github.liamtuan.semicon.sim.core.Processor;
import io.github.liamtuan.semicon.sim.UnitInput;
import io.github.liamtuan.semicon.sim.UnitOutput;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

public class Circuit{
    private DataTable data;
    private int debug_level;
    private StateListener output_listender;
    private StateListener output_cell_notifier;
    private Set<Cell> changedoutputs;

    public void setDebugLevel(int i){
        debug_level = i;
    }

    public Circuit(){
        data = new DataTable();
        debug_level = 0;
        changedoutputs = new HashSet<>();
        output_listender = new StateListener() {
            @Override
            public void onStateChanged(Cell cell, boolean state) {
                changedoutputs.add(cell);
            }
        };
        output_cell_notifier = null;
    }

    public void setOutputNotifier(StateListener notifier){
        output_cell_notifier = notifier;
    }

    public void add(Unit unit){
        if(debug_level >= 1)
            System.out.println("add " + unit);

        Cell pos = unit.getPos();
        data.putUnit(pos, unit);

        // check neighbors
        Set<Dir> faces = unit.getFaces();
        for(Dir face : faces){
            Node node = unit.getNode(face);

            Node neighbor_node = neighborNode(pos, face);
            if(node == null || neighbor_node == null || node == neighbor_node)
                continue;

            if(debug_level >= 2)
                System.out.println("merge " + node + " to " + neighbor_node);
            neighbor_node.merge(node);
            data.replaceNode(node, neighbor_node);
        }

        if(unit instanceof UnitOutput){
            UnitOutput output = (UnitOutput)unit;
            output.setListener(output_listender);
        }

        evaluate(unit.getNodeSet());
    }

    public void remove(Cell cell){
        if(debug_level >= 1){
            System.out.println("remove " + data.getUnit(cell));
        }
        Unit unit = data.getUnit(cell);
        if(unit instanceof UnitOutput){
            UnitOutput output = (UnitOutput)unit;
            output.setListener(null);
        }
        Set<Node> changednodes = unit.getNodeSet();
        if(unit instanceof UnitWire){
            changednodes = removeWire((UnitWire)unit);
            if(debug_level >= 2){
                String s = "";
                for(Node node : changednodes)
                    s += node + ",";
                System.out.println("break wire and generate [" + s + "]");
            }
        }else{
            data.removeCell(cell);
        }

        unit.dettach();
        evaluate(changednodes);
    }

    public void setInpuState(Cell cell, boolean state){
        UnitInput input = (UnitInput)data.getUnit(cell);
        if(debug_level >= 1)
            System.out.println("set input state " + input + " to " + state);
        input.setState(state);
        Set<Node> changednodes = input.getNodeSet();
        evaluate(changednodes);
    }

    public boolean getOutputState(Cell cell){
        UnitOutput output = (UnitOutput)data.getUnit(cell);
        if(debug_level >= 1)
            System.out.println("get output state " + output + " " + output.getState());
        return output.getState();
    }


    private Node neighborNode(Cell pos, Dir dir){
        Unit unit = data.getUnit(pos);
        if(unit == null)
            return null;
        Node node = unit.getNodes().get(dir);
        if(node == null)
            return null;
        Unit neighbor = data.getUnit(dir.offset(pos));
        if(neighbor == null)
            return null;
        Node neighbor_node = neighbor.getNodes().get(dir.opposite());
        return neighbor_node;
    }

    private Set<Node> updateNodes(Set<Node> nodes){
        Set<Node> extendnodes = new HashSet<>();
        for(Node node : nodes) {
            node.setState(false);
            Set<Unit> units = data.getNodeUnits(node);
            for(Unit unit : units){
                if(unit instanceof UnitInput){
                    // if node linked to input unit, sync input state to node
                    ((UnitInput)unit).syncStateToNode();
                }else if(unit instanceof UnitGate){
                    // if node linked to gate output, add gate input to evaluate queue
                    UnitGate gate = (UnitGate)unit;
                    if(gate.getOutputNodes().contains(node))
                        extendnodes.addAll(gate.getInputNodes());
                }
            }
            extendnodes.add(node);
            node.invokeListener();
        }
        return extendnodes;
    }

    private void evaluate(Set<Node> nodes){
        changedoutputs.clear();

        nodes = updateNodes(nodes);

        Node[] nodearr = nodes.toArray(new Node[0]);
        Processor p = new Processor();
        p.eval(nodearr);

        if(output_cell_notifier != null){
            for(Cell changed : changedoutputs){
                UnitOutput output = (UnitOutput)data.getUnit(changed);
                output_cell_notifier.onStateChanged(changed, output.getState());
            }
        }

        if(debug_level >= 2){
            String s = "evaluate ";
            for(Node node : nodes)
                s += node + ",";
            System.out.println(s);
            s = "changed output [";
            for(Cell changed : changedoutputs){
                UnitOutput output = (UnitOutput)data.getUnit(changed);
                s += output + ",";
            }
            s += "]";
            System.out.println(s);
        }
    }

    private Set<Node> removeWire(UnitWire wire){
        Set<Node> newnodes = new HashSet<>();
        Set<Dir> faces = wire.getFaces();
        for(Dir face : faces){
            Node newnode = new Node();
            newnodes.add(newnode);
            Joint joint = new Joint(wire.getPos(), face);
            setWireSegNode(joint, newnode);
        }
        data.removeCell(wire.getPos());
        return newnodes;
    }

    private void setWireSegNode(Joint startjoint, Node newnode){
        String debugstr = "";
        if(debug_level >= 3)
            debugstr = "set wire to " + newnode + " begin at " + startjoint + " on [";

        Stack<Joint> open = new Stack<>();
        Set<Joint> closed = new HashSet<>();
        // add all wire joint to closed
        for(Dir d : Dir.values()) {
            closed.add(new Joint(startjoint.cell, d));
        }
        open.add(startjoint.getLinkedJoint());
        while(!open.isEmpty()){
            Joint joint = open.pop();
            Unit unit = data.getUnit(joint.cell);
            if(unit == null)
                continue;
            if(!unit.getFaces().contains(joint.dir))
                continue;   // not connected
            // new unit

            if(unit instanceof UnitWire) {
                UnitWire unitwire = (UnitWire) unit;
                Set<Dir> connected = unitwire.getConnected(joint.dir);
                data.setUnitNode(unit, joint.dir, newnode);
                if(debug_level >= 3)
                    debugstr += new Joint(unit.getPos(), joint.dir) + ",";

                // for connected faces
                for (Dir d : connected) {
                    // every wire joint
                    Joint wirejoint = new Joint(unitwire.getPos(), d);
                    if (closed.contains(wirejoint))
                        continue;
                    data.setUnitNode(unit, d, newnode);
                    if(debug_level >= 3)
                        debugstr += new Joint(unit.getPos(), d) + ",";

                    open.add(wirejoint.getLinkedJoint());
                }
            }else{
                data.setUnitNode(unit, joint.dir, newnode);
                if(debug_level >= 3)
                    debugstr += joint + ",";
            }

        }

        if(debug_level >= 3){
            debugstr += "]";
            System.out.println(debugstr);
        }
    }

    public void printCircuitRoute(Set<Cell> inputs){
        Set<Node> nodes = new HashSet<>();
        for(Cell cell : inputs){
            nodes.addAll(data.getUnit(cell).getNodeSet());
        }
        Analyser analyser = new Analyser();
        analyser.getGraphViz(nodes.toArray(new Node[0]));
    }

    public void printSvg(){
        System.out.println(data.toSvg());
    }

    public void printNodes(){
        System.out.println(data.nodeTableString());
    }

    public String toJson(){
        JSONArray unitarr = new JSONArray();
        Set<Unit> units = data.getAllUnits();
        for(Unit unit : units){
            unitarr.put(unit.toJson());
        }
        return unitarr.toString();
    }

    public static Circuit fromJson(String json){
        JSONArray unitarr = new JSONArray(json);
        Circuit circuit = new Circuit();

        for(int i = 0; i < unitarr.length(); i++){
            JSONObject obj = unitarr.getJSONObject(i);
            Unit unit = Unit.createUnitFromJson(obj);
            circuit.add(unit);
        }
        return circuit;
    }
}

