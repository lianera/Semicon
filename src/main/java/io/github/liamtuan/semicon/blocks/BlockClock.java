package io.github.liamtuan.semicon.blocks;

import net.minecraft.util.EnumFacing;

public class BlockClock extends BlockInput {
    public BlockClock() {
        setRegistryName("clock");
        setUnlocalizedName("clock");
    }

    @Override
    EnumFacing[] getConnectedFaces() {
        return new EnumFacing[]{EnumFacing.NORTH};
    }
}
