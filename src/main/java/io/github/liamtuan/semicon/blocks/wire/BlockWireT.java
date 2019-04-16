package io.github.liamtuan.semicon.blocks.wire;

import net.minecraft.util.EnumFacing;

public class BlockWireT extends BlockWire {
    public BlockWireT(){
        super("wiret");
    }

    @Override
    EnumFacing[] getLocalFaces() {
        return new EnumFacing[]{
                EnumFacing.WEST,
                EnumFacing.NORTH,
                EnumFacing.EAST
        };
    }
}
