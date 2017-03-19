package com.goodvin1709.example.savestateapplication.threadhandlerscreen;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.goodvin1709.example.savestateapplication.R;

import java.lang.ref.WeakReference;

public class TaskHandlerActivity extends Activity {

    public static final int UPDATE_POOL_INFORMATION_MSG_ID = 0xFF;
    private TaskPool pool;
    private TextView queueTaskCount, taskCount, poolSize, maxPoolSize,
            completeTaskCount, workedTaskCount;
    private final UpdatePoolHandler handler = new UpdatePoolHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread_handler);
        queueTaskCount = getTextView(R.id.queue_task_count);
        taskCount = getTextView(R.id.task_count);
        poolSize = getTextView(R.id.pool_size);
        maxPoolSize = getTextView(R.id.max_pool_size);
        completeTaskCount = getTextView(R.id.complete_task_count);
        workedTaskCount = getTextView(R.id.worked_task_count);
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
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public Object onRetainNonConfigurationInstance() {
        return pool;
    }

    private TextView getTextView(int id) {
        return (TextView) findViewById(id);
    }

    public void addTask(View view) {
        pool.addTask(new Task());
    }

    void updateInformation(UpdatePoolInformation info) {
        queueTaskCount.setText(getString(R.string.queue_tasks, info.queueTaskCount));
        taskCount.setText(getString(R.string.task_count, info.taskCount));
        poolSize.setText(getString(R.string.pool_size, info.poolSize));
        maxPoolSize.setText(getString(R.string.max_pool_size, info.maxPoolSize));
        completeTaskCount.setText(getString(R.string.complete_tasks, info.completeTaskCount));
        workedTaskCount.setText(getString(R.string.active_tasks, info.workedTaskCount));
    }

    private static class UpdatePoolHandler extends Handler {
        private final WeakReference<TaskHandlerActivity> view;

        UpdatePoolHandler(TaskHandlerActivity view) {
            this.view = new WeakReference<TaskHandlerActivity>(view);
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == UPDATE_POOL_INFORMATION_MSG_ID) {
                view.get().updateInformation((UpdatePoolInformation) msg.obj);
            }
        }
    }
}
