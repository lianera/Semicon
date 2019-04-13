package io.github.liamtuan.semicon.blocks.io;

import io.github.liamtuan.semicon.Util;
import io.github.liamtuan.semicon.blocks.BlockOriented;
import io.github.liamtuan.semicon.blocks.BlockUnit;
import io.github.liamtuan.semicon.sim.Cell;
import io.github.liamtuan.semicon.sim.Circuit;
import io.github.liamtuan.semicon.sim.Dir;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BlockIO extends BlockUnit {
    public static final PropertyBool PROPERTYSTATE = PropertyBool.create("state");

    BlockIO() {
        super(Material.IRON);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        BlockStateContainer container = new BlockStateContainer(this, new IProperty[] {PROPERTYFACING, PROPERTYSTATE});
        return container;
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        IBlockState blockState = super.getStateFromMeta(meta);
        boolean on_off_state = (meta & 0x4) != 0;
        return blockState.withProperty(PROPERTYSTATE, on_off_state);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        int meta = super.getMetaFromState(state);
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
        IBlockState state = super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand);
        state = state.withProperty(PROPERTYSTATE, false);
        return state;
    }

    boolean getBlockState(World world, BlockPos pos){
        return world.getBlockState(pos).getValue(PROPERTYSTATE);
    }

    void setBlockState(World world, BlockPos pos, boolean state){
        IBlockState oldblockstate = world.getBlockState(pos);
        if(oldblockstate.getValue(PROPERTYSTATE) == state)
            return;
        IBlockState newblockstate = oldblockstate.withProperty(PROPERTYSTATE, state);
        world.setBlockState(pos, newblockstate);
    }
}


abstract class BlockInput extends BlockIO{
}

abstract class BlockOutput extends BlockIO{

}