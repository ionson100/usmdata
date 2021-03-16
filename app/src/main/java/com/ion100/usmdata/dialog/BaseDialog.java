package com.ion100.usmdata.dialog;

import android.content.Context;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.HashSet;
import java.util.Set;

public class BaseDialog extends DialogFragment {

    private String tag;
    private static Set<String> strings=new HashSet<>();
    @Override
    public void show(FragmentManager manager, String tag) {
        try {
            if(strings.contains(tag)) {
                return;
            }
            strings.add(tag);
            this.tag=tag;
            FragmentTransaction ft = manager.beginTransaction();
            ft.add(this, tag);
            ft.commitAllowingStateLoss();
            ft.commit();
        } catch (IllegalStateException e) {
            Log.d("ABSDIALOGFRAG", "Exception", e);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        strings.remove(this.tag);
    }
    public void hideKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getDialog().getWindow().getDecorView().getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
    }
}