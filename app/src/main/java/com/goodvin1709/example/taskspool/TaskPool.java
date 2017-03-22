package com.goodvin1709.example.taskspool;

import android.graphics.Bitmap;

import java.util.List;
import java.util.concurrent.Callable;

public interface TaskPool {

    void addTaskToDownloadImage(Callable<Bitmap> imageTask);

    void addTaskToDownloadImageList(Callable<List<String>> imageList);
}
