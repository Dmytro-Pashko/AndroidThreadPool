package com.goodvin1709.example.taskspool.tasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.Callable;

class ImageTask implements Callable<Bitmap> {

    private String url;

    ImageTask(String url) {
        this.url = url;
    }

    @Override
    public Bitmap call() throws Exception {
        return downloadImage();
    }

    private Bitmap downloadImage() {
        Bitmap bitmap = null;
            try {
                bitmap = BitmapFactory.decodeStream((InputStream) new URL(url).getContent());
            } catch (IOException e) {
                e.printStackTrace();
            }
        return bitmap;
    }
}
