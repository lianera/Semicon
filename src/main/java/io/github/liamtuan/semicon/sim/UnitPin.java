package io.github.liamtuan.semicon.sim;

import io.github.liamtuan.semicon.sim.core.Node;

import java.util.Map;

public class UnitPin extends UnitInput {
    private Dir dir;
    Node node;
    boolean state;

    public UnitPin(Cell pos, Dir dir){
        super(pos);
        this.dir = dir;
        node = new Node();
        state = false;
    }

    Node getNode(){
        return node;
    }

    @Override
    Node getNode(Dir dir) {
        if(dir == this.dir)
            return node;
        return null;
    }

    @Override
    void setNode(Dir dir, Node node) {
        if(dir == this.dir)
            this.node = node;
    }

    @Override
    public String toString() {
        return "Pin{" + getPos() + "," + dir + "," + state + "," + node + "}";
    }

    @Override
    void setState(boolean state) {
        this.state = state;
        node.setState(state);
    }

    @Override
    void update() {
        node.setState(state);
    }
}
