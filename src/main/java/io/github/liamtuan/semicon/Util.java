package io.github.liamtuan.semicon;

import io.github.liamtuan.semicon.sim.Cell;
import io.github.liamtuan.semicon.sim.Dir;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import static io.github.liamtuan.semicon.sim.Dir.*;

public class Util {
    public static Cell blockPosToCell(BlockPos pos){
        return new Cell(pos.getX(), pos.getY(), pos.getZ());
    }

    public static BlockPos cellToBlockPos(Cell cell){
        return new BlockPos(cell.x, cell.y, cell.z);
    }

    public static Dir facingToDir(EnumFacing facing){
        switch (facing){
            case EAST:
                return POSX;
            case UP:
                return POSY;
            case SOUTH:
                return POSZ;
            case WEST:
                return NEGX;
            case DOWN:
                return NEGY;
            case NORTH:
                return NEGZ;
        }
        return NEGZ;
    }

    public static Dir[] localFacesToWorldDirs(EnumFacing[] faces, EnumFacing base){
        Dir basedir = Util.facingToDir(base);
        Dir[] dirs = new Dir[faces.length];
        for(int i = 0; i < faces.length; i++){
            dirs[i] = Util.facingToDir(faces[i]).horizOffsetDir(basedir);
        }
        return dirs;
    }

    public static Dir[][] localFaceGroupsToWorldDirGroups(EnumFacing[][] face_groups, EnumFacing base) {
        Dir basedir = Util.facingToDir(base);
        Dir[][] dir_groups = new Dir[face_groups.length][];
        for(int i = 0; i < face_groups.length; i++){
            EnumFacing[] faces = face_groups[i];
            Dir[] dirs = new Dir[faces.length];
            for(int j = 0; j < faces.length; j++){
                dirs[j] = Util.facingToDir(faces[j]).horizOffsetDir(basedir);
            }
            dir_groups[i] = dirs;
        }
        return dir_groups;
    }
}
