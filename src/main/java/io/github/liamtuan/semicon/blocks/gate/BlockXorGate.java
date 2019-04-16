package io.github.liamtuan.semicon.blocks.gate;


import net.minecraft.util.EnumFacing;

public class BlockXorGate extends BlockGate {
    public BlockXorGate() {
        super("xorgate");
    }

    @Override
    String getGateType() {
        return "xor";
    }

    @Override
    EnumFacing[] getLocalInputFaces() {
        return new EnumFacing[]{EnumFacing.SOUTH, EnumFacing.EAST};
    }

    @Override
    EnumFacing[] getLocalOutputFaces() {
        return new EnumFacing[]{EnumFacing.NORTH};
    }

}
