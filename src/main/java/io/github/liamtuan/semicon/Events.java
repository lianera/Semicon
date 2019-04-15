package io.github.liamtuan.semicon;

import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

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


    @SubscribeEvent
    public static void worldTick(TickEvent.WorldTickEvent event) {
        final float tps = 20.f;
        final float dt = 1.f / tps;
        if (event.phase == TickEvent.Phase.END){
            App.getCircuit().update(dt);
            App.updateOutputs(event.world);
        }
    }
}
