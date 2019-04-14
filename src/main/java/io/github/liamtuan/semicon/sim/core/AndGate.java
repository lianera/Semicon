package io.github.liamtuan.semicon.sim.core;


public class AndGate extends X2Y1Gate{

    @Override
    public void evel() {
        y.setState(x1.getState() && x2.getState());
    }

    @Override
    public String getName() {
        return "and";
    }

}