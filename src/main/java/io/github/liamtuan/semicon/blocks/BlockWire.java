package io.github.liamtuan.semicon.blocks;

import io.github.liamtuan.semicon.Circuit;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        EnumFacing block_facing = state.getValue(PROPERTYFACING);
        List jointfacinglist = new ArrayList<EnumFacing>();
        for(EnumFacing jointfacing : getConnectedFaces()){
            jointfacing = getWorldFacing(block_facing, jointfacing);
            jointfacinglist.add(jointfacing);
        }
        EnumFacing[] jointfacearr = new EnumFacing[jointfacinglist.size()];
        jointfacinglist.toArray(jointfacearr);
        Circuit.addBlockWire(worldIn, pos, jointfacearr);
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
    }
}
