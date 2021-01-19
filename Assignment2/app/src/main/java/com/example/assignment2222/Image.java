package com.example.assignment2222;

import android.graphics.Bitmap;

public class Image {
    private int id;
    private String name;
    Bitmap imageSource;

    public Image(int id, String name, Bitmap imageSource) {
        this.id = id;
        this.name = name;
        this.imageSource = imageSource;
    }

    public Image(String name, Bitmap imageSource) {
        this.id = 0;
        this.name = name;
        this.imageSource = imageSource;
    }

    public Image() {
        this.id = 0;
        this.name = null;
        this.imageSource = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Bitmap getImage_source() {
        return imageSource;
    }

    public void setImage_source(Bitmap imageSource) {
        this.imageSource = imageSource;
    }
}
