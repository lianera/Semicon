package io.github.liamtuan.semicon.sim;

import io.github.liamtuan.semicon.core.Gate;
import io.github.liamtuan.semicon.core.Node;
import net.minecraft.util.EnumFacing;

import java.util.*;

public class Persist{
    static private Map<Joint, Node> joint_nodes = new HashMap<>();
    static private Map<Node, List<Cell>> outputs = new HashMap<>();
    static private Map<Cell, Gate> gates = new HashMap<>();

    public static Node addJointNode(Joint joint, Node node){
        Node linkednode = joint_nodes.get(joint.linkedJoint());
        if(linkednode == null){
            joint_nodes.put(joint, node);
            return node;
        }else{
            linkednode.merge(node);
            return linkednode;
        }
    }

    public static Node getJointNode(Joint joint){
        return joint_nodes.get(joint);
    }

    public static void removeJointNode(Joint joint){
        joint_nodes.remove(joint);
    }

    public static void addOutput(Cell cell, Node node){
        List<Cell> cells = outputs.get(node);
        if(cells == null){
            cells = new ArrayList<>();
        }
        cells.add(cell);
        outputs.put(node, cells);
    }

    public static void removeOutput(Cell cell) {
        Node node = null;
        for(EnumFacing facing : EnumFacing.values()){
            Joint joint = new Joint(cell.world, cell.pos, facing);
            node = joint_nodes.get(joint);
            if(node != null)
                break;
        }
        List<Cell> cells = outputs.get(node);
        cells.remove(cell);
    }

    public static Cell[] getOutputCells(Node node){
        List<Cell> cells = outputs.get(node);
        return cells.toArray(new Cell[0]);
    }

    public static void addGate(Cell cell, Gate gate){
        gates.put(cell, gate);
    }

    public static Gate removeGate(Cell cell){
        Gate gate = gates.get(cell);
        gates.remove(cell);
        return gate;
    }

    public static void replaceNodes(Joint start_joint, Node newnode){
        Stack<Joint> path = new Stack<>();
        path.push(start_joint);
        while(!path.isEmpty()){
            Joint joint = path.pop().linkedJoint();
            // replace all nodes
            Node oldnode = joint_nodes.get(joint);
            outputs.put(newnode, outputs.get(oldnode));
            outputs.remove(oldnode);
            joint_nodes.put(joint, newnode);

            for(EnumFacing facing : EnumFacing.values()){
                Joint next = new Joint(joint.world, joint.pos, facing);
                if(joint_nodes.get(next) != null)
                    path.push(next);
            }
        }
    }

}