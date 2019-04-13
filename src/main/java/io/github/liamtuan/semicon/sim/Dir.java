package io.github.liamtuan.semicon.sim;

public enum Dir {
    POSX(0),
    POSY(1),
    POSZ(2),
    NEGX(3),
    NEGY(4),
    NEGZ(5);

    private final int index;
    private Dir(int index){
        this.index = index;
    }

    private static final Dir[] opposite_dirs = {
            NEGX, NEGY, NEGZ,
            POSX, POSY, POSZ,
    };
    public Dir opposite(){
        return opposite_dirs[index];
    }

    private static final Dir[] horiz_dirs = {
        POSX, POSZ, NEGX, NEGZ
    };
    public static Dir[] horizValues(){
        return horiz_dirs;
    }

    private static final int[] horiz_angles = {
        90, 0, 180,
        270, 0, 0
    };
    public int getHorizAngle(){
        return horiz_angles[index];
    }
    public static Dir fromHorizAngle(int angle){
        angle = ((angle%360)+360)%360;
        switch (angle){
            case 90:
                return POSX;
            case 180:
                return POSZ;
            case 270:
                return NEGX;
            case 0:
                return NEGZ;
        }
        return POSY;
    }

    public Dir horizOffsetDir(Dir base){
        return fromHorizAngle(getHorizAngle() + base.getHorizAngle());
    }

    public Dir horizRelativeTo(Dir target){
        return fromHorizAngle(getHorizAngle() - target.getHorizAngle());
    }

    Cell offset(Cell cell){
        int dx = 0, dy = 0, dz = 0;
        switch (index){
            case 0: dx = 1; break;
            case 1: dy = 1; break;
            case 2: dz = 1; break;
            case 3: dx = -1; break;
            case 4: dy = -1; break;
            case 5: dz = -1; break;
        }
        return new Cell(cell.x + dx, cell.y + dy, cell.z + dz);
    }

    public int index(){
        return index;
    }

    public static final int DIRNUM = 6;

    @Override
    public String toString() {
        final String[] names = {
                "POSX", "POSY", "POSZ",
                "NEGX", "NEGY", "NEGZ",
        };
        return names[index];
    }
}
