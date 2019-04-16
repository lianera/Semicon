package io.github.liamtuan.semicon.blocks.wire;

import net.minecraft.util.EnumFacing;

public class BlockWireFull extends BlockWire {

    public BlockWireFull(){
        super("wirefull");
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
