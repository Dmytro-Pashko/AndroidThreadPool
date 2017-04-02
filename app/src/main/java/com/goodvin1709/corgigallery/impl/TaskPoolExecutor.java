package com.goodvin1709.corgigallery.impl;

import com.goodvin1709.corgigallery.TaskFactory;
import com.goodvin1709.corgigallery.TaskPool;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

class TaskPoolExecutor extends ThreadPoolExecutor implements TaskPool {

    private static final int ALIVE_THREAD_SEC = 10;

    TaskPoolExecutor() {
        super(Runtime.getRuntime().availableProcessors(),
                Runtime.getRuntime().availableProcessors() * 2, ALIVE_THREAD_SEC,
                TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), new TaskFactory());
    }

    @Override
    public void addTaskToPool(Runnable task) {
        execute(task);
    }

    @Override
    public void stopAll() {
        shutdownNow();
    }
}