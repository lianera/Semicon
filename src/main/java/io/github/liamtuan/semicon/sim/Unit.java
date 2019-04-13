package io.github.liamtuan.semicon.sim;

import io.github.liamtuan.semicon.sim.core.Node;

import java.util.*;

public abstract class Unit {
    private Cell pos;
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

    void update(){};
    void dettach(){};
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

    abstract void setState(boolean state);
}

abstract class UnitOutput extends UnitIO{

    UnitOutput(Cell pos) {
        super(pos);
    }

    abstract boolean getState();
}