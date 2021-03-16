package com.ion100.usmdata.orm2;
/********************************************************************
 * Copyright © 2016-2017 OOO Bitnic                                 *
 * Created by OOO Bitnic on 08.02.16   corp@bitnic.ru               *
 * ******************************************************************/

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class DataBaseHelper extends SQLiteOpenHelper {

    //public static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DataBaseHelper.class);
    private static String DB_PATH = "";
    private static Context mContext;

    public DataBaseHelper(Context context, String databasePath) {
        super(context, databasePath, null, 1);
        DB_PATH = databasePath;
        mContext = context;
    }

    public SQLiteDatabase openDataBaseForReadable() throws SQLException {
        return this.getReadableDatabase();
    }

    public SQLiteDatabase openDataBaseForWritable() throws SQLException {
        return this.getWritableDatabase();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}

