package io.github.liamtuan.semicon.sim;

import io.github.liamtuan.semicon.sim.core.Node;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UnitClock extends Unit {
    private Node node;
    private Dir dir;
    UnitClock(Cell pos, Dir dir) {
        super(pos);
        this.dir = dir;
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

    @Override
    JSONObject toJson() {
        return null;
    }
}
