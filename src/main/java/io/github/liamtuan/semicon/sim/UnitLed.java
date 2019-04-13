package io.github.liamtuan.semicon.sim;

import io.github.liamtuan.semicon.sim.core.Node;

import java.util.HashMap;
import java.util.Map;

public class UnitLed extends UnitOutput {
    Node[] nodes;
    public UnitLed(Cell pos) {
        super(pos);
        nodes = new Node[Dir.DIRNUM];
        for(int i = 0; i < Dir.DIRNUM; i++)
            nodes[i] = new Node();
    }

    @Override
    Map<Dir, Node> getNodes() {
        Map<Dir, Node> map = new HashMap<>();
        for(Dir d : Dir.values())
            map.put(d, nodes[d.index()]);
        return map;
    }

    @Override
    void setNodes(Map<Dir, Node> nodemap) {
        for(Map.Entry<Dir, Node> entry : nodemap.entrySet()){
            Dir d = entry.getKey();
            nodes[d.index()] = entry.getValue();
        }
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
