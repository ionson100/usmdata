package com.ion100.usmdata.ui.slideshow;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.ion100.usmdata.IAction;
import com.ion100.usmdata.MainActivity;
import com.ion100.usmdata.R;
import com.ion100.usmdata.dialog.DialogFactory;
import com.ion100.usmdata.models.MWork;
import com.ion100.usmdata.orm2.Configure;
import com.ion100.usmdata.senders.SendefFileToServer;
import com.ion100.usmdata.senders.SenderWorkToServer;
import com.ion100.usmdata.utils.Patcher;
import com.ion100.usmdata.utils.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

public class SlideshowFragment extends Fragment {


    private static String curentPathImage;

    private static String curentPathVideo;

    private TextView textViewLocale;
    private  Spinner spinnerLocale;

    private MWork mWorkCurrent;
    private TextView textViewLevel;
    private  Spinner spinnerLelel;
    private EditText text_description;
    public static final int RequestPermissionCode = 1;
    public LinearLayout panelHost;
    private final int RESULT_LOAD_IMG=21;
    protected static final int RESULT_SPEECH = 2;



    final int REQUEST_CODE_PHOTO = 1;
    final int REQUEST_CODE_VIDEO = 4;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);
        panelHost=root.findViewById(R.id.panel_host);


        EnableRuntimePermission();

        final List<MWork> mWorks= Configure.getSession().getList(MWork.class,"is_commit = 0 order by date");
        if(mWorks.size()==0){
            MWork mWork=new MWork();
            mWork.dateCreate= new Date();
            Configure.getSession().insert(mWork);
            mWorkCurrent=mWork;
        }else {
            mWorkCurrent=mWorks.get(0);
        }



        {
            textViewLocale = root.findViewById(R.id.locale_text);
            spinnerLocale = root. findViewById(R.id.spinner_locale_text);
            spinnerLocale.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    textViewLocale.setText(spinnerLocale.getSelectedItem().toString());
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    textViewLocale.setText(spinnerLocale.getSelectedItem().toString());
                }
            });
        }

        {
            textViewLevel = root.findViewById(R.id.level_text);
            spinnerLelel = root. findViewById(R.id.spinner_level_text);
            spinnerLelel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    textViewLevel.setText(spinnerLelel.getSelectedItem().toString());
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    textViewLevel.setText(spinnerLelel.getSelectedItem().toString());
                }
            });
        }
        {
            text_description = root.findViewById(R.id.edittext_description);
            text_description.requestFocus();
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(text_description, InputMethodManager.SHOW_IMPLICIT);
        }

        {
            root.findViewById(R.id.bt_photo_galery).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
                }
            });
        }



        {
            root.findViewById(R.id.bt_photo).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    curentPathImage=Patcher.patchDirDirUsm+"/"+UUID.randomUUID()+".jpeg";
                    File cameraPhoto = new File(curentPathImage);
                    Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    Uri photoUri = FileProvider.getUriForFile(
                            getActivity(), "com.ion100.usmdata.provider", cameraPhoto);
                    takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                    startActivityForResult(takePhotoIntent, REQUEST_CODE_PHOTO);

                }


            });
        }

        {
            root.findViewById(R.id.bt_video).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    curentPathVideo=Patcher.patchDirDirUsm+"/"+UUID.randomUUID()+".mp4";
                    File cameraPhoto = new File(curentPathVideo);
                    Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                    Uri photoUri = FileProvider.getUriForFile(
                            getActivity(), "com.ion100.usmdata.provider", cameraPhoto);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                    startActivityForResult(intent, REQUEST_CODE_VIDEO);
                }


            });
        }




        {
            root.findViewById(R.id.bt_speackTo_text).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                    intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Говори сука");
                    try {
                        startActivityForResult(intent, RESULT_SPEECH);
                    } catch (ActivityNotFoundException a) {
                        Toast.makeText(getActivity(), "Микрофон не установлен",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        {
            root.findViewById(R.id.bt_sender_work).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



                    if(validate()){
                        SenderWorkToServer senderWorkToServer=new SenderWorkToServer();
                        senderWorkToServer.send(getActivity(), mWorkCurrent, new IAction<MWork>() {
                            @Override
                            public void action(MWork o) {


                                if(o.listImage.size()==0){
                                    commitSend();
                                }else{
                                   new SendefFileToServer().send(getActivity(), o.listImage, new IAction<Object>() {
                                       @Override
                                       public void action(Object o) {
                                           commitSend();
                                       }
                                   });
                                }

                            }

                            private void commitSend() {
                                mWorkCurrent.isCommit=true;
                                Configure.getSession().update(mWorkCurrent);
                                DialogFactory.dialogInfo((AppCompatActivity) getActivity(),"Отправка данных","Успешно",null);
                            }
                        });
                    }


                }

                private boolean validate() {
                    if(mWorkCurrent.description.trim().length()==0){
                        DialogFactory.dialogInfo((AppCompatActivity) getActivity(),Utils.ERROR,"Описание работы не заполнено",null);
                        return false;
                    }
                    return true;
                }
            });
        }
        text_description.addTextChangedListener(new TextWatcher(){
            @Override
            public void afterTextChanged(Editable s) {
               mWorkCurrent.description=s.toString();
               Configure.getSession().update(mWorkCurrent);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        initSliderImage();


        return root;
    }






    public void EnableRuntimePermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                Manifest.permission.CAMERA)) {
            Toast.makeText(getActivity(),"CAMERA permission allows us to Access CAMERA app",     Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(getActivity(),new String[]{
                    Manifest.permission.CAMERA}, RequestPermissionCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] result) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (result.length > 0 && result[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getActivity(), "Permission Granted, Now your application can access CAMERA.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "Permission Canceled, Now your application cannot access CAMERA.", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }



    private void initSliderImage(){
        for (final String s : mWorkCurrent.listImage) {

            if(s.contains("mp4")){
                addImageToHostVideo(s);
            }else{
                addImageToHost(s);
            }

        }
        text_description.setText(mWorkCurrent.description);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);



        if(resultCode==RESULT_OK){
            switch (requestCode){
                case REQUEST_CODE_PHOTO :{

                    if(curentPathImage==null) break;

                    File file=new File(curentPathImage);
                    if(file.exists()){
                        addImageToHost(curentPathImage);
                        mWorkCurrent.listImage.add(curentPathImage);
                        Configure.getSession().update(mWorkCurrent);
                    }
                    break;
                }
                case REQUEST_CODE_VIDEO :{

                    if(curentPathVideo==null) break;

                    File file=new File(curentPathVideo);
                    if(file.exists()){
                        addImageToHostVideo(curentPathVideo);
                        mWorkCurrent.listImage.add(curentPathVideo);
                        Configure.getSession().update(mWorkCurrent);
                    }
                    break;
                }
                case RESULT_LOAD_IMG:{

                    try {
                        final Uri imageUri = data.getData();
                        final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                        final Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                        final String path=saveImage(bitmap);
                        addImageToHost(path);
                        mWorkCurrent.listImage.add(path);
                        Configure.getSession().update(mWorkCurrent);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG).show();
                    }
                    break;
                }
                case RESULT_SPEECH: {
                    if (resultCode == RESULT_OK && null != data) {

                        ArrayList<String> text = data
                                .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                        String d=text_description.getText().toString();
                        text_description.setText(d+" "+text.get(0));
                    }
                    break;
                }
            }
        }
    }

    private void addImageToHost(final String path) {
        ImageView imageView = new ImageView(getContext());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(5, 5, 5, 5);
        lp.weight = 300;
        lp.height = 300;
        imageView.setLayoutParams(lp);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setAdjustViewBounds(true);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFactory.dialogShowImage((AppCompatActivity) getActivity(), path, new IAction<Object>() {
                    @Override
                    public void action(Object o) {
                        String p= (String) o;
                        mWorkCurrent.listImage.remove(p);
                        Configure.getSession().update(mWorkCurrent);
                        panelHost.removeAllViews();
                        initSliderImage();
                    }
                });
            }
        });
        imageView.setTag(path);


        imageView.setImageBitmap(Utils.getBitmap(path));


        panelHost.addView(imageView);
    }

    private void addImageToHostVideo(final String path) {

        ImageView imageView = new ImageView(getContext());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(5, 5, 5, 5);


        imageView.setLayoutParams(lp);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setAdjustViewBounds(true);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFactory.dialogShowImage((AppCompatActivity) getActivity(), path, new IAction<Object>() {
                    @Override
                    public void action(Object o) {
                        String p= (String) o;
                        mWorkCurrent.listImage.remove(p);
                        Configure.getSession().update(mWorkCurrent);
                        panelHost.removeAllViews();
                        initSliderImage();
                    }
                });
            }
        });
        imageView.setTag(path);
        Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(path, MediaStore.Images.Thumbnails.MINI_KIND);
        imageView.setImageBitmap(thumbnail);
        panelHost.addView(imageView);
    }


    private String saveImage(Bitmap bitmap){


        File file = new File(Patcher.patchDirDirUsm, UUID.randomUUID().toString() +".jpg");


        try {
            file.createNewFile();
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
            return null;
        }
        return file.getAbsolutePath();
    }


}