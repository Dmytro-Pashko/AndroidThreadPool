package com.goodvin1709.corgigallery;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.GridView;
import android.widget.Toast;

import com.goodvin1709.corgigallery.dialog.download.DownloadDialog;
import com.goodvin1709.corgigallery.dialog.download.impl.DownloadDialogImpl;

import java.lang.ref.WeakReference;

public class GalleryActivity extends Activity {

    public static final int DOWNLOADING_LIST_STARTED_MSG_ID = 0xfa;
    public static final int DOWNLOADING_LIST_COMPLETE_MSG_ID = 0xfb;
    public static final int CONNECTION_ERROR_MSG_ID = 0xfc;
    public static final int GALLERY_IMAGES_UPDATED = 0xfe;
    private static final String DOWNLOAD_DIALOG_STATE_KEY = "DOWNLOAD_DIALOG_STATE";

    private DownloadDialog downloadDialog;
    private GalleryAdapter galleryAdapter;
    private GalleryPresenter presenter;
    private final GalleryHandler handler = new GalleryHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_activity);
        GridView galleryView = (GridView) findViewById(R.id.imagesGridContainer);
        galleryAdapter = new GalleryAdapter(this);
        galleryView.setAdapter(galleryAdapter);
        presenter = ((CorgiGallery) getApplicationContext()).getPresenter();
        presenter.attachHandler(handler);
        downloadDialog = new DownloadDialogImpl(this);
        galleryAdapter.addImages(presenter.getImages());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(DOWNLOAD_DIALOG_STATE_KEY, downloadDialog.isShowing());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        downloadDialog.destroy();
        downloadDialog = null;
        super.onDestroy();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState.getBoolean(DOWNLOAD_DIALOG_STATE_KEY)) {
            showDownloadProgress();
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    private void showDownloadProgress() {
        downloadDialog.show();
    }

    private void hideDownloadProgress() {
        downloadDialog.hide();
        galleryAdapter.addImages(presenter.getImages());
    }

    private void showConnectionError() {
        downloadDialog.hide();
        Toast.makeText(this, R.string.connection_error, Toast.LENGTH_SHORT).show();
    }

    private void imagesUpdated() {
        galleryAdapter.notifyDataSetChanged();
    }

    private static class GalleryHandler extends Handler {
        private final WeakReference<GalleryActivity> view;

        GalleryHandler(GalleryActivity view) {
            this.view = new WeakReference<GalleryActivity>(view);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWNLOADING_LIST_STARTED_MSG_ID:
                    view.get().showDownloadProgress();
                    break;
                case DOWNLOADING_LIST_COMPLETE_MSG_ID:
                    view.get().hideDownloadProgress();
                    break;
                case CONNECTION_ERROR_MSG_ID:
                    view.get().showConnectionError();
                    break;
                case GALLERY_IMAGES_UPDATED:
                    view.get().imagesUpdated();
                    break;
                default:
                    break;
            }
        }
    }
}
