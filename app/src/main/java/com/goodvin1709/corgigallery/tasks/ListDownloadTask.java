package com.goodvin1709.corgigallery.tasks;

import com.goodvin1709.corgigallery.DownloadListener;
import com.goodvin1709.corgigallery.Image;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class ListDownloadTask implements Runnable {

    private static final String LIST_URL = "https://raw.githubusercontent.com/goodvin1709/AndroidThreadPool/develop/images/list.txt";
    private DownloadListener handler;

    public ListDownloadTask(DownloadListener handler) {
        this.handler = handler;
    }

    @Override
    public void run() {
        try {
            downloadList();
        } catch (IOException e) {
            handler.onDownloadListError();
        }
    }

    private void downloadList() throws IOException {
        URL url = new URL(LIST_URL);
        BufferedReader input = new BufferedReader(new InputStreamReader(url.openStream()));
        ArrayList<Image> list = new ArrayList<Image>();
        String line;
        while ((line = input.readLine()) != null) {
            list.add(new Image(line));
        }
        input.close();
        handler.onImageListDownloaded(list);
        handler = null;
    }
}
