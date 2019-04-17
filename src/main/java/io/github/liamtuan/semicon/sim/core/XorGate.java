package io.github.liamtuan.semicon.sim.core;

public class XorGate extends X2Y1Gate{
    @Override
    public void eval() {
        y.setState(x1.getState() != x2.getState());
    }

    @Override
    public String getName() {
        return "xor";
    }

}
