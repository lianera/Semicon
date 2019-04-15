package io.github.liamtuan.semicon.blocks.gate;

import net.minecraft.util.EnumFacing;

public class BlockSrLatchGate extends BlockGate{
    public BlockSrLatchGate() {
        setRegistryName("srlatchgate");
        setUnlocalizedName("srlatchgate");
    }

    @Override
    String getGateType() {
        return "srlatch";
    }

    @Override
    EnumFacing[] getLocalInputFaces() {
        return new EnumFacing[]{EnumFacing.SOUTH, EnumFacing.EAST};
    }

    @Override
    EnumFacing[] getLocalOutputFaces() {
        return new EnumFacing[]{EnumFacing.NORTH, EnumFacing.WEST};
    }

}
