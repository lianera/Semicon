package io.github.liamtuan.semicon.sim;

import com.sun.javaws.exceptions.InvalidArgumentException;
import com.sun.jna.platform.win32.WinBase;
import io.github.liamtuan.semicon.sim.core.Gate;
import io.github.liamtuan.semicon.sim.core.Node;
import org.json.JSONArray;
import org.json.JSONObject;

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

    DataTable createFromJson(JSONObject obj) throws InvalidArgumentException {
        Map<Integer, Gate> gatetable = new HashMap<>();
        Map<Integer, JSONObject> gate_obj_table = new HashMap<>();
        Map<Integer, Node> nodetable = new HashMap<>();

        JSONArray gate_arr = obj.getJSONArray("gates");
        for(int i = 0; i < gate_arr.length(); i++){
            JSONObject gateobj = gate_arr.getJSONObject(i);
            Gate gate = Gate.createGateFromJson(gateobj);
            gatetable.put(gate.getId(), gate);
            gate_obj_table.put(gate.getId(), gateobj);
        }
        JSONArray node_arr = obj.getJSONArray("nodes");
        for(int i = 0; i < node_arr.length(); i++){
            JSONObject nodeobj = node_arr.getJSONObject(i);
            Node node = Node.createNodeFromJson(nodeobj, gatetable);
            nodetable.put(node.getId(), node);
        }
        // attach gates to nodes
        for(Gate gate : gatetable.values()){
            JSONObject gateobj = gate_obj_table.get(gate.getId());
            gate.attachNodesFromJson(gateobj, nodetable);
        }

        DataTable data = new DataTable();

        // units
        JSONArray unit_arr = obj.getJSONArray("units");
        for(int i = 0; i < unit_arr.length(); i++){
            JSONObject unitobj = unit_arr.getJSONObject(i);
            Unit unit = Unit.createUnitFromJson(unitobj, nodetable, gatetable);
            data.putUnit(unit.getPos(), unit);
        }
        return data;
    }

    JSONObject serializeToJson(){
        JSONObject obj = new JSONObject();
        Set<Unit> allunits = new HashSet<>(cell_unit.values());
        Set<Node> allnodes = new HashSet<>();
        Set<Gate> allgates = new HashSet<>();
        for(Unit unit : allunits){
            allnodes.addAll(unit.getNodeSet());
            if(unit instanceof UnitGate){
                UnitGate unitgate = (UnitGate)unit;
                allgates.add(unitgate.getGate());
            }
        }

        JSONArray unit_arr = new JSONArray();
        for(Unit unit : allunits)
            unit_arr.put(unit.serializeToJson());
        obj.put("units", unit_arr);

        JSONArray node_arr = new JSONArray();
        for(Node node : allnodes)
            node_arr.put(node.serializeToJson());
        obj.put("nodes", node_arr);

        JSONArray gate_arr = new JSONArray();
        for(Gate gate : allgates)
            gate_arr.put(gate.serializeToJson());
        obj.put("gates", gate_arr);

        return obj;
    }
}
