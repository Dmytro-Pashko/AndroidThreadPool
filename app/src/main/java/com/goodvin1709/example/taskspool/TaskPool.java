package com.goodvin1709.example.taskspool;

import android.os.Handler;

class TaskPool {

    private TaskPoolExecutor executor;

    TaskPool(Handler handler) {
        executor = new TaskPoolExecutor(handler);
    }

    void addTask(Runnable r) {
        executor.execute(r);
    }

    void attachHandler(Handler handler) {
        executor.attachHandler(handler);
    }
}
