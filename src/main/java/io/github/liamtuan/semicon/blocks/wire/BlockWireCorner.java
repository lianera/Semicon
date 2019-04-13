package io.github.liamtuan.semicon.blocks.wire;

import net.minecraft.util.EnumFacing;

public class BlockWireCorner extends BlockWire {
    public BlockWireCorner(){
        setRegistryName("wirecorner");
        setUnlocalizedName("wirecorner");
    }

    @Override
    EnumFacing[] getLocalFaces() {
        return new EnumFacing[]{
                EnumFacing.NORTH,
                EnumFacing.EAST
        };
    }
}
