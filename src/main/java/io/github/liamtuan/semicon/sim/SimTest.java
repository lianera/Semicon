package io.github.liamtuan.semicon.sim;

import io.github.liamtuan.semicon.core.AndGate;
import io.github.liamtuan.semicon.core.Gate;
import io.github.liamtuan.semicon.core.Node;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class SimTest {
    public SimTest(){
    }

    public void testAll(){
        if(!jointTest())
            System.out.println("joint test failed");
        if(!ioTest())
            System.out.println("io test failed");
        if(!wireTest())
            System.out.println("wire test failed");
        if(!gateTest())
            System.out.println("gate test failed");
        if(!wireLoopTest())
            System.out.println("wire loop test failed");
        if(!gateLoopTest())
            System.out.println("gate loop test failed");
    }

    private boolean jointTest(){
        Joint j1 = new Joint(new BlockPos(1,2,3), EnumFacing.NORTH);
        Joint j2 = new Joint(new BlockPos(1,2,3), EnumFacing.NORTH);
        if(!j1.equals(j2))
            return false;
        if(j1.hashCode() != j2.hashCode())
            return false;
        return true;
    }

    private boolean ioTest(){
        Circuit.init();
        BlockPos in = new BlockPos(0,0,0);
        Circuit.addInput(in, EnumFacing.NORTH);
        BlockPos led = new BlockPos(0, 0, -1);
        Circuit.addOutput(led, EnumFacing.values());
        Circuit.setInputState(in, EnumFacing.NORTH, true);
        if(!Circuit.getState(led))
            return false;

        Circuit.removeInput(in, new EnumFacing[]{EnumFacing.NORTH});
        if(Circuit.getState(led))
            return false;

        return true;
    }

    private boolean wireTest(){
        Circuit.init();
        //Circuit.setVerbose(true);
        BlockPos in = new BlockPos(0,0,0);
        Circuit.addInput(in, EnumFacing.NORTH);
        BlockPos wire = new BlockPos(0, 0, -1);
        EnumFacing[] wirefaces = new EnumFacing[]{EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.EAST};
        Circuit.addBlockWire(wire, wirefaces);
        BlockPos led = new BlockPos(0, 0, -2);
        Circuit.addOutput(led, EnumFacing.values());
        BlockPos led2 = new BlockPos(1, 0, -1);
        Circuit.addOutput(led2, EnumFacing.values());
        Circuit.setInputState(in, EnumFacing.NORTH, true);
        if(!Circuit.getState(led) || !Circuit.getState(led2))
            return false;

        Circuit.removeBlockWire(wire, wirefaces);
        if(Circuit.getState(led) || Circuit.getState(led2))
            return false;

        return true;
    }

    private static boolean wireLoopTest(){
        Circuit.init();
        //Circuit.setVerbose(true);
        BlockPos in = new BlockPos(0,0,0);
        Circuit.addInput(in, EnumFacing.SOUTH);
        BlockPos led = new BlockPos(-2, 0, 1);
        Circuit.addOutput(led, EnumFacing.values());
        EnumFacing[] faces = {EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.WEST};
        Circuit.addBlockWire(new BlockPos(-1, 0, 1), faces);
        Circuit.addBlockWire(new BlockPos(-1, 0, 2), faces);
        Circuit.addBlockWire(new BlockPos(0, 0, 2), faces);
        Circuit.addBlockWire(new BlockPos(0, 0, 1), faces);
        //Circuit.printCircuit();
        Circuit.setInputState(in, EnumFacing.SOUTH, true);
        if(!Circuit.getState(led))
            return false;
        Circuit.removeBlockWire(new BlockPos(0, 0, 1), faces);
        if(Circuit.getState(led))
            return false;
        Circuit.removeBlockWire(new BlockPos(0, 0, 2), faces);
        if(Circuit.getState(led))
            return false;
        Circuit.addBlockWire(new BlockPos(0, 0, 2), faces);
        if(Circuit.getState(led))
            return false;
        Circuit.addBlockWire(new BlockPos(0, 0, 1), faces);
        if(!Circuit.getState(led))
            return false;
        Circuit.removeBlockWire(new BlockPos(0, 0, 2), faces);
        Circuit.removeBlockWire(new BlockPos(0, 0, 1), faces);
        if(Circuit.getState(led))
            return false;

        return true;
    }

    private boolean gateTest(){
        Circuit.init();
        //Circuit.setVerbose(true);
        BlockPos in = new BlockPos(0,0,0);
        Circuit.addInput(in, EnumFacing.NORTH);
        BlockPos in2 = new BlockPos(1,0,-1);
        Circuit.addInput(in2, EnumFacing.WEST);
        BlockPos gatepos = new BlockPos(0, 0, -1);
        Gate gate = new AndGate(new Node(), new Node(), new Node());
        EnumFacing[] infaces = new EnumFacing[]{EnumFacing.SOUTH, EnumFacing.EAST};
        EnumFacing[] outfaces = new EnumFacing[]{EnumFacing.NORTH};
        Circuit.addBlockGate(gatepos, gate,
                infaces, outfaces);

        BlockPos led = new BlockPos(0, 0, -2);
        Circuit.addOutput(led, EnumFacing.values());

        Circuit.setInputState(in, EnumFacing.NORTH, true);
        Circuit.setInputState(in2, EnumFacing.WEST, true);
        if(!Circuit.getState(led))
            return false;

        Circuit.removeBlockGate(gatepos, infaces, outfaces);
        if(Circuit.getState(led))
            return false;

        return true;
    }

    private boolean gateLoopTest(){
        Circuit.init();
        //Circuit.setVerbose(true);
        BlockPos led = new BlockPos(0, 0, 4);
        Circuit.addOutput(led, EnumFacing.values());
        EnumFacing[] faces = {EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.WEST};
        Circuit.addBlockWire(new BlockPos(0, 0, 1), faces);
        Circuit.addBlockWire(new BlockPos(1, 0, 1), faces);
        Circuit.addBlockWire(new BlockPos(1, 0, 2), faces);
        Circuit.addBlockWire(new BlockPos(1, 0, 3), faces);
        Circuit.addBlockWire(new BlockPos(0, 0, 3), faces);

        Circuit.removeBlockWire(new BlockPos(1, 0, 2), faces);

        BlockPos in = new BlockPos(0,0,0);
        Circuit.addInput(in, EnumFacing.SOUTH);

        if(!Circuit.getState(led))
            return false;

        Circuit.setInputState(in, EnumFacing.SOUTH, true);
        if(!Circuit.getState(led))
            return false;

        return true;
    }
}
