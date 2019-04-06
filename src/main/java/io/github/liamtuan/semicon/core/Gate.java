package io.github.liamtuan.semicon.core;


public abstract class Gate{
    public abstract Node[] getInputNodes();
    public abstract Node[] getOutputNodes();
    public abstract void setInputNodes(Node[] nodes);
    public abstract void setOutputNodes(Node[] nodes);
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

    public void replaceNode(Node oldnode, Node newnode){
        Node[] inputs = getInputNodes();
        for(int i = 0; i < inputs.length; i++){
            Node node = inputs[i];
            if(node == oldnode){
                node.outgates.remove(this);
                newnode.outgates.add(this);
                inputs[i] = newnode;
            }
        }
        setInputNodes(inputs);
        Node[] outputs = getOutputNodes();
        for(int i = 0; i < outputs.length; i++){
            Node node = outputs[i];
            if(node == oldnode){
                node.ingates.remove(this);
                newnode.ingates.add(this);
                outputs[i] = newnode;
            }
        }
        setOutputNodes(outputs);
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
    public Node[] getInputNodes() {
        return new Node[]{x1, x2};
    }

    @Override
    public Node[] getOutputNodes() {
        return new Node[]{y};
    }

    @Override
    public void setInputNodes(Node[] nodes){
        x1 = nodes[0];
        x2 = nodes[1];
    }

    @Override
    public void setOutputNodes(Node[] nodes){
        y = nodes[0];
    }

}

