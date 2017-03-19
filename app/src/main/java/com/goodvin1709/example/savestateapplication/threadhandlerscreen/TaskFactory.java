package com.goodvin1709.example.savestateapplication.threadhandlerscreen;

class TaskFactory implements java.util.concurrent.ThreadFactory {

    private static final String TASK_NAME_PREFIX = "LowPriorityTask";
    private static final int TASK_PRIORITY = 3;

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r);
        thread.setName(TASK_NAME_PREFIX);
        thread.setPriority(TASK_PRIORITY);
        return thread;
    }
}
