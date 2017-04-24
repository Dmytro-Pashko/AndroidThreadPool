package com.goodvin1709.corgigallery.controller;

import android.graphics.Bitmap;

import com.goodvin1709.corgigallery.model.Image;

import java.util.List;

public interface DownloadListener {

    void onListDownloaded(List<Image> images);

    void onDownloadListError();

    void onImageDownloaded(Image image, Bitmap bitmap);

    void onDownloadImageError(Image image);
}
