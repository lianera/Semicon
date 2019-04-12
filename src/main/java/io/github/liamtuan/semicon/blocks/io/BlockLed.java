package io.github.liamtuan.semicon.blocks.io;

import net.minecraft.util.EnumFacing;

public class BlockLed extends BlockOutput{
    public BlockLed() {
        setRegistryName("led");
        setUnlocalizedName("led");
    }

    @Override
    EnumFacing[] getConnectedFaces() {
        return new EnumFacing[]{
                EnumFacing.NORTH, EnumFacing.EAST,
                EnumFacing.SOUTH, EnumFacing.WEST,
                EnumFacing.DOWN, EnumFacing.UP
        };
    }
}
