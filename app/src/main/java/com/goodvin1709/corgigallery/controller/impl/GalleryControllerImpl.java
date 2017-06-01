package com.goodvin1709.corgigallery.controller.impl;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.widget.ImageView;

import com.goodvin1709.corgigallery.activity.GalleryActivity;
import com.goodvin1709.corgigallery.controller.CacheListener;
import com.goodvin1709.corgigallery.controller.ControllerStatus;
import com.goodvin1709.corgigallery.controller.DownloadListener;
import com.goodvin1709.corgigallery.controller.GalleryController;
import com.goodvin1709.corgigallery.model.Image;
import com.goodvin1709.corgigallery.model.ImageStatus;
import com.goodvin1709.corgigallery.pool.TaskPool;
import com.goodvin1709.corgigallery.pool.impl.TaskPoolExecutor;
import com.goodvin1709.corgigallery.pool.task.ImageDownloadTask;
import com.goodvin1709.corgigallery.pool.task.LoadBitmapTask;
import com.goodvin1709.corgigallery.pool.task.ListDownloadTask;
import com.goodvin1709.corgigallery.utils.CacheUtils;
import com.goodvin1709.corgigallery.utils.Logger;
import com.goodvin1709.corgigallery.utils.impl.CacheUtilsImpl;

import java.util.ArrayList;
import java.util.List;

public class GalleryControllerImpl implements GalleryController, DownloadListener, CacheListener {

    private static final String LIST_URL = "https://raw.githubusercontent.com/goodvin1709/AndroidThreadPool/develop/images/list.txt";
    private ControllerStatus status;
    private final TaskPool pool;
    private List<Image> images;
    private Handler handler;
    private CacheUtils cache;

    public GalleryControllerImpl(Context context) {
        cache = new CacheUtilsImpl(this, context);
        images = new ArrayList<>();
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
    public void loadImage(Image image, int bitmapSize, ImageView view) {
        if (cache.isCachedInMemory(image, bitmapSize)) {
            loadImageFromMemory(image, view);
        } else if (cache.isCachedInExternal(image)) {
            loadImageFromExternal(image, bitmapSize);
        } else {
            DownloadImage(image);
        }
    }

    private void loadImageFromMemory(Image image, ImageView view) {
        cache.loadBitmapFromMemoryCache(image, view);
    }

    private void loadImageFromExternal(Image image, int bitmapSize) {
        if (image.getStatus() != ImageStatus.CACHING) {
            image.setStatus(ImageStatus.CACHING);
            pool.addTaskToPool(new LoadBitmapTask(image, cache.getCacheDir(), bitmapSize, this));
        }
    }

    private void DownloadImage(Image image) {
        if (image.getStatus() != ImageStatus.LOADING) {
            image.setStatus(ImageStatus.LOADING);
            pool.addTaskToPool(new ImageDownloadTask(image, cache.getCacheDir(), this));
        }
    }

    @Override
    public void onListDownloaded(List<Image> images) {
        status = ControllerStatus.LOADED;
        this.images = images;
        Logger.log("List of Images has been downloaded [%d images].", images.size());
        showOnView(GalleryActivity.DOWNLOADING_LIST_COMPLETE_MSG_ID);
    }

    @Override
    public void onDownloadListError() {
        status = ControllerStatus.CONNECTION_ERROR;
        showOnView(GalleryActivity.CONNECTION_ERROR_MSG_ID);
        Logger.log("Error while downloading image list.");
    }

    @Override
    public void onImageDownloaded(Image image) {
        image.setStatus(ImageStatus.IDLE);
        onImageUpdated(image);
        Logger.log("Image[%s] downloaded.", image.getUrl());
    }

    @Override
    public void onDownloadImageError(Image image) {
        image.setStatus(ImageStatus.LOADING_ERROR);
        onImageUpdated(image);
        Logger.log("Error while downloading Image[%s]", image.getUrl());
    }

    @Override
    public void onImageCachedToMemory(Image image) {
        image.setStatus(ImageStatus.IDLE);
        onImageUpdated(image);
        Logger.log("Image[%s] saved to memory cache.", image.getUrl());
    }

    @Override
    public void onLoadCacheError(Image image) {
        image.setStatus(ImageStatus.CACHED_ERROR);
        onImageUpdated(image);
        Logger.log("Error while loading Image[%s] from cache.", image.getUrl());
    }

    @Override
    public void onImageLoadedFromMemoryCache(Image image) {
        image.setStatus(ImageStatus.IDLE);
        Logger.log("Image[%s] loaded from memory cache.", image.getUrl());
    }

    @Override
    public void onImageLoadedFromExternalCache(Image image, int bitmapSize, Bitmap bitmap) {
        cache.saveBitmapToMemoryCache(image, bitmapSize, bitmap);
        Logger.log("Image[%s] loaded from external cache.", image.getUrl());
    }

    private void onImageUpdated(Image image) {
        showOnView(GalleryActivity.GALLERY_IMAGES_UPDATED, getImagePosition(image), 0);
    }

    private int getImagePosition(Image image) {
        for (int i = 0; i < images.size(); i++) {
            if (images.get(i).getUrl().equals(image.getUrl())) {
                return i;
            }
        }
        return -1;
    }

    private void startDownloadImagesList() {
        status = ControllerStatus.LOADING;
        pool.addTaskToPool(new ListDownloadTask(LIST_URL, this));
        Logger.log("Started downloading image list.");
        showOnView(GalleryActivity.DOWNLOADING_LIST_STARTED_MSG_ID);
    }

    private void showOnView(int msgId) {
        if (handler != null) {
            handler.sendMessage(handler.obtainMessage(msgId));
        }
    }

    private void showOnView(int msgId, int arg1, int arg2) {
        if (handler != null) {
            handler.sendMessage(handler.obtainMessage(msgId, arg1, arg2));
        }
    }
}
