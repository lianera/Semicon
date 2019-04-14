package io.github.liamtuan.semicon.sim;

import com.google.gson.JsonObject;
import com.sun.javaws.exceptions.InvalidArgumentException;
import io.github.liamtuan.semicon.sim.core.Node;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UnitPin extends UnitInput {
    private Dir dir;
    Node node;
    boolean state;

    public UnitPin(Cell pos, Dir dir, boolean state){
        super(pos);
        this.dir = dir;
        node = new Node();
        state = false;
    }

    public UnitPin(Cell pos, Dir dir){
        this(pos, dir, false);
    }

    Node getNode(){
        return node;
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

    @Override
    JSONObject toJson() {
        JSONObject obj = new JSONObject();
        obj.put("type", "pin");
        obj.put("pos", getPos().toString());
        obj.put("dir", dir.toString());
        obj.put("state", state);
        return obj;
    }

    static UnitPin fromJson(JSONObject obj)  {
        Cell pos = Cell.fromString(obj.getString("pos"));
        Dir dir = Dir.valueOf(obj.getString("dir"));
        boolean state = obj.getBoolean("state");
        return new UnitPin(pos, dir, state);
    }
}
