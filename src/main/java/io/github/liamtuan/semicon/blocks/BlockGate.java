package io.github.liamtuan.semicon.blocks;

import io.github.liamtuan.semicon.sim.Circuit;
import io.github.liamtuan.semicon.core.Gate;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BlockGate extends BlockOriented {

    BlockGate() {
        super(Material.IRON);
    }

    abstract protected EnumFacing[] localIntpuFaces();
    abstract protected EnumFacing[] localOutputFaces();
    abstract protected Gate createGate();

    public EnumFacing[] getInputs(EnumFacing block_facing){
        EnumFacing[] input_faces = localIntpuFaces();
        for(int i = 0; i < input_faces.length; i++){
            input_faces[i] = getWorldFacing(block_facing, input_faces[i]);
        }
        return input_faces;
    }

    public EnumFacing[] getOutputs(EnumFacing block_facing){
        EnumFacing[] output_faces = localOutputFaces();
        for(int i = 0; i < output_faces.length; i++){
            output_faces[i] = getWorldFacing(block_facing, output_faces[i]);
        }
        return output_faces;
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        super.onBlockAdded(worldIn, pos, state);

        EnumFacing block_facing = state.getValue(PROPERTYFACING);

        Gate gate = createGate();

        EnumFacing[] input_faces = getInputs(block_facing);
        EnumFacing[] output_faces = getOutputs(block_facing);
        Circuit.addBlockGate(pos, gate, input_faces, output_faces);

    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        EnumFacing block_facing = state.getValue(PROPERTYFACING);
        EnumFacing[] input_faces = getInputs(block_facing);
        EnumFacing[] output_faces = getOutputs(block_facing);

        Circuit.removeBlockGate(pos, input_faces, output_faces);
        super.breakBlock(worldIn, pos, state);
    }

}
