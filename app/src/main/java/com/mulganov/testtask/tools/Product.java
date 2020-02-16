package com.mulganov.testtask.tools;

import android.graphics.Bitmap;

public class Product {

    Bitmap bitmap;


    public Product(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String toString(){
        return bitmap.getWidth() + "X" + bitmap.getHeight();
    }
}