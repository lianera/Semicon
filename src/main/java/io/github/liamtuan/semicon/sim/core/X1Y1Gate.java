package io.github.liamtuan.semicon.sim.core;

public abstract class X1Y1Gate extends Gate{
    Node x, y;
    public X1Y1Gate(Node x, Node y)
    {
        this.x = x;
        this.y  =y;
        super.attach();
    }

    @Override
    public Node[] getInputNodes() {
        return new Node[]{x};
    }

    @Override
    public Node[] getOutputNodes() {
        return new Node[]{y};
    }

    @Override
    public void setInputNodes(Node[] nodes) {
        x = nodes[0];
    }

    @Override
    public void setOutputNodes(Node[] nodes) {
        y = nodes[0];
    }

}
