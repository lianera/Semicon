package io.github.liamtuan.semicon.blocks.gate;

import io.github.liamtuan.semicon.sim.core.Gate;
import io.github.liamtuan.semicon.sim.core.Node;
import io.github.liamtuan.semicon.sim.core.OrGate;
import net.minecraft.util.EnumFacing;

public class BlockOrGate extends BlockGate {
    public BlockOrGate() {
        super("orgate");
    }

    @Override
    String getGateType() {
        return "or";
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
