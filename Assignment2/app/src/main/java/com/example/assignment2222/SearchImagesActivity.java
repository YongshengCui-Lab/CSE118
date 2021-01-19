package com.example.assignment2222;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class SearchImagesActivity extends AppCompatActivity implements View.OnClickListener{
    Intent intent = null;
    ArrayList<Image> imageList;
    Button search_button;
    CustomAdapter adapter;
    EditText search_editText;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_images);
        search_button = findViewById(R.id.search_button);
        search_editText = findViewById(R.id.search_bar);

        imageList = new ArrayList<Image>();

        search_button.setOnClickListener(SearchImagesActivity.this);
    }

    @Override
    public void onClick(View view){
        if (view.getId() == R.id.search_button){
            String image_name_string = search_editText.getText().toString();
            listView = (ListView) findViewById(R.id.ListView);
            try{
                imageList = MainActivity.database.searchImage(image_name_string);
            } catch(Exception e){
                Toast.makeText(SearchImagesActivity.this, "Error loading images", Toast.LENGTH_LONG).show();
            }
            adapter = new CustomAdapter(SearchImagesActivity.this, R.layout.custom_row, imageList);
            listView.setAdapter(adapter);
        }
    }
}
