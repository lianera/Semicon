package io.github.liamtuan.semicon.sim.core;


public class AndGate extends X2Y1Gate{

    public AndGate(Node x1, Node x2, Node y)
    {
        super(x1, x2, y);
    }
    @Override
    public void evel() {
        y.setState(x1.getState() && x2.getState());
    }

    @Override
    public String toString() {
        return "AndGate" + getId();
    }
}