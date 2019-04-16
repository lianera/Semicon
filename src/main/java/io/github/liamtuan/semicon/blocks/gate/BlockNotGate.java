package io.github.liamtuan.semicon.blocks.gate;

import io.github.liamtuan.semicon.sim.core.Gate;
import io.github.liamtuan.semicon.sim.core.Node;
import io.github.liamtuan.semicon.sim.core.NotGate;
import net.minecraft.util.EnumFacing;

public class BlockNotGate extends BlockGate {
    public BlockNotGate() {
        super("notgate");
    }

    @Override
    String getGateType() {
        return "not";
    }

    @Override
    EnumFacing[] getLocalInputFaces() {
        return new EnumFacing[]{EnumFacing.SOUTH};
    }

    @Override
    EnumFacing[] getLocalOutputFaces() {
        return new EnumFacing[]{EnumFacing.NORTH};
    }

}
