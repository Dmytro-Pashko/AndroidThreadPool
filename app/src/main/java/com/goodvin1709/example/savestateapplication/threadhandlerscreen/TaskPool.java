package com.goodvin1709.example.savestateapplication.threadhandlerscreen;

import android.os.Handler;
import android.os.Message;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.goodvin1709.example.savestateapplication.threadhandlerscreen.TaskHandlerActivity.UPDATE_POOL_INFORMATION_MSG_ID;

class TaskPool {

    private TaskPoolExecutor executor;

    TaskPool(Handler handler) {
        executor = new TaskPoolExecutor(handler);
        executor.update();
    }

    void addTask(Runnable r) {
        executor.execute(r);
    }

    void attachHandler(Handler handler) {
        executor.attachHandler(handler);
        executor.update();
    }

    private static class TaskPoolExecutor extends ThreadPoolExecutor {
        private Handler handler;
        private UpdatePoolInformation information = new UpdatePoolInformation();
        private static final int ALIVE_THREAD_TIME_SEC = 5;

        TaskPoolExecutor(Handler handler) {
            super(Runtime.getRuntime().availableProcessors(),
                    Runtime.getRuntime().availableProcessors() * 2, ALIVE_THREAD_TIME_SEC,
                    TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), new TaskFactory());
            this.handler = handler;
        }

        void attachHandler(Handler handler) {
            this.handler = handler;
        }

        @Override
        protected void beforeExecute(Thread t, Runnable r) {
            super.beforeExecute(t, r);
            update();
        }

        @Override
        protected void afterExecute(Runnable r, Throwable t) {
            super.afterExecute(r, t);
            update();
        }

        private void update() {
            information.poolSize = getPoolSize();
            information.maxPoolSize = getMaximumPoolSize();
            information.queueTaskCount = getQueue().size();
            information.taskCount = getTaskCount();
            information.completeTaskCount = getCompletedTaskCount();
            information.workedTaskCount = getActiveCount();
            Message message = Message.obtain(handler, UPDATE_POOL_INFORMATION_MSG_ID, information);
            handler.sendMessage(message);
        }
    }
}
