package io.github.liamtuan.semicon.sim.core;

import java.util.*;

public class Node {
    private boolean state;
    Set<Gate> ingates;
    Set<Gate> outgates;
    private int id;
    private static int ID_ = 0;

    Set<NodeStateListener> listeners;

    public Node()
    {
        this.state = false;
        ingates = new HashSet<Gate>();
        outgates = new HashSet<Gate>();
        listeners = new HashSet<>();
        id = ID_;
        ID_ ++;
    }

    public Node(boolean state){
        this.state = state;
        ingates = new HashSet<Gate>();
        outgates = new HashSet<Gate>();
    }

    public int getId(){
        return id;
    }

    public boolean getState(){
        return state;
    }

    public void setState(boolean state){
        if(this.state != state && !listeners.isEmpty()){
            this.state = state;
            for(NodeStateListener listener : listeners)
                listener.onNodeStateChanged(this);
        }else{
            this.state = state;
        }
    }

    public void addListener(NodeStateListener listener){
        listeners.add(listener);
    }
    public void removeListener(NodeStateListener listener) {
        listeners.remove(listener);
    }

    public void invokeListener(){
        for(NodeStateListener listener : listeners)
            listener.onNodeStateChanged(this);
    }

    @Override
    public String toString() {
        return "Node" + id;
    }

    public void merge(Node node){
        for(Gate gate : node.ingates){
            Node[] nodes = gate.getOutputNodes();
            for(int i = 0; i < nodes.length; i++){
                if(nodes[i] == node){
                    nodes[i] = this;
                    break;
                }
            }
            gate.setOutputNodes(nodes);
        }
        for(Gate gate : node.outgates) {
            Node[] nodes = gate.getInputNodes();
            for(int i = 0; i < nodes.length; i++){
                if(nodes[i] == node){
                    nodes[i] = this;
                    break;
                }
            }
            gate.setInputNodes(nodes);
        }
        ingates.addAll(node.ingates);
        outgates.addAll(node.outgates);
        listeners.addAll(node.listeners);
        if(node.state)
            this.state = true;
    }
}