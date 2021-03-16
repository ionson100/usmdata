package com.ion100.usmdata.orm2;

import java.util.ArrayList;
import java.util.List;

public class MyChach {

    private static Object lock=new Object();
    private static List<MapCache<Object>> mapCaches=new ArrayList<>();

   public  static  <T> List<T> getList(Class<T> tClass, int cache){
       synchronized (lock){
           for (MapCache<Object> mapCach : mapCaches) {
               if(mapCach.aClass==tClass&&mapCach.cache==cache&&mapCach.mList!=null){
                   //System.out.println("**************************  list");
                   return (List<T>) mapCach.mList;
               }
           }
           //System.out.println("**************************  null");
           return null;

       }

    }

    public  static void  addChach(Class tClass, int cache, List list){
        synchronized (lock){
            if(list.size()==0) return;
            MapCache mapCache=new MapCache();
            mapCache.aClass=tClass;
            mapCache.cache=cache;
            mapCache.mList= list;
            mapCaches.add(mapCache);
            //System.out.println("**************************  add");
        }

    }




    private static class MapCache<Object>{
       public Class aClass;
       public int cache;
       public List<Object> mList;

    }

    public static   void clear(Class aClass){
       synchronized (lock){
           List<MapCache> mapCachesDelete=new ArrayList<>();
           for (MapCache<Object> mapCach : mapCaches) {
               if(mapCach.aClass==aClass){
                   mapCachesDelete.add(mapCach);
               }
           }
           mapCaches.removeAll(mapCachesDelete);
           //System.out.println("**************************  delete");
       }

    }
}
