package com.goodvin1709.example.taskspool.tasks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class ListTask implements Callable<List<String>> {
    private static final String LIST_URL = "https://raw.githubusercontent.com/goodvin1709/AndroidThreadPool/master/images/list.txt";

    @Override
    public List<String> call() throws Exception {
        return downloadImagesURLList();
    }

    private List<String> downloadImagesURLList() throws IOException {
        URL url = new URL(LIST_URL);
        BufferedReader input = new BufferedReader(new InputStreamReader(url.openStream()));
        ArrayList<String> list = new ArrayList<String>();
        String line;
        while ((line = input.readLine()) != null) {
            list.add(line);
        }
        input.close();
        return list;
    }
}
