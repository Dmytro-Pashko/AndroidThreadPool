package com.goodvin1709.corgigallery.model;


import android.graphics.Bitmap;

public class CachedBitmap {

    private int bitmapSize;

    private Bitmap bitmap;

    public CachedBitmap(int bitmapSize, Bitmap bitmap) {
        this.bitmapSize = bitmapSize;
        this.bitmap = bitmap;
    }

    public int getBitmapSize() {
        return bitmapSize;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

}
