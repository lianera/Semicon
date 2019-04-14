package io.github.liamtuan.semicon;

import io.github.liamtuan.semicon.sim.Circuit;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;

public class AppData extends WorldSavedData {
    public static final String IDENTIFIER = App.MODID;

    public AppData(){
        super(IDENTIFIER);
    }

    public AppData(String name) {
        super(name);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        System.out.println("read circuit data");
        App.setCircuit(Circuit.fromJson(nbt.getString(IDENTIFIER)));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        System.out.println("write circuit data");
        compound.setString(IDENTIFIER, App.getCircuit().toJson());
        return compound;
    }

    public static AppData get(World world)
    {
        AppData data = (AppData) world.loadData(AppData.class, IDENTIFIER);
        if (data == null)
        {
            data = new AppData();
            world.setData(IDENTIFIER, data);
        }
        return data;
    }

}
