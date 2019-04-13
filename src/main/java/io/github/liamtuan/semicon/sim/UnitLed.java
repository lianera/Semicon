package io.github.liamtuan.semicon.sim;

import io.github.liamtuan.semicon.sim.core.Node;

public class UnitLed extends UnitOutput {
    Node[] nodes;
    public UnitLed(Cell pos) {
        super(pos);
        nodes = new Node[Dir.DIRNUM];
        for(int i = 0; i < Dir.DIRNUM; i++)
            nodes[i] = new Node();
    }

    @Override
    Node getNode(Dir dir) {
        return nodes[dir.index()];
    }

    @Override
    void setNode(Dir dir, Node node) {
        nodes[dir.index()] = node;
    }

    boolean getState(){
        for(Node node : nodes)
            if(node.getState())
                return true;
        return false;
    }


    @Override
    public String toString() {
        String s = "Led{" + getPos() + ",";
        for(Dir d : Dir.values()){
            s += nodes[d.index()] + ",";
        }
        s += "}";
        return s;
    }
}
