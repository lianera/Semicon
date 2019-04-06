package io.github.liamtuan.semicon.blocks;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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
