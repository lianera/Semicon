package io.github.liamtuan.semicon.blocks.io;

import io.github.liamtuan.semicon.Util;
import io.github.liamtuan.semicon.sim.Cell;
import io.github.liamtuan.semicon.sim.UnitLed;
import io.github.liamtuan.semicon.sim.Unit;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockLed extends BlockOutput {
    public BlockLed() {
        setRegistryName("led");
        setUnlocalizedName("led");
    }

    @Override
    public Unit createUnit(World worldIn, BlockPos pos) {
        Cell cell = Util.blockPosToCell(pos);
        return new UnitLed(cell);
    }
}
