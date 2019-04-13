package io.github.liamtuan.semicon.blocks.wire;

import net.minecraft.util.EnumFacing;

public class BlockWireBar extends BlockWire{
    public BlockWireBar(){
        setRegistryName("wirebar");
        setUnlocalizedName("wirebar");
    }

    @Override
    EnumFacing[] getLocalFaces() {
        return new EnumFacing[]{
            EnumFacing.NORTH,
            EnumFacing.SOUTH
        };
    }
}
