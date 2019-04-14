package io.github.liamtuan.semicon.sim;

import com.sun.javaws.exceptions.InvalidArgumentException;
import io.github.liamtuan.semicon.sim.core.Gate;
import io.github.liamtuan.semicon.sim.core.Node;
import org.json.JSONObject;

import java.util.*;

public abstract class Unit {
    Cell pos;
    Unit(Cell pos){
        this.pos = pos;
    }
    Cell getPos(){
        return pos;
    }
    abstract Map<Dir, Node> getNodes();
    abstract void setNodes(Map<Dir, Node> nodemap);

    Set<Node> getNodeSet(){
        Set<Node> nodes = new HashSet<>(getNodes().values());
        return nodes;
    }

    void replaceNode(Node oldnode, Node newnode){
        Map<Dir, Node> nodes = getNodes();
        nodes.forEach((dir, node)->{
            if(node == oldnode)
                nodes.put(dir, newnode);
        });
        setNodes(nodes);
    }

    Node getNode(Dir dir){
        Map<Dir, Node> nodes = getNodes();
        return nodes.get(dir);
    }

    void setNode(Dir dir, Node node){
        Map<Dir, Node> nodeset = new HashMap<>();
        nodeset.put(dir, node);
        setNodes(nodeset);
    }

    Set<Dir> getFaces(){
        return getNodes().keySet();
    }

    void dettach(){};

    JSONObject serializeToJson(){
        JSONObject obj = new JSONObject();
        obj.put("pos", getPos().toString());
        return obj;
    }

    static Unit createUnitFromJson(JSONObject obj, Map<Integer,Node> nodetable, Map<Integer, Gate> gatetable) throws InvalidArgumentException {
        String type = obj.getString("type");
        switch (type){
            case "gate":
                return UnitGate.createFromJson(obj, gatetable);
            case "led":
                return UnitLed.createFromJson(obj, nodetable);
            case "pin":
                return UnitPin.createFromJson(obj, nodetable);
            case "wire":
                return UnitWire.createFromJson(obj, nodetable);
        }
        return null;
    }

    @Override
    public String toString() {
        return serializeToJson().toString();
    }
}

abstract class UnitIO extends Unit{
    UnitIO(Cell pos) {
        super(pos);
    }
}

abstract class UnitInput extends UnitIO{

    UnitInput(Cell pos) {
        super(pos);
    }

    abstract void syncStateToNode();
    abstract void setState(boolean state);
}

abstract class UnitOutput extends UnitIO{
    StateListener output_listener;

    UnitOutput(Cell pos) {
        super(pos);
    }

    void setListener(StateListener listener){
        output_listener = listener;

    }
    abstract boolean getState();
}