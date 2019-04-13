package io.github.liamtuan.semicon.blocks.wire;

import net.minecraft.util.EnumFacing;

public class BlockWireCross extends BlockWire {
    public BlockWireCross(){
        setRegistryName("wirecross");
        setUnlocalizedName("wirecross");
    }

    @Override
    EnumFacing[] getLocalFaces() {
        return new EnumFacing[]{
                EnumFacing.NORTH,
                EnumFacing.EAST,
                EnumFacing.SOUTH,
                EnumFacing.WEST
        };
    }
}
