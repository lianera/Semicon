package io.github.liamtuan.semicon.blocks;

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

public abstract class BlockIO extends BlockOriented{
    public static final PropertyBool PROPERTYSTATE = PropertyBool.create("state");

    BlockIO() {
        super(Material.IRON);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        BlockStateContainer container = new BlockStateContainer(this, new IProperty[] {PROPERTYFACING, PROPERTYSTATE});
        return container;
    }

    public void setState(World worldIn, BlockPos pos, boolean on_off_state){
        IBlockState blockState = worldIn.getBlockState(pos);
        blockState = blockState.withProperty(PROPERTYSTATE, on_off_state);
        worldIn.setBlockState(pos, blockState);
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


    abstract EnumFacing[] getConnectedFaces();


    public EnumFacing[] getJointFaces(EnumFacing block_facing){
        EnumFacing[] faces = getConnectedFaces();
        for(int i = 0; i < faces.length; i++){
            faces[i] = getWorldFacing(block_facing, faces[i]);
        }
        return faces;
    }
}
