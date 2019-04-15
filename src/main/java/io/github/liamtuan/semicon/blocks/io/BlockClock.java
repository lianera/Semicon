package io.github.liamtuan.semicon.blocks.io;

import io.github.liamtuan.semicon.App;
import io.github.liamtuan.semicon.Util;
import io.github.liamtuan.semicon.blocks.BlockUnit;
import io.github.liamtuan.semicon.sim.*;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockClock extends BlockUnit {
    private static final int maxfreq = 3;
    public static final PropertyDirection PROPERTYFACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    public static final PropertyInteger PROPERTY_FREQUENCY = PropertyInteger.create("frequency", 0, maxfreq);

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

    @Override
    protected BlockStateContainer createBlockState() {
        BlockStateContainer container = new BlockStateContainer(this, PROPERTYFACING, PROPERTY_FREQUENCY);
        return container;
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        EnumFacing facing = EnumFacing.getHorizontal(meta);
        int freq = (meta >> 2)&0x3;
        return this.getDefaultState().withProperty(PROPERTYFACING, facing).withProperty(PROPERTY_FREQUENCY, freq);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        EnumFacing facing = (EnumFacing)state.getValue(PROPERTYFACING);
        int meta = facing.getHorizontalIndex();
        int freq = state.getValue(PROPERTY_FREQUENCY);
        meta |= (freq << 2);
        return meta;
    }


    int getFrequency(World world, BlockPos pos){
        return world.getBlockState(pos).getValue(PROPERTY_FREQUENCY);
    }

    public void setFrequency(World world, BlockPos pos, int frequency){
        IBlockState oldblockstate = world.getBlockState(pos);
        if(oldblockstate.getValue(PROPERTY_FREQUENCY) == frequency)
            return;

        IBlockState newblockstate = oldblockstate.withProperty(PROPERTY_FREQUENCY, frequency);
        world.setBlockState(pos, newblockstate);
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
        EnumFacing enumfacing = EnumFacing.fromAngle(placer.rotationYaw);
        return this.getDefaultState().withProperty(PROPERTYFACING, enumfacing).withProperty(PROPERTY_FREQUENCY, 0);
    }

    public EnumFacing getBlockFacing(World world, BlockPos pos){
        IBlockState state = world.getBlockState(pos);
        EnumFacing block_facing = state.getValue(PROPERTYFACING);
        return block_facing;
    }

    @Override
    public boolean rightClicked(World worldIn, BlockPos pos, EnumFacing handonface, boolean apply) {
        EnumFacing facing = getBlockFacing(worldIn, pos);
        if (facing == handonface)
            return false;
        if(apply) {
            int oldfreq = getFrequency(worldIn, pos);
            int newfreq = oldfreq + 1;
            if(newfreq > maxfreq)
                newfreq = 0;
            setFrequency(worldIn, pos, newfreq);
            Cell cell = Util.blockPosToCell(pos);
            int realfreq = 1<<newfreq;
            App.getCircuit().setClockFrequency(cell, (float)realfreq);
        }
        return true;
    }
}
