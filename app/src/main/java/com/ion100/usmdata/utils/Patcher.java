package com.ion100.usmdata.utils;

import android.os.Build;
import android.os.Environment;
import android.widget.Toast;

import com.ion100.usmdata.MainActivity;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Patcher {

    public static final String patchDirDirUsm= Environment.getExternalStorageDirectory().getAbsolutePath() + "/usm";
    public static final String patchDirBase= Environment.getExternalStorageDirectory().getAbsolutePath() + "/usm/usm_000232.sqlite";
    public static final String patchDirSettings= Environment.getExternalStorageDirectory().getAbsolutePath() + "/usm/settings_usm.txt";
    static {
        File f = new File(Patcher.patchDirDirUsm);
        if (!f.isFile()) {
            if (!(f.isDirectory())) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    try {
                        Files.createDirectory(Paths.get(f.getAbsolutePath()));
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.mainActivity, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    f.mkdir();
                }
            }
        }
    }


}
