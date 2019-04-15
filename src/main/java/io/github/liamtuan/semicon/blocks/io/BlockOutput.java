package io.github.liamtuan.semicon.blocks.io;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface BlockOutput {
    void setState(World world, BlockPos pos, boolean state);
}
