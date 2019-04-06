package io.github.liamtuan.semicon.blocks;

import io.github.liamtuan.semicon.Circuit;
import io.github.liamtuan.semicon.core.Gate;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public abstract class BlockGate extends BlockOriented {

    BlockGate() {
        super(Material.IRON);
    }

    abstract protected Gate createGate(World world, BlockPos pos, EnumFacing facing,
                                       List<EnumFacing> input_faces, List<EnumFacing> output_faces);

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        EnumFacing block_facing = state.getValue(PROPERTYFACING);
        List input_faces = new ArrayList<EnumFacing>();
        List output_faces = new ArrayList<EnumFacing>();

        Gate gate = createGate(worldIn, pos, block_facing, input_faces, output_faces);

        EnumFacing[] input_face_arr = new EnumFacing[input_faces.size()];
        for(int i = 0; i < input_faces.size(); i++){
            input_face_arr[i] = getWorldFacing(block_facing, (EnumFacing)input_faces.get(i));
        }
        EnumFacing[] output_face_arr = new EnumFacing[output_faces.size()];
        for(int i = 0; i < output_faces.size(); i++){
            output_face_arr[i] = getWorldFacing(block_facing, (EnumFacing)output_faces.get(i));
        }

        Circuit.addBlockGate(worldIn, pos, gate, input_face_arr, output_face_arr);

        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
    }
}
