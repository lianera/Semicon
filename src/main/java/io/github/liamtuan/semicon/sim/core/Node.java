package io.github.liamtuan.semicon.sim.core;

import com.sun.javaws.exceptions.InvalidArgumentException;
import org.json.*;

import java.util.*;

public class Node {
    private boolean state;
    Set<Gate> ingates;
    Set<Gate> outgates;
    private int id;
    private static int ID_ = 0;

    Set<NodeStateListener> listeners;

    public Node()
    {
        this.state = false;
        ingates = new HashSet<Gate>();
        outgates = new HashSet<Gate>();
        listeners = new HashSet<>();
        id = ID_;
        ID_ ++;
    }

    public Node(boolean state){
        this.state = state;
        ingates = new HashSet<Gate>();
        outgates = new HashSet<Gate>();
    }

    public int getId(){
        return id;
    }

    public boolean getState(){
        return state;
    }

    public void setState(boolean state){
        if(this.state != state && !listeners.isEmpty()){
            this.state = state;
            for(NodeStateListener listener : listeners)
                listener.onNodeStateChanged(this);
        }else{
            this.state = state;
        }
    }

    public void addListener(NodeStateListener listener){
        listeners.add(listener);
    }
    public void removeListener(NodeStateListener listener) {
        listeners.remove(listener);
    }

    public void invokeListener(){
        for(NodeStateListener listener : listeners)
            listener.onNodeStateChanged(this);
    }

    @Override
    public String toString() {
        return serializeToJson().toString();
    }

    public void merge(Node node){
        for(Gate gate : node.ingates){
            Node[] nodes = gate.getOutputNodes();
            for(int i = 0; i < nodes.length; i++){
                if(nodes[i] == node){
                    nodes[i] = this;
                    break;
                }
            }
            gate.setOutputNodes(nodes);
        }
        for(Gate gate : node.outgates) {
            Node[] nodes = gate.getInputNodes();
            for(int i = 0; i < nodes.length; i++){
                if(nodes[i] == node){
                    nodes[i] = this;
                    break;
                }
            }
            gate.setInputNodes(nodes);
        }
        ingates.addAll(node.ingates);
        outgates.addAll(node.outgates);
        listeners.addAll(node.listeners);
        if(node.state)
            this.state = true;
    }

    public JSONObject serializeToJson(){
        JSONObject obj = new JSONObject();
        obj.put("type", "node");
        obj.put("id", id);
        obj.put("state", state);
        JSONArray ingate_arr = new JSONArray();
        for(Gate gate : ingates){
            ingate_arr.put(gate.getId());
        }
        obj.put("ingates", ingate_arr);

        JSONArray outgate_arr = new JSONArray();
        for(Gate gate : outgates){
            outgate_arr.put(gate.getId());
        }
        obj.put("outgates", outgate_arr);
        return obj;
    }

    public static Node createNodeFromJson(JSONObject obj, Map<Integer, Gate> gatetable) throws InvalidArgumentException {
        if(obj.getString("type") != "node")
            throw new InvalidArgumentException(new String[]{"json object is not node"});
        Node node = new Node();
        node.id = obj.getInt("id");
        ID_ = node.id+1;
        node.state = obj.getBoolean("state");
        JSONArray ingate_arr = obj.getJSONArray("ingates");
        for(int i = 0; i < ingate_arr.length(); i++){
            int id = ingate_arr.getInt(i);
            Gate gate = gatetable.get(id);
            node.ingates.add(gate);
        }
        JSONArray outgate_arr = obj.getJSONArray("outgates");
        for(int i = 0; i < outgate_arr.length(); i++){
            int id = outgate_arr.getInt(i);
            Gate gate = gatetable.get(id);
            node.outgates.add(gate);
        }
        return node;
    }
}