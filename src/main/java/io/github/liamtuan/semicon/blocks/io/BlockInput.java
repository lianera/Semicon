package io.github.liamtuan.semicon.blocks.io;

import io.github.liamtuan.semicon.sim.Circuit;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BlockInput extends BlockIO{

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        super.onBlockAdded(worldIn, pos, state);

        EnumFacing block_facing = state.getValue(PROPERTYFACING);
        EnumFacing[] jointfaces = getConnectedFaces();
        for(int i = 0; i < jointfaces.length; i++){
            EnumFacing realFacing = getWorldFacing(block_facing, jointfaces[i]);
            Circuit.addInput(pos, realFacing);
        }

    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        EnumFacing block_facing = state.getValue(PROPERTYFACING);
        EnumFacing[] faces = getJointFaces(block_facing);

        Circuit.removeInput(pos, faces);
        super.breakBlock(worldIn, pos, state);
    }

}
