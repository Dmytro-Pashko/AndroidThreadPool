package com.goodvin1709.corgigallery.controller.impl;

import android.os.Handler;

import com.goodvin1709.corgigallery.controller.ControllerStatus;
import com.goodvin1709.corgigallery.controller.DownloadListener;
import com.goodvin1709.corgigallery.activity.GalleryActivity;
import com.goodvin1709.corgigallery.controller.GalleryController;
import com.goodvin1709.corgigallery.model.Image;
import com.goodvin1709.corgigallery.pool.TaskPool;
import com.goodvin1709.corgigallery.pool.impl.TaskPoolExecutor;
import com.goodvin1709.corgigallery.pool.task.ImageDownloadTask;
import com.goodvin1709.corgigallery.pool.task.ListDownloadTask;

import java.util.ArrayList;
import java.util.List;

public class GalleryPresenterImpl implements GalleryController, DownloadListener {

    private ControllerStatus status;

    private TaskPool pool;
    private List<Image> images;
    private Handler handler;

    public GalleryPresenterImpl() {
        images = new ArrayList<Image>();
        pool = new TaskPoolExecutor();
        status = ControllerStatus.CREATED;
    }

    @Override
    public void attachHandler(Handler handler) {
        this.handler = handler;
        if (status == ControllerStatus.CREATED) {
            startDownloadImagesList();
        }
    }

    @Override
    public void loadURLList() {
        if (!isLoadingList()) {
            status = ControllerStatus.LOADING;
            startDownloadImagesList();
        }
    }

    @Override
    public boolean isLoadingList() {
        return status == ControllerStatus.LOADING;
    }

    @Override
    public boolean isConnectionError() {
        return status == ControllerStatus.CONNECTION_ERROR;
    }

    @Override
    public boolean isListLoaded() {
        return status == ControllerStatus.LOADED;
    }

    @Override
    public List<Image> getImages() {
        return images;
    }

    @Override
    public void loadBitmap(Image image, int imageSize) {
        pool.addTaskToPool(new ImageDownloadTask(image, imageSize, this));
    }

    @Override
    public void onImageListDownloaded(List<Image> images) {
        this.images = images;
        status = ControllerStatus.LOADED;
        showOnView(GalleryActivity.DOWNLOADING_LIST_COMPLETE_MSG_ID);
    }

    @Override
    public void onDownloadListError() {
        status = ControllerStatus.CONNECTION_ERROR;
        showOnView(GalleryActivity.CONNECTION_ERROR_MSG_ID);
    }

    @Override
    public void onImageDownloaded(Image image) {
        showOnView(GalleryActivity.GALLERY_IMAGES_UPDATED);
    }

    private void startDownloadImagesList() {
        pool.addTaskToPool(new ListDownloadTask(this));
    }

    @Override
    public void onDownloadImageError(Image image) {
    }

    private void showOnView(int msgId) {
        if (handler != null) {
            handler.sendMessage(handler.obtainMessage(msgId));
        }
    }
}
