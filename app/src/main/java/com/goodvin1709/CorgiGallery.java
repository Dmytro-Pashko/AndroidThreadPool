package com.goodvin1709;

import android.app.Application;

import com.goodvin1709.corgigallery.GalleryPresenter;
import com.goodvin1709.corgigallery.impl.GalleryPresenterImpl;

public class CorgiGallery extends Application {

    private GalleryPresenter presenter;

    public CorgiGallery() {
        presenter = new GalleryPresenterImpl();
    }

    public GalleryPresenter getPresenter() {
        return presenter;
    }
}
