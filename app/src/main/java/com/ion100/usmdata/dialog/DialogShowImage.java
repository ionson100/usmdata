package com.ion100.usmdata.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.ion100.usmdata.IAction;
import com.ion100.usmdata.R;

import java.io.File;

public class DialogShowImage extends BaseDialog {
    public static final String TAG = "a15c2b78-92dc-4a78-9425-67259868d10f";
    public IAction<Object> iAction;
    public String patchImage;



    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_show_image, null);

        ((TextView)v.findViewById(R.id.title_dalog)).setText("Удалить фото?");
        ImageView imageView=v.findViewById(R.id.image_view);
        v.findViewById(R.id.bt_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v1) {
                DialogShowImage.this.dismiss();
                if (iAction != null) {
                    iAction.action(patchImage);
                }
            }
        });

        v.findViewById(R.id.bt_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v1) {
                DialogShowImage.this.dismiss();
            }
        });
        imageView.setImageBitmap(getBitmap(patchImage));

        builder.setView(v);


        AlertDialog dialog=builder.create();
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }

    public Bitmap getBitmap(String path){
        File imgFile = new  File(path);

        if(imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(path);
            return myBitmap;
        }else {
            return null;
        }
    }
}
