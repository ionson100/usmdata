package com.ion100.usmdata.models;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.ion100.usmdata.orm2.Column;
import com.ion100.usmdata.orm2.PrimaryKey;
import com.ion100.usmdata.orm2.Table;
import com.ion100.usmdata.orm2.UserField;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Table("work")
public class MWork {

    @PrimaryKey("_id")
    public int _id;

    @Column("id_core")
    public String idCore;
    @Column("description")
    public String description;

    @Column("work_locale")
    public String workLocale;

    @Column("work_level")
    public String workLevel;

    @Column("date")
    public Date dateCreate;

    @Column("is_send")
    public boolean isSend;




    @Column("is_commit")
    public boolean isCommit;


    @UserField(IUserType = ListUser.class)
    @Column("images")
    public List<String> listImage=new ArrayList<>();




}

