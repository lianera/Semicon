package io.github.liamtuan.semicon;

import io.github.liamtuan.semicon.sim.core.CoreTest;
import io.github.liamtuan.semicon.sim.SimTest;

public class Test {

    public static void main(String[] args) {
        CoreTest coretest = new CoreTest();
        coretest.testAll();

        SimTest simtest = new SimTest();
        simtest.testAll();
    }
}
