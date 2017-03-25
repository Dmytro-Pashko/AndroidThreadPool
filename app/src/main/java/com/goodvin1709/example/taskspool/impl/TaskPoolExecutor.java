package com.goodvin1709.example.taskspool.impl;

import com.goodvin1709.example.taskspool.TaskFactory;
import com.goodvin1709.example.taskspool.TaskPool;
import com.goodvin1709.example.taskspool.tasks.ImageDownloadTask;
import com.goodvin1709.example.taskspool.tasks.ListDownloadTask;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

class TaskPoolExecutor extends ThreadPoolExecutor implements TaskPool {

    private static final int ALIVE_THREAD_TIME_SEC = 5;

    TaskPoolExecutor() {
        super(Runtime.getRuntime().availableProcessors(),
                Runtime.getRuntime().availableProcessors() * 2, ALIVE_THREAD_TIME_SEC,
                TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), new TaskFactory());
    }

    @Override
    public void addTaskToDownloadList(ListDownloadTask loadListTask) {
        loadListTask.executeOnExecutor(this);
    }

    @Override
    public void addTaskToDownloadImage(ImageDownloadTask loadImageTask) {
        loadImageTask.executeOnExecutor(this);
    }
}