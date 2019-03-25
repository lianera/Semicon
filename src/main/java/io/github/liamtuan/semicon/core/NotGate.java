package io.github.liamtuan.semicon.core;


public class NotGate extends Gate{
    Node x, y;
    public NotGate(Node x, Node y)
    {
        this.x = x;
        this.y  =y;
        super.attach();
    }

    @Override
    Node[] getInputNodes() {
        return new Node[]{x};
    }

    @Override
    Node[] getOutputNodes() {
        return new Node[]{y};
    }

    @Override
    void evel() {
        y.state = !x.state;
    }
}
