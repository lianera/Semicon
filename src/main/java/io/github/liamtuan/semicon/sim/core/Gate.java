package io.github.liamtuan.semicon.sim.core;


import com.sun.javaws.exceptions.InvalidArgumentException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.omg.CORBA.PUBLIC_MEMBER;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class Gate{
    public abstract Node[] getInputNodes();
    public abstract Node[] getOutputNodes();
    public abstract void setInputNodes(Node[] nodes);
    public abstract void setOutputNodes(Node[] nodes);
    public abstract void evel();

    private static int ID_ = 0;
    private int id;

    public Gate(){
        id = ID_;
        ID_ ++;
    }

    abstract public String getName();

    public int getId(){
        return id;
    }

    public void attach()
    {
        for(Node node : getInputNodes()){
            node.outgates.add(this);
        }
        for(Node node : getOutputNodes()){
            node.ingates.add(this);
        }
    }

    public void dettach()
    {
        for(Node node : getInputNodes()){
            node.outgates.remove(this);
        }
        for(Node node : getOutputNodes()){
            node.ingates.remove(this);
        }
    }

    public void replaceNode(Node oldnode, Node newnode){
        Node[] inputs = getInputNodes();
        for(int i = 0; i < inputs.length; i++){
            Node node = inputs[i];
            if(node == oldnode){
                node.outgates.remove(this);
                newnode.outgates.add(this);
                inputs[i] = newnode;
            }
        }
        setInputNodes(inputs);
        Node[] outputs = getOutputNodes();
        for(int i = 0; i < outputs.length; i++){
            Node node = outputs[i];
            if(node == oldnode){
                node.ingates.remove(this);
                newnode.ingates.add(this);
                outputs[i] = newnode;
            }
        }
        setOutputNodes(outputs);
    }

    public static Gate createGateFromName(String name){
        Gate gate = null;
        switch (name){
            case "and":
                gate =  new AndGate();
                break;
            case "or":
                gate = new OrGate();
                break;
            case "not":
                gate = new NotGate();
                break;
        }
        return gate;
    }


    public JSONObject serializeToJson(){
        JSONObject obj = new JSONObject();
        obj.put("type", "node");
        obj.put("id", getId());
        obj.put("name", getName());
        JSONArray innode_arr = new JSONArray();
        for(Node node : getInputNodes()){
            innode_arr.put(node.getId());
        }
        obj.put("input_nodes", innode_arr);

        JSONArray outnode_arr = new JSONArray();
        for(Node node : getOutputNodes()){
            outnode_arr.put(node.getId());
        }
        obj.put("output_nodes", outnode_arr);
        return obj;
    }

    public Gate createGateFromJson(JSONObject obj) throws InvalidArgumentException {
        if(obj.getString("type") != "gate")
            throw new InvalidArgumentException(new String[]{"json object is not gate"});
        Gate gate = createGateFromName(obj.getString("name"));
        gate.id = obj.getInt("id");
        ID_ = gate.id+1;
        return gate;
    }

    public void attachNodesFromJson(Gate gate, JSONObject obj, Map<Integer, Node> nodetable) throws InvalidArgumentException {
        if(obj.getString("type") != "gate")
            throw new InvalidArgumentException(new String[]{"json object is not gate"});
        JSONArray innode_arr = obj.getJSONArray("input_nodes");
        List<Node> input_nodes = new ArrayList<>();
        for(int i = 0; i < innode_arr.length(); i++){
            int id = innode_arr.getInt(i);
            Node node = nodetable.get(id);
            input_nodes.add(node);
        }

        JSONArray output_arr = obj.getJSONArray("output_nodes");
        List<Node> output_nodes = new ArrayList<>();
        for(int i = 0; i < output_arr.length(); i++){
            int id = output_arr.getInt(i);
            Node node = nodetable.get(id);
            output_nodes.add(node);
        }
        gate.setInputNodes(input_nodes.toArray(new Node[0]));
        gate.setOutputNodes(output_nodes.toArray(new Node[0]));
        gate.attach();
    }
}

