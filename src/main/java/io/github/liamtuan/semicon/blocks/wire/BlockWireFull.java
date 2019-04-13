package io.github.liamtuan.semicon.blocks.wire;

import net.minecraft.util.EnumFacing;

public class BlockWireFull extends BlockWire {

    public BlockWireFull(){
        setRegistryName("wirefull");
        setUnlocalizedName("wirefull");
    }

    @Override
    EnumFacing[] getLocalFaces() {
        return EnumFacing.values();
    }
}
