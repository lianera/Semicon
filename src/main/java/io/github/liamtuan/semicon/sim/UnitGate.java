package io.github.liamtuan.semicon.sim;

import com.sun.javaws.exceptions.InvalidArgumentException;
import io.github.liamtuan.semicon.sim.core.*;

import java.util.HashMap;
import java.util.Map;

public class UnitGate extends Unit {
    private Gate gate;
    private Map<Dir, Integer> input_idx;
    private Map<Dir, Integer> output_idx;
    public UnitGate(Cell pos, Dir[] input_faces, Dir[] output_faces, String gatetype) {
        super(pos);
        input_idx = new HashMap<>();
        output_idx = new HashMap<>();
        for(int i = 0; i < input_faces.length; i++)
            input_idx.put(input_faces[i], i);
        for(int i = 0; i < output_faces.length; i++)
            output_idx.put(output_faces[i], i);

        switch (gatetype.toLowerCase()){
            case "and":
                gate = new AndGate(new Node(), new Node(), new Node());
                break;
            case "or":
                gate = new OrGate(new Node(), new Node(), new Node());
                break;
            case "not":
                gate = new NotGate(new Node(), new Node());
            default:
                assert false;
        }
    }

    @Override
    Node getNode(Dir dir) {
        Integer id = input_idx.get(dir);
        if(id != null){
            Node[] inputs = gate.getInputNodes();
            return inputs[id];
        }
        id = output_idx.get(dir);
        if(id != null){
            Node[] outputs = gate.getOutputNodes();
            return outputs[id];
        }
        return null;
    }

    @Override
    void setNode(Dir dir, Node node) {
        Integer id = input_idx.get(dir);
        if(id != null){
            Node[] inputs = gate.getInputNodes();
            inputs[id] = node;
        }
        id = output_idx.get(dir);
        if(id != null){
            Node[] outputs = gate.getOutputNodes();
            outputs[id] = node;
        }
    }

    @Override
    void dettach() {
        gate.dettach();
    }

    @Override
    void update() {
        gate.evel();
    }

    @Override
    public String toString() {
        String s = "Gate{" + gate + "," + getPos() + ",";
        s += "input[";
        Node[] inputs = gate.getInputNodes();
        for (Map.Entry<Dir, Integer> entry : input_idx.entrySet()) {
            s += entry.getKey() + ":" + inputs[entry.getValue()] + ",";
        }
        s += "],output[";
        Node[] outpus = gate.getOutputNodes();
        for (Map.Entry<Dir, Integer> entry : output_idx.entrySet()) {
            s += entry.getKey() + ":" + outpus[entry.getValue()] + ",";
        }
        s += "]}";
        return s;
    }

}
