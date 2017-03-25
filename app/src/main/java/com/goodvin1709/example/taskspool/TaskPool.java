package com.goodvin1709.example.taskspool;

import com.goodvin1709.example.taskspool.tasks.ImageDownloadTask;
import com.goodvin1709.example.taskspool.tasks.ListDownloadTask;

public interface TaskPool {

    void addTaskToDownloadList(ListDownloadTask loadListTask);

    void addTaskToDownloadImage(ImageDownloadTask loadListTask);
}
