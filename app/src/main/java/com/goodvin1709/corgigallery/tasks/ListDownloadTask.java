package com.goodvin1709.corgigallery.tasks;

import com.goodvin1709.corgigallery.DownloadListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class ListDownloadTask implements Runnable {

    private static final String LIST_URL = "https://raw.githubusercontent.com/goodvin1709/AndroidThreadPool/master/images/list.txt";
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
        ArrayList<String> list = new ArrayList<String>();
        String line;
        while ((line = input.readLine()) != null) {
            list.add(line);
        }
        input.close();
        handler.onImageListDownloaded(list);
        handler = null;
    }
}
