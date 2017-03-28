package com.goodvin1709.example.taskspool.tasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.goodvin1709.example.taskspool.Dimensions;
import com.goodvin1709.example.taskspool.DownloadListener;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class ImageDownloadTask implements Runnable {

    private String url;
    private DownloadListener handler;
    private Dimensions outDimensions;

    public ImageDownloadTask(String url, Dimensions outDimensions, DownloadListener handler) {
        this.url = url;
        this.handler = handler;
        this.outDimensions = outDimensions;
    }

    private void downloadImage() throws IOException {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(new URL(url).openStream(), null, options);
        options.inSampleSize = getScale(options);
        options.inJustDecodeBounds = false;
        handler.onImageDownloaded(BitmapFactory.decodeStream(new URL(url).openStream(), null, options));
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

    private int getScale(BitmapFactory.Options options) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        if (height > width) {
            return Math.round((float) height / (float) outDimensions.height);
        } else {
            return Math.round((float) width / (float) outDimensions.width);
        }
    }
}
