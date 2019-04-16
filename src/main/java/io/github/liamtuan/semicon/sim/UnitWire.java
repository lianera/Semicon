package io.github.liamtuan.semicon.sim;

import io.github.liamtuan.semicon.sim.core.Node;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

public class UnitWire extends Unit {
    private Node[] nodes;
    private Dir[][] face_groups;
    public UnitWire(Cell pos, Dir[] faces) {
        super(pos);
        this.face_groups = new Dir[1][];
        this.face_groups[0] = faces;

        nodes = new Node[Dir.DIRNUM];

        Node node = new Node();
        for(Dir dir : faces)
            nodes[dir.index()] = node;
    }

    public UnitWire(Cell pos, Dir[][] connected_face_groups){
        super(pos);
        this.face_groups = connected_face_groups;

        nodes = new Node[Dir.DIRNUM];
        for(Dir[] connected_faces : connected_face_groups){
            Node node = new Node();
            for(Dir face : connected_faces){
                nodes[face.index()] = node;
            }
        }
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
        JSONArray face_group_arr = new JSONArray();
        for(Dir[] faces : face_groups) {
            JSONArray facearr = new JSONArray();
            for(Dir face : faces){
                facearr.put(face.toString());
            }
            face_group_arr.put(facearr);
        }
        obj.put("face_groups", face_group_arr);
        return obj;
    }

    static UnitWire fromJson(JSONObject obj){
        Cell pos = Cell.fromString(obj.getString("pos"));
        JSONArray face_group_arr = obj.getJSONArray("face_groups");
        Dir[][] face_groups = new Dir[face_group_arr.length()][];
        for(int i = 0; i < face_group_arr.length(); i++){
            JSONArray facearr = face_group_arr.getJSONArray(i);
            Dir[] faces = new Dir[facearr.length()];
            for(int j = 0; j < facearr.length(); j++){
                faces[j] = Dir.valueOf(facearr.getString(j));
            }
            face_groups[i] = faces;
        }
        return new UnitWire(pos, face_groups);
    }
}
