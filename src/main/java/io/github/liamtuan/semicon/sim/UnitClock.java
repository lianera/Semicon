package io.github.liamtuan.semicon.sim;

import io.github.liamtuan.semicon.sim.core.Node;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UnitClock extends UnitInput {
    private Node node;
    private Dir dir;
    private float phase;
    private float frequency;
    private boolean state;
    public UnitClock(Cell pos, Dir dir, float frequency, boolean state, float phase) {
        super(pos);
        this.dir = dir;
        this.phase = 0;
        this.frequency = frequency;
        this.state = state;
        this.node = new Node();
    }

    public UnitClock(Cell pos, Dir dir, float frequency){
        this(pos, dir, frequency, false, 0.f);
    }

    // update and return if complete cycles
    boolean update(float dt){
        phase += dt;
        float half_t = 0.5f/frequency;
        if(phase >= half_t){
            phase -= half_t;
            setState(!this.state);
            return true;
        }
        return false;
    }

    void setFrequency(float frequency){
        this.frequency = frequency;
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
    JSONObject toJson() {
        JSONObject obj = new JSONObject();
        obj.put("type", "clock");
        obj.put("pos", getPos().toString());
        obj.put("dir", dir.toString());
        obj.put("frequency", frequency);
        obj.put("state", state);
        obj.put("phase", phase);
        return obj;
    }


    static UnitClock fromJson(JSONObject obj)  {
        Cell pos = Cell.fromString(obj.getString("pos"));
        Dir dir = Dir.valueOf(obj.getString("dir"));
        float frequency = obj.getFloat("frequency");
        boolean state = obj.getBoolean("state");
        float phase = obj.getFloat("phase");
        return new UnitClock(pos, dir, frequency, state, phase);
    }
}
