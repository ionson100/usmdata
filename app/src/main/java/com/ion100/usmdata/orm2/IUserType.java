package com.ion100.usmdata.orm2;

/********************************************************************
 * Copyright © 2016-2017 OOO Bitnic                                 *
 * Created by OOO Bitnic on 08.02.16   corp@bitnic.ru               *
 * ******************************************************************/
public interface IUserType {
    Object getObject(String str);

    String getString(Object ojb);
}
