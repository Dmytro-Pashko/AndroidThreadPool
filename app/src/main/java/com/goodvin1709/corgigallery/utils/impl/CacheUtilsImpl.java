package com.goodvin1709.corgigallery.utils.impl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.goodvin1709.corgigallery.controller.CacheListener;
import com.goodvin1709.corgigallery.model.Image;
import com.goodvin1709.corgigallery.utils.CacheUtils;
import com.goodvin1709.corgigallery.utils.HashUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class CacheUtilsImpl implements CacheUtils {

    private static final int COMPRESS_QUALITY = 90;
    private Context context;
    private CacheListener handler;

    public CacheUtilsImpl(Context context, CacheListener handler) {
        this.context = context;
        this.handler = handler;
    }

    @Override
    public boolean isCached(Image image) {
        File cachedImage = new File(context.getExternalCacheDir(), HashUtils.md5(image.getUrl()));
        return cachedImage.exists();
    }

    @Override
    public void saveBitmapToCache(Image image, Bitmap bitmap) {
        File file = new File(context.getExternalCacheDir(), HashUtils.md5(image.getUrl()));
        try {
            createFile(file);
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, COMPRESS_QUALITY, out);
            out.flush();
            out.close();
            handler.onImageCached(image);
        } catch (FileNotFoundException e) {
            handler.onSaveCacheError(image);
        } catch (IOException e) {
            handler.onSaveCacheError(image);
        }
    }

    private void createFile(File file) throws IOException {
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
    }

    @Override
    public void loadBitmapFromCache(Image image, ImageView view) {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            loadBitmap(options, image);
            options.inSampleSize = getScale(options, view);
            options.inJustDecodeBounds = false;
            Bitmap bitmap = loadBitmap(options, image);
            view.setImageBitmap(bitmap);
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            handler.onImageLoadedFromCache(image);
        } catch (IOException e) {
            handler.onLoadCacheError(image);
        }
    }

    private Bitmap loadBitmap(BitmapFactory.Options options, Image image) throws IOException {
        File file = new File(context.getExternalCacheDir(), HashUtils.md5(image.getUrl()));
        return BitmapFactory.decodeStream(new FileInputStream(file), null, options);
    }

    private int getScale(BitmapFactory.Options options, ImageView view) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        if (height > width) {
            return Math.round((float) height / (float) view.getLayoutParams().height);
        } else {
            return Math.round((float) width / (float)view.getLayoutParams().width);
        }
    }
}
