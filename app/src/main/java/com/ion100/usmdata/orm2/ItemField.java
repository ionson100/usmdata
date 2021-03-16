package com.ion100.usmdata.orm2;
/********************************************************************
 * Copyright © 2016-2017 OOO Bitnic                                 *
 * Created by OOO Bitnic on 08.02.16   corp@bitnic.ru               *
 * ******************************************************************/

import java.lang.reflect.Field;
import java.lang.reflect.Type;

class ItemField {
    public Field field;
    public String columnName;
    public String fieldName;
    public Type type;
    public boolean isUserType;
    public Class aClassUserType;
}

