package io.github.liamtuan.semicon.blocks.wire;

import io.github.liamtuan.semicon.Util;
import io.github.liamtuan.semicon.blocks.BlockUnit;
import io.github.liamtuan.semicon.sim.*;
import io.github.liamtuan.semicon.sim.Unit;
import io.github.liamtuan.semicon.sim.UnitWire;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class BlockWire extends BlockUnit {
    BlockWire() {
        super(Material.IRON);
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState iBlockState) {
        return false;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState iBlockState) {
        return EnumBlockRenderType.MODEL;
    }

    abstract EnumFacing[] getLocalFaces();

    @Override
    public Unit createUnit(World worldIn, BlockPos pos) {
        EnumFacing block_facing = getBlockFacing(worldIn, pos);
        Cell cell = Util.blockPosToCell(pos);
        Dir[] dirs = Util.localFacesToWorldDirs(getLocalFaces(), block_facing);
        UnitWire wire = new UnitWire(cell, dirs);
        return wire;
    }
}
