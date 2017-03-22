package com.goodvin1709.example.taskspool.impl;

import android.graphics.Bitmap;

import com.goodvin1709.example.taskspool.DownloadListener;
import com.goodvin1709.example.taskspool.GalleryPresenter;
import com.goodvin1709.example.taskspool.TaskPool;
import com.goodvin1709.example.taskspool.tasks.ImageTask;
import com.goodvin1709.example.taskspool.tasks.ListTask;

import java.util.ArrayList;
import java.util.List;

public class GalleryPresenterImpl implements GalleryPresenter, DownloadListener {

    private TaskPool pool;
    private List<String> imagesUrlList;
    private List<Bitmap> images;

    public GalleryPresenterImpl() {
        pool = new TaskPoolExecutor(this);
        images = new ArrayList<Bitmap>();
    }

    @Override
    public void downloadImageList() {
        pool.addTaskToDownloadImageList(new ListTask());
    }

    @Override
    public void onImageListDownloaded(List<String> imageList) {
        imagesUrlList = imageList;
        downloadImages();
    }

    @Override
    public void onImageDownloaded(Bitmap image) {
        images.add(image);
    }

    @Override
    public void onDownloadListError() {

    }

    @Override
    public void onDownloadImageError() {

    }

    private void downloadImages() {
        if (!imagesUrlList.isEmpty()) {
            for (String url : imagesUrlList) {
                pool.addTaskToDownloadImage(new ImageTask(url));
            }
        }
    }

}
