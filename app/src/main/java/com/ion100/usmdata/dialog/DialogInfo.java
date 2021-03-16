package com.ion100.usmdata.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.ion100.usmdata.IAction;
import com.ion100.usmdata.R;

public class DialogInfo extends BaseDialog {

    public static final String TAG = "a15c2b78-92dc-4a78-9425-67259868d10f";
    public IAction<Object> iAction;
    public String title;
    public String message;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_info, null);

        ((TextView)v.findViewById(R.id.title_dalog)).setText(this.title);
        ((TextView)v.findViewById(R.id.d_message)).setText(this.message);

        v.findViewById(R.id.bt_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v1) {
                DialogInfo.this.dismiss();
                if (iAction != null) {
                    iAction.action(null);
                }

            }
        });

        builder.setView(v);


        AlertDialog dialog=builder.create();
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }
}
