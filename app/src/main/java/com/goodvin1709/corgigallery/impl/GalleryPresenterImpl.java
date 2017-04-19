package com.goodvin1709.corgigallery.impl;

import android.os.Handler;

import com.goodvin1709.corgigallery.DownloadListener;
import com.goodvin1709.corgigallery.GalleryActivity;
import com.goodvin1709.corgigallery.GalleryPresenter;
import com.goodvin1709.corgigallery.Image;
import com.goodvin1709.corgigallery.TaskPool;
import com.goodvin1709.corgigallery.tasks.ImageDownloadTask;
import com.goodvin1709.corgigallery.tasks.ListDownloadTask;

import java.util.ArrayList;
import java.util.List;

public class GalleryPresenterImpl implements GalleryPresenter, DownloadListener {

    private TaskPool pool;
    private List<Image> images;
    private Handler handler;

    public GalleryPresenterImpl() {
        images = new ArrayList<Image>();
        pool = new TaskPoolExecutor();
    }

    @Override
    public void attachHandler(Handler handler) {
        this.handler = handler;
    }

    @Override
    public List<Image> getImages() {
        if (images.isEmpty()) {
            startDownloadImagesList();
        }
        return images;
    }

    @Override
    public void loadBitmap(Image image, int imageSize) {
        pool.addTaskToPool(new ImageDownloadTask(image, imageSize, this));
    }

    private void startDownloadImagesList() {
        pool.addTaskToPool(new ListDownloadTask(this));
        showOnView(GalleryActivity.DOWNLOADING_LIST_STARTED_MSG_ID);
    }

    @Override
    public void onImageListDownloaded(List<Image> images) {
        this.images = images;
        showOnView(GalleryActivity.DOWNLOADING_LIST_COMPLETE_MSG_ID);
    }

    @Override
    public void onDownloadListError() {
        showOnView(GalleryActivity.CONNECTION_ERROR_MSG_ID);
    }

    @Override
    public void onImageDownloaded(Image image) {
        showOnView(GalleryActivity.GALLERY_IMAGES_UPDATED);
    }

    @Override
    public void onDownloadImageError(Image image) {
        //TODO image set broken icon.
    }

    private void showOnView(int msgId) {
        handler.sendMessage(handler.obtainMessage(msgId));
    }
}
