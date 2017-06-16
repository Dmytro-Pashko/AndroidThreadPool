package com.goodvin1709.corgigallery;

import android.app.Application;

import com.goodvin1709.corgigallery.controller.GalleryController;
import com.goodvin1709.corgigallery.controller.impl.GalleryControllerImpl;
import com.goodvin1709.corgigallery.utils.CacheUtils;
import com.goodvin1709.corgigallery.utils.impl.CacheUtilsImpl;

public class CorgiGallery extends Application {

    private GalleryController presenter;
    private static CorgiGallery instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        presenter = provideController();
    }

    public static CorgiGallery getInstance() {
        return instance;
    }

    public GalleryController getPresenter() {
        return presenter;
    }

    private CacheUtils provideCache() {
        return new CacheUtilsImpl(getCacheDir());
    }

    private GalleryController provideController() {
        return new GalleryControllerImpl(provideCache());
    }
}
