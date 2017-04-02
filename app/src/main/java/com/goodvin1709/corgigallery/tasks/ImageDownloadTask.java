package com.goodvin1709.corgigallery.tasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.goodvin1709.corgigallery.DownloadListener;
import com.goodvin1709.corgigallery.Image;

import java.io.IOException;
import java.net.URL;

public class ImageDownloadTask implements Runnable {

    private String url;
    private DownloadListener handler;
    private int outHeight, outWidth;

    public ImageDownloadTask(String url, int outHeight, int outWidth, DownloadListener handler) {
        this.url = url;
        this.handler = handler;
        this.outHeight = outHeight;
        this.outWidth = outWidth;
    }

    @Override
    public void run() {
        try {
            downloadImage();
        } catch (IOException e) {
            handler.onDownloadImageError(url);
            handler = null;
        }
    }

    private void downloadImage() throws IOException {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        decodeBitmap(options);
        options.inSampleSize = getScale(options);
        options.inJustDecodeBounds = false;
        Image image = new Image(url, decodeBitmap(options));
        handler.onImageDownloaded(image);
        handler = null;
    }

    private Bitmap decodeBitmap(BitmapFactory.Options options) throws IOException {
        return BitmapFactory.decodeStream(new URL(url).openStream(), null, options);
    }

    private int getScale(BitmapFactory.Options options) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        if (height > width) {
            return Math.round((float) height / (float) outHeight);
        } else {
            return Math.round((float) width / (float) outWidth);
        }
    }
}
