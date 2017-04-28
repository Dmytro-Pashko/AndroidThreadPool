package com.goodvin1709.corgigallery.utils.impl;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.widget.ImageView;

import com.goodvin1709.corgigallery.controller.CacheListener;
import com.goodvin1709.corgigallery.model.Image;
import com.goodvin1709.corgigallery.utils.CacheUtils;
import com.goodvin1709.corgigallery.utils.HashUtils;
import com.goodvin1709.corgigallery.utils.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CacheUtilsImpl implements CacheUtils {

    private static final int COMPRESS_QUALITY = 90;
    private static final String EXTERNAL_CACHE_DIR = "CorgiGallery";
    private CacheListener handler;
    private LruCache<String, Bitmap> imageLruCache;

    public CacheUtilsImpl(CacheListener handler) {
        this.handler = handler;
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 4;
        imageLruCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount() / 1024;
            }
        };
        Logger.log("Created memory cache [%d] bytes.", cacheSize);
    }

    @Override
    public boolean isCached(Image image) {
        File cachedImage = getImageCacheFile(image);
        return cachedImage.exists() || imageLruCache.get(image.getUrl()) != null;
    }

    @Override
    public void saveBitmapToCache(Image image, Bitmap bitmap) {
        File file = getImageCacheFile(image);
        try {
            createFile(file);
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, COMPRESS_QUALITY, out);
            out.flush();
            out.close();
            Logger.log("Image[%s] saved to external cache.", image.getUrl());
            handler.onImageCached(image);
        } catch (FileNotFoundException e) {
            handler.onSaveCacheError(image);
        } catch (IOException e) {
            handler.onSaveCacheError(image);
        }
    }

    @Override
    public void loadBitmapFromCache(Image image, ImageView view) {
        Bitmap bitmap = loadBitmap(image, view.getLayoutParams().height,view.getLayoutParams().width);
        view.setImageBitmap(bitmap);
        view.setVisibility(View.VISIBLE);
        handler.onImageLoadedFromCache(image);
    }

    private File getImageCacheFile(Image image) {
        return new File(Environment.getExternalStorageDirectory() + File.separator + EXTERNAL_CACHE_DIR,
                HashUtils.md5(image.getUrl()));
    }

    private void createFile(File file) throws IOException {
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdir();
        }
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
    }

    private Bitmap loadBitmap(Image image, int height, int width) {
        Bitmap bitmap = imageLruCache.get(image.getUrl());
        if (bitmap == null) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            loadBitmapFromExternalCache(options, image);
            options.inSampleSize = getScale(options, height, width);
            options.inJustDecodeBounds = false;
            bitmap = loadBitmapFromExternalCache(options, image);
            Logger.log("Image[%s] loaded from external cache.", image.getUrl());
            if (bitmap != null) {
                imageLruCache.put(image.getUrl(), bitmap);
            }
        } else {
            Logger.log("Image[%s] loaded from memory cache.", image.getUrl());
        }
        return bitmap;
    }

    private Bitmap loadBitmapFromExternalCache(BitmapFactory.Options options, Image image) {
        File file = getImageCacheFile(image);
        try {
            FileInputStream inputStream = new FileInputStream(file);
            return BitmapFactory.decodeStream(inputStream, null, options);
        } catch (FileNotFoundException e) {
            Logger.log("Image[%s] cache file not found in external.", image.getUrl());
            handler.onLoadCacheError(image);
        }
        return null;
    }

    private int getScale(BitmapFactory.Options options, int reqHeight, int reqWidth) {
        Logger.log("Edited msg.");
        final int height = options.outHeight;
        final int width = options.outWidth;
        if (height > width) {
            return Math.round((float) height / (float) reqHeight);
        } else {
            return Math.round((float) width / (float) reqWidth);
        }
    }
}
