package com.goodvin1709.corgigallery.pool.task;

import com.goodvin1709.corgigallery.controller.DownloadListener;
import com.goodvin1709.corgigallery.controller.ImageLoadingHandler;
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
    private int bitmapSize;
    private CacheUtils cache;
    private DownloadListener controllerHandler;
    private ImageLoadingHandler viewHandler;

    public ImageDownloadTask(Image image, int bitmapSize, CacheUtils cache, DownloadListener handler,
                             ImageLoadingHandler viewListener) {
        this.bitmapSize = bitmapSize;
        this.cache = cache;
        this.controllerHandler = handler;
        this.image = image;
        this.viewHandler = viewListener;
    }

    @Override
    public void run() {
        try {
            Logger.log("Started downloading Image[%s]", image.getUrl());
            downloadImage();
        } catch (IOException e) {
            Logger.log("Error while downloading Image[%s]", image.getUrl());
            image.setStatus(ImageStatus.LOADING_ERROR);
            viewHandler.sendMessage(viewHandler.obtainMessage(ImageLoadingHandler.IMAGE_LOADED_FAIL_MSG));
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
            viewHandler.sendMessage(viewHandler.obtainMessage(ImageLoadingHandler.IMAGE_LOADED_FAIL_MSG));
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
        controllerHandler.onImageDownloaded(image, viewHandler, bitmapSize);
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

}