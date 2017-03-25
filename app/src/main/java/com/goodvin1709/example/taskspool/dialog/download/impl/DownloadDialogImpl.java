package com.goodvin1709.example.taskspool.dialog.download.impl;

import android.app.ProgressDialog;
import android.content.Context;

import com.goodvin1709.example.taskspool.R;
import com.goodvin1709.example.taskspool.dialog.download.DownloadDialog;

public class DownloadDialogImpl implements DownloadDialog {

    private Context context;
    private ProgressDialog dialog;

    public DownloadDialogImpl(Context context) {
        this.context = context;
        buildDialog();
    }

    private void buildDialog() {
        dialog = new ProgressDialog(context);
        dialog.setTitle(R.string.download_dialog_title);
        dialog.setMessage(context.getString(R.string.download_dialog_msg));
        //dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
    }

    @Override
    public void show() {
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    @Override
    public void hide() {
        if (dialog.isShowing()) {
            dialog.hide();
        }
    }
}
