package io.github.liamtuan.semicon.blocks.io;

import io.github.liamtuan.semicon.sim.Unit;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockClock extends BlockIO {
    public BlockClock() {
        setRegistryName("clock");
        setUnlocalizedName("clock");
    }

    @Override
    public Unit createUnit(World worldIn, BlockPos pos) {
        return null;
    }

    @Override
    public void rightClicked(World worldIn, BlockPos pos, EnumFacing handonface) {

    }
}
