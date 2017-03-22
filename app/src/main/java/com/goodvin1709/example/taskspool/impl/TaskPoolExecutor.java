package com.goodvin1709.example.taskspool.impl;

import android.graphics.Bitmap;

import com.goodvin1709.example.taskspool.DownloadListener;
import com.goodvin1709.example.taskspool.TaskFactory;
import com.goodvin1709.example.taskspool.TaskPool;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

class TaskPoolExecutor extends ThreadPoolExecutor implements TaskPool {

    private static final int ALIVE_THREAD_TIME_SEC = 5;
    private DownloadListener downloadListener;

    TaskPoolExecutor(DownloadListener downloadListener) {
        super(Runtime.getRuntime().availableProcessors(),
                Runtime.getRuntime().availableProcessors() * 2, ALIVE_THREAD_TIME_SEC,
                TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), new TaskFactory());
        this.downloadListener = downloadListener;
    }

    @Override
    public void addTaskToDownloadImage(Callable<Bitmap> imageTask) {
        Future<Bitmap> image = submit(imageTask);
        try {
            downloadListener.onImageDownloaded(image.get());
        } catch (InterruptedException e) {
            downloadListener.onDownloadImageError();
        } catch (ExecutionException e) {
            downloadListener.onDownloadImageError();
        }
    }

    @Override
    public void addTaskToDownloadImageList(Callable<List<String>> imageList) {
        Future<List<String>> list = submit(imageList);
        try {
            downloadListener.onImageListDownloaded(list.get());
        } catch (InterruptedException e) {
            downloadListener.onDownloadListError();
        } catch (ExecutionException e) {
            downloadListener.onDownloadListError();
        }
    }

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
    }
}