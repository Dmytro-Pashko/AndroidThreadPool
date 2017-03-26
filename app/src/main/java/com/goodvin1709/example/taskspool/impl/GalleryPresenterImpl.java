package com.goodvin1709.example.taskspool.impl;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;

import com.goodvin1709.example.taskspool.DownloadListener;
import com.goodvin1709.example.taskspool.GalleryActivity;
import com.goodvin1709.example.taskspool.GalleryPresenter;
import com.goodvin1709.example.taskspool.TaskPool;
import com.goodvin1709.example.taskspool.tasks.ImageDownloadTask;
import com.goodvin1709.example.taskspool.tasks.ListDownloadTask;

import java.util.ArrayList;
import java.util.List;

public class GalleryPresenterImpl implements GalleryPresenter, DownloadListener {

    private TaskPool pool;
    private List<String> imagesUrlList;
    private List<Bitmap> images;
    private Handler viewHandler;

    public GalleryPresenterImpl(Handler viewHandler) {
        this.viewHandler = viewHandler;
        images = new ArrayList<Bitmap>();
        pool = new TaskPoolExecutor();
    }

    @Override
    public void startDownloadImagesList() {
        pool.addTaskToPool(new ListDownloadTask(this));
        showOnView(GalleryActivity.DOWNLOADING_LIST_STARTED_MSG);
    }

    @Override
    public void attachViewHandler(Handler viewHandler) {
        this.viewHandler = viewHandler;
    }

    @Override
    public void onImageListDownloaded(List<String> imageList) {
        imagesUrlList = imageList;
        showOnView(GalleryActivity.DOWNLOADING_LIST_COMPLETE_MSG);
        downloadImages();
    }

    @Override
    public void onDownloadListError() {
        showOnView(GalleryActivity.DOWNLOADING_ERROR);
    }

    @Override
    public void onImageDownloaded(Bitmap image) {
        images.add(image);
    }

    @Override
    public void onDownloadImageError(String url) {

    }

    private void showOnView(int msgId) {
        Message downloadCompleteMsg = viewHandler.obtainMessage(msgId);
        viewHandler.sendMessage(downloadCompleteMsg);
    }

    private void downloadImages() {
        for (String url : imagesUrlList) {
            pool.addTaskToPool(new ImageDownloadTask(url, this));
        }
    }

}
