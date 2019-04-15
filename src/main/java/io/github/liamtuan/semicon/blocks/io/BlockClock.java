package io.github.liamtuan.semicon.blocks.io;

import io.github.liamtuan.semicon.Util;
import io.github.liamtuan.semicon.sim.*;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockClock extends BlockOutput {
    public BlockClock() {
        setRegistryName("clock");
        setUnlocalizedName("clock");
    }

    @Override
    public Unit createUnit(World worldIn, BlockPos pos) {
        EnumFacing facing = getBlockFacing(worldIn, pos);
        Dir dir = Util.facingToDir(facing);
        Cell cell = Util.blockPosToCell(pos);
        return new UnitClock(cell, dir, 1.f);
    }

}
