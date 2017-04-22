package com.goodvin1709.corgigallery.model;

import android.graphics.Bitmap;

public class Image {

    private final String url;
    private Bitmap bitmap;

    public Image(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
