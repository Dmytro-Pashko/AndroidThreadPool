package com.goodvin1709.corgigallery.utils;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.goodvin1709.corgigallery.model.Image;

public interface CacheUtils {

    boolean isCachedInMemory(Image image);

    boolean isCachedInExternal(Image image);

    void saveBitmapToMemoryCache(Image image, Bitmap bitmap);

    void loadBitmapFromMemoryCache(Image image, ImageView view);

}
