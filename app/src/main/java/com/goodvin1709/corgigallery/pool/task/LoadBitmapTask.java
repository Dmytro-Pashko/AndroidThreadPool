package com.goodvin1709.corgigallery.pool.task;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.goodvin1709.corgigallery.controller.CacheListener;
import com.goodvin1709.corgigallery.model.Image;
import com.goodvin1709.corgigallery.utils.HashUtils;

import java.io.File;

public class LoadBitmapTask implements Runnable {

    private Image image;
    private int bitmapSize;
    private File cacheDir;
    private CacheListener handler;

    public LoadBitmapTask(Image image, File cacheDir, int bitmapSize, CacheListener handler) {
        this.cacheDir = cacheDir;
        this.image = image;
        this.bitmapSize = bitmapSize;
        this.handler = handler;
    }

    @Override
    public void run() {
        loadBitmap();
    }

    private void loadBitmap() {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        loadBitmapFromExternalCache(options, image);
        options.inSampleSize = getScale(options);
        options.inJustDecodeBounds = false;
        Bitmap bitmap = loadBitmapFromExternalCache(options, image);
        if (bitmap == null) {
            handler.onLoadCacheError(image);
        } else {
            handler.onImageLoadedFromExternalCache(image, bitmapSize, bitmap);
        }
    }

    private int getScale(BitmapFactory.Options options) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        if (height > width) {
            return Math.round((float) height / (float) bitmapSize);
        } else {
            return Math.round((float) width / (float) bitmapSize);
        }
    }

    private Bitmap loadBitmapFromExternalCache(BitmapFactory.Options options, Image image) {
        File file = getImageCacheFile(image);
        return BitmapFactory.decodeFile(file.getAbsolutePath(), options);
    }

    private File getImageCacheFile(Image image) {
        return new File(cacheDir, HashUtils.md5(image.getUrl()));
    }
}
