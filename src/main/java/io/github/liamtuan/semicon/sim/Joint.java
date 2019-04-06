package io.github.liamtuan.semicon.sim;

import net.minecraft.block.Block;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Joint{
    World world;
    BlockPos pos;
    EnumFacing facing;

    Joint(World world, BlockPos pos, EnumFacing facing){
        this.world = world;
        this.pos = pos;
        this.facing = facing;
    }

    Joint linkedJoint(){
        BlockPos linkedpos = pos.offset(facing);
        EnumFacing linkedfacing = facing.getOpposite();
        return new Joint(world, linkedpos, linkedfacing);
    }

    boolean isLinked(Joint j){
        return this.linkedJoint().equals(j);
    }

    Block getBlock(){
        return world.getBlockState(pos).getBlock();
    }

    @Override
    public boolean equals(Object obj) {
        Joint jointobj = (Joint)obj;
        return jointobj.world == world &&
                jointobj.pos.getX() == pos.getX() &&
                jointobj.pos.getY() == pos.getY() &&
                jointobj.pos.getZ() == pos.getZ() &&
                jointobj.facing == facing;
    }

    public Cell getCell(){
        return new Cell(world, pos);
    }

    @Override
    public int hashCode() {
        return
                pos.getX()*320000000 +
                        pos.getY()*320000 +
                        pos.getZ()*320 +
                        facing.hashCode()*5 +
                        world.hashCode();
    }
}