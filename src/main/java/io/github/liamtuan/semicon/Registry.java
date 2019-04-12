package io.github.liamtuan.semicon;

import io.github.liamtuan.semicon.blocks.gate.BlockAndGate;
import io.github.liamtuan.semicon.blocks.gate.BlockNotGate;
import io.github.liamtuan.semicon.blocks.gate.BlockOrGate;
import io.github.liamtuan.semicon.blocks.io.BlockClock;
import io.github.liamtuan.semicon.blocks.io.BlockLed;
import io.github.liamtuan.semicon.blocks.io.BlockPin;
import io.github.liamtuan.semicon.blocks.wire.*;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(modid=App.MODID)
public class Registry {
    public static Block andgate;
    public static Block orgate;
    public static Block notgate;
    public static Block wire_bar;
    public static Block wire_corner;
    public static Block wire_t;
    public static Block wire_cross;
    public static Block wire_full;
    public static Block pin;
    public static Block clock;
    public static Block led;

    static void preInit(){
        andgate = new BlockAndGate();
        orgate = new BlockOrGate();
        notgate = new BlockNotGate();
        wire_bar = new BlockWireBar();
        wire_corner = new BlockWireCorner();
        wire_t = new BlockWireT();
        wire_cross = new BlockWireCross();
        wire_full = new BlockWireFull();
        pin = new BlockPin();
        clock = new BlockClock();
        led = new BlockLed();
    }

    static Block[] allBlocks(){
        return new Block[]{
                andgate, orgate, notgate,
                wire_bar, wire_corner, wire_t, wire_cross, wire_full,
                pin, clock, led
        };
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(allBlocks());

    }

    @SubscribeEvent
    public static void registerItemBlocks(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> registry = event.getRegistry();
        for(Block block : allBlocks()){
            registry.register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
        }
    }

    @SubscribeEvent
    public static void registerRenders(ModelRegistryEvent event) {
        for(Block block : allBlocks()) {
            registerRender(Item.getItemFromBlock(block));
        }
    }

    public static void registerRender(Item item) {
        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation( item.getRegistryName(), "inventory"));
    }

}
