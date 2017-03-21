package com.goodvin1709.example.taskspool;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

public class TaskHandlerActivity extends Activity {

    public static final int IMAGE_DOWNLOADED_MSG_ID = 0xFF;
    private TaskPool pool;
    private final UpdatePoolHandler handler = new UpdatePoolHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread_handler);
        pool = getTaskPool();
        pool.attachHandler(handler);
    }

    private TaskPool getTaskPool() {
        TaskPool pool = (TaskPool) getLastNonConfigurationInstance();
        if (pool == null) {
            return new TaskPool(handler);
        }
        return pool;
    }

    @Override
    public Object onRetainNonConfigurationInstance() {
        return pool;
    }

    void addImageToContainer(Bitmap bitmap) {
     task task  = new task();
        task.cancel(true);
    }

    private static class UpdatePoolHandler extends Handler {
        private final WeakReference<TaskHandlerActivity> view;

        UpdatePoolHandler(TaskHandlerActivity view) {
            this.view = new WeakReference<TaskHandlerActivity>(view);
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == IMAGE_DOWNLOADED_MSG_ID) {
                view.get().addImageToContainer((Bitmap) msg.obj);
            }
        }
    }

    class task extends AsyncTask<String,Void, Void>
    {
        @Override
        protected Void doInBackground(String... params) {
            return null;
        }
    }
}
