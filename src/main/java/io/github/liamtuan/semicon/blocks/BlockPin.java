package io.github.liamtuan.semicon.blocks;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockPin extends BlockIO {
    public BlockPin() {
        setRegistryName("pin");
        setUnlocalizedName("pin");

    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos,
                                    IBlockState state,
                                    EntityPlayer playerIn,
                                    EnumHand hand,
                                    EnumFacing facing,
                                    float hitX, float hitY, float hitZ) {
        if(facing != EnumFacing.NORTH) {
            boolean onoff_state = state.getValue(PROPERTYSTATE);
            state = state.withProperty(PROPERTYSTATE, !onoff_state);
            worldIn.setBlockState(pos, state);
            return true;
        }
        return false;
    }
}
