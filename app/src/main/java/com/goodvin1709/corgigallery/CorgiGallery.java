package com.goodvin1709.corgigallery;

import android.app.Application;

import com.goodvin1709.corgigallery.controller.GalleryController;
import com.goodvin1709.corgigallery.controller.impl.GalleryControllerImpl;

public class CorgiGallery extends Application {

    private GalleryController presenter;

    @Override
    public void onCreate() {
        super.onCreate();
        presenter = new GalleryControllerImpl(this);
    }

    public GalleryController getPresenter() {
        return presenter;
    }
}
