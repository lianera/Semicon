package io.github.liamtuan.semicon.blocks.io;

import io.github.liamtuan.semicon.Util;
import io.github.liamtuan.semicon.sim.Cell;
import io.github.liamtuan.semicon.sim.UnitLed;
import io.github.liamtuan.semicon.sim.Unit;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class BlockLed extends BlockOutput {
    public BlockLed() {
        setRegistryName("led");
        setUnlocalizedName("led");
        setLightLevel(0.5f);
    }

    @Override
    public Unit createUnit(World worldIn, BlockPos pos) {
        Cell cell = Util.blockPosToCell(pos);
        return new UnitLed(cell);
    }
}
