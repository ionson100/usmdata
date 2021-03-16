package com.ion100.usmdata.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class Utils {
    public static final int READ_CONNECT_TIMEOUT = 35000;
    public static final String USER_CODE = "1";
    public static final String ERROR="Ошибка";


    public static void closerableAll(Closeable... d) throws IOException {
        for (Closeable closeable : d) {
            if (closeable != null) {
                closeable.close();
            }
        }

    }

    public static void writeToFile(String patch, String data) throws IOException {
        File file=new File(patch);
        if(file.exists()==false){
            if(file.createNewFile()==false){

            }
        }

        FileOutputStream fOut = new FileOutputStream(file);
        OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
        myOutWriter.append(data);
        myOutWriter.close();
        fOut.flush();
        fOut.close();

    }


    public synchronized static String readFromFile(String patch) throws Exception {

        File file=new File(patch);
        if(file.exists()==false){
            file.createNewFile();
        }
        StringBuilder text = new StringBuilder();
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        while ((line = br.readLine()) != null) {
            text.append(line);
            text.append('\n');
        }
        br.close();
        return text.toString().trim();
    }

    public static Bitmap getBitmap(String path){
        File imgFile = new  File(path);

        if(imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(path);
            return myBitmap;
        }else {
            return null;
        }
    }
}
