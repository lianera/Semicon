package io.github.liamtuan.semicon.blocks;

import io.github.liamtuan.semicon.sim.Circuit;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BlockOutput extends BlockIO{

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        EnumFacing block_facing = state.getValue(PROPERTYFACING);
        EnumFacing[] jointfaces = getConnectedFaces();
        for(int i = 0; i < jointfaces.length; i++){
            jointfaces[i] = getWorldFacing(block_facing, jointfaces[i]);
        }
        Circuit.setWorld(worldIn);

        Circuit.addOutput(pos, jointfaces);

        super.onBlockAdded(worldIn, pos, state);
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        EnumFacing block_facing = state.getValue(PROPERTYFACING);
        EnumFacing[] faces = getJointFaces(block_facing);

        Circuit.removeOutput(pos, faces);
        super.breakBlock(worldIn, pos, state);
    }

}
