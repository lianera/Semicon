package io.github.liamtuan.semicon.blocks.wire;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class BlockWireAll extends BlockWire{
    public BlockWireAll() {
        super("wireall");
    }

    @Override
    EnumFacing[] getLocalFaces() {
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
