package io.github.liamtuan.semicon.sim.core;


public class NotGate extends X1Y1Gate{

    @Override
    public void eval() {
        y.setState(!x.getState());
    }

    @Override
    public String getName() {
        return "not";
    }
}
