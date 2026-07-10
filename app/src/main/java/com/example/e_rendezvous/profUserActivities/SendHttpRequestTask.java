package com.example.e_rendezvous.profUserActivities;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SendHttpRequestTask extends AsyncTask<String, Void, Bitmap> {

    private String parameter;

    public SendHttpRequestTask(String username){
        this.parameter = username;
    }
    @Override
    protected Bitmap doInBackground(String... params) {
        try {
            URL url = new URL("https://example.com/api/uploads/" + parameter + ".jpg");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        }catch (Exception e){
            System.out.println("Photo download failed");
        }
        return null;
    }
}
