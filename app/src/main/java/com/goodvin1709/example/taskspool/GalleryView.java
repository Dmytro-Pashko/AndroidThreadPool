package com.goodvin1709.example.taskspool;

import android.graphics.Bitmap;

public interface GalleryView {

    void showDownloadProgress();

    void hideDownloadProgress();

    void addImage(Bitmap bitmap);

    void showConnectionError();
}
