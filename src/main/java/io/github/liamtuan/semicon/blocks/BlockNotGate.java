package io.github.liamtuan.semicon.blocks;

import io.github.liamtuan.semicon.core.Gate;
import io.github.liamtuan.semicon.core.Node;
import io.github.liamtuan.semicon.core.NotGate;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class BlockNotGate extends BlockGate {
    public BlockNotGate() {
        setRegistryName("notgate");
        setUnlocalizedName("notgate");
    }

    @Override
    protected Gate createGate(World world, BlockPos pos, EnumFacing facing, List<EnumFacing> input_faces, List<EnumFacing> output_faces) {
        Node y = new Node();
        Node a = new Node();
        output_faces.add(facing);
        input_faces.add(facing.getOpposite());
        return new NotGate(a, y);
    }

}
