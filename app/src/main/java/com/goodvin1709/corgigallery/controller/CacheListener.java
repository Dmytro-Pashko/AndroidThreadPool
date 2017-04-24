package com.goodvin1709.corgigallery.controller;

import com.goodvin1709.corgigallery.model.Image;

public interface CacheListener {

    void onImageCached(Image image);

    void onSaveCacheError(Image image);

    void onLoadCacheError(Image image);

    void onImageLoadedFromCache(Image image);
}
