package com.goodvin1709.corgigallery;

import java.util.List;

public interface DownloadListener {

    void onImageListDownloaded(List<String> urlList);

    void onDownloadListError();

    void onImageDownloaded(Image image);

    void onDownloadImageError(String url);
}
