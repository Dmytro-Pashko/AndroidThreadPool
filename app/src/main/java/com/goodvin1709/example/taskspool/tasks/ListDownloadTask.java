package com.goodvin1709.example.taskspool.tasks;

import android.os.AsyncTask;

import com.goodvin1709.example.taskspool.DownloadListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class ListDownloadTask extends AsyncTask<Void, Void, Void> {

    private static final String LIST_URL = "https://raw.githubusercontent.com/goodvin1709/AndroidThreadPool/master/images/list.txt";
    private DownloadListener handler;

    public ListDownloadTask(DownloadListener handler) {
        this.handler = handler;
    }

    private void downloadImagesURLList() throws IOException {
        URL url = new URL(LIST_URL);
        BufferedReader input = new BufferedReader(new InputStreamReader(url.openStream()));
        ArrayList<String> list = new ArrayList<String>();
        String line;
        while ((line = input.readLine()) != null) {
            list.add(line);
        }
        input.close();
        handler.onImageListDownloaded(list);
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            downloadImagesURLList();
        } catch (IOException e) {
            handler.onDownloadListError();
        }
        return null;
    }
}
