package com.goodvin1709.corgigallery.activity.dialog.impl;

import android.app.ProgressDialog;
import android.content.Context;

import com.goodvin1709.corgigallery.R;
import com.goodvin1709.corgigallery.activity.dialog.DownloadDialog;

public class DownloadDialogImpl implements DownloadDialog {

    private ProgressDialog dialog;

    public DownloadDialogImpl(Context context) {
        buildDialog(context);
    }

    private void buildDialog(Context context) {
        dialog = new ProgressDialog(context);
        dialog.setTitle(R.string.download_dialog_title);
        dialog.setMessage(context.getString(R.string.download_dialog_msg));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
    }

    @Override
    public void show() {
        dialog.show();
    }

    @Override
    public void hide() {
        dialog.hide();
    }
}
