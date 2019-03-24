package io.github.liamtuan.redsilicon;

import io.github.liamtuan.redsilicon.blocks.BlockAndGate;
import io.github.liamtuan.redsilicon.blocks.BlockNotGate;
import io.github.liamtuan.redsilicon.blocks.BlockOrGate;
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

    static void preInit(){
        andgate = new BlockAndGate();
        orgate = new BlockOrGate();
        notgate = new BlockNotGate();
    }

    static Block[] allBlocks(){
        return new Block[]{andgate, orgate, notgate};
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
