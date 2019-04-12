package io.github.liamtuan.semicon.sim;

import io.github.liamtuan.semicon.blocks.io.BlockOutput;
import io.github.liamtuan.semicon.core.*;
import net.minecraft.block.Block;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.*;

public class Circuit{

    private static NodeStateListener output_listener;
    private static Set<BlockPos> changed_outputs;

    private static JointNodeMap joint_node;
    private static Set<BlockPos> output_cells;
    private static Map<BlockPos, Boolean> input_blocks;
    private static Map<BlockPos, Gate> gate_cells;
    private static boolean verbose;
    private static World world = null;

    public static void init(){
        changed_outputs = new HashSet<>();
        joint_node = new JointNodeMap();
        output_cells = new HashSet<>();
        input_blocks = new HashMap<>();
        gate_cells = new HashMap<>();
        verbose = false;

        output_listener = new NodeStateListener() {
            @Override
            public void onNodeStateChanged(Node node) {
                BlockPos[] outputs = getOutputCells(node);
                for(BlockPos pos : outputs){
                    changed_outputs.add(pos);
                }
            }
        };
    }

    public static void setWorld(World world){
        Circuit.world = world;
    }

    public static void setVerbose(boolean verbose){
        Circuit.verbose = verbose;
    }

    private static BlockPos[] getOutputCells(Node node){
        Joint[] joints = joint_node.getNodeJoints(node);
        Set<BlockPos> cells = new HashSet<>();
        for(Joint joint : joints){
            if(output_cells.contains(joint.pos))
                cells.add(joint.pos);
        }
        return cells.toArray(new BlockPos[0]);
    }

    private static Node installJoint(Joint joint, Node node){
        Joint linkedjoint = joint.linkedJoint();
        Node linkednode = joint_node.get(linkedjoint);
        if(linkednode == null || linkednode == node){
            joint_node.put(joint, node);
            if(verbose)
                System.out.println("add " + node + " at " + joint);
            return node;
        }else {
            linkednode.merge(node);
            joint_node.replaceNode(node, linkednode);
            joint_node.put(joint, linkednode);
            if(verbose)
                System.out.println("merge " + node + " to " + linkednode + " at " + joint);
            return linkednode;
        }
    }

    private static void applyOutputChanges(){
        for(BlockPos pos : changed_outputs){
            boolean state = false;
            for(EnumFacing facing : EnumFacing.values()){
                Joint joint = new Joint(pos, facing);
                if(joint_node.get(joint).getState())
                    state = true;
            }
            //System.out.println(cell.pos + ":" + state);
            if(verbose)
                System.out.println("apply state " + state + " at " + pos);
            if(world == null)
                continue;
            Block block = world.getBlockState(pos).getBlock();
            if(block instanceof BlockOutput){
                BlockOutput output = (BlockOutput)block;
                output.setState(world, pos, state);
            }
        }
        changed_outputs.clear();
    }

    private static void evaluate(Node[] nodes){
        for(Node node : nodes)
            node.invokeListener();
        new Processor().eval(nodes);
        applyOutputChanges();
    }

    public static void setInputState(BlockPos pos, EnumFacing facing, boolean state){
        input_blocks.put(pos, state);
        Joint joint = new Joint(pos, facing);
        Node node = joint_node.get(joint);
        node.setState(state);

        if(verbose)
            System.out.println("set state " + state + " on " + node + " at " + joint);
        evaluate(new Node[]{node});
    }

    public static boolean getState(BlockPos pos, EnumFacing facing){
        Joint joint = new Joint(pos, facing);
        Node node = joint_node.get(joint);
        return node.getState();
    }

    public static boolean getState(BlockPos pos){
        for(EnumFacing facing : EnumFacing.values()){
            Joint joint = new Joint(pos, facing);
            Node node = joint_node.get(joint);
            if(node.getState())
                return true;
        }
        return false;
    }

    public static void addInput(BlockPos pos, EnumFacing facing){
        Joint joint = new Joint(pos, facing);
        Node node = installJoint(joint, new Node());
        input_blocks.put(joint.pos, node.getState());
        if(verbose)
            System.out.println("add input " + node + " at " + joint);
        evaluate(new Node[]{node});
    }

    public static void removeInput(BlockPos pos, EnumFacing[] faces){
        Set<Node> nodes = new HashSet<>();
        for(EnumFacing facing : faces){
            Joint joint = new Joint(pos, facing);
            Node node = joint_node.get(joint);
            node.setState(false);
            nodes.add(node);
            joint_node.remove(joint);
        }
        if(verbose)
            System.out.println("remove input at " + pos);
        input_blocks.remove(pos);
        evaluate(nodes.toArray(new Node[0]));
    }

