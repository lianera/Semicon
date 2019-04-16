package io.github.liamtuan.semicon.blocks.io;

import io.github.liamtuan.semicon.App;
import io.github.liamtuan.semicon.Util;
import io.github.liamtuan.semicon.blocks.BlockUnit;
import io.github.liamtuan.semicon.sim.*;
import io.github.liamtuan.semicon.sim.Unit;
import io.github.liamtuan.semicon.sim.UnitPin;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockPin extends BlockUnit {
    public static final PropertyDirection PROPERTYFACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    public static final PropertyBool PROPERTYSTATE = PropertyBool.create("state");

    public BlockPin() {
        super("pin");
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
            boolean state = getBlockCircuitState(worldIn, pos);
            state = !state;
            setBlockCircuitState(worldIn, pos, state);
            Cell cell = Util.blockPosToCell(pos);
            App.getCircuit().setInpuState(cell, state);
        }
        return true;
    }


    @Override
    protected BlockStateContainer createBlockState() {
        BlockStateContainer container = new BlockStateContainer(this, new IProperty[] {PROPERTYFACING, PROPERTYSTATE});
        return container;
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        EnumFacing facing = EnumFacing.byHorizontalIndex(meta);
        boolean on_off_state = (meta & 0x4) != 0;
        return this.getDefaultState().withProperty(PROPERTYFACING, facing).withProperty(PROPERTYSTATE, on_off_state);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        EnumFacing facing = (EnumFacing)state.getValue(PROPERTYFACING);

        int meta = facing.getHorizontalIndex();

        boolean on_off_state = state.getValue(PROPERTYSTATE);
        if(on_off_state)
            meta = meta | 0x4;
        else
            meta = meta & ~0x4;
        return meta;
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
        EnumFacing enumfacing = (placer == null) ? EnumFacing.NORTH : EnumFacing.fromAngle(placer.rotationYaw);
        return this.getDefaultState().withProperty(PROPERTYFACING, enumfacing).withProperty(PROPERTYSTATE, false);
    }

    boolean getBlockCircuitState(World world, BlockPos pos){
        return world.getBlockState(pos).getValue(PROPERTYSTATE);
    }

    public void setBlockCircuitState(World world, BlockPos pos, boolean state){
        IBlockState oldblockstate = world.getBlockState(pos);
        if(oldblockstate.getValue(PROPERTYSTATE) == state)
            return;

        IBlockState newblockstate = oldblockstate.withProperty(PROPERTYSTATE, state);
        world.setBlockState(pos, newblockstate);
    }


    public EnumFacing getBlockFacing(World world, BlockPos pos){
        IBlockState state = world.getBlockState(pos);
        EnumFacing block_facing = state.getValue(PROPERTYFACING);
        return block_facing;
    }
}