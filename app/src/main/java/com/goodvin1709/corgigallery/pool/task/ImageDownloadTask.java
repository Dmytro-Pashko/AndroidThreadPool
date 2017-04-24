package com.goodvin1709.corgigallery.pool.task;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.goodvin1709.corgigallery.controller.DownloadListener;
import com.goodvin1709.corgigallery.model.Image;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

public class ImageDownloadTask implements Runnable {

    private static final int CONNECTION_TIMEOUT = 10000;
    private Image image;
    private DownloadListener handler;

    public ImageDownloadTask(Image image, DownloadListener handler) {
        this.handler = handler;
        this.image = image;
    }

    private void downloadImage() throws IOException {
        URL url = new URL(image.getUrl());
        URLConnection connection = url.openConnection();
        connection.setConnectTimeout(CONNECTION_TIMEOUT);
        Bitmap bitmap = BitmapFactory.decodeStream(connection.getInputStream());
        handler.onImageDownloaded(image, bitmap);
    }

    @Override
    public void run() {
        try {
            downloadImage();
        } catch (IOException e) {
            handler.onDownloadImageError(image);
        }
    }
}