package io.github.liamtuan.semicon.sim;

public class Joint {
    Cell pos;
    Dir dir;
    Joint(Cell cell, Dir dir){
        this.pos = cell;
        this.dir = dir;
    }

    Joint linkedJoint(){
        return new Joint(dir.offset(pos), dir.opposite());
    }

    @Override
    public int hashCode() {
        return pos.hashCode()*6 + dir.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        Joint jobj = (Joint)obj;
        return jobj.pos.equals(pos) && jobj.dir.equals(dir);
    }
}
