package com.goodvin1709.corgigallery.controller.impl;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import com.goodvin1709.corgigallery.activity.GalleryActivity;
import com.goodvin1709.corgigallery.controller.CacheListener;
import com.goodvin1709.corgigallery.controller.ControllerStatus;
import com.goodvin1709.corgigallery.controller.DownloadListener;
import com.goodvin1709.corgigallery.controller.GalleryController;
import com.goodvin1709.corgigallery.model.Image;
import com.goodvin1709.corgigallery.pool.TaskPool;
import com.goodvin1709.corgigallery.pool.impl.TaskPoolExecutor;
import com.goodvin1709.corgigallery.pool.task.ImageDownloadTask;
import com.goodvin1709.corgigallery.pool.task.ListDownloadTask;
import com.goodvin1709.corgigallery.pool.task.LoadFromCacheTask;
import com.goodvin1709.corgigallery.pool.task.SaveToCacheTask;
import com.goodvin1709.corgigallery.utils.CacheUtils;
import com.goodvin1709.corgigallery.utils.impl.CacheUtilsImpl;

import java.util.ArrayList;
import java.util.List;

public class GalleryControllerImpl implements GalleryController, DownloadListener, CacheListener {

    private static final String TAG = "GalleryController";

    private Context context;
    private ControllerStatus status;
    private final TaskPool pool;
    private List<Image> images;
    private Handler handler;
    private CacheUtils cache;

    public GalleryControllerImpl(Context context) {
        this.context = context;
        cache = new CacheUtilsImpl(context);
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
    public void loadImage(Image image, ImageView view) {
        if (cache.isCached(image)) {
            pool.addTaskToPool(new LoadFromCacheTask(context, image, view, this));
        } else {
            pool.addTaskToPool(new ImageDownloadTask(image, this));
        }
    }

    @Override
    public void onListDownloaded(List<Image> images) {
        Log.d(TAG, "Image list has been downloaded.");
        this.images = images;
        status = ControllerStatus.LOADED;
        showOnView(GalleryActivity.DOWNLOADING_LIST_COMPLETE_MSG_ID);
    }

    @Override
    public void onDownloadListError() {
        Log.d(TAG, "Error while downloading image list.");
        status = ControllerStatus.CONNECTION_ERROR;
        showOnView(GalleryActivity.CONNECTION_ERROR_MSG_ID);
    }

    @Override
    public void onImageDownloaded(Image image, Bitmap bitmap) {
        Log.d(TAG, String.format("Image %s downloaded.", image.getUrl()));
        pool.addTaskToPool(new SaveToCacheTask(context, image, bitmap, this));
    }

    @Override
    public void onDownloadImageError(Image image) {
        Log.d(TAG, String.format("Error while downloading image %s", image.getUrl()));
        image.setBroken(true);
    }

    @Override
    public void onImageCached(Image image) {
        Log.d(TAG, String.format("Image %s saved to cache.", image.getUrl()));
        showOnView(GalleryActivity.GALLERY_IMAGES_UPDATED);
    }

    @Override
    public void onSaveCacheError(Image image) {
        Log.d(TAG, String.format("Error while saving image %s to cache.", image.getUrl()));
    }

    @Override
    public void onLoadCacheError(Image image) {
        Log.d(TAG, String.format("Error while loading image %s from cache.", image.getUrl()));
    }

    @Override
    public void onImageLoadedFromCache(Image image) {
        Log.d(TAG, String.format("Image %s loaded from cache.", image.getUrl()));
        showOnView(GalleryActivity.GALLERY_IMAGES_UPDATED);
    }

    private void startDownloadImagesList() {
        Log.d(TAG, "Started downloading image list.");
        status = ControllerStatus.LOADING;
        showOnView(GalleryActivity.DOWNLOADING_LIST_STARTED_MSG_ID);
        pool.addTaskToPool(new ListDownloadTask(this));
    }

    private void showOnView(int msgId) {
        if (handler != null) {
            handler.sendMessage(handler.obtainMessage(msgId));
        }
    }
}
