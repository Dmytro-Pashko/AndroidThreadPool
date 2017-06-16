package com.goodvin1709.corgigallery.pool.task;

import android.widget.ImageView;

import com.goodvin1709.corgigallery.controller.DownloadListener;
import com.goodvin1709.corgigallery.controller.LoadingListener;
import com.goodvin1709.corgigallery.model.Image;
import com.goodvin1709.corgigallery.model.ImageStatus;
import com.goodvin1709.corgigallery.utils.CacheUtils;
import com.goodvin1709.corgigallery.utils.HashUtils;
import com.goodvin1709.corgigallery.utils.Logger;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class ImageDownloadTask implements Runnable {

    private static final int CONNECTION_TIMEOUT = 10000;
    private static final long MAX_FILE_SIZE = 1 << 23; // 2^23 = 8MB
    private static final int BUFFER_SIZE = 8192;
    private Image image;
    private CacheUtils cache;
    private DownloadListener handler;
    private ImageView view;
    private LoadingListener listener;

    public ImageDownloadTask(Image image, CacheUtils cache, ImageView view, DownloadListener handler,
                             LoadingListener listener) {
        this.cache = cache;
        this.image = image;
        this.handler = handler;
        this.view = view;
        this.listener = listener;
    }

    @Override
    public void run() {
        try {
            Logger.log("Started downloading %s", image);
            downloadImage();
        } catch (IOException e) {
            Logger.log("Error while downloading %s", image);
            image.setStatus(ImageStatus.LOADING_ERROR);
            downloadingError();
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
            image.setStatus(ImageStatus.LOADING_ERROR);
            Logger.log("Download %s cancelled, file size is too long.", image);
            downloadingError();
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
        Logger.log("%s downloaded.", image);
        handler.onImageDownloaded(image, view, listener);
        in.close();
        out.flush();
        out.close();
    }

    private File getImageCacheFile(Image image) {
        return new File(cache.getCacheDir(), HashUtils.md5(image.getUrl()));
    }

    private void createFile(File file) throws IOException {
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
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