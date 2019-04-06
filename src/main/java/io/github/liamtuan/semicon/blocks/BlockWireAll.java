package io.github.liamtuan.semicon.blocks;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class BlockWireAll extends BlockWire{
    public BlockWireAll() {
        setRegistryName("wireall");
        setUnlocalizedName("wireall");
    }

    @Override
    EnumFacing[] getConnectedFaces() {
        return new EnumFacing[]{
                EnumFacing.NORTH,
                EnumFacing.EAST,
                EnumFacing.SOUTH,
                EnumFacing.WEST,
                EnumFacing.UP,
                EnumFacing.DOWN
        };
    }


}
