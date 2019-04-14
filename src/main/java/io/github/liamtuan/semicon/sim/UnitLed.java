package io.github.liamtuan.semicon.sim;

import com.sun.javaws.exceptions.InvalidArgumentException;
import io.github.liamtuan.semicon.sim.core.Node;
import io.github.liamtuan.semicon.sim.core.NodeStateListener;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UnitLed extends UnitOutput {
    private NodeStateListener node_listener;
    Node[] nodes;
    public UnitLed(Cell pos) {
        super(pos);
        initNodeListener();
        nodes = new Node[Dir.DIRNUM];
        for(int i = 0; i < Dir.DIRNUM; i++) {
            Node node = new Node();
            nodes[i] = node;
            node.addListener(node_listener);
        }
    }

    public UnitLed(Cell pos, Node[] nodes){
        super(pos);
        initNodeListener();
        this.nodes = nodes;
        for(int i = 0; i < Dir.DIRNUM; i++) {
            this.nodes[i].addListener(node_listener);
        }
    }

    void initNodeListener(){
        node_listener = new NodeStateListener() {
            @Override
            public void onNodeStateChanged(Node node) {
                if(output_listener != null){
                    output_listener.onStateChanged(getPos(), node.getState());
                }
            }
        };
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
            nodes[d.index()].removeListener(node_listener);
            nodes[d.index()] = entry.getValue();
            nodes[d.index()].addListener(node_listener);
        }
    }

    boolean getState(){
        for(Node node : nodes)
            if(node.getState())
                return true;
        return false;
    }

    @Override
    void dettach() {
        for(Node node : nodes)
            node.removeListener(node_listener);
    }

    @Override
    JSONObject serializeToJson() {
        JSONObject obj = super.serializeToJson();
        obj.put("type", "led");
        JSONArray nodes_arr = new JSONArray();
        for(Node node : nodes)
            nodes_arr.put(node.getId());
        obj.put("nodes", nodes_arr);
        return obj;
    }


    static UnitLed createFromJson(JSONObject obj, Map<Integer, Node> nodetable) throws InvalidArgumentException {
        if(obj.getString("type") != "led")
            throw new InvalidArgumentException(new String[]{"json object is not led"});

        Cell pos = Cell.fromString(obj.getString("pos"));
        JSONArray node_arr = obj.getJSONArray("nodes");
        List<Node> nodes = new ArrayList<>();
        for(int i = 0; i < node_arr.length(); i++){
            int nodeid = node_arr.getInt(i);
            Node node = nodetable.get(nodeid);
            nodes.add(node);
        }
        UnitLed led = new UnitLed(pos, nodes.toArray(new Node[0]));
        return led;
    }
}
