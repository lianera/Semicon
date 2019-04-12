package io.github.liamtuan.semicon.blocks.gate;

import io.github.liamtuan.semicon.core.AndGate;
import io.github.liamtuan.semicon.core.Gate;
import io.github.liamtuan.semicon.core.Node;
import net.minecraft.util.EnumFacing;

public class BlockAndGate extends BlockGate {
    public BlockAndGate() {
        setRegistryName("andgate");
        setUnlocalizedName("andgate");

    }

    @Override
    protected EnumFacing[] localIntpuFaces() {
        return new EnumFacing[]{EnumFacing.SOUTH, EnumFacing.EAST};
    }

    @Override
    protected EnumFacing[] localOutputFaces() {
        return new EnumFacing[]{EnumFacing.NORTH};
    }

    @Override
    protected Gate createGate() {
        Node y = new Node();
        Node a = new Node();
        Node b = new Node();
        return new AndGate(a, b, y);
    }
}
