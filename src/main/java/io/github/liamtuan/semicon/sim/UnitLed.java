package io.github.liamtuan.semicon.sim;

import io.github.liamtuan.semicon.sim.core.Node;
import io.github.liamtuan.semicon.sim.core.NodeStateListener;

import java.util.HashMap;
import java.util.Map;

public class UnitLed extends UnitOutput {
    private NodeStateListener node_listener;
    Node[] nodes;
    public UnitLed(Cell pos) {
        super(pos);
        node_listener = new NodeStateListener() {
            @Override
            public void onNodeStateChanged(Node node) {
                if(output_listener != null){
                    output_listener.onStateChanged(getPos(), node.getState());
                }
            }
        };
        nodes = new Node[Dir.DIRNUM];
        for(int i = 0; i < Dir.DIRNUM; i++) {
            Node node = new Node();
            node.addListener(node_listener);
            nodes[i] = node;
        }
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
    public String toString() {
        String s = "Led{" + getPos() + ",";
        for(Dir d : Dir.values()){
            s += nodes[d.index()] + ",";
        }
        s += "}";
        return s;
    }
}
