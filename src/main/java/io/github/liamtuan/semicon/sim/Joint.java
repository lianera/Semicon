package io.github.liamtuan.semicon.sim;

public class Joint {
    Cell cell;
    Dir dir;
    Joint(Cell cell, Dir dir){
        this.cell = cell;
        this.dir = dir;
    }

    Joint getLinkedJoint(){
        Cell pos = dir.offset(cell);
        return new Joint(pos, dir.opposite());
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Joint))
            return false;
        Joint jobj = (Joint)obj;
        return this.cell.equals(jobj.cell) && this.dir.equals(jobj.dir);
    }

    @Override
    public int hashCode() {
        return cell.hashCode()*6 + dir.hashCode();
    }

    @Override
    public String toString() {
        return "(" + cell.x + "," + cell.y + "," + cell.z + "," + dir + ")";
    }
}
