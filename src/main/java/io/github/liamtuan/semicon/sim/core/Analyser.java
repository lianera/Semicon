package io.github.liamtuan.semicon.sim.core;

import java.util.*;

public class Analyser {
    public String getGraphViz(Node[] inputs){
        Stack<Node> open = new Stack<>();
        Set<Node> closed = new HashSet<>();
        open.addAll(Arrays.asList(inputs));
        String s = "digraph circuit {\n";
        while(!open.isEmpty()){
            Node node = open.pop();
            if(closed.contains(node))
                continue;
            s += node + "\n";
            closed.add(node);
            for(Gate gate : node.outgates){
                for(Node next : gate.getOutputNodes()) {
                    open.push(next);
                    s += node + " -> " + next + "\n";
                }
            }
        }

        s += "}";
        return s;
    }
}
