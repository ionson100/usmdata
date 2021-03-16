package com.ion100.usmdata.senders;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Base64;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.ion100.usmdata.IAction;
import com.ion100.usmdata.dialog.DialogFactory;
import com.ion100.usmdata.models.MWork;
import com.ion100.usmdata.utils.TotalSettings;
import com.ion100.usmdata.utils.Utils;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



public class SenderWorkToServer {

    private MWork mWork;
    private IAction<MWork> iAction;
    private ProgressDialog mDialog ;
    private String message;
    private Exception exception;
    private Activity activity;

    public void send(Activity activity, MWork mWork, IAction<MWork> o) {
        this.activity = activity;
        this.mWork = mWork;
        iAction = o;
        try{

            Gson gson=new Gson();
            message = gson.toJson(mWork);
        }catch (Exception e){
            DialogFactory.dialogInfo((AppCompatActivity) activity,"Error конвертация",e.toString(),null);
        }

        new Inneraction().execute();
    }

    private class Inneraction extends AsyncTask<Void,Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            String url = String.format("http://%s/api/values/5",
                    TotalSettings.getTotalSettings().serverUrl);
            InputStream stream = null;
            BufferedWriter httpRequestBodyWriter = null;
            OutputStream outputStreamToRequestBody = null;
            OutputStreamWriter dd = null;
            HttpURLConnection connection = null;
            try {
                URL serverUrl = new URL(url);
                connection = (HttpURLConnection) serverUrl.openConnection();
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                connection.setReadTimeout(Utils.READ_CONNECT_TIMEOUT /*milliseconds*/);
                connection.setConnectTimeout(Utils.READ_CONNECT_TIMEOUT /* milliseconds */);
                outputStreamToRequestBody = connection.getOutputStream();
                dd = new OutputStreamWriter(outputStreamToRequestBody);
                httpRequestBodyWriter = new BufferedWriter(dd);
                httpRequestBodyWriter.flush();
                stream = new ByteArrayInputStream(message.getBytes());
                int bytesRead;
                byte[] dataBuffer = new byte[1024];
                while ((bytesRead = stream.read(dataBuffer)) != -1) {
                    outputStreamToRequestBody.write(dataBuffer, 0, bytesRead);
                }
                outputStreamToRequestBody.flush();
                httpRequestBodyWriter.flush();
                outputStreamToRequestBody.close();
                httpRequestBodyWriter.close();
                connection.connect();

                int status = connection.getResponseCode();

                if (status == 200) {

                }else {
                    String s = String.format(" Отправка данных на сервер  статус: %d ", status);
                    throw new RuntimeException(s);
                }

            } catch (Exception ex) {
                exception = ex;
            } finally {

                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    Utils.closerableAll(httpRequestBodyWriter, outputStreamToRequestBody, stream, dd);
                } catch (Exception ex) {

                }

            }


            return null;
        }

        @Override
        protected void onPreExecute() {
            if(iAction!=null){
                mDialog  = new ProgressDialog(activity);
                mDialog.setMessage("Отправка данных на сервер");
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
                    iAction.action(mWork);
                }
            }


        }
    }
}


