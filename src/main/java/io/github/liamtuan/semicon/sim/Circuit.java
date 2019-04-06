package io.github.liamtuan.semicon.sim;

import com.sun.jna.platform.win32.WinDef;
import io.github.liamtuan.semicon.blocks.BlockOutput;
import io.github.liamtuan.semicon.core.Gate;
import io.github.liamtuan.semicon.core.Node;
import io.github.liamtuan.semicon.core.NodeStateListener;
import io.github.liamtuan.semicon.core.Processor;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Map;
import java.util.Stack;

public class Circuit{

    private static NodeStateListener output_listener;

    public static void init(){
        output_listener = new NodeStateListener() {
            @Override
            public void onNodeStateChanged(Node node) {
                Cell[] outputs = Persist.getOutputCells(node);
                for(Cell cell : outputs){
                    BlockOutput block = (BlockOutput)cell.getBlock();
                    block.setState(cell.world, cell.pos, node.getState());
                }
            }
        };
    }

    public static void setState(World world, BlockPos pos, EnumFacing facing, boolean state){
        Joint joint = new Joint(world, pos, facing);
        Node node = Persist.getJointNode(joint);
        node.setState(state);

        new Processor().eval(new Node[]{node});
    }

    public static void addInput(World world, BlockPos pos, EnumFacing facing){
        Joint joint = new Joint(world, pos, facing);
        Persist.addJointNode(joint, new Node());
    }

    public static void removeInput(World world, BlockPos pos){
        for(EnumFacing facing : EnumFacing.values()){
            Joint joint = new Joint(world, pos, facing);
            Persist.removeJointNode(joint);
        }
    }

    public static void addOutput(World world, BlockPos pos, EnumFacing[] faces){
        for(EnumFacing facing : faces){
            Joint joint = new Joint(world, pos, facing);
            Node node = Persist.addJointNode(joint, new Node());
            node.addListener(output_listener);
        }

    }

    public static void removeOutput(World world, BlockPos pos){
        for(EnumFacing facing : EnumFacing.values()){
            Joint joint = new Joint(world, pos, facing);
            Persist.removeJointNode(joint);
        }
    }

    public static void addBlockGate(World world, BlockPos pos,
                                    Gate gate, EnumFacing[] input_faces, EnumFacing[] output_faces){
        Node[] input_nodes = gate.getInputNodes();
        for(int i = 0; i < input_faces.length; i++){
            Joint joint = new Joint(world, pos, input_faces[i]);
            Persist.addJointNode(joint, input_nodes[i]);
        }
        Node[] output_nodes = gate.getOutputNodes();
        for(int i = 0; i < input_faces.length; i++){
            Joint joint = new Joint(world, pos, output_faces[i]);
            Persist.addJointNode(joint, output_nodes[i]);
        }
        Cell cell = new Cell(world, pos);
        Persist.addGate(cell, gate);

        new Processor().eval(input_nodes);
    }

    public static void removeBlockGate(World world, BlockPos pos){
        Cell cell = new Cell(world, pos);
        Gate gate = Persist.removeGate(cell);
        gate.dettach();
        for(EnumFacing facing : EnumFacing.values()){
            Joint joint = new Joint(world, pos, facing);
            Persist.removeJointNode(joint);
        }
    }

    public static void addBlockWire(World world, BlockPos pos, EnumFacing[] faces){
        for(EnumFacing facing : faces){
            Joint joint = new Joint(world, pos, facing);
            Persist.addJointNode(joint, new Node());
        }
    }

    public static void removeBlockWire(World world, BlockPos pos){
        for(EnumFacing facing : EnumFacing.values()){
            Joint joint = new Joint(world, pos, facing);
            Persist.replaceNodes(joint, new Node());
            Persist.removeJointNode(joint);
        }
    }
}