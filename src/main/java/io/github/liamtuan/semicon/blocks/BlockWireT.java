package io.github.liamtuan.semicon.blocks;

import net.minecraft.util.EnumFacing;

public class BlockWireT extends BlockWire{
    public BlockWireT(){
        setRegistryName("wiret");
        setUnlocalizedName("wiret");
    }

    @Override
    EnumFacing[] getConnectedFaces() {
        return new EnumFacing[]{
                EnumFacing.WEST,
                EnumFacing.NORTH,
                EnumFacing.EAST
        };
    }
}
