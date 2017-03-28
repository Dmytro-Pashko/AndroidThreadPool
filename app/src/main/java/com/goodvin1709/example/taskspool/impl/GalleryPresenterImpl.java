package com.goodvin1709.example.taskspool.impl;

import android.content.Context;
import android.content.Loader;
import android.graphics.Bitmap;
import android.os.Handler;

import com.goodvin1709.example.taskspool.Dimensions;
import com.goodvin1709.example.taskspool.DownloadListener;
import com.goodvin1709.example.taskspool.GalleryActivity;
import com.goodvin1709.example.taskspool.GalleryPresenter;
import com.goodvin1709.example.taskspool.TaskPool;
import com.goodvin1709.example.taskspool.tasks.ImageDownloadTask;
import com.goodvin1709.example.taskspool.tasks.ListDownloadTask;

import java.util.ArrayList;
import java.util.List;

public class GalleryPresenterImpl extends Loader<List<Bitmap>>
        implements GalleryPresenter, DownloadListener {

    private TaskPool pool;
    private List<String> imagesUrlList;
    private List<Bitmap> images;
    private Handler handler;

    public GalleryPresenterImpl(Context context) {
        super(context);
        images = new ArrayList<Bitmap>();
        pool = new TaskPoolExecutor();
    }

    @Override
    public void attachHandler(Handler handler) {
        this.handler = handler;
    }

    @Override
    protected void onStartLoading() {
        startDownloadImagesList();
    }

    @Override
    public void deliverResult(List<Bitmap> data) {
        super.deliverResult(data);
    }

    @Override
    protected void onStopLoading() {
        pool.stopAll();
        super.onStopLoading();
    }

    private void startDownloadImagesList() {
        pool.addTaskToPool(new ListDownloadTask(this));
        showOnView(GalleryActivity.DOWNLOADING_LIST_STARTED_MSG_ID);
    }

    @Override
    public void onImageListDownloaded(List<String> imageList) {
        imagesUrlList = imageList;
        showOnView(GalleryActivity.DOWNLOADING_LIST_COMPLETE_MSG_ID);
        downloadImages();
    }

    @Override
    public void onDownloadListError() {
        showOnView(GalleryActivity.CONNECTION_ERROR_MSG_ID);
    }

    @Override
    public void onImageDownloaded(Bitmap image) {
        images.add(image);
    }

    @Override
    public void onDownloadImageError(String url) {
    }

    private void showOnView(int msgId) {
        handler.sendMessage(handler.obtainMessage(msgId));
    }

    private void downloadImages() {
        for (String url : imagesUrlList) {
            pool.addTaskToPool(new ImageDownloadTask(url, new Dimensions(100,100),this));
        }
    }
}
