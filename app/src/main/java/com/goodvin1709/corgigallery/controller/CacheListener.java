package com.goodvin1709.corgigallery.controller;

import android.graphics.Bitmap;

import com.goodvin1709.corgigallery.model.Image;

public interface CacheListener {

    void onImageCachedToMemory(Image image);

    void onLoadCacheError(Image image);

    void onImageLoadedFromMemoryCache(Image image);

    void onImageLoadedFromExternalCache(Image image, int bitmapSize, Bitmap bitmap);
}
