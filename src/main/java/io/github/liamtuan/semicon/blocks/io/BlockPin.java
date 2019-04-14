package io.github.liamtuan.semicon.blocks.io;

import io.github.liamtuan.semicon.App;
import io.github.liamtuan.semicon.Util;
import io.github.liamtuan.semicon.sim.*;
import io.github.liamtuan.semicon.sim.Unit;
import io.github.liamtuan.semicon.sim.UnitPin;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockPin extends BlockInput {
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
    public boolean rightClicked(World worldIn, BlockPos pos, EnumFacing handonface, boolean apply) {
        EnumFacing facing = getBlockFacing(worldIn, pos);
        if (facing == handonface)
            return false;
        if(apply) {
            boolean state = setBlockCircuitState(worldIn, pos);
            state = !state;
            setBlockCircuitState(worldIn, pos, state);
            Cell cell = Util.blockPosToCell(pos);
            App.getCircuit().setInpuState(cell, state);
        }
        return true;
    }
}