package com.goodvin1709.corgigallery.pool.impl;

import com.goodvin1709.corgigallery.pool.TaskFactory;
import com.goodvin1709.corgigallery.pool.TaskPool;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TaskPoolExecutor extends ThreadPoolExecutor implements TaskPool {

    private static final int ALIVE_THREAD_SEC = 10;

    public TaskPoolExecutor() {
        super(Runtime.getRuntime().availableProcessors(),
                Runtime.getRuntime().availableProcessors() * 2, ALIVE_THREAD_SEC,
                TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), new TaskFactory());
    }

    @Override
    public void addTaskToPool(Runnable task) {
        execute(task);
    }
}