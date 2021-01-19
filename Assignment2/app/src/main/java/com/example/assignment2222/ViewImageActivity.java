package com.example.assignment2222;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;

public class ViewImageActivity extends AppCompatActivity {

    public static ArrayList<Image> imageList;
    Intent intent = null;
    CustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);

        ListView image_listView = (ListView) findViewById(R.id.image_listView);
        imageList = new ArrayList<Image>();
        try{
            imageList = MainActivity.database.getAllImages();
        }
        catch (Exception e){
            Toast.makeText(ViewImageActivity.this, "Error", Toast.LENGTH_LONG).show();
        }
        adapter = new CustomAdapter(ViewImageActivity.this, R.layout.custom_row, imageList);
        image_listView.setAdapter(adapter);
    }
}
