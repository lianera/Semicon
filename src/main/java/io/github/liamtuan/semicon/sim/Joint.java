package io.github.liamtuan.semicon.sim;

import net.minecraft.block.Block;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Joint{
    BlockPos pos;
    EnumFacing facing;

    Joint(BlockPos pos, EnumFacing facing){
        this.pos = pos;
        this.facing = facing;
    }

    Joint linkedJoint(){
        BlockPos linkedpos = pos.offset(facing);
        EnumFacing linkedfacing = facing.getOpposite();
        return new Joint(linkedpos, linkedfacing);
    }

    boolean isLinked(Joint j){
        return this.linkedJoint().equals(j);
    }

    Block getBlock(World world){
        return world.getBlockState(pos).getBlock();
    }

    @Override
    public boolean equals(Object obj) {
        Joint jobj = (Joint)obj;
        return jobj.pos.equals(pos) && jobj.facing == facing;
    }

    @Override
    public int hashCode() {
        return pos.hashCode()*6 + facing.hashCode();
    }

    @Override
    public String toString() {
        return pos.toString() + " " + facing.toString();
    }
}