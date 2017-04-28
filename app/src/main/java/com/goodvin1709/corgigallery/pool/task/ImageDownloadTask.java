package com.goodvin1709.corgigallery.pool.task;

import android.graphics.BitmapFactory;

import com.goodvin1709.corgigallery.controller.DownloadListener;
import com.goodvin1709.corgigallery.model.Image;
import com.goodvin1709.corgigallery.utils.Logger;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class ImageDownloadTask implements Runnable {

    private static final int CONNECTION_TIMEOUT = 10000;
    private static final long MAX_FILE_SIZE = 4194304 * 2; // 2^23 = 8MB
    private static final int FREQUENCY_UPDATE_PERCENT = 10;
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
        InputStream inputStream = new BufferedInputStream(connection.getInputStream(), BUFFER_SIZE);
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        int minIncrement = (connection.getContentLength() / 100) * FREQUENCY_UPDATE_PERCENT;
        int total = 0;
        int count;
        byte data[] = new byte[connection.getContentLength()];
        while ((count = inputStream.read(data, 0, minIncrement)) != -1) {
            total += count;
            outStream.write(data, 0, count);
            int progress = (int) ((total * 100f) / connection.getContentLength());
            onDownloadProgressChanged(progress);
        }
        handler.onImageDownloaded(image,
                BitmapFactory.decodeByteArray(outStream.toByteArray(), 0, data.length));
        inputStream.close();
        outStream.close();
    }

    private void onDownloadProgressChanged(int progress) {
        //Logger.log("Downloading progress changed Image[%s], Progress=%d%%", image.getUrl(), progress);
    }
}