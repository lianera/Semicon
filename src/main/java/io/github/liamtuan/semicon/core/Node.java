package io.github.liamtuan.semicon.core;

import java.util.*;

public class Node{
    private boolean state;
    Set<Gate> ingates;
    Set<Gate> outgates;

    List<NodeStateListener> listeners;

    public Node()
    {
        this.state = false;
        ingates = new HashSet<Gate>();
        outgates = new HashSet<Gate>();
        listeners = new ArrayList<>();
    }

    public Node(boolean state){
        this.state = state;
        ingates = new HashSet<Gate>();
        outgates = new HashSet<Gate>();
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

    @Override
    public String toString() {
        return state?"1":"0";
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
    }
}