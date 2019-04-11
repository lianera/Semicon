package io.github.liamtuan.semicon.blocks;

import io.github.liamtuan.semicon.sim.Circuit;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockPin extends BlockInput {
    public BlockPin() {
        setRegistryName("pin");
        setUnlocalizedName("pin");

    }

    @Override
    EnumFacing[] getConnectedFaces() {
        return new EnumFacing[]{EnumFacing.NORTH};
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos,
                                    IBlockState state,
                                    EntityPlayer playerIn,
                                    EnumHand hand,
                                    EnumFacing facing,
                                    float hitX, float hitY, float hitZ) {
        EnumFacing blockfacing = state.getValue(PROPERTYFACING);
        EnumFacing handonface = getLocalFacing(blockfacing, facing);
        if(handonface != EnumFacing.NORTH) {
            boolean onoff_state = state.getValue(PROPERTYSTATE);
            onoff_state = !onoff_state;
            state = state.withProperty(PROPERTYSTATE, onoff_state);
            worldIn.setBlockState(pos, state);

            Circuit.setInputState(pos, blockfacing, onoff_state);
            return true;
        }
        return false;
    }
}
