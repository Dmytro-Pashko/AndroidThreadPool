package com.goodvin1709.corgigallery.controller.impl;

import android.os.Handler;
import android.widget.ImageView;

import com.goodvin1709.corgigallery.activity.GalleryActivity;
import com.goodvin1709.corgigallery.controller.ControllerStatus;
import com.goodvin1709.corgigallery.controller.DownloadListener;
import com.goodvin1709.corgigallery.controller.GalleryController;
import com.goodvin1709.corgigallery.controller.LoadingListener;
import com.goodvin1709.corgigallery.model.Image;
import com.goodvin1709.corgigallery.model.ImageStatus;
import com.goodvin1709.corgigallery.pool.TaskPool;
import com.goodvin1709.corgigallery.pool.impl.TaskPoolExecutor;
import com.goodvin1709.corgigallery.pool.task.ImageDownloadTask;
import com.goodvin1709.corgigallery.pool.task.ListDownloadTask;
import com.goodvin1709.corgigallery.pool.task.LoadBitmapTask;
import com.goodvin1709.corgigallery.utils.CacheUtils;
import com.goodvin1709.corgigallery.utils.Logger;

import java.util.ArrayList;
import java.util.List;

public class GalleryControllerImpl implements GalleryController, DownloadListener {

    private static final String LIST_URL = "https://raw.githubusercontent.com/goodvin1709/AndroidThreadPool/develop/images/list.txt";
    private ControllerStatus status;
    private final TaskPool pool;
    private List<Image> images;
    private Handler handler;
    private CacheUtils cache;

    public GalleryControllerImpl(CacheUtils cache) {
        this.cache = cache;
        this.images = new ArrayList<>();
        this.pool = new TaskPoolExecutor();
        this.status = ControllerStatus.CREATED;
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
    public void loadImage(Image image, ImageView view, LoadingListener listener) {
        if (cache.isCachedInMemory(image, view)) {
            view.setImageBitmap(cache.loadBitmapFromMemoryCache(image));
            listener.onLoadComplete();
        } else if (cache.isCachedInExternal(image)) {
            loadImageFromExternal(image, view, listener);
        } else {
            downloadImage(image, view, listener);
        }
    }

    @Override
    public void onListDownloaded(List<Image> images) {
        status = ControllerStatus.LOADED;
        this.images = images;
        Logger.log("List of Images has been downloaded [%d images].", this.images.size());
        showOnView(GalleryActivity.DOWNLOADING_LIST_COMPLETE_MSG_ID);
    }

    @Override
    public void onDownloadListError() {
        status = ControllerStatus.CONNECTION_ERROR;
        showOnView(GalleryActivity.CONNECTION_ERROR_MSG_ID);
        Logger.log("Error while downloading image list.");
    }

    @Override
    public void onImageDownloaded(Image image, ImageView view, LoadingListener listener) {
        image.setStatus(ImageStatus.IDLE);
        loadImageFromExternal(image, view, listener);
    }

    private void startDownloadImagesList() {
        status = ControllerStatus.LOADING;
        pool.addTaskToPool(new ListDownloadTask(LIST_URL, this));
        Logger.log("Started downloading image list.");
        showOnView(GalleryActivity.DOWNLOADING_LIST_STARTED_MSG_ID);
    }

    private void loadImageFromExternal(Image image, ImageView view, LoadingListener listener) {
        if (image.getStatus() != ImageStatus.CACHING) {
            image.setStatus(ImageStatus.CACHING);
            pool.addTaskToPool(new LoadBitmapTask(image, cache, view, listener));
        }
    }

    private void downloadImage(Image image, ImageView view, LoadingListener listener) {
        if (image.getStatus() != ImageStatus.LOADING) {
            image.setStatus(ImageStatus.LOADING);
            pool.addTaskToPool(new ImageDownloadTask(image, cache, view, this, listener));
        }
    }

    private void showOnView(int msgId) {
        if (handler != null) {
            handler.sendMessage(handler.obtainMessage(msgId));
        }
    }
}
