package io.github.liamtuan.semicon.sim.core;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.Map;

public abstract class Gate{
    public abstract Node[] getInputNodes();
    public abstract Node[] getOutputNodes();
    public abstract void setInputNodes(Node[] nodes);
    public abstract void setOutputNodes(Node[] nodes);
    public abstract void evel();

    private static int ID_ = 0;
    private int id;

    public Gate(){
        id = ID_;
        ID_ ++;
    }

    abstract public String getName();

    public int getId(){
        return id;
    }

    public void attach()
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

    public static Gate createGateFromName(String name){
        Gate gate = null;
        switch (name){
            case "and":
                gate =  new AndGate();
                break;
            case "or":
                gate = new OrGate();
                break;
            case "not":
                gate = new NotGate();
                break;
            case "xor":
                gate = new XorGate();
                break;
            case "srlatch":
                gate = new SrLatchGate();
        }
        return gate;
    }

    @Override
    public String toString() {
        return "Gate"+id;
    }
}

