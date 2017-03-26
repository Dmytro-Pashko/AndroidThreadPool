package com.goodvin1709.example.taskspool;

import android.graphics.Bitmap;

import java.util.List;

public interface DownloadListener {

    void onImageListDownloaded(List<String> imageList);

    void onDownloadListError();

    void onImageDownloaded(Bitmap image);

    void onDownloadImageError(String url);
}
