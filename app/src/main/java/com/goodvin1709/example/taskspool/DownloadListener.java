package com.goodvin1709.example.taskspool;

import android.graphics.Bitmap;

import java.util.List;

public interface DownloadListener {

    void onImageListDownloaded(List<String> imageList);

    void onImageDownloaded(Bitmap image);

    void onDownloadListError();

    void onDownloadImageError();
}
