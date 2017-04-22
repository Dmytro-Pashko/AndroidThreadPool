package com.goodvin1709.corgigallery;

import android.app.Application;

import com.goodvin1709.corgigallery.controller.GalleryController;
import com.goodvin1709.corgigallery.controller.impl.GalleryPresenterImpl;

public class CorgiGallery extends Application {

    private GalleryController presenter;

    public CorgiGallery() {
        presenter = new GalleryPresenterImpl();
    }

    public GalleryController getPresenter() {
        return presenter;
    }
}
