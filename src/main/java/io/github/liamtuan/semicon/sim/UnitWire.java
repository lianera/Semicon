package io.github.liamtuan.semicon.sim;

import io.github.liamtuan.semicon.sim.core.Node;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class UnitWire extends Unit {
    private Node[] nodes;
    public UnitWire(Cell pos, Dir[] faces) {
        super(pos);
        nodes = new Node[Dir.DIRNUM];

        Node node = new Node();
        for(Dir dir : faces)
            nodes[dir.index()] = node;
    }

    @Override
    public String toString() {
        String s =  "Wire{" + getPos() + ",";
        for(Dir d : Dir.values()){
            Node node = nodes[d.index()];
            if(node == null)
                continue;
            s += d + "=" + node + ",";
        }
        s+= "}";
        return s;
    }


    @Override
    Map<Dir, Node> getNodes() {
        Map<Dir, Node> map = new HashMap<>();
        for(Dir d : Dir.values()) {
            Node node = nodes[d.index()];
            if(node != null)
                map.put(d, nodes[d.index()]);
        }
        return map;
    }

    @Override
    void setNodes(Map<Dir, Node> nodemap) {
        for(Map.Entry<Dir, Node> entry : nodemap.entrySet()){
            Dir d = entry.getKey();
            nodes[d.index()] = entry.getValue();
        }
    }

    Set<Dir> getConnected(Dir dir){
        Set<Dir> result = new HashSet<>();
        Node node = nodes[dir.index()];
        for(Dir d : Dir.values()){
            if(d == dir)
                continue;
            if(nodes[d.index()] == node)
                result.add(d);
        }
        return result;
    }
}
