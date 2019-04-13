package io.github.liamtuan.semicon;

import io.github.liamtuan.semicon.blocks.io.BlockOutput;
import io.github.liamtuan.semicon.sim.Cell;
import io.github.liamtuan.semicon.sim.Circuit;
import io.github.liamtuan.semicon.sim.StateListener;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


@Mod(modid = App.MODID)
public class App {
    public static final String MODID = "semicon";
    private static StateListener output_notifier;
    private static Map<BlockPos, Boolean> output_changed;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        Registry.preInit();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        output_notifier = null;
        output_changed = new HashMap<>();

        Circuit.init();
        Circuit.setDebugLevel(5);

        output_notifier = new StateListener() {
            @Override
            public void onStateChanged(Cell cell, boolean state) {
                BlockPos pos = Util.cellToBlockPos(cell);
                output_changed.put(pos, state);
            }
        };
        Circuit.setOutputNotifier(output_notifier);
    }

    public static void updateOutputs(World world){
        for(Map.Entry<BlockPos, Boolean> entry : output_changed.entrySet()){
            BlockPos pos = entry.getKey();
            boolean state = entry.getValue();

            Block block = world.getBlockState(pos).getBlock();
            if(block instanceof BlockOutput){
                BlockOutput output = (BlockOutput)block;
                output.setBlockCircuitState(world, pos, state);
            }
        }
        output_changed.clear();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    }

}
