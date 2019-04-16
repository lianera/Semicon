package io.github.liamtuan.semicon.blocks.wire;

import io.github.liamtuan.semicon.Util;
import io.github.liamtuan.semicon.blocks.BlockUnit;
import io.github.liamtuan.semicon.sim.*;
import io.github.liamtuan.semicon.sim.Unit;
import io.github.liamtuan.semicon.sim.UnitWire;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class BlockWire extends BlockUnit {
    public static final PropertyDirection PROPERTYFACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

    BlockWire(String name) {
        super(name);
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

    abstract EnumFacing[][] getLocalFaceGroups();

    @Override
    public Unit createUnit(World worldIn, BlockPos pos) {
        EnumFacing block_facing = getBlockFacing(worldIn, pos);
        Cell cell = Util.blockPosToCell(pos);
        Dir[][] dir_groups = Util.localFaceGroupsToWorldDirGroups(getLocalFaceGroups(), block_facing);
        UnitWire wire = new UnitWire(cell, dir_groups);
        return wire;
    }


    // orientation

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
