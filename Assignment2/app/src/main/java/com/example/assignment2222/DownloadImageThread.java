package com.example.assignment2222;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadImageThread extends AsyncTask <String, Void, Bitmap>{
    @Override
    protected Bitmap doInBackground(String... urls){
        try{
            URL url = new URL(urls[0]);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.connect();
            InputStream in = connection.getInputStream();
            Bitmap bitmap_image = BitmapFactory.decodeStream(in);
            in.close();
            return bitmap_image;
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
    @Override
    protected void onPostExecute(Bitmap bitmap){
        super.onPostExecute(bitmap);
    }
}

