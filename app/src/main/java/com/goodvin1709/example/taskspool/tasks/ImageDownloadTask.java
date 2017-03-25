package com.goodvin1709.example.taskspool.tasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.Callable;

public class ImageDownloadTask implements Callable<Bitmap> {

    private String url;

    public ImageDownloadTask(String url) {
        this.url = url;
    }

    @Override
    public Bitmap call() throws Exception {
        return downloadImage();
    }

    private Bitmap downloadImage() throws IOException {
        return BitmapFactory.decodeStream(new URL(url).openStream());
    }
}
