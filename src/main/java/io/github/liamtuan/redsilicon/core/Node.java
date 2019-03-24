package io.github.liamtuan.redsilicon.core;

import java.util.HashSet;
import java.util.Set;

public class Node{
    boolean state;
    Set<Gate> ingates;
    Set<Gate> outgates;

    public Node()
    {
        this.state = false;
        ingates = new HashSet<Gate>();
        outgates = new HashSet<Gate>();
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
        this.state = state;
    }

    @Override
    public String toString() {
        return state?"1":"0";
    }
}