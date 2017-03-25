package com.goodvin1709.example.taskspool;

import java.util.List;

public interface DownloadListener {

    void onImageListDownloaded(List<String> imageList);

    void onDownloadListError();

}
