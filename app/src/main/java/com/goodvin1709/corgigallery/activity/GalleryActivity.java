package com.goodvin1709.corgigallery.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.goodvin1709.corgigallery.CorgiGallery;
import com.goodvin1709.corgigallery.R;
import com.goodvin1709.corgigallery.activity.dialog.DownloadDialog;
import com.goodvin1709.corgigallery.activity.dialog.impl.DownloadDialogImpl;
import com.goodvin1709.corgigallery.controller.GalleryController;

public class GalleryActivity extends Activity {

    public static final int DOWNLOADING_LIST_STARTED_MSG_ID = 0xfa;
    public static final int DOWNLOADING_LIST_COMPLETE_MSG_ID = 0xfb;
    public static final int CONNECTION_ERROR_MSG_ID = 0xfc;
    public static final int GALLERY_IMAGES_UPDATED = 0xfe;

    private DownloadDialog downloadDialog;
    private RecyclerView galleryView;
    private GalleryAdapter galleryAdapter;
    private GalleryController controller;
    private RelativeLayout connectionErrorContainer;
    private final GalleryHandler handler = new GalleryHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_activity);
        controller = ((CorgiGallery) getApplicationContext()).getPresenter();
        controller.attachHandler(handler);

        galleryView = (RecyclerView) findViewById(R.id.images_grid_container);
        galleryView.setLayoutManager(new GridLayoutManager(this, getRowsCount()));

        connectionErrorContainer = (RelativeLayout) findViewById(R.id.connection_error_container);
        downloadDialog = new DownloadDialogImpl(this);
        checkControllerStatus();
    }


    public void onRetryDownloadClicked(View view) {
        hideConnectionErrorContainer();
        controller.loadURLList();
    }

    private void checkControllerStatus() {
        if (controller.isLoadingList()) {
            downloadDialog.show();
        } else if (controller.isConnectionError()) {
            showConnectionErrorContainer();
        } else if (controller.isListLoaded()) {
            setGalleryAdapter();
        }
    }

    private void setGalleryAdapter() {
        galleryAdapter = new GalleryAdapter(controller, getRowsCount());
        galleryView.setAdapter(galleryAdapter);
    }

    private int getRowsCount() {
        if (getResources().getConfiguration().orientation == 1) {
            return getResources().getInteger(R.integer.gallery_portrait_column_count);
        } else {
            return getResources().getInteger(R.integer.gallery_landscape_column_count);
        }
    }

    private void showConnectionErrorContainer() {
        connectionErrorContainer.setVisibility(View.VISIBLE);
    }

    private void hideConnectionErrorContainer() {
        connectionErrorContainer.setVisibility(View.GONE);
    }

    private static class GalleryHandler extends Handler {
        private final GalleryActivity view;

        GalleryHandler(GalleryActivity view) {
            this.view = view;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWNLOADING_LIST_STARTED_MSG_ID:
                    onDownloadingStarted();
                    break;
                case DOWNLOADING_LIST_COMPLETE_MSG_ID:
                    onDownloadFinished();
                    break;
                case CONNECTION_ERROR_MSG_ID:
                    showConnectionError();
                    break;
                case GALLERY_IMAGES_UPDATED:
                    onImageUpdated(msg.arg1);
                    break;
                default:
                    break;
            }
        }

        private void onDownloadingStarted() {
            view.downloadDialog.show();
        }

        private void onDownloadFinished() {
            view.downloadDialog.hide();
            view.setGalleryAdapter();
        }

        private void showConnectionError() {
            view.downloadDialog.hide();
            view.showConnectionErrorContainer();
        }

        private void onImageUpdated(int position) {
            if (position != -1) {
                view.galleryAdapter.notifyItemChanged(position);
            }
        }
    }
}
