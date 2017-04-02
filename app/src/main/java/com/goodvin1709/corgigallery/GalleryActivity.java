package com.goodvin1709.corgigallery;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Loader;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.GridView;
import android.widget.Toast;

import com.goodvin1709.corgigallery.dialog.download.DownloadDialog;
import com.goodvin1709.corgigallery.dialog.download.impl.DownloadDialogImpl;
import com.goodvin1709.corgigallery.impl.GalleryPresenterImpl;

import java.lang.ref.WeakReference;
import java.util.List;

public class GalleryActivity extends Activity implements LoaderManager.LoaderCallbacks<List<Bitmap>> {

    public static final int DOWNLOADING_LIST_STARTED_MSG_ID = 0xfa;
    public static final int DOWNLOADING_LIST_COMPLETE_MSG_ID = 0xfb;
    public static final int CONNECTION_ERROR_MSG_ID = 0xfc;
    public static final int GALLERY_IMAGES_UPDATED = 0xfe;
    private static final String DOWNLOAD_DIALOG_STATE_KEY = "DOWNLOAD_DIALOG_STATE";
    private static final int GALLERY_LOADER_ID = 0xfd;

    private GalleryPresenter galleryLoader;
    private DownloadDialog downloadDialog;
    private GridView container;
    private GalleryAdapter adapter;
    private final GalleryHandler handler = new GalleryHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_activity);
        container = (GridView) findViewById(R.id.imagesGridContainer);
        adapter = new GalleryAdapter(this);
        container.setAdapter(adapter);
        downloadDialog = new DownloadDialogImpl(this);
        galleryLoader = (GalleryPresenterImpl) getLoaderManager().initLoader(GALLERY_LOADER_ID, null, this);
        galleryLoader.attachHandler(handler);
        adapter.updateList(galleryLoader.getImages());
    }

    @Override
    public Loader<List<Bitmap>> onCreateLoader(int id, Bundle args) {
        return new GalleryPresenterImpl(this);
    }

    @Override
    public void onLoadFinished(Loader<List<Bitmap>> loader, List<Bitmap> data) {
    }

    @Override
    public void onLoaderReset(Loader<List<Bitmap>> loader) {
        galleryLoader.attachHandler(null);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(DOWNLOAD_DIALOG_STATE_KEY, downloadDialog.isShowing());
        super.onSaveInstanceState(outState);
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
    }

    private void showConnectionError() {
        downloadDialog.hide();
        Toast.makeText(this, R.string.connection_error, Toast.LENGTH_SHORT).show();
    }

    private void updateGallery() {
        adapter.updateList(galleryLoader.getImages());
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
                    view.get().updateGallery();
                    break;
            }
        }
    }
}
