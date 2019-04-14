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
    JSONObject toJson() {
        JSONObject obj = new JSONObject();
        obj.put("type", "led");
        obj.put("pos", getPos().toString());
        return obj;
    }

    static UnitLed fromJson(JSONObject obj) {
        Cell pos = Cell.fromString(obj.getString("pos"));
        return new UnitLed(pos);
    }
}
