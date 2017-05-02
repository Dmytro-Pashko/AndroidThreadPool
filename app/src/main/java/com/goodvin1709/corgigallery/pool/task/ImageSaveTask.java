package com.goodvin1709.corgigallery.pool.task;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.goodvin1709.corgigallery.controller.CacheListener;
import com.goodvin1709.corgigallery.model.Image;
import com.goodvin1709.corgigallery.utils.HashUtils;
import com.goodvin1709.corgigallery.utils.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageSaveTask implements Runnable {

    private static final String EXTERNAL_CACHE_DIR = "CorgiGallery";
    private static final int COMPRESS_QUALITY = 90;
    private Image image;
    private Bitmap bitmap;
    private CacheListener handler;

    public ImageSaveTask(Image image, Bitmap bitmap, CacheListener handler) {
        this.image = image;
        this.bitmap = bitmap;
        this.handler = handler;
    }

    @Override
    public void run() {
        saveBitmapToCache(image, bitmap);
    }


    private void saveBitmapToCache(Image image, Bitmap bitmap) {
        File file = getImageCacheFile(image);
        try {
            createFile(file);
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, COMPRESS_QUALITY, out);
            out.flush();
            out.close();
            handler.onImageCachedToExternal(image);
        } catch (FileNotFoundException e) {
            handler.onSaveCacheError(image);
        } catch (IOException e) {
            handler.onSaveCacheError(image);
        }
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
}
