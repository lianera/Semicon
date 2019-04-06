package io.github.liamtuan.semicon;

import io.github.liamtuan.semicon.sim.Circuit;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;


@Mod(modid = App.MODID)
public class App {
    public static final String MODID = "semicon";

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        Registry.preInit();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        Circuit.init();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    }

}
