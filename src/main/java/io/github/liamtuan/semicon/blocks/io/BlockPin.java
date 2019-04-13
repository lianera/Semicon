package io.github.liamtuan.semicon.blocks.io;

import io.github.liamtuan.semicon.Util;
import io.github.liamtuan.semicon.sim.*;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockPin extends BlockIO {
    public BlockPin() {
        setRegistryName("pin");
        setUnlocalizedName("pin");

    }

    @Override
    public Unit createUnit(World worldIn, BlockPos pos) {
        EnumFacing facing = getBlockFacing(worldIn, pos);
        Dir dir = Util.facingToDir(facing);
        Cell cell = Util.blockPosToCell(pos);
        return new UnitPin(cell, dir);
    }

    @Override
    public void rightClicked(World worldIn, BlockPos pos, EnumFacing handonface) {
        EnumFacing facing = getBlockFacing(worldIn, pos);
        if (facing == handonface)
            return;
        boolean state = getBlockState(worldIn, pos);
        state = !state;
        setBlockState(worldIn, pos, state);
        Cell cell = Util.blockPosToCell(pos);
        Circuit.setInpuState(cell, state);
    }
}