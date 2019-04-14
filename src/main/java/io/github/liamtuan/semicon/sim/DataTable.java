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

    void putUnit(Cell cell, Unit unit){
        cell_unit.put(cell, unit);
        Set<Node> nodeset = unit.getNodeSet();
        for(Node node : nodeset){
            addNodeCell(node, cell);
        }
    }

    Unit getUnit(Cell cell){
        return cell_unit.get(cell);
    }

    Set<Cell> getNodeCells(Node node){
        return node_cells.get(node);
    }

    Set<Unit> getNodeUnits(Node node){
        Set<Unit> units = new HashSet<>();
        Set<Cell> cells = node_cells.get(node);
        if(cells == null)
            return units;
        for(Cell cell : cells)
            units.add(cell_unit.get(cell));
        return units;
    }

    void removeCell(Cell cell){
        Unit unit = cell_unit.get(cell);
        Set<Node> nodeset = unit.getNodeSet();
        for(Node node : nodeset) {
            removeNodeCell(node, cell);
        }
        cell_unit.remove(cell);
    }

    void replaceNode(Node src, Node dst){
        Set<Cell> srccells = node_cells.get(src);
        Set<Cell> dstcells = node_cells.get(dst);
        if(srccells == null)
            return;
        if(dstcells == null){
            dstcells = new HashSet<>(srccells);
            node_cells.put(dst, dstcells);
        }
        // replace all nodes
        for(Cell sc : srccells){
            Unit unit = cell_unit.get(sc);
            unit.replaceNode(src, dst);
        }

        // combine node cells
        dstcells.addAll(srccells);
        node_cells.remove(src);
    }

    void setUnitNode(Unit unit, Dir dir, Node node){
        Node oldnode = unit.getNode(dir);
        unit.setNode(dir, node);
        Set<Node> nodeset = unit.getNodeSet();
        if(!nodeset.contains(oldnode))
            removeNodeCell(oldnode, unit.getPos());
        addNodeCell(node, unit.getPos());
    }

    private void addNodeCell(Node node, Cell cell){
        Set<Cell> cellset = node_cells.get(node);
        if(cellset == null){
            cellset = new HashSet<>();
            node_cells.put(node, cellset);
        }
        cellset.add(cell);

    }

    private void removeNodeCell(Node node, Cell cell){
        Set<Cell> cellset = node_cells.get(node);
        if(cellset != null && cellset.contains(cell)) {
            cellset.remove(cell);
            if(cellset.isEmpty())
                node_cells.remove(node);
        }
    }

    public String toSvg(){
        Visualizer visualizer = new Visualizer(100, 100);
        return visualizer.toSvg(cell_unit);
    }

    public String nodeTableString(){
        String s = "";
        for(Map.Entry<Node, Set<Cell>> entry : node_cells.entrySet()){
            Node node = entry.getKey();
            s += node + ":" + node.getState() + "[";
            Set<Cell> cells = entry.getValue();
            for(Cell cell : cells){
                s += cell + ",";
            }
            s += "]\n";
        }
        return s;
    }
}
