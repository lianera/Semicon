package io.github.liamtuan.semicon.sim.core;

public class ThreeStateGate extends X2Y1Gate{
    @Override
    public void eval() {
        if(x2.getState()){
            y.setState(x1.getState());
        }
    }

    @Override
    public String getName() {
        return "threestategate";
    }
}