    public static void addOutput(BlockPos pos, EnumFacing[] faces){
        Set<Node> nodes = new HashSet<>();
        for(EnumFacing facing : faces){
            Joint joint = new Joint(pos, facing);
            Node node = installJoint(joint, new Node());
            node.addListener(output_listener);
            nodes.add(node);
        }
        output_cells.add(pos);

        if(verbose){
            for(Node node : nodes){
                System.out.println("add output " + node + " at " + pos);
            }
        }
        evaluate(nodes.toArray(new Node[0]));
    }

    public static void removeOutput(BlockPos pos, EnumFacing[] faces){
        for(EnumFacing facing : faces){
            Joint joint = new Joint(pos, facing);
            joint_node.remove(joint);
        }
        if(verbose)
            System.out.println("remove output at " + pos);
        output_cells.remove(pos);
    }

    public static void addBlockGate(BlockPos pos, Gate gate,
                                    EnumFacing[] input_faces, EnumFacing[] output_faces){
        Node[] input_nodes = gate.getInputNodes();
        for(int i = 0; i < input_faces.length; i++){
            Joint joint = new Joint(pos, input_faces[i]);
            installJoint(joint, input_nodes[i]);
        }
        Node[] output_nodes = gate.getOutputNodes();
        for(int i = 0; i < output_faces.length; i++){
            Joint joint = new Joint(pos, output_faces[i]);
            installJoint(joint, output_nodes[i]);
        }
        gate_cells.put(pos, gate);
        if(verbose)
            System.out.println("add " + gate + " at " + pos);
        evaluate(input_nodes);
    }

    public static void removeBlockGate(BlockPos pos, EnumFacing[] input_faces, EnumFacing[] output_faces){
        Set<Node> nodes = new HashSet<>();
        Gate gate = gate_cells.get(pos);
        gate.dettach();

        for(EnumFacing facing : input_faces){
            Joint joint = new Joint(pos, facing);
            joint_node.remove(joint);
        }

        for(EnumFacing facing : output_faces){
            Joint joint = new Joint(pos, facing);
            nodes.add(joint_node.get(joint));
            joint_node.remove(joint);
        }

        if(verbose)
            System.out.println("remove " + gate + " at " + pos);
        gate_cells.remove(pos);
        for(Node node : nodes)
            node.setState(false);
        evaluate(nodes.toArray(new Node[0]));
    }

    public static void addBlockWire(BlockPos pos, EnumFacing[] faces){
        Node node = new Node();
        for(EnumFacing facing : faces){
            Joint joint = new Joint(pos, facing);
            node = installJoint(joint, node);
            if(verbose)
                System.out.println("add wire " + node + " at " + joint);
        }
        evaluate(new Node[]{node});
    }

    public static void removeBlockWire(BlockPos pos, EnumFacing[] faces){
        Set<Node> newnodes = new HashSet<>();
        for(EnumFacing facing : faces){
            Joint wirejoint = new Joint(pos, facing);
            Node oldnode = joint_node.get(wirejoint);
            Node newnode = new Node();
            Joint[] linkedjoints = joint_node.getLinkedJoints(wirejoint);
            joint_node.remove(wirejoint);
            for(Joint joint : linkedjoints){
                Gate gate = gate_cells.get(joint.pos);
                if(gate != null){
                    gate.replaceNode(oldnode, newnode);
                }
                Boolean inputstate = input_blocks.get(joint.pos);
                if(inputstate != null)
                    newnode.setState(inputstate);
                if(output_cells.contains(joint.pos))
                    newnode.addListener(output_listener);
                joint_node.remove(joint);
                joint_node.put(joint, newnode);
            }
            if(linkedjoints.length > 0)
                newnodes.add(newnode);
        }
        if(verbose)
            System.out.println("remove wire " + " at " + pos);

        evaluate(newnodes.toArray(new Node[0]));
    }

    private static Node[] getInputNodes(){
        Set<Node> nodes = new HashSet<>();
        for(BlockPos pos : input_blocks.keySet()){
            Node node = null;
            for(EnumFacing facing : EnumFacing.values()){
                Joint joint = new Joint(pos, facing);
                node = joint_node.get(joint);
                if(node != null)
                    break;
            }
            nodes.add(node);
        }
        return nodes.toArray(new Node[0]);
    }

    public static void printCircuit(){
        Analyser analyser = new Analyser();
        String s = analyser.getGraphViz(getInputNodes());
        System.out.println(s);
    }
}