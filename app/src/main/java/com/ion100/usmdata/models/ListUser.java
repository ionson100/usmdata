package com.ion100.usmdata.models;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ion100.usmdata.orm2.IUserType;

import java.util.List;

public class ListUser implements IUserType {

    @Override
    public Object getObject(String str) {
        if(str==null){
            str="[]";
        }
        Gson gson = new Gson(); List actionList = gson.fromJson(str, new TypeToken<List>() { }.getType());
        return actionList;
    }

    @Override
    public String getString(Object ojb) {
        String string = new Gson().toJson(ojb);
        return string;
    }
}
