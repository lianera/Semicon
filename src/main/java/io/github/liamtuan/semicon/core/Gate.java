package io.github.liamtuan.semicon.core;


public abstract class Gate{
    abstract Node[] getInputNodes();
    abstract Node[] getOutputNodes();
    abstract void evel();
    void attach()
    {
        for(Node node : getInputNodes()){
            node.outgates.add(this);
        }
        for(Node node : getOutputNodes()){
            node.ingates.add(this);
        }
    }

    public void dettach()
    {
        for(Node node : getInputNodes()){
            node.outgates.remove(this);
        }
        for(Node node : getOutputNodes()){
            node.ingates.remove(this);
        }
    }
}

abstract class X2Y1Gate extends Gate{
    Node x1, x2;
    Node y;
    X2Y1Gate(Node x1, Node x2, Node y)
    {
        this.x1 = x1;
        this.x2 = x2;
        this.y = y;
        super.attach();
    }

    @Override
    Node[] getInputNodes() {
        return new Node[]{x1, x2};
    }

    @Override
    Node[] getOutputNodes() {
        return new Node[]{y};
    }
}

