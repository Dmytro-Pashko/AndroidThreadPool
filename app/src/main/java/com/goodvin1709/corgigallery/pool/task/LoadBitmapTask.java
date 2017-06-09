package com.goodvin1709.corgigallery.pool.task;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.goodvin1709.corgigallery.controller.ImageLoadingHandler;
import com.goodvin1709.corgigallery.model.Image;
import com.goodvin1709.corgigallery.model.ImageStatus;
import com.goodvin1709.corgigallery.utils.CacheUtils;
import com.goodvin1709.corgigallery.utils.HashUtils;
import com.goodvin1709.corgigallery.utils.Logger;

import java.io.File;

public class LoadBitmapTask implements Runnable {

    private Image image;
    private int bitmapSize;
    private CacheUtils cache;
    private ImageLoadingHandler handler;

    public LoadBitmapTask(Image image, CacheUtils cache, int bitmapSize, ImageLoadingHandler handler) {
        this.cache = cache;
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
            image.setStatus(ImageStatus.CACHED_ERROR);
            Logger.log("Error while loading Image[%s] from cache.", image.getUrl());
            handler.sendMessage(handler.obtainMessage(ImageLoadingHandler.IMAGE_LOADED_FAIL_MSG));
        } else {
            Logger.log("Image[%s] loaded from external cache.", image.getUrl());
            image.setStatus(ImageStatus.IDLE);
            cache.saveBitmapToMemoryCache(image, bitmapSize, bitmap);
            Logger.log("Image[%s] saved to memory cache.", image.getUrl());
            handler.sendMessage(handler.obtainMessage(ImageLoadingHandler.IMAGE_LOADED_SUCCESS_MSG));
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
        return new File(cache.getCacheDir(), HashUtils.md5(image.getUrl()));
    }
}
