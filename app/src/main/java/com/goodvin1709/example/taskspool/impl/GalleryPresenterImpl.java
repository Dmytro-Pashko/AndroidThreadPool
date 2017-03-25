package com.goodvin1709.example.taskspool.impl;

import android.graphics.Bitmap;

import com.goodvin1709.example.taskspool.DownloadListener;
import com.goodvin1709.example.taskspool.GalleryPresenter;
import com.goodvin1709.example.taskspool.TaskPool;
import com.goodvin1709.example.taskspool.tasks.ImageDownloadTask;
import com.goodvin1709.example.taskspool.tasks.ListDownloadTask;
import com.goodvin1709.example.taskspool.GalleryView;

import java.util.ArrayList;
import java.util.List;

public class GalleryPresenterImpl implements GalleryPresenter, DownloadListener {

    private TaskPool pool;
    private List<String> imagesUrlList;
    private List<Bitmap> images;
    private GalleryView view;

    public GalleryPresenterImpl(GalleryView view) {
        this.view = view;
        pool = new TaskPoolExecutor(this);
        images = new ArrayList<Bitmap>();
    }

    @Override
    public void downloadImageList() {
        view.showDownloadProgress();
        pool.addTaskToDownloadImageList(new ListDownloadTask());
    }

    @Override
    public void onImageListDownloaded(List<String> imageList) {
        view.hideDownloadProgress();
        imagesUrlList = imageList;
    }

    @Override
    public void onImageDownloaded(Bitmap image) {
        images.add(image);
    }

    @Override
    public void onDownloadListError() {
        view.hideDownloadProgress();
        view.showConnectionError();
    }

    @Override
    public void onDownloadImageError() {
    }

    private void downloadImages() {
        for (String url : imagesUrlList) {
            pool.addTaskToDownloadImage(new ImageDownloadTask(url));
        }
    }
}
