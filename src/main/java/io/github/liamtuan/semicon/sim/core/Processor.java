package io.github.liamtuan.semicon.sim.core;

import java.util.*;

public class Processor {
    private Queue<Node> event_queue;
    private Queue<Gate> gate_queue;
    private Map<Gate, Integer> gate_eval_count;
    private final int MAX_EVAL_COUNT = 10;

    public Processor(){
        event_queue = new LinkedList<Node>();
        gate_queue = new LinkedList<Gate>();
    }

    public void eval(Node[] changed_nodes)
    {
        gate_eval_count = new HashMap<>();
        for(Node node : changed_nodes){
            event_queue.add(node);
        }

        while(!event_queue.isEmpty()){
            evenProcess();
            if(!gate_queue.isEmpty())
                gateProcess();
        }
    }

    private void evenProcess()
    {
        while(!event_queue.isEmpty()){
            Node node = event_queue.poll();
            for(Gate gate : node.outgates){
                Integer eval_count = gate_eval_count.get(gate);
                if(eval_count != null && eval_count.equals(MAX_EVAL_COUNT))
                    continue;
                if(gate_queue.contains(gate))
                    continue;
                gate_queue.add(gate);

                gate_eval_count.put(gate, eval_count == null ? 1 : eval_count + 1);
            }
        }
    }

    private void gateProcess()
    {
        while(!gate_queue.isEmpty()){
            Gate gate = gate_queue.poll();
            boolean[] oldstates = gateOutStates(gate);
            gate.evel();
            Node[] outs = gate.getOutputNodes();
            for(int i = 0; i < outs.length; i++){
                if(outs[i].getState() != oldstates[i])
                    event_queue.add(outs[i]);
            }
        }
    }

    private boolean[] gateOutStates(Gate gate)
    {
        Node[] outs = gate.getOutputNodes();
        boolean[] oldstates = new boolean[outs.length];
        for(int i = 0; i < outs.length; i++)
            oldstates[i] = outs[i].getState();
        return oldstates;
    }
}

