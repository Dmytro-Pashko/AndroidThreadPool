package com.goodvin1709.corgigallery.pool.task;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.widget.ImageView;

import com.goodvin1709.corgigallery.controller.CacheListener;
import com.goodvin1709.corgigallery.model.Image;
import com.goodvin1709.corgigallery.utils.HashUtils;
import com.goodvin1709.corgigallery.utils.Logger;

import java.io.File;

public class ImageLoadTask implements Runnable {

    private static final String EXTERNAL_CACHE_DIR = "CorgiGallery";
    private Image image;
    private int height;
    private int width;
    private CacheListener handler;

    public ImageLoadTask(Image image, ImageView view, CacheListener handler) {
        this.image = image;
        this.height = view.getLayoutParams().height;
        this.width = view.getLayoutParams().width;
        this.handler = handler;
    }

    @Override
    public void run() {
        loadBitmap(image, height, width);
    }

    private void loadBitmap(Image image, int height, int width) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        loadBitmapFromExternalCache(options, image);
        options.inSampleSize = getScale(options, height, width);
        options.inJustDecodeBounds = false;
        Bitmap bitmap = loadBitmapFromExternalCache(options, image);
        if (bitmap == null)
        {
            Logger.log("Error");
        }
        handler.onImageLoadedFromExternalCache(image, bitmap);
    }

    private int getScale(BitmapFactory.Options options, int reqHeight, int reqWidth) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        if (height > width) {
            return Math.round((float) height / (float) reqHeight);
        } else {
            return Math.round((float) width / (float) reqWidth);
        }
    }

    private Bitmap loadBitmapFromExternalCache(BitmapFactory.Options options, Image image) {
        File file = getImageCacheFile(image);
        return BitmapFactory.decodeFile(file.getAbsolutePath(), options);
    }

    private File getImageCacheFile(Image image) {
        return new File(Environment.getExternalStorageDirectory() + File.separator + EXTERNAL_CACHE_DIR,
                HashUtils.md5(image.getUrl()));
    }
}
