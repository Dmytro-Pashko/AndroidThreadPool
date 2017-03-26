package com.goodvin1709.example.taskspool.tasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.goodvin1709.example.taskspool.DownloadListener;

import java.io.IOException;
import java.net.URL;

public class ImageDownloadTask implements Runnable {

    private String url;
    private DownloadListener handler;

    public ImageDownloadTask(String url, DownloadListener handler) {
        this.url = url;
        this.handler = handler;
    }

    private void downloadImage() throws IOException {
        Bitmap image = BitmapFactory.decodeStream(new URL(url).openStream());
        handler.onImageDownloaded(image);
        handler = null;
    }

    @Override
    public void run() {
        try {
            downloadImage();
        } catch (IOException e) {
            handler.onDownloadImageError(url);
        }
    }
}
