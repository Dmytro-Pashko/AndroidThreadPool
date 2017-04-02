package com.goodvin1709.corgigallery;

import android.graphics.Bitmap;

public class Image {

    private String url;
    private Bitmap bitmap;

    public Image(String url, Bitmap bitmap) {
        this.url = url;
        this.bitmap = bitmap;
    }

    public String getUrl() {
        return url;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
}
