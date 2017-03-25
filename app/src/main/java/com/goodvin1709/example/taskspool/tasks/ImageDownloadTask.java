package com.goodvin1709.example.taskspool.tasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.Callable;

public class ImageDownloadTask extends AsyncTask<String, Void, Bitmap> {

    private String url;

    public ImageDownloadTask(String url) {
        this.url = url;
    }

    private Bitmap downloadImage() throws IOException {
        return BitmapFactory.decodeStream(new URL(url).openStream());
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        try {
            return downloadImage();
        } catch (IOException e) {
            return null;
        }
    }
}
