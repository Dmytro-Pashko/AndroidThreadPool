package com.goodvin1709.corgigallery.utils.impl;

import android.content.Context;

import com.goodvin1709.corgigallery.model.Image;
import com.goodvin1709.corgigallery.utils.CacheUtils;
import com.goodvin1709.corgigallery.utils.HashUtils;

import java.io.File;


public class CacheUtilsImpl implements CacheUtils {

    private Context context;

    public CacheUtilsImpl(Context context) {
        this.context = context;
    }

    @Override
    public boolean isCached(Image image) {
        File cachedImage = new File(context.getExternalCacheDir(), HashUtils.md5(image.getUrl()));
        return cachedImage.exists();
    }
}
