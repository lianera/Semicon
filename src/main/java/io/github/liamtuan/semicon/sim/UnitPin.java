package io.github.liamtuan.semicon.sim;

import io.github.liamtuan.semicon.sim.core.Node;

import java.util.HashMap;
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
    public String toString() {
        return "Pin{" + getPos() + "," + dir + "," + state + "," + node + "}";
    }

    @Override
    void syncStateToNode() {
        node.setState(state);
    }

    @Override
    void setState(boolean state) {
        this.state = state;
        node.setState(state);
    }

    @Override
    Map<Dir, Node> getNodes() {
        Map<Dir, Node> map = new HashMap<>();
        map.put(dir, node);
        return map;
    }

    @Override
    void setNodes(Map<Dir, Node> nodemap) {
        Node t = nodemap.get(dir);
        if(t != null)
            node = t;
    }

}
