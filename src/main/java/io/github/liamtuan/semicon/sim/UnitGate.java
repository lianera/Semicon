package io.github.liamtuan.semicon.sim;

import io.github.liamtuan.semicon.sim.core.*;

import java.util.*;

public class UnitGate extends Unit {
    private Gate gate;
    private Dir[] input_faces;
    private Dir[] output_faces;
    public UnitGate(Cell pos, Dir[] input_faces, Dir[] output_faces, String gatetype) {
        super(pos);
        this.input_faces = input_faces;
        this.output_faces = output_faces;

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
    void dettach() {
        gate.dettach();
    }

    @Override
    Map<Dir, Node> getNodes() {
        Map<Dir, Node> map = new HashMap<>();
        Node[] inputs = gate.getInputNodes();
        for(int i = 0; i < input_faces.length; i++) {
            map.put(input_faces[i], inputs[i]);
        }
        Node[] outputs = gate.getOutputNodes();
        for(int i = 0; i < output_faces.length; i++) {
            map.put(output_faces[i], outputs[i]);
        }
        return map;
    }

    @Override
    void setNodes(Map<Dir, Node> nodemap) {
        Node[] inputs = gate.getInputNodes();
        Node[] outputs = gate.getOutputNodes();
        gate.dettach();
        for (int i = 0; i < input_faces.length; i++) {
            Node node = nodemap.get(input_faces[i]);
            if(node != null)
                inputs[i] = node;
        }
        for (int i = 0; i < output_faces.length; i++) {
            Node node = nodemap.get(output_faces[i]);
            if(node != null)
                outputs[i] = node;
        }
        gate.setInputNodes(inputs);
        gate.setOutputNodes(outputs);
        gate.attach();
    }

    @Override
    public String toString() {
        String s = "Gate{" + gate + "," + getPos() + ",";
        s += "input[";
        Node[] inputs = gate.getInputNodes();
        for(int i = 0; i < input_faces.length; i++){
            Dir d = input_faces[i];
            s += d + ":" + inputs[i] + ",";
        }
        s += "],output[";
        Node[] outpus = gate.getOutputNodes();
        for(int i = 0; i < output_faces.length; i++){
            Dir d = output_faces[i];
            s += d + ":" + outpus[i] + ",";
        }
        s += "]}";
        return s;
    }

    public Set<Node> getInputNodes(){
        Set<Node> nodes = new HashSet<>(Arrays.asList(gate.getInputNodes()));
        return nodes;
    }

    public Set<Node> getOutputNodes(){
        Set<Node> nodes = new HashSet<>(Arrays.asList(gate.getOutputNodes()));
        return nodes;
    }
}
