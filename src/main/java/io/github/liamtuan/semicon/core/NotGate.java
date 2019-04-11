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

    @Override
    void evel() {
        y.setState(!x.getState());
    }

    @Override
    public String toString() {
        return "NotGate" + getId();
    }
}
