package io.github.liamtuan.semicon.blocks;

import io.github.liamtuan.semicon.sim.Circuit;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BlockInput extends BlockIO{

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        EnumFacing block_facing = state.getValue(PROPERTYFACING);
        EnumFacing[] jointfaces = getConnectedFaces();
        for(int i = 0; i < jointfaces.length; i++){
            EnumFacing realFacing = getWorldFacing(block_facing, jointfaces[i]);
            Circuit.addInput(worldIn, pos, realFacing);
        }

        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
    }
}
