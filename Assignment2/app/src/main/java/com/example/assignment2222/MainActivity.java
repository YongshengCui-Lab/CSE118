package com.example.assignment2222;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button download,search,view;
    public static DatabaseHelper database;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        download = findViewById(R.id.DownloadImage);
        search = findViewById(R.id.SearchImage);
        view = findViewById(R.id.ViewImages);
        download.setOnClickListener(MainActivity.this);
        search.setOnClickListener(MainActivity.this);
        view.setOnClickListener(MainActivity.this);
        database = new DatabaseHelper(MainActivity.this, null, null, 1);
    }

    @Override
    public void onClick(View view){
        if(view.getId() == R.id.DownloadImage) {
            Intent intent = new Intent(MainActivity.this, DownloadImageActivity.class);
            startActivity(intent);
        }
        else if(view.getId() == R.id.SearchImage) {
            Intent intent = new Intent(MainActivity.this, SearchImagesActivity.class);
            startActivity(intent);
        }
        else if(view.getId() == R.id.ViewImages) {
            Intent intent = new Intent(MainActivity.this, ViewImageActivity.class);
            startActivity(intent);
        }
    }
}
