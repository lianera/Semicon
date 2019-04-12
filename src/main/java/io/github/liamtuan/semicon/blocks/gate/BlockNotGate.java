package io.github.liamtuan.semicon.blocks.gate;

import io.github.liamtuan.semicon.core.Gate;
import io.github.liamtuan.semicon.core.Node;
import io.github.liamtuan.semicon.core.NotGate;
import net.minecraft.util.EnumFacing;

public class BlockNotGate extends BlockGate {
    public BlockNotGate() {
        setRegistryName("notgate");
        setUnlocalizedName("notgate");
    }

    @Override
    protected EnumFacing[] localIntpuFaces() {
        return new EnumFacing[]{EnumFacing.SOUTH};
    }

    @Override
    protected EnumFacing[] localOutputFaces() {
        return new EnumFacing[]{EnumFacing.NORTH};
    }

    @Override
    protected Gate createGate() {
        Node y = new Node();
        Node a = new Node();
        return new NotGate(a, y);
    }

}
