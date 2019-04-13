package io.github.liamtuan.semicon.sim;

import io.github.liamtuan.semicon.sim.core.Node;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class Unit {
    private Cell pos;
    Unit(Cell pos){
        this.pos = pos;
    }
    Cell getPos(){
        return pos;
    }
    abstract Node getNode(Dir dir);
    abstract void setNode(Dir dir, Node node);
    void update(){};
    void dettach(){};

    Set<Node> getAllNodes(){
        Set<Node> nodeset = new HashSet<>();
        for(Dir d : Dir.values()){
            Node node = getNode(d);
            if(node != null)
                nodeset.add(node);
        }
        return nodeset;
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

    abstract void setState(boolean state);
}

abstract class UnitOutput extends UnitIO{

    UnitOutput(Cell pos) {
        super(pos);
    }

    abstract boolean getState();
}