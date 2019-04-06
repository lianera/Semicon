package io.github.liamtuan.semicon.blocks;

import io.github.liamtuan.semicon.core.AndGate;
import io.github.liamtuan.semicon.core.Gate;
import io.github.liamtuan.semicon.core.Node;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class BlockAndGate extends BlockGate {
    public BlockAndGate() {
        setRegistryName("andgate");
        setUnlocalizedName("andgate");

    }

    @Override
    protected Gate createGate(World world, BlockPos pos, EnumFacing facing, List<EnumFacing> input_faces, List<EnumFacing> output_faces) {
        Node y = new Node();
        Node a = new Node();
        Node b = new Node();
        output_faces.add(facing);
        input_faces.add(facing.getOpposite());
        input_faces.add(facing.rotateY());
        return new AndGate(a, b, y);
    }

}
