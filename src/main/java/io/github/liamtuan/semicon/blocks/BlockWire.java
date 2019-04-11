package io.github.liamtuan.semicon.blocks;

import io.github.liamtuan.semicon.sim.Circuit;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class BlockWire extends BlockOriented {
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

    abstract EnumFacing[] getConnectedFaces();

    public EnumFacing[] getJointFaces(EnumFacing block_facing){
        EnumFacing[] faces = getConnectedFaces();
        for(int i = 0; i < faces.length; i++){
            faces[i] = getWorldFacing(block_facing, faces[i]);
        }
        return faces;
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        super.onBlockAdded(worldIn, pos, state);

        EnumFacing block_facing = state.getValue(PROPERTYFACING);
        EnumFacing[] jointfaces = getJointFaces(block_facing);
        Circuit.addBlockWire(pos, jointfaces);

    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        EnumFacing block_facing = state.getValue(PROPERTYFACING);
        EnumFacing[] faces = getJointFaces(block_facing);

        Circuit.removeBlockWire(pos, faces);
        super.breakBlock(worldIn, pos, state);
    }

}
