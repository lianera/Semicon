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
    protected Gate createGate(List<EnumFacing> input_faces, List<EnumFacing> output_faces) {
        Node y = new Node();
        Node a = new Node();
        Node b = new Node();
        output_faces.add(EnumFacing.NORTH);
        input_faces.add(EnumFacing.SOUTH);
        input_faces.add(EnumFacing.EAST);

        return new OrGate(a, b, y);
    }

}
