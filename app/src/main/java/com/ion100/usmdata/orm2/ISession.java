package com.ion100.usmdata.orm2;
/********************************************************************
 * Copyright © 2016-2017 OOO Bitnic                                 *
 * Created by OOO Bitnic on 08.02.16   corp@bitnic.ru               *
 * ******************************************************************/

import android.database.sqlite.SQLiteDatabase;

import java.util.List;

public interface ISession {
    SQLiteDatabase getSqLiteDatabase();

    <T> int update(T item);

    <T> int updateWhere(T item, String whereSql);

    <T> int insert(T item);

    <T> int delete(T item);

    <T> List<T> getList(Class<T> tClass, String where, Object... objects);
    //<T> List<T> getListForCache(Class<T> tClass, String where, Object... objects);

    <T> T get(Class<T> tClass, Object id);

    Object executeScalar(String sql, Object... objects);

    void execSQL(String sql, Object... objects);

    void beginTransaction();

    void commitTransaction();

    void endTransaction();

    int deleteTable(String tableName);

    int deleteTable(String tableName, String where, Object... objects);
}



