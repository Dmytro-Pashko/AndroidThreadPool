package com.goodvin1709.corgigallery.utils;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.goodvin1709.corgigallery.model.Image;

import java.io.File;

public interface CacheUtils {

    boolean isCachedInMemory(Image image , ImageView view);

    boolean isCachedInExternal(Image image);

    void saveBitmapToMemoryCache(Image image, int bitmapSize, Bitmap bitmap);

    Bitmap loadBitmapFromMemoryCache(Image image);

    File getCacheDir();

}
