package com.goodvin1709.corgigallery.utils.impl;

import android.graphics.Bitmap;
import android.util.LruCache;
import android.widget.ImageView;

import com.goodvin1709.corgigallery.model.CachedBitmap;
import com.goodvin1709.corgigallery.model.Image;
import com.goodvin1709.corgigallery.utils.CacheUtils;
import com.goodvin1709.corgigallery.utils.HashUtils;
import com.goodvin1709.corgigallery.utils.Logger;

import java.io.File;

public class CacheUtilsImpl implements CacheUtils {

    private File cacheDir;
    private LruCache<String, CachedBitmap> imageLruCache;

    public CacheUtilsImpl(File cacheDir) {
        this.cacheDir = cacheDir;
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 2;
        imageLruCache = new LruCache<String, CachedBitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, CachedBitmap image) {
                return image.getBitmap().getByteCount() / 1024;
            }
        };
        Logger.log("Created memory cache [%d] bytes.", cacheSize);
    }

    @Override
    public boolean isCachedInMemory(Image image, int bitmapSize) {
        CachedBitmap bitmap = imageLruCache.get(image.getUrl());
        return bitmap != null && bitmap.getBitmapSize() == bitmapSize;
    }

    @Override
    public boolean isCachedInExternal(Image image) {
        return new File(cacheDir, HashUtils.md5(image.getUrl())).exists();
    }

    @Override
    public void saveBitmapToMemoryCache(Image image, int bitmapSize, Bitmap bitmap) {
        imageLruCache.put(image.getUrl(), new CachedBitmap(bitmapSize, bitmap));
    }

    @Override
    public void loadBitmapFromMemoryCache(Image image, ImageView view) {
        view.setImageBitmap(imageLruCache.get(image.getUrl()).getBitmap());
    }

    @Override
    public File getCacheDir() {
        return cacheDir;
    }
}
