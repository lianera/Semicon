package io.github.liamtuan.semicon.sim.core;


public class NotGate extends X1Y1Gate{
    public NotGate(Node x, Node y)
    {
        super(x, y);
    }

    @Override
    public void evel() {
        y.setState(!x.getState());
    }

    @Override
    public String toString() {
        return "NotGate" + getId();
    }
}
