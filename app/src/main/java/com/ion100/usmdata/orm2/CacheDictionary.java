package com.ion100.usmdata.orm2;
/********************************************************************
 * Copyright © 2016-2017 OOO Bitnic                                 *
 * Created by OOO Bitnic on 08.02.16   corp@bitnic.ru               *
 * ******************************************************************/

import java.util.Hashtable;
import java.util.Map;

class CacheDictionary {
    //public static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(CacheDictionary.class);

    private static final Object lock = new Object();
    private static final Map<String, cacheMetaDate> dic = new Hashtable();

    public static cacheMetaDate getCacheMetaDate(Class aClass) {
        if (dic.get(aClass.getName()) == null) {
            synchronized (lock) {
                try {
                    String d = AnotationOrm.getTableName(aClass);
                } catch (Exception ex) {
                    //log.error(ex);
                    throw new RuntimeException("not use type for orm as model type - " + aClass.getName());
                }
                if (dic.get(aClass.getName()) == null) {
                    dic.put(aClass.getName(), new cacheMetaDate<>(aClass));
                }
            }
        }
        return dic.get(aClass.getName());
    }
}
