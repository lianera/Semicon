package io.github.liamtuan.semicon;

import com.sun.javaws.exceptions.InvalidArgumentException;
import io.github.liamtuan.semicon.blocks.BlockIO;
import io.github.liamtuan.semicon.core.Gate;
import io.github.liamtuan.semicon.core.Node;
import io.github.liamtuan.semicon.core.NodeStateListener;
import io.github.liamtuan.semicon.core.Processor;
import net.minecraft.block.Block;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.*;

class Joint{
    World world;
    BlockPos pos;
    EnumFacing facing;

    Joint(World world, BlockPos pos, EnumFacing facing){
        this.world = world;
        this.pos = pos;
        this.facing = facing;
    }

    Joint linkedJoint(){
        BlockPos linkedpos = pos.offset(facing);
        EnumFacing linkedfacing = facing.getOpposite();
        return new Joint(world, linkedpos, linkedfacing);
    }

    Block getBlock(){
        return world.getBlockState(pos).getBlock();
    }

    @Override
    public boolean equals(Object obj) {
        Joint jointobj = (Joint)obj;
        return jointobj.world == world &&
                jointobj.pos.getX() == pos.getX() &&
                jointobj.pos.getY() == pos.getY() &&
                jointobj.pos.getZ() == pos.getZ() &&
                jointobj.facing == facing;
    }

    @Override
    public int hashCode() {
        return
        pos.getX()*320000000 +
        pos.getY()*320000 +
        pos.getZ()*320 +
        facing.hashCode()*5 +
        world.hashCode();
    }
}

public class Circuit {

    static Map<Joint, Node> nodes = new HashMap<>();
    static Map<Node, List<Joint>> nodejoints = new HashMap<>();
    static NodeStateListener nodeStateListener = null;

    public static void init(){
        nodeStateListener = new NodeStateListener() {
            @Override
            public void onNodeStateChanged(Node node) {
                List<Joint> joints = nodejoints.get(node);
                for(Joint joint : joints){
                    Block block = joint.getBlock();
                    if(block instanceof  BlockIO){
                        BlockIO blockio = (BlockIO) joint.getBlock();
                        blockio.setState(joint.world, joint.pos, node.getState());
                    }
                }
            }
        };
    }

    public static void procEvents(List<Node> events){
        Processor p = new Processor();
        Node[] nodearr = new Node[events.size()];
        events.toArray(nodearr);
        p.eval(nodearr);
        events.clear();
    }

    public static void addInput(World world, BlockPos pos, EnumFacing facing){
        Joint joint = new Joint(world, pos, facing);
        addJoint(joint, new Node());
    }

    public static void addOutput(World world, BlockPos pos, EnumFacing[] faces){
        Node node = new Node();
        for(EnumFacing facing : faces){
            Joint joint = new Joint(world, pos, facing);
            node = addJoint(joint, node);
        }

        node.addListener(nodeStateListener);
        node.invokeListener();
    }

    public static void setState(World world, BlockPos pos, EnumFacing facing, boolean state){
        Joint joint = new Joint(world, pos, facing);
        Node node = nodes.get(joint);
        node.setState(state);

        List<Node> events = new ArrayList<>();
        events.add(node);
        procEvents(events);
    }

    public static void addBlockGate(World world, BlockPos pos, Gate gate,
                                    EnumFacing[] input_faces, EnumFacing[] output_faces){
        List<Node> events = new ArrayList<>();

        Node[] gate_input_nodes = gate.getInputNodes();
        for(int i = 0; i < input_faces.length; i++){
            Node node = gate_input_nodes[i];
            Joint joint = new Joint(world, pos, input_faces[i]);
            addJoint(joint, node);

            events.add(node);
        }

        Node[] gate_output_nodes = gate.getOutputNodes();
        for(int i = 0; i < output_faces.length; i++){
            Node node = gate_output_nodes[i];
            Joint joint = new Joint(world, pos, output_faces[i]);
            addJoint(joint, node);
        }

        procEvents(events);
    }

    public static void addBlockWire(World world, BlockPos pos, EnumFacing[] joint_faces){
        Node node = new Node();
        for(EnumFacing facing : joint_faces){
            Joint joint = new Joint(world, pos, facing);
            node = addJoint(joint, node);
        }
    }

    // return jointed node
    private static Node addJoint(Joint joint, Node node){
        Joint linkedjoint = joint.linkedJoint();
        Node linkednode = nodes.get(linkedjoint);
        List<Node> events = new ArrayList<>();

        if(linkednode != null){ // merge
            linkednode.merge(node);
            // update node
            events.add(linkednode);

            // update joints
            List<Joint> originjoints = nodejoints.get(node);
            if(originjoints != null) {
                for (Joint j : originjoints) {
                    nodes.put(j, linkednode);
                }
            }
            node = linkednode;

        }

        // add node
        List<Joint> joints = nodejoints.get(node);
        if(joints == null){
            joints = new ArrayList<>();
            nodejoints.put(node, joints);
        }
        joints.add(joint);
        nodes.put(joint, node);

        procEvents(events);
        return node;
    }

}
