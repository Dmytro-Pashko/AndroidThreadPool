package com.goodvin1709.corgigallery.pool.task;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.goodvin1709.corgigallery.controller.CacheListener;
import com.goodvin1709.corgigallery.model.Image;
import com.goodvin1709.corgigallery.utils.HashUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class LoadFromCacheTask implements Runnable {

    private final Image image;
    private ImageView view;
    private Context context;
    private CacheListener handler;

    public LoadFromCacheTask(Context context, Image image, ImageView view, CacheListener handler) {
        this.context = context;
        this.image = image;
        this.handler = handler;
        this.view = view;
    }

    @Override
    public void run() {
        try {
            loadFromCache();
        } catch (IOException e) {
            handler.onLoadCacheError(image);
        }
    }

    private void loadFromCache() throws IOException {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        loadBitmap(options);
        options.inSampleSize = getScale(options);
        options.inJustDecodeBounds = false;
        //view.setImageBitmap(loadBitmap(options));
        //view.setScaleType(ImageView.ScaleType.CENTER_CROP);
        handler.onImageLoadedFromCache(image);
    }

    private Bitmap loadBitmap(BitmapFactory.Options options) throws IOException {
        File file = new File(context.getExternalCacheDir(), HashUtils.md5(image.getUrl()));
        return BitmapFactory.decodeStream(new FileInputStream(file), null, options);
    }

    private int getScale(BitmapFactory.Options options) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        if (height > width) {
            return Math.round((float) height / (float) view.getHeight());
        } else {
            return Math.round((float) width / (float) view.getWidth());
        }
    }
}
