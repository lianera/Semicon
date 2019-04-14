package io.github.liamtuan.semicon.sim;

import com.sun.javaws.exceptions.InvalidArgumentException;
import io.github.liamtuan.semicon.sim.core.Node;
import org.json.JSONObject;

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

    public UnitPin(Cell pos, Dir dir, Node node, boolean state){
        super(pos);
        this.dir = dir;
        this.node = node;
        this.state = state;
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

    @Override
    JSONObject serializeToJson() {
        JSONObject obj = super.serializeToJson();
        obj.put("type", "pin");
        obj.put("dir", dir.toString());
        obj.put("node", node.getId());
        obj.put("state", state);
        return obj;
    }

    static UnitPin createFromJson(JSONObject obj, Map<Integer, Node> nodetable) throws InvalidArgumentException {
        if(obj.getString("type") != "pin")
            throw new InvalidArgumentException(new String[]{"json object is not pin"});
        Cell pos = Cell.fromString(obj.getString("pos"));
        Dir dir = Dir.valueOf(obj.getString("dir"));
        Node node = nodetable.get(obj.getInt("node"));
        boolean state = obj.getBoolean("state");
        UnitPin pin = new UnitPin(pos, dir, node, state);
        return pin;
    }
}
