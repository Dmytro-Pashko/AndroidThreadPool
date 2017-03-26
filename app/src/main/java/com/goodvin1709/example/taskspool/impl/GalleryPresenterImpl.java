package com.goodvin1709.example.taskspool.impl;

import android.os.Handler;
import android.os.Message;

import com.goodvin1709.example.taskspool.DownloadListener;
import com.goodvin1709.example.taskspool.GalleryActivity;
import com.goodvin1709.example.taskspool.GalleryPresenter;
import com.goodvin1709.example.taskspool.TaskPool;
import com.goodvin1709.example.taskspool.tasks.ListDownloadTask;

import java.util.List;

public class GalleryPresenterImpl implements GalleryPresenter, DownloadListener {

    private TaskPool pool;
    private List<String> imagesUrlList;
    private Handler viewHandler;

    public GalleryPresenterImpl(Handler viewHandler) {
        this.viewHandler = viewHandler;
        pool = new TaskPoolExecutor();
    }

    @Override
    public void startDownloadImagesList() {
        pool.addTaskToDownloadList(new ListDownloadTask(this));
        showOnView(GalleryActivity.DOWNLOADING_LIST_STARTED_MSG);
    }

    @Override
    public void attachViewHandler(Handler viewHandler) {
        this.viewHandler = viewHandler;
    }

    @Override
    public void onImageListDownloaded(List<String> imageList) {
        imagesUrlList = imageList;
        showOnView(GalleryActivity.DOWNLOADING_LIST_COMPLETE_MSG);
    }

    @Override
    public void onDownloadListError() {
        showOnView(GalleryActivity.DOWNLOADING_ERROR);
    }

    private void showOnView(int msgId) {
        Message downloadCompleteMsg = viewHandler.obtainMessage(msgId);
        viewHandler.sendMessage(downloadCompleteMsg);
    }

}
