package io.github.liamtuan.semicon;

import io.github.liamtuan.semicon.core.CoreTest;
import io.github.liamtuan.semicon.sim.Joint;
import io.github.liamtuan.semicon.sim.SimTest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class Test {

    public static void main(String[] args) {
        CoreTest coretest = new CoreTest();
        coretest.testAll();

        SimTest simtest = new SimTest();
        simtest.testAll();
    }
}
