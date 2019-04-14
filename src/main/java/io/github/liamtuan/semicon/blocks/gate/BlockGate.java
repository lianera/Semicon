package io.github.liamtuan.semicon.blocks.gate;

import io.github.liamtuan.semicon.Util;
import io.github.liamtuan.semicon.blocks.BlockUnit;
import io.github.liamtuan.semicon.sim.*;
import io.github.liamtuan.semicon.sim.Unit;
import io.github.liamtuan.semicon.sim.UnitGate;
import net.minecraft.block.material.Material;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BlockGate extends BlockUnit {

    BlockGate() {
        super(Material.IRON);
    }

    abstract String getGateType();
    abstract EnumFacing[] getLocalInputFaces();
    abstract EnumFacing[] getLocalOutputFaces();

    @Override
    public Unit createUnit(World worldIn, BlockPos pos) {
        String gatetype = getGateType();
        Cell cell = Util.blockPosToCell(pos);
        EnumFacing facing = getBlockFacing(worldIn, pos);
        Dir[] inputdirs = Util.localFacesToWorldDirs(getLocalInputFaces(), facing);
        Dir[] outputdirs = Util.localFacesToWorldDirs(getLocalOutputFaces(), facing);
        UnitGate gate = new UnitGate(cell, inputdirs, outputdirs, gatetype);
        return gate;
    }
}
