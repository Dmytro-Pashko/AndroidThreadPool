package com.goodvin1709.example.taskspool;


import android.os.Handler;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


class TaskPoolExecutor extends ThreadPoolExecutor {
    private Handler handler;
    private static final int ALIVE_THREAD_TIME_SEC = 5;

    TaskPoolExecutor(Handler handler) {
        super(Runtime.getRuntime().availableProcessors(),
                Runtime.getRuntime().availableProcessors() * 2, ALIVE_THREAD_TIME_SEC,
                TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), new TaskFactory());
        this.handler = handler;
    }

    @Override
    public void execute(Runnable command) {
        super.execute(command);
    }

    void attachHandler(Handler handler) {
        this.handler = handler;
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