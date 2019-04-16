package io.github.liamtuan.semicon.blocks.wire;

import net.minecraft.util.EnumFacing;

public class BlockWireCrossBridge extends BlockWire{
    public BlockWireCrossBridge(){
        super("wirecrossbridge");
    }

    @Override
    EnumFacing[][] getLocalFaceGroups() {
        return new EnumFacing[][]{
                {
                        EnumFacing.NORTH,
                        EnumFacing.SOUTH
                },
                {
                        EnumFacing.EAST,
                        EnumFacing.WEST
                }
        };
    }
}
