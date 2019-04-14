package io.github.liamtuan.semicon;

import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Events {

    @SubscribeEvent
    public static void onWorldLoaded(WorldEvent.Load event) {

        System.out.println("load app data");
        AppData.get(event.getWorld()).markDirty();
    }

    @SubscribeEvent
    public static void onWorldSave(WorldEvent.Save event){
        System.out.println("save app data");
    }
}
