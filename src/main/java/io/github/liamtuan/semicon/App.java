package io.github.liamtuan.semicon;

import io.github.liamtuan.semicon.blocks.io.BlockOutput;
import io.github.liamtuan.semicon.sim.Cell;
import io.github.liamtuan.semicon.sim.Circuit;
import io.github.liamtuan.semicon.sim.StateListener;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.HashMap;
import java.util.Map;


@Mod(modid = App.MODID)
public class App {
    public static final String MODID = "semicon";
    private static StateListener output_notifier;
    private static Map<BlockPos, Boolean> output_changed;
    private static Circuit circuit;
    private static int debug_level;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        Registry.preInit();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        output_notifier = null;
        output_changed = new HashMap<>();

        MinecraftForge.EVENT_BUS.register(Events.class);

        output_notifier = new StateListener() {
            @Override
            public void onStateChanged(Cell cell, boolean state) {
                BlockPos pos = Util.cellToBlockPos(cell);
                output_changed.put(pos, state);
            }
        };

        debug_level = 0;
        setCircuit(new Circuit());
    }

    public static Circuit getCircuit(){
        return circuit;
    }

    public static void setCircuit(Circuit circuit){
        App.circuit = circuit;
        App.circuit.setOutputNotifier(output_notifier);
        App.circuit.setDebugLevel(debug_level);
    }

    public static void updateOutputs(World world){
        for(Map.Entry<BlockPos, Boolean> entry : output_changed.entrySet()){
            BlockPos pos = entry.getKey();
            boolean state = entry.getValue();

            Block block = world.getBlockState(pos).getBlock();
            if(block instanceof BlockOutput){
                BlockOutput output = (BlockOutput)block;
                output.setState(world, pos, state);
            }
        }
        output_changed.clear();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    }

}
