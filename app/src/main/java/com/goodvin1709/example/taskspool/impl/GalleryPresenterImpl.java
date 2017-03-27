package com.goodvin1709.example.taskspool.impl;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;

import com.goodvin1709.example.taskspool.DownloadListener;
import com.goodvin1709.example.taskspool.GalleryActivity;
import com.goodvin1709.example.taskspool.GalleryPresenter;
import com.goodvin1709.example.taskspool.TaskPool;
import com.goodvin1709.example.taskspool.tasks.ImageDownloadTask;
import com.goodvin1709.example.taskspool.tasks.ListDownloadTask;

import java.util.ArrayList;
import java.util.List;

public class GalleryPresenterImpl extends AsyncTaskLoader<List<Bitmap>>
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
    protected void onStartLoading() {
        startDownloadImagesList();
    }

    private void startDownloadImagesList() {
        pool.addTaskToPool(new ListDownloadTask(this));
        showOnView(GalleryActivity.DOWNLOADING_LIST_STARTED_MSG);
    }

    @Override
    public List<Bitmap> loadInBackground() {
        return images;
    }

    @Override
    public void deliverResult(List<Bitmap> data) {
        super.deliverResult(data);
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
        handler.sendMessage(handler.obtainMessage(msgId));
    }

    private void downloadImages() {
        for (String url : imagesUrlList) {
            pool.addTaskToPool(new ImageDownloadTask(url, this));
        }
    }

    @Override
    public void attachHandler(Handler handler) {
        this.handler = handler;
    }
}
