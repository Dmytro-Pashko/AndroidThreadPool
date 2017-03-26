package com.goodvin1709.example.taskspool;


import android.os.Handler;

public interface GalleryPresenter {

    void startDownloadImagesList();

    void attachViewHandler(Handler viewHandler);
}
