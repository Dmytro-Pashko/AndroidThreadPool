package com.goodvin1709.corgigallery.controller;

import android.os.Handler;
import android.os.Message;

public class ImageLoadingHandler extends Handler {

    public static final int IMAGE_LOADED_SUCCESS_MSG = 0xfd;
    public static final int IMAGE_LOADED_FAIL_MSG = 0xfe;
    private LoadingListener listener;

    public ImageLoadingHandler(LoadingListener listener) {
        this.listener = listener;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case IMAGE_LOADED_SUCCESS_MSG:
                listener.onLoadComplete();
                break;
            case IMAGE_LOADED_FAIL_MSG:
                listener.onLoadFail();
                break;
            default:
                break;
        }
    }
}
