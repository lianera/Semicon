package io.github.liamtuan.semicon.blocks.wire;

import net.minecraft.util.EnumFacing;

public class BlockWireCorner extends BlockWire {
    public BlockWireCorner(){
        super("wirecorner");
    }

    @Override
    EnumFacing[][] getLocalFaceGroups() {
        return new EnumFacing[][]{{
                EnumFacing.NORTH,
                EnumFacing.EAST
        }};
    }
}
