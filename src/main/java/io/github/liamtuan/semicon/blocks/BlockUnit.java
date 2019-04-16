package io.github.liamtuan.semicon.blocks;

import io.github.liamtuan.semicon.App;
import io.github.liamtuan.semicon.AppData;
import io.github.liamtuan.semicon.Util;
import io.github.liamtuan.semicon.sim.*;
import io.github.liamtuan.semicon.sim.Unit;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BlockUnit extends Block {
    public BlockUnit(String name) {
        this(name, Material.IRON);
    }
    public BlockUnit(String name, Material material){
        super(material);
        setRegistryName(name);
        setTranslationKey(App.MODID + "." + name);
    }

    abstract public Unit createUnit(World worldIn, BlockPos pos);
    public boolean rightClicked(World worldIn, BlockPos pos, EnumFacing handonface, boolean apply){
        return false;
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        super.onBlockAdded(worldIn, pos, state);
        Unit unit = createUnit(worldIn, pos);
        App.getCircuit().add(unit);
        App.updateOutputs(worldIn);

        AppData.get(worldIn).markDirty();
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        Cell cell = Util.blockPosToCell(pos);
        App.getCircuit().remove(cell);
        App.updateOutputs(worldIn);
        super.breakBlock(worldIn, pos, state);
    }


    @Override
    public boolean onBlockActivated(World worldIn,
                                    BlockPos pos,
                                    IBlockState state,
                                    EntityPlayer playerIn,
                                    EnumHand hand,
                                    EnumFacing facing,
                                    float hitX, float hitY, float hitZ) {
       if(hand == EnumHand.MAIN_HAND) {
            boolean apply = !worldIn.isRemote;
            boolean changed = rightClicked(worldIn, pos, facing, apply);
            App.updateOutputs(worldIn);
            return changed;
       }
       return false;
    }
}
