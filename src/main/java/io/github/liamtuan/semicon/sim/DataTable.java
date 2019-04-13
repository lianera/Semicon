package io.github.liamtuan.semicon.sim;

import io.github.liamtuan.semicon.sim.core.Node;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DataTable {
    private static Map<Cell, Unit> cell_unit;
    private static Map<Node, Set<Cell>> node_cells;

    DataTable(){
        cell_unit = new HashMap<>();
        node_cells = new HashMap<>();
    }

    void put(Cell cell, Unit unit){
        cell_unit.put(cell, unit);
        Set<Node> nodeset = unit.getAllNodes();
        for(Node node : nodeset){
            Set<Cell> cellset = node_cells.get(node);
            if(cellset == null){
                cellset = new HashSet<>();
                node_cells.put(node, cellset);
            }
            cellset.add(cell);
        }
    }

    Unit get(Cell cell){
        return cell_unit.get(cell);
    }

    Set<Cell> get(Node node){
        return node_cells.get(node);
    }

    void remove(Cell cell){
        Unit unit = cell_unit.get(cell);
        Set<Node> nodeset = unit.getAllNodes();
        for(Node node : nodeset) {
            Set<Cell> cellset = node_cells.get(node);
            if(cellset != null && cellset.contains(cell)) {
                cellset.remove(cell);
                if(cellset.isEmpty())
                    node_cells.remove(node);
            }
        }
        cell_unit.remove(cell);
    }

    void replace(Node src, Node dst){
        Set<Cell> srccells = node_cells.get(src);
        Set<Cell> dstcells = node_cells.get(dst);
        if(srccells == null)
            return;
        if(dstcells == null){
            dstcells = new HashSet<>(srccells);
            node_cells.put(dst, dstcells);
        }
        // replace all nodes
        replace(srccells, src, dst);

        // combine node cells
        dstcells.addAll(srccells);
        node_cells.remove(src);
    }

    void replace(Set<Cell> cells, Node src, Node dst){
        for(Cell sc : cells){
            Unit unit = cell_unit.get(sc);
            for(Dir d : Dir.values()){
                if(unit.getNode(d) == src)
                    unit.setNode(d, dst);
            }
        }
    }

    void replaceJointsNode(Set<Joint> joints, Node src, Node dst){
        for(Joint joint : joints){
            Unit unit = cell_unit.get(joint.pos);
            if(unit.getNode(joint.dir) == src)
                unit.setNode(joint.dir, dst);
        }
    }
}
