package com.goodvin1709.example.taskspool.tasks;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class ListTask implements Callable<List<String>> {
    private static final String LIST_URL = "";

    @Override
    public List<String> call() throws Exception {
        return downloadImageList();
    }

    private List<String> downloadImageList() {
        ArrayList<String> imagesList = new ArrayList<String>();
        return imagesList;
    }
}
