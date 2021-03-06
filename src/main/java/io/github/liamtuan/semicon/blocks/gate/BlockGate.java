package io.github.liamtuan.semicon.blocks.gate;

import io.github.liamtuan.semicon.Util;
import io.github.liamtuan.semicon.blocks.BlockUnit;
import io.github.liamtuan.semicon.sim.*;
import io.github.liamtuan.semicon.sim.Unit;
import io.github.liamtuan.semicon.sim.UnitGate;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BlockGate extends BlockUnit {
    public static final PropertyDirection PROPERTYFACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

    BlockGate(String name) {
        super(name);
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



    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] {PROPERTYFACING});
    }

    @Override
    public IBlockState getStateForPlacement(World world,
                                            BlockPos pos,
                                            EnumFacing facing,
                                            float hitX,
                                            float hitY,
                                            float hitZ,
                                            int meta,
                                            EntityLivingBase placer,
                                            EnumHand hand)
    {
        // find the quadrant the player is facing
        EnumFacing enumfacing = (placer == null) ? EnumFacing.NORTH : EnumFacing.fromAngle(placer.rotationYaw);

        return this.getDefaultState().withProperty(PROPERTYFACING, enumfacing);
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        EnumFacing facing = EnumFacing.byHorizontalIndex(meta);
        return this.getDefaultState().withProperty(PROPERTYFACING, facing);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        EnumFacing facing = (EnumFacing)state.getValue(PROPERTYFACING);

        int facingbits = facing.getHorizontalIndex();
        return facingbits;
    }

    public EnumFacing getBlockFacing(World world, BlockPos pos){
        IBlockState state = world.getBlockState(pos);
        EnumFacing block_facing = state.getValue(PROPERTYFACING);
        return block_facing;
    }
}
