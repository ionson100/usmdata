package com.ion100.usmdata.orm2;
/********************************************************************
 * Copyright © 2016-2017 OOO Bitnic                                 *
 * Created by OOO Bitnic on 08.02.16   corp@bitnic.ru               *
 * ******************************************************************/

import android.database.Cursor;
import android.database.sqlite.SQLiteQuery;
import android.util.Log;

import com.ion100.usmdata.BuildConfig;

import java.lang.reflect.Field;


public class Logger {
    private final static boolean isWrite = BuildConfig.DEBUG;

    public static void logE(String msg) {
        if (isWrite) {
            Log.e("____ORM____", msg);
        }
    }

    public static void logI(String msg) {
        if (isWrite) {
            Log.i("____ORM____", msg);
        }
    }

    public static void printSql(Cursor cursor) {
        if (isWrite) {
            try {
                Field[] dd = cursor.getClass().getDeclaredFields();
                Field ddd = cursor.getClass().getDeclaredField("mQuery");
                ddd.setAccessible(true);
                SQLiteQuery v = (SQLiteQuery) ddd.get(cursor);
                Logger.logI(v.toString());
            } catch (Exception ignored) {
            }
        }
    }
}
