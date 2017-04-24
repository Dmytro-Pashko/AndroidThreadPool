package com.goodvin1709.corgigallery.pool.task;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.goodvin1709.corgigallery.controller.DownloadListener;
import com.goodvin1709.corgigallery.model.Image;

import java.io.IOException;
import java.net.URL;

public class ImageDownloadTask implements Runnable {

    private Image image;
    private DownloadListener handler;

    public ImageDownloadTask(Image image, DownloadListener handler) {
        this.handler = handler;
        this.image = image;
    }

    private void downloadImage() throws IOException {
        Bitmap bitmap = BitmapFactory.decodeStream(new URL(image.getUrl()).openStream());
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