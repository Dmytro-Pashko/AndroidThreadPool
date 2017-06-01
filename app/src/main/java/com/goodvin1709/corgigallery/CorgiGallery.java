package com.goodvin1709.corgigallery;

import android.app.Application;

import com.goodvin1709.corgigallery.controller.GalleryController;
import com.goodvin1709.corgigallery.controller.impl.GalleryControllerImpl;
import com.goodvin1709.corgigallery.utils.CacheUtils;
import com.goodvin1709.corgigallery.utils.impl.CacheUtilsImpl;

public class CorgiGallery extends Application {

    private GalleryController presenter;

    @Override
    public void onCreate() {
        super.onCreate();
        presenter = provideController();
    }

    public GalleryController getPresenter() {
        return presenter;
    }

    private GalleryController provideController() {
        return new GalleryControllerImpl(provideCache());
    }

    private CacheUtils provideCache() {
        return new CacheUtilsImpl(getCacheDir());
    }
}
