package com.ion100.usmdata.senders;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.ion100.usmdata.IAction;
import com.ion100.usmdata.dialog.DialogFactory;
import com.ion100.usmdata.models.MWork;
import com.ion100.usmdata.utils.MultipartUtility;
import com.ion100.usmdata.utils.Patcher;
import com.ion100.usmdata.utils.TotalSettings;
import com.ion100.usmdata.utils.Utils;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class SendefFileToServer {

    private ProgressDialog mDialog;
    private Activity activity;
    private List<String> filenames;
    private IAction<Object> iAction;
    private Exception exception;

    public void send(Activity activity, List<String> filenames, IAction<Object> o) {
        this.activity = activity;
        this.filenames = filenames;

        iAction = o;
        new SendefFileToServer.Inneraction().execute();

    }

    private class Inneraction extends AsyncTask<Void,Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {


            String url = String.format("http://%s/api/upload",
                    TotalSettings.getTotalSettings().serverUrl);
            try
            {
                List<TempFile> fileBodies=new ArrayList<>();
                for (String filename : filenames) {
                    fileBodies.add(new TempFile(filename.substring(filename.lastIndexOf("/")+1),new FileBody(new File(filename))));
                }
                HttpClient client;
                client = new DefaultHttpClient();
                HttpPost post = new HttpPost(url);
                MultipartEntity reqEntity = new MultipartEntity();

                for (TempFile fileBody : fileBodies) {
                    reqEntity.addPart(fileBody.filename, fileBody.file);
                }
                reqEntity.addPart("user", new StringBody("User"));
                post.setEntity(reqEntity);
                HttpResponse response = client.execute(post);
                if(response.getStatusLine().getStatusCode()==200){

                }else {
                    throw  new Exception("Status code response:"+response.getStatusLine().getStatusCode());
                }


            }
            catch (Exception ex){
                exception=ex;
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            if(iAction!=null){
                mDialog  = new ProgressDialog(activity);
                mDialog.setMessage("Отправка фото на сервер");
                mDialog.setProgress(ProgressDialog.STYLE_SPINNER);
                mDialog.setCancelable(false);
                mDialog.show();
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(mDialog!=null){
                mDialog.dismiss();
            }
            if(exception!=null){

                DialogFactory.dialogInfo((AppCompatActivity) activity,Utils.ERROR,
                        "Ошибка отправления данных на сервер: "+exception.getMessage(),null);
            }else {
                if(iAction!=null){
                    iAction.action(null);
                }
            }


        }
    }
    class TempFile{
        public final String filename;
        public final FileBody file;

        public TempFile(String filename, FileBody  file){

            this.filename = filename;
            this.file = file;
        }
    }


}
