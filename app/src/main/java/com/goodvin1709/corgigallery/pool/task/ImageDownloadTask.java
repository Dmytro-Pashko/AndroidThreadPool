package com.goodvin1709.corgigallery.pool.task;

import android.util.Log;

import com.goodvin1709.corgigallery.controller.DownloadListener;
import com.goodvin1709.corgigallery.model.Image;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class ImageDownloadTask implements Runnable {

    private static final String TAG = "ImageDownloadTask";
    private static final int CONNECTION_TIMEOUT = 10000;
    private static final long MAX_FILE_SIZE = 2000000;
    private static final int BUFFER_SIZE = 8192;
    private Image image;
    private DownloadListener handler;

    public ImageDownloadTask(Image image, DownloadListener handler) {
        this.handler = handler;
        this.image = image;
    }

    @Override
    public void run() {
        try {
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
            //handler.onImageDownloaded(image, bitmap);
        } else {
            handler.onDownloadImageError(image);
        }
    }

    private void readData(URLConnection connection) throws IOException {
        InputStream stream = new BufferedInputStream(connection.getInputStream(), BUFFER_SIZE);
        int total, count;
        byte data[] = new byte[1024];
        while ((count = stream.read(data)) != -1) {
            total=+count;
            int progress = (total * 100) / connection.getContentLength();
            onDownloadProgressChanged(progress);
        }
        stream.close();
    }


    private void onDownloadProgressChanged(int progress) {
        Log.d(TAG, String.format("Downloading progress changed Image %s, Progress =%d", image.getUrl(), progress));
    }
}