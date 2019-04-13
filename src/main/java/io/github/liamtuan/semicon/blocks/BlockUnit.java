package io.github.liamtuan.semicon.blocks;

import io.github.liamtuan.semicon.Util;
import io.github.liamtuan.semicon.sim.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BlockUnit extends BlockOriented{
    public BlockUnit(Material material) {
        super(material);
    }

    abstract public Unit createUnit(World worldIn, BlockPos pos);
    abstract public void rightClicked(World worldIn, BlockPos pos, EnumFacing handonface);

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        super.onBlockAdded(worldIn, pos, state);
        Unit unit = createUnit(worldIn, pos);
        Circuit.add(unit);
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        Cell cell = Util.blockPosToCell(pos);
        Circuit.remove(cell);
        super.breakBlock(worldIn, pos, state);
    }


    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos,
                                    IBlockState state,
                                    EntityPlayer playerIn,
                                    EnumHand hand,
                                    EnumFacing facing,
                                    float hitX, float hitY, float hitZ) {

        if(!worldIn.isRemote) {
            rightClicked(worldIn, pos, facing);
            return true;
        }
        return false;
    }
}
