package io.github.liamtuan.semicon.core;


public class AndGate extends X2Y1Gate{

    public AndGate(Node x1, Node x2, Node y)
    {
        super(x1, x2, y);
    }
    @Override
    void evel() {
        y.state = x1.state && x2.state;
    }
}