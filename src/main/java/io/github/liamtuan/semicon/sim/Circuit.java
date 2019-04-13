package io.github.liamtuan.semicon.sim;


import io.github.liamtuan.semicon.sim.core.Analyser;
import io.github.liamtuan.semicon.sim.core.Node;
import io.github.liamtuan.semicon.sim.core.Processor;
import org.apache.http.impl.conn.Wire;
import org.lwjgl.Sys;

import java.util.*;

public class Circuit{
    private static DataTable data;
    private static int debug_level;


    public static void setDebugLevel(int i){
        debug_level = i;
    }

    public static void init(){
        data = new DataTable();
        debug_level = 0;
    }

    public static void add(Unit unit){
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
    }

    public static void remove(Cell cell){
        if(debug_level >= 1){
            System.out.println("remove " + data.getUnit(cell));
        }
        Unit unit = data.getUnit(cell);
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
        updateNodes(changednodes);
        evaluate(changednodes);
    }

    public static void setInpuState(Cell cell, boolean state){
        UnitInput input = (UnitInput)data.getUnit(cell);
        if(debug_level >= 1)
            System.out.println("set input state " + input + " to " + state);
        input.setState(state);
        Set<Node> changednodes = input.getNodeSet();
        updateNodes(changednodes);
        evaluate(changednodes);
    }

    public static boolean getOutputState(Cell cell){
        UnitOutput output = (UnitOutput)data.getUnit(cell);
        if(debug_level >= 1)
            System.out.println("get output state " + output + " " + output.getState());
        return output.getState();
    }

    private static Node neighborNode(Cell pos, Dir dir){
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

    private static void updateNodes(Set<Node> nodes){
        for(Node node : nodes) {
            node.setState(false);
            Set<Unit> units = data.getNodeUnits(node);
            for(Unit unit : units)
                unit.update();
        }
        if(debug_level >= 2){
            String s = "update ";
            for(Node node : nodes)
                s += node + "=" + node.getState() + ",";
            System.out.println(s);
        }
    }

    private static void evaluate(Set<Node> nodes){
        Node[] nodearr = nodes.toArray(new Node[0]);
        Processor p = new Processor();
        p.eval(nodearr);

        if(debug_level >= 2){
            String s = "evaluate ";
            for(Node node : nodes)
                s += node + "=" + node.getState() + ",";
            System.out.println(s);
        }
    }

    private static Set<Node> removeWire(UnitWire wire){
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

    private static void setWireSegNode(Joint startjoint, Node newnode){
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
}

