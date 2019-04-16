package io.github.liamtuan.semicon.blocks.io;

import io.github.liamtuan.semicon.Util;
import io.github.liamtuan.semicon.blocks.BlockUnit;
import io.github.liamtuan.semicon.sim.Cell;
import io.github.liamtuan.semicon.sim.UnitLed;
import io.github.liamtuan.semicon.sim.Unit;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class BlockLed extends BlockUnit implements BlockOutput{
    public static final PropertyBool PROPERTYSTATE = PropertyBool.create("state");

    public BlockLed() {
        super("led");
        setLightLevel(0.5f);
    }

    @Override
    public Unit createUnit(World worldIn, BlockPos pos) {
        Cell cell = Util.blockPosToCell(pos);
        return new UnitLed(cell);
    }



    @Override
    protected BlockStateContainer createBlockState() {
        BlockStateContainer container = new BlockStateContainer(this, PROPERTYSTATE);
        return container;
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        boolean on_off_state = (meta & 0x1) != 0;
        return this.getDefaultState().withProperty(PROPERTYSTATE, on_off_state);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        int meta = 0;
        boolean on_off_state = state.getValue(PROPERTYSTATE);
        if(on_off_state)
            meta = meta | 0x1;
        else
            meta = meta & ~0x1;
        return meta;
    }


    @Override
    public IBlockState getStateForPlacement(World world,
                                            BlockPos pos,
                                            EnumFacing facing,
                                            float hitX,
                                            float hitY,
                                            float hitZ,
                                            int meta,
                                            EntityLivingBase placer,
                                            EnumHand hand)
    {
        return this.getDefaultState().withProperty(PROPERTYSTATE, false);
    }

    @Override
    public void setState(World world, BlockPos pos, boolean state) {
        IBlockState oldblockstate = world.getBlockState(pos);
        if(oldblockstate.getValue(PROPERTYSTATE) == state)
            return;

        IBlockState newblockstate = oldblockstate.withProperty(PROPERTYSTATE, state);
        world.setBlockState(pos, newblockstate);
    }
}
