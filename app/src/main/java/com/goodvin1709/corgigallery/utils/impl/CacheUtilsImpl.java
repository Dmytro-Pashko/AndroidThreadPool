package com.goodvin1709.corgigallery.utils.impl;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;
import android.widget.ImageView;

import com.goodvin1709.corgigallery.controller.CacheListener;
import com.goodvin1709.corgigallery.model.Image;
import com.goodvin1709.corgigallery.utils.CacheUtils;
import com.goodvin1709.corgigallery.utils.HashUtils;
import com.goodvin1709.corgigallery.utils.Logger;

import java.io.File;

public class CacheUtilsImpl implements CacheUtils {

    private Context context;
    private LruCache<String, Bitmap> imageLruCache;
    private CacheListener handler;

    public CacheUtilsImpl(CacheListener handler, Context context) {
        this.handler = handler;
        this.context = context;
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 2;
        imageLruCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount() / 1024;
            }
        };
        Logger.log("Created memory cache [%d] bytes.", cacheSize);
    }

    @Override
    public boolean isCachedInMemory(Image image) {
        return imageLruCache.get(image.getUrl()) != null;
    }

    @Override
    public boolean isCachedInExternal(Image image) {
        return new File(context.getCacheDir(), HashUtils.md5(image.getUrl())).exists();
    }

    @Override
    public void saveBitmapToMemoryCache(Image image, Bitmap bitmap) {
        imageLruCache.put(image.getUrl(), bitmap);
        handler.onImageCachedToMemory(image);
    }

    @Override
    public void loadBitmapFromMemoryCache(Image image, ImageView view) {
        view.setImageBitmap(imageLruCache.get(image.getUrl()));
        handler.onImageLoadedFromMemoryCache(image);
    }

    @Override
    public File getCacheDir() {
        return context.getCacheDir();
    }
}
