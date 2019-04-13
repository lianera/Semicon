package io.github.liamtuan.semicon.sim;

import io.github.liamtuan.semicon.sim.core.Node;

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
    Node getNode(Dir dir) {
        return nodes[dir.index()];
    }

    @Override
    void setNode(Dir dir, Node node) {
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
}
