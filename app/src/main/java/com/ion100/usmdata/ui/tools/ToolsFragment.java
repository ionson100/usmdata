package com.ion100.usmdata.ui.tools;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.ion100.usmdata.R;
import com.ion100.usmdata.dialog.DialogFactory;
import com.ion100.usmdata.utils.TotalSettings;

public class ToolsFragment extends Fragment {


    EditText editTextNameUser,editTextServerIp;
    TotalSettings totalSettings;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_settings, container, false);
        editTextNameUser=root.findViewById(R.id.et_user_name);
        editTextServerIp=root.findViewById(R.id.et_server_ip);
        totalSettings=TotalSettings.getTotalSettings();
        editTextNameUser.setText(totalSettings.userName);
        editTextServerIp.setText(totalSettings.serverUrl);

        root.findViewById(R.id.bt_save_settings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                totalSettings.serverUrl=editTextServerIp.getText().toString().trim();
                totalSettings.userName=editTextNameUser.getText().toString();
                TotalSettings.Save();
                DialogFactory.dialogInfo((AppCompatActivity) getActivity(),"Сохранение настроек","Настройки сохранены",null);
            }
        });


        return root;
    }
}