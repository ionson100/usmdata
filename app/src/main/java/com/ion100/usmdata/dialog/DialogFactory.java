package com.ion100.usmdata.dialog;

import androidx.appcompat.app.AppCompatActivity;

import com.ion100.usmdata.IAction;

public  class DialogFactory {

    public static void dialogInfo(AppCompatActivity activity, String title, String message, IAction<Object> iAction){
        DialogInfo dialog=new DialogInfo();
        dialog.iAction=iAction;
        dialog.title=title;
        dialog.message=message;
        dialog.show(activity.getSupportFragmentManager(),DialogInfo.TAG);
    }

    public static void dialogShowImage(AppCompatActivity activity,String patch, IAction<Object> iAction){
        DialogShowImage dialog=new DialogShowImage();
        dialog.iAction=iAction;
        dialog.patchImage=patch;
        dialog.show(activity.getSupportFragmentManager(),DialogInfo.TAG);
    }
}
