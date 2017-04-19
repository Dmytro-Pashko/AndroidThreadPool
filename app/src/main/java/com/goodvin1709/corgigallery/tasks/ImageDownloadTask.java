package com.goodvin1709.corgigallery.tasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.goodvin1709.corgigallery.DownloadListener;
import com.goodvin1709.corgigallery.Image;

import java.io.IOException;
import java.net.URL;

public class ImageDownloadTask implements Runnable {

    private Image image;
    private DownloadListener handler;
    private int outHeight;
    private int outWidth;

    public ImageDownloadTask(Image image, int outHeight, int outWidth, DownloadListener handler) {
        this.image = image;
        this.handler = handler;
        this.outHeight = outHeight;
        this.outWidth = outWidth;
    }

    @Override
    public void run() {
        try {
            downloadImage();
        } catch (IOException e) {
            handler.onDownloadImageError(image);
            handler = null;
        }
    }

    private void downloadImage() throws IOException {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        decodeBitmap(options);
        options.inSampleSize = getScale(options);
        options.inJustDecodeBounds = false;
        image.setBitmap(decodeBitmap(options));
        handler.onImageDownloaded(image);
        handler = null;
    }

    private Bitmap decodeBitmap(BitmapFactory.Options options) throws IOException {
        return BitmapFactory.decodeStream(new URL(image.getUrl()).openStream(), null, options);
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
