package io.github.liamtuan.semicon.sim;

import com.sun.javaws.exceptions.InvalidArgumentException;
import io.github.liamtuan.semicon.sim.core.Node;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

public class UnitWire extends Unit {
    private Node[] nodes;
    private Dir[] faces;
    public UnitWire(Cell pos, Dir[] faces) {
        super(pos);
        this.faces = faces;

        nodes = new Node[Dir.DIRNUM];

        Node node = new Node();
        for(Dir dir : faces)
            nodes[dir.index()] = node;
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
    JSONObject toJson() {
        JSONObject obj = new JSONObject();
        obj.put("type", "wire");
        obj.put("pos", getPos().toString());
        JSONArray facearr = new JSONArray();
        for(Dir face : faces) {
            facearr.put(face);
        }
        obj.put("faces", facearr);
        return obj;
    }

    static UnitWire fromJson(JSONObject obj){
        Cell pos = Cell.fromString(obj.getString("pos"));
        JSONArray facearr = obj.getJSONArray("faces");
        Dir[] faces = new Dir[facearr.length()];
        for(int i = 0; i < facearr.length(); i++){
            faces[i] = Dir.valueOf(facearr.getString(i));
        }
        return new UnitWire(pos, faces);
    }
}
