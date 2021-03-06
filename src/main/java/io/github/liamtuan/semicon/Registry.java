package io.github.liamtuan.semicon;

import io.github.liamtuan.semicon.blocks.gate.*;
import io.github.liamtuan.semicon.blocks.io.*;
import io.github.liamtuan.semicon.blocks.wire.*;
import io.github.liamtuan.semicon.sim.StateListener;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
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
    public static Block xorgate;
    public static Block srlatch;
    public static Block threestategate;
    public static Block wire_bar;
    public static Block wire_corner;
    public static Block wire_t;
    public static Block wire_cross;
    public static Block wire_cross_bridge;
    public static Block wire_full;
    public static Block pin;
    public static Block clock;
    public static Block led;
    public static final CreativeTabs tab_semicon = new CreativeTabs(App.MODID) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(clock);
        }
    };

    static void preInit(){
        andgate = new BlockAndGate();
        orgate = new BlockOrGate();
        notgate = new BlockNotGate();
        xorgate = new BlockXorGate();
        srlatch = new BlockSrLatchGate();
        threestategate = new BlockThreeStateGate();
        wire_bar = new BlockWireBar();
        wire_corner = new BlockWireCorner();
        wire_t = new BlockWireT();
        wire_cross = new BlockWireCross();
        wire_cross_bridge = new BlockWireCrossBridge();
        wire_full = new BlockWireFull();
        pin = new BlockPin();
        clock = new BlockClock();
        led = new BlockLed();

    }

    static Block[] allBlocks(){
        return new Block[]{
                andgate, orgate, notgate,
                xorgate, srlatch, threestategate,
                wire_bar, wire_corner, wire_t, wire_cross, wire_cross_bridge, wire_full,
                pin, clock, led,
        };
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        for(Block block : allBlocks()){
            block.setCreativeTab(tab_semicon);
        }
        event.getRegistry().registerAll(allBlocks());

    }

    @SubscribeEvent
    public static void registerItemBlocks(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> registry = event.getRegistry();
        for(Block block : allBlocks()){
            registry.register(
                    new ItemBlock(block)
                    .setRegistryName(block.getRegistryName())
            );
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
