package io.github.liamtuan.semicon.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;

public class BlockIO extends BlockOriented{
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
        blockState.withProperty(PROPERTYSTATE, on_off_state);
        return blockState;
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        int meta = super.getMetaFromState(state);
        boolean on_off_state = state.getValue(PROPERTYSTATE);
        if(on_off_state)
            meta = meta & ~0x4;
        else
            meta = meta | 0x4;
        return meta;
    }
}
