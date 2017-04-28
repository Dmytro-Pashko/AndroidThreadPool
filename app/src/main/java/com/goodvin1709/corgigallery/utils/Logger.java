package com.goodvin1709.corgigallery.utils;

import android.util.Log;

import java.util.Locale;

public class Logger {

    private static final String TAG = "CorgiGallery";

    private Logger() {
        throw new IllegalStateException("Logger is utility class.");
    }

    public static void log(String msg, Object... params) {
        Log.d(TAG, String.format(Locale.ENGLISH, msg, params));
    }
}
