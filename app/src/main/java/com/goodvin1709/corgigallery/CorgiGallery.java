package com.goodvin1709.corgigallery;

import android.app.Application;

import com.goodvin1709.corgigallery.controller.GalleryController;
import com.goodvin1709.corgigallery.controller.impl.GalleryControllerImpl;

public class CorgiGallery extends Application {

    private final GalleryController presenter;

    public CorgiGallery() {
        presenter = new GalleryControllerImpl();
    }

    public GalleryController getPresenter() {
        return presenter;
    }
}
