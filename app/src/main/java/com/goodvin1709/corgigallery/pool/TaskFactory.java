package com.goodvin1709.corgigallery.pool;

import android.support.annotation.NonNull;

public class TaskFactory implements java.util.concurrent.ThreadFactory {

    private static final String TASK_NAME_PREFIX = "CorgiGalleryTask";
    private static final int TASK_PRIORITY = 5;

    @Override
    public Thread newThread(@NonNull Runnable r) {
        Thread thread = new Thread(r);
        thread.setName(TASK_NAME_PREFIX);
        thread.setPriority(TASK_PRIORITY);
        return thread;
    }
}
