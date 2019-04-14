package io.github.liamtuan.semicon.sim;

import java.util.Map;

public class Visualizer {

    int cellwidth;
    int cellheight;

    int basex, basey;

    Visualizer(int cellwidth, int cellheight){
        this.cellwidth = cellwidth;
        this.cellheight = cellheight;
    }

    public String toSvg(Map<Cell, Unit> cellunit){
        String s = "";
        int minx = Integer.MAX_VALUE, miny = Integer.MAX_VALUE, maxx = Integer.MIN_VALUE, maxy = Integer.MIN_VALUE;
        for(Cell cell : cellunit.keySet()){
            if(cell.x > maxx)
                maxx = cell.x;
            if(cell.x < minx)
                minx = cell.x;
            if(cell.z > maxy)
                maxy = cell.z;
            if(cell.z < miny)
                miny = cell.z;

        }
        this.basex = minx*cellwidth;
        this.basey = miny*cellheight;
        int width = (maxx - minx + 1)*cellwidth;
        int height = (maxy - miny + 1)*cellheight;

        for(Map.Entry<Cell, Unit> entry : cellunit.entrySet()){
            Cell cell = entry.getKey();


            Unit unit = entry.getValue();
            s += unitSvg(unit) + "\n";
        }
        return "<svg " + size(width, height) + ">\n" + s + "</svg>";
    }

    private String unitSvg(Unit unit){
        Cell pos = unit.getPos();
        int bx = pos.x*cellwidth;
        int by = pos.z*cellheight;
        int cx = bx + cellwidth/2;
        int cy = by + cellheight / 2;
        if(unit instanceof UnitWire) {
            UnitWire wire = (UnitWire)unit;
            String s = "";
            for(Dir d : wire.getFaces()){
                if(d == Dir.POSX)
                    s += lineSvg(cx, cy, bx + cellwidth, cy);
                else if(d == Dir.POSZ)
                    s += lineSvg(cx, cy, cx, by + cellheight);
                else if(d == Dir.NEGX)
                    s += lineSvg(cx, cy, bx, cy);
                else if(d == Dir.NEGZ)
                    s += lineSvg(cx, cy, cx, by);
                s += "\n";
            }
            return s;
        }else {
            String rect = rectSvg(pos);
            String unitname = unit.toString();
            String label = textSvg(unitname, bx, cy);
            return rect + "\n" + label;
        }
    }

    private String lineSvg(int x1, int y1, int x2, int y2){
        return "<line x1=\"" + (x1-basex) + "\" y1=\"" + (y1-basey) +
                "\" x2=\"" + (x2-basex) + "\" y2=\"" + (y2-basey) +
                "\" style=\"stroke:rgb(255,0,0);stroke-width:2\" />";
    }

    private String textSvg(String text, int x, int y){
        return "<text x=\"" + (x-basex) + "\" y=\"" + (y-basey) +
                "\" dominant-baseline=\"middle\" stroke=\"white\" font-size=\"10\">"+
                text + "</text>";
    }

    private String rectSvg(Cell cell){
        return "<rect " + position(cell) + size(cellwidth, cellheight) +
                "fill=\"#434343\" stroke=\"yellow\"" +
                "/>";
    }



    private String size(int w, int h){
        return " width=\"" + w + "\" height=\"" + h + "\" ";
    }

    private String position(Cell pos){
        return " x=\"" + (pos.x*cellwidth - basex) + "\" " +
                " y=\"" + (pos.z*cellheight - basey) + "\" ";
    }


}
