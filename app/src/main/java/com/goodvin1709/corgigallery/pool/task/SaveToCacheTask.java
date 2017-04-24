package com.goodvin1709.corgigallery.pool.task;

import android.content.Context;
import android.graphics.Bitmap;

import com.goodvin1709.corgigallery.controller.CacheListener;
import com.goodvin1709.corgigallery.model.Image;
import com.goodvin1709.corgigallery.utils.HashUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SaveToCacheTask implements Runnable {

    private static final int COMPRESS_QUALITY = 90;
    private Context context;
    private Image image;
    private File file;
    private CacheListener handler;
    private Bitmap bitmap;

    public SaveToCacheTask(Context context, Image image, Bitmap bitmap, CacheListener handler) {
        this.context = context;
        this.image = image;
        this.bitmap = bitmap;
        this.handler = handler;
    }

    @Override
    public void run() {
        file = new File(context.getExternalCacheDir(), HashUtils.md5(image.getUrl()));
        try {
            createFile();
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

    private void createFile() throws IOException {
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
    }

}
