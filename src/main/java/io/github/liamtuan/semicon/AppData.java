package io.github.liamtuan.semicon;

import io.github.liamtuan.semicon.sim.Circuit;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

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
        byte[] data = nbt.getByteArray(IDENTIFIER);
        String rawjson = gunzip(data);
        JSONObject obj = new JSONObject(rawjson);
        String version = obj.getString("version");
        if(!version.equals(App.VERSION)) {
            System.out.println("version not match, will not build circuit");
            return;
        }
        App.setCircuit(Circuit.fromJson(obj));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        System.out.println("write circuit data");
        JSONObject obj = App.getCircuit().toJson();
        obj.put("version", App.VERSION);
        String rawjson = obj.toString();
        byte[] compressed = gzip(rawjson);
        compound.setByteArray(IDENTIFIER, compressed);
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

    public static byte[] gzip(String primStr) {
        if (primStr == null || primStr.length() == 0) {
            return new byte[0];
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        GZIPOutputStream gzip=null;
        try {
            gzip = new GZIPOutputStream(out);
            gzip.write(primStr.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            if(gzip!=null){
                try {
                    gzip.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return out.toByteArray();
    }

    public static String gunzip(byte[] compressed){
        if(compressed==null){
            return "";
        }

        ByteArrayOutputStream out= new ByteArrayOutputStream();
        ByteArrayInputStream in=null;
        GZIPInputStream ginzip=null;
        String decompressed = null;
        try {
            in=new ByteArrayInputStream(compressed);
            ginzip=new GZIPInputStream(in);

            byte[] buffer = new byte[1024];
            int offset = -1;
            while ((offset = ginzip.read(buffer)) != -1) {
                out.write(buffer, 0, offset);
            }
            decompressed=out.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ginzip != null) {
                try {
                    ginzip.close();
                } catch (IOException e) {
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        }

        return decompressed;
    }

}
