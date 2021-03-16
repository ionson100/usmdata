package com.ion100.usmdata.utils;

import com.google.gson.Gson;
import com.ion100.usmdata.MainActivity;
import com.ion100.usmdata.dialog.DialogFactory;
import com.ion100.usmdata.orm2.Configure;

import java.io.File;
import java.util.List;

public class TotalSettings {

    private static Object lock=new Object();
    private static TotalSettings totalSettings;

    public String userName="";

    public String userId="";
    public String serverUrl="0.0.0.0";

    public static  TotalSettings getTotalSettings(){
        synchronized (lock){
            try{
                if(totalSettings==null){
                    File file=new File(Patcher.patchDirSettings);
                    if(file.exists()==false){
                        TotalSettings totalSettings = new TotalSettings();
                        Gson gson=new Gson();
                        String json=gson.toJson(totalSettings);
                        Utils.writeToFile(Patcher.patchDirSettings,json);
                    }
                    String ss=Utils.readFromFile(Patcher.patchDirSettings);
                    Gson gson=new Gson();
                    totalSettings=gson.fromJson(ss,TotalSettings.class);
                }

            }catch (Exception e){
                DialogFactory.dialogInfo(MainActivity.mainActivity,Utils.ERROR,e.getMessage(),null);
            }
            return totalSettings;

        }
    }

    public static void Save(){
        if(totalSettings==null) return;
        try {
            Gson gson=new Gson();
            String json=gson.toJson(totalSettings);
            Utils.writeToFile(Patcher.patchDirSettings,json);
        }catch (Exception e){
            DialogFactory.dialogInfo(MainActivity.mainActivity,Utils.ERROR,e.getMessage(),null);
        }
    }
}
