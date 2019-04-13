package io.github.liamtuan.semicon.blocks.gate;

import io.github.liamtuan.semicon.sim.core.AndGate;
import io.github.liamtuan.semicon.sim.core.Gate;
import io.github.liamtuan.semicon.sim.core.Node;
import net.minecraft.util.EnumFacing;

public class BlockAndGate extends BlockGate {
    public BlockAndGate() {
        setRegistryName("andgate");
        setUnlocalizedName("andgate");

    }

    @Override
    String getGateType() {
        return "and";
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
