package io.github.liamtuan.semicon.blocks;

import net.minecraft.util.EnumFacing;

public class BlockWireCorner extends BlockWire{
    public BlockWireCorner(){
        setRegistryName("wirecorner");
        setUnlocalizedName("wirecorner");
    }

    @Override
    EnumFacing[] getConnectedFaces() {
        return new EnumFacing[]{
                EnumFacing.NORTH,
                EnumFacing.EAST
        };
    }
}
