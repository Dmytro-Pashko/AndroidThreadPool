package com.goodvin1709.example.taskspool;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Toast;

import com.goodvin1709.example.taskspool.impl.GalleryPresenterImpl;

public class GalleryActivity extends Activity implements GalleryView {

    private GalleryPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread_handler);
        presenter = getPresenter();
        presenter.downloadImageList();
    }

    private GalleryPresenter getPresenter() {
        GalleryPresenter presenter = (GalleryPresenter) getLastNonConfigurationInstance();
        if (presenter == null) {
            return new GalleryPresenterImpl();
        }
        return presenter;
    }

    @Override
    public Object onRetainNonConfigurationInstance() {
        return presenter;
    }

    @Override
    public void addImage(Bitmap bitmap) {

    }

    @Override
    public void showConnectionError() {
        Toast.makeText(this, R.string.connection_error, Toast.LENGTH_SHORT).show();
    }
}
