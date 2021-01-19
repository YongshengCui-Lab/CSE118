package com.example.assignment2222;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DownloadImageActivity extends AppCompatActivity implements View.OnClickListener{

    ConnectivityManager connectivityManager;
    DownloadImageThread image_download_thread;
    Intent intent = null;
    Button download_images_button;
    EditText name, url;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        Log.d(DownloadImageActivity.class.getSimpleName(), "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_image);

        download_images_button = findViewById(R.id.download);
        name = findViewById(R.id.image_name);
        url = findViewById(R.id.image_url);
        download_images_button.setOnClickListener(DownloadImageActivity.this);
    }

    @Override
    public void onClick(View view){
        if (view.getId() == R.id.download){
            ArrayList<EditText> textFields = new ArrayList<EditText>();
            textFields.add(name);
            textFields.add(url);

            if(test_cases(textFields) == true){
                image_download_thread = new DownloadImageThread();
                String url_string = url.getText().toString();
                try{
                    connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                    if(connectivityManager == null){
                        Toast.makeText(DownloadImageActivity.this, "No internet connection!", Toast.LENGTH_LONG).show();
                    }
                    else {
                        String image_name_string = name.getText().toString();
                        Bitmap image_bitmap = image_download_thread.execute(url_string).get();
                        Image image = new Image(image_name_string, image_bitmap);
                        MainActivity.database.addImage(image);
                        finish();
                    }
                }
                catch(Exception e) {
                    Toast.makeText(DownloadImageActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    private boolean isValidUrl(String url){
        Pattern pattern = Patterns.WEB_URL;
        Matcher matcher = pattern.matcher(url.toLowerCase());

        ArrayList<String> image_extensions_4 = new ArrayList<String>();
        ArrayList<String> image_extensions_3 = new ArrayList<String>();
        ArrayList<String> image_extensions_2 = new ArrayList<String>();

        image_extensions_4.add(".jpeg");
        image_extensions_4.add(".indd");
        image_extensions_4.add(".tiff");
        image_extensions_3.add(".jpg");
        image_extensions_3.add(".png");
        image_extensions_3.add(".gif");
        image_extensions_3.add(".psd");
        image_extensions_3.add(".pdf");
        image_extensions_3.add(".eps");
        image_extensions_3.add(".raw");
        image_extensions_2.add(".ai");

        if(matcher.matches()){
            try{
                String url_extension = url.toLowerCase().substring(url.length()-3, url.length());

                for(int i = 0; i < image_extensions_2.size(); i++) {
                    if(image_extensions_2.get(i).equals(url_extension)) {
                        return true;
                    }
                }
                url_extension = url.toLowerCase().substring(url.length()-4, url.length());
                for(int i = 0; i < image_extensions_3.size(); i++){
                    if(image_extensions_3.get(i).equals(url_extension)) {
                        return true;
                    }
                }
                url_extension = url.toLowerCase().substring(url.length()-5, url.length());

                for(int i = 0; i < image_extensions_4.size(); i++){
                    if(image_extensions_4.get(i).equals(url_extension)){
                        return true;
                    }
                }
            } catch(Exception e) {
                return false;
            }
        }
        return false;
    }

    private boolean test_cases(ArrayList<EditText> arrayList){
        for(int i = 0; i < arrayList.size(); i++){
            if(arrayList.get(i).getText().toString().length() == 0){
                Toast.makeText(DownloadImageActivity.this, "Please fill out information", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        String urls = url.getText().toString();
        if(isValidUrl(urls)){
            return true;
        }
        else{
            Toast.makeText(DownloadImageActivity.this, "Invalid URL", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}

