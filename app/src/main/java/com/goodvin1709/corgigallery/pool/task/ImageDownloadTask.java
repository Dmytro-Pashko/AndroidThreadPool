package com.goodvin1709.corgigallery.pool.task;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.goodvin1709.corgigallery.controller.DownloadListener;
import com.goodvin1709.corgigallery.model.Image;
import com.goodvin1709.corgigallery.utils.HashUtils;
import com.goodvin1709.corgigallery.utils.Logger;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class ImageDownloadTask implements Runnable {

    private static final int CONNECTION_TIMEOUT = 10000;
    private static final long MAX_FILE_SIZE = 4194304 * 2; // 2^23 = 8MB
    private static final int BUFFER_SIZE = 8192;
    private static final String EXTERNAL_CACHE_DIR = "CorgiGallery";
    private Image image;
    private DownloadListener handler;

    public ImageDownloadTask(Image image, DownloadListener handler) {
        this.handler = handler;
        this.image = image;
    }

    @Override
    public void run() {
        try {
            Logger.log("Started downloading Image[%s]", image.getUrl());
            downloadImage();
        } catch (IOException e) {
            handler.onDownloadImageError(image);
        }
    }

    private void downloadImage() throws IOException {
        URL url = new URL(image.getUrl());
        URLConnection connection = url.openConnection();
        connection.setConnectTimeout(CONNECTION_TIMEOUT);
        connection.connect();
        int length = connection.getContentLength();
        if (length < MAX_FILE_SIZE) {
            readData(connection);
        } else {
            Logger.log("Download image[%s] cancelled, file size is too long.", image.getUrl());
            handler.onDownloadImageError(image);
        }
    }

    private void readData(URLConnection connection) throws IOException {
        InputStream in = new BufferedInputStream(connection.getInputStream(), BUFFER_SIZE);
        createFile(getImageCacheFile(image));
        FileOutputStream out = new FileOutputStream(getImageCacheFile(image));
        int count;
        byte buffer[] = new byte[BUFFER_SIZE];
        while ((count = in.read(buffer)) != -1) {
            out.write(buffer, 0, count);
        }
        handler.onImageDownloaded(image);
        in.close();
        out.flush();
        out.close();
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