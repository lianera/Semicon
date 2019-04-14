package io.github.liamtuan.semicon.sim;

import com.sun.javaws.exceptions.InvalidArgumentException;
import io.github.liamtuan.semicon.sim.core.Node;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

public class UnitWire extends Unit {
    private Node[] nodes;
    public UnitWire(Cell pos, Dir[] faces) {
        super(pos);
        nodes = new Node[Dir.DIRNUM];

        Node node = new Node();
        for(Dir dir : faces)
            nodes[dir.index()] = node;
    }

    public UnitWire(Cell pos, Node[] nodes){
        super(pos);
        this.nodes = nodes;
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

    @Override
    JSONObject serializeToJson() {
        JSONObject obj = super.serializeToJson();
        obj.put("type", "wire");
        JSONArray nodes_arr = new JSONArray();
        for(Node node : nodes) {
            nodes_arr.put(node == null ? -1 : node.getId());
        }
        obj.put("nodes", nodes_arr);
        return obj;
    }


    static UnitWire createFromJson(JSONObject obj, Map<Integer, Node> nodetable) throws InvalidArgumentException {
        if(obj.getString("type") != "led")
            throw new InvalidArgumentException(new String[]{"json object is not wire"});

        Cell pos = Cell.fromString(obj.getString("pos"));
        JSONArray node_arr = obj.getJSONArray("nodes");
        Node[] nodes = new Node[Dir.values().length];
        if(node_arr.length() != nodes.length)
            throw new InvalidArgumentException(new String[]{"wire node num not match"});

        for(int i = 0; i < node_arr.length(); i++){
            int nodeid = node_arr.getInt(i);
            if(nodeid >= 0)
                nodes[i] = nodetable.get(nodeid);
        }
        UnitWire wire = new UnitWire(pos, nodes);
        return wire;
    }
}
