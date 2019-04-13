package io.github.liamtuan.semicon.sim;

import io.github.liamtuan.semicon.sim.core.Node;

public class UnitClock extends Unit {
    private Node node;

    UnitClock(Cell pos) {
        super(pos);
    }

    @Override
    Node getNode(Dir dir) {
        if(dir == Dir.NEGZ)
            return node;
        return null;
    }

    @Override
    void setNode(Dir dir, Node node) {
        if(dir == Dir.NEGZ)
            this.node = node;
    }
}
