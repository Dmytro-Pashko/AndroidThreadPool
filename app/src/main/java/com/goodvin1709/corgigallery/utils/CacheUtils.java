package com.goodvin1709.corgigallery.utils;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.goodvin1709.corgigallery.model.Image;

public interface CacheUtils {

    boolean isCached(Image image);

    void saveBitmapToCache(Image image, Bitmap bitmap);

    void loadBitmapFromCache(Image image, ImageView view);

}
