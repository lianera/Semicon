package io.github.liamtuan.semicon.sim;


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
        data.put(pos, unit);

        // check neighbors
        for(Dir dir : Dir.values()){
            Node node = unit.getNode(dir);
            Node neighbor_node = neighborNode(pos, dir);
            if(node == null || neighbor_node == null || node == neighbor_node)
                continue;

            if(debug_level >= 2)
                System.out.println("merge " + node + " to " + neighbor_node);
            neighbor_node.merge(node);
            data.replace(node, neighbor_node);
        }
    }

    public static void remove(Cell cell){
        if(debug_level >= 1){
            System.out.println("remove " + data.get(cell));
        }
        Unit unit = data.get(cell);
        Set<Node> changednodes = unit.getAllNodes();
        if(unit instanceof UnitWire){
            changednodes.clear();
            Set<Node> oldnodes = unit.getAllNodes();
            for(Dir d : Dir.values()){
                Set<Joint> linked = findLinkedJoints(cell, d);
                if(linked == null || linked.isEmpty())
                    continue;
                for(Node oldnode : oldnodes) {
                    Node newnode = new Node();
                    data.replaceJointsNode(linked, oldnode, newnode);
                    changednodes.add(newnode);
                }
            }

            if(debug_level >= 2){
                String s = "";
                for(Node node : changednodes)
                    s += node + ",";
                System.out.println("break wire and generate {" + s + "}");
            }
        }
        unit.dettach();
        data.remove(cell);
        updateNodes(changednodes);
        evaluate(changednodes);
    }

    public static void setInpuState(Cell cell, boolean state){
        UnitInput input = (UnitInput)data.get(cell);
        if(debug_level >= 1)
            System.out.println("set input state " + input + " to " + state);
        input.setState(state);
        Set<Node> changednodes = input.getAllNodes();
        updateNodes(changednodes);
        evaluate(changednodes);
    }

    public static boolean getOutputState(Cell cell){
        UnitOutput output = (UnitOutput)data.get(cell);
        if(debug_level >= 1)
            System.out.println("get input state " + output + " " + output.getState());
        return output.getState();
    }

    private static Set<Joint> findLinkedJoints(Cell cell, Dir dir){
        Set<Joint> results = new HashSet<>();
        Joint startjoint = new Joint(cell, dir);
        Stack<Joint> open = new Stack<>();
        Set<Joint> closed = new HashSet<>();
        open.add(startjoint);
        while(!open.isEmpty()){
            Joint joint = open.pop();
            results.add(joint);
            closed.add(joint);
            Unit unit = data.get(joint.pos);
            if(!(unit instanceof UnitWire)){
                continue;
            }
            for(Dir d : Dir.values()){
                if(d == joint.dir || unit.getNode(d) == null)
                    continue;
                Joint j = new Joint(joint.pos, d);
                if(closed.contains(j))
                    continue;
                results.add(j);
                Joint linked = j.linkedJoint();
                if(closed.contains(linked))
                    continue;
                if(data.get(linked.pos) == null)
                    continue;
                open.add(linked);
            }
        }
        return results;
    }

    private static Node neighborNode(Cell pos, Dir dir){
        Unit unit = data.get(pos);
        if(unit == null)
            return null;
        Node node = unit.getNode(dir);
        if(node == null)
            return null;
        Unit neighbor = data.get(dir.offset(pos));
        if(neighbor == null)
            return null;
        Node neighbor_node = neighbor.getNode(dir.opposite());
        return neighbor_node;
    }

    private static void updateNode(Node node){
        node.setState(false);
        Set<Cell> cells = data.get(node);
        if(cells == null)
            return;
        for(Cell c : cells){
            Unit unit = data.get(c);
            unit.update();
        }
    }

    private static void updateNodes(Node[] nodes){
        for(Node node : nodes)
            updateNode(node);
    }

    private static void updateNodes(Set<Node> nodes){
        updateNodes(nodes.toArray(new Node[0]));
    }

    private static void evaluate(Node[] nodes){
        Processor p = new Processor();
        p.eval(nodes);
    }

    private static void evaluate(Set<Node> nodes){
        evaluate(nodes.toArray(new Node[0]));
    }
}
