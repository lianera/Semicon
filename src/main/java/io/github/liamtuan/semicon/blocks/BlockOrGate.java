package io.github.liamtuan.semicon.blocks;

import io.github.liamtuan.semicon.core.Gate;
import io.github.liamtuan.semicon.core.Node;
import io.github.liamtuan.semicon.core.OrGate;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class BlockOrGate extends BlockGate {
    public BlockOrGate() {
        setRegistryName("orgate");
        setUnlocalizedName("orgate");
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
        return new OrGate(a, b, y);
    }

}
