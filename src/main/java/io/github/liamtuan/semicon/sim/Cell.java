package io.github.liamtuan.semicon.sim;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Cell{
    World world;
    BlockPos pos;

    Cell(World world, BlockPos pos){
        this.world = world;
        this.pos = pos;
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
                jointobj.pos.getZ() == pos.getZ();
    }

    @Override
    public int hashCode() {
        return pos.getX()*5000000 +
                pos.getY()*5000 +
                pos.getZ()*5 +
                world.hashCode();
    }
}