package com.goodvin1709.corgigallery.pool.task;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.goodvin1709.corgigallery.controller.LoadingListener;
import com.goodvin1709.corgigallery.model.Image;
import com.goodvin1709.corgigallery.model.ImageStatus;
import com.goodvin1709.corgigallery.utils.CacheUtils;
import com.goodvin1709.corgigallery.utils.HashUtils;
import com.goodvin1709.corgigallery.utils.Logger;

import java.io.File;

public class LoadBitmapTask implements Runnable {

    private Image image;
    private CacheUtils cache;
    private ImageView view;
    private LoadingListener listener;

    public LoadBitmapTask(Image image, CacheUtils cache, ImageView view, LoadingListener listener) {
        this.cache = cache;
        this.image = image;
        this.view = view;
        this.listener = listener;
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
            Logger.log("Error while loading %s from cache.", image);
            downloadingError();
        } else {
            Logger.log("%s loaded from external cache.", image);
            image.setStatus(ImageStatus.IDLE);
            cache.saveBitmapToMemoryCache(image, view.getWidth(), bitmap);
            Logger.log("%s saved to memory cache.", image);
            setBitmapIntoView(bitmap);
        }
    }

    private int getScale(BitmapFactory.Options options) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        if (height > width) {
            return Math.round((float) height / (float) view.getWidth());
        } else {
            return Math.round((float) width / (float) view.getWidth());
        }
    }

    private Bitmap loadBitmapFromExternalCache(BitmapFactory.Options options, Image image) {
        File file = getImageCacheFile(image);
        return BitmapFactory.decodeFile(file.getAbsolutePath(), options);
    }

    private File getImageCacheFile(Image image) {
        return new File(cache.getCacheDir(), HashUtils.md5(image.getUrl()));
    }

    private void setBitmapIntoView(final Bitmap bitmap) {
        view.post(new Runnable() {
            @Override
            public void run() {
                view.setImageBitmap(bitmap);
                listener.onLoadComplete();
            }
        });
    }

    private void downloadingError() {
        view.post(new Runnable() {
            @Override
            public void run() {
                listener.onLoadFail();
            }
        });
    }
}
