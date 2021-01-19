package com.example.assignment2222;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<Image> implements View.OnClickListener{
    TextView id, name;
    ImageView bitmap;
    Context customContext;
    ImageButton delete_button;
    int custom_resource_layout;

    public CustomAdapter(Context context, int resource, ArrayList<Image> images){
        super(context, resource, images);
        custom_resource_layout = resource;
        customContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        int imageId = getItem(position).getId();
        String imageName = getItem(position).getName();
        Bitmap imageBitmap = getItem(position).getImage_source();

        LayoutInflater inflater = LayoutInflater.from(customContext);

        convertView = inflater.inflate(custom_resource_layout, parent, false);

        id = convertView.findViewById(R.id.id);
        name = convertView.findViewById(R.id.title);
        bitmap = convertView.findViewById(R.id.image);
        delete_button = convertView.findViewById(R.id.delete);

        delete_button.setTag(position);
        id.setText(Integer.toString(imageId));
        bitmap.setImageBitmap(imageBitmap);
        name.setText(imageName);
        delete_button.setOnClickListener(CustomAdapter.this);
        return convertView;
    }

    @Override
    public void onClick(View view){
        if(view.getId() == R.id.delete){
            Integer index = (Integer) view.getTag();
            int id = getItem(index.intValue()).getId();
            String name = getItem(index.intValue()).getName() ;
            Bitmap bitmap = getItem(index.intValue()).getImage_source();
            Image image_to_delete = new Image(id, name, bitmap);
            remove(getItem(index.intValue()));
            MainActivity.database.deleteImage(image_to_delete);
        }
    }
}

