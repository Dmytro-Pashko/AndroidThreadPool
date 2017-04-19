package com.goodvin1709.corgigallery.dialog.download.impl;

import android.app.ProgressDialog;
import android.content.Context;

import com.goodvin1709.corgigallery.R;
import com.goodvin1709.corgigallery.dialog.download.DownloadDialog;

public class DownloadDialogImpl implements DownloadDialog {

    private Context context;
    private ProgressDialog dialog;
    private boolean isShowing;

    public DownloadDialogImpl(Context context) {
        this.context = context;
        buildDialog();
    }

    private void buildDialog() {
        dialog = new ProgressDialog(context);
        dialog.setTitle(R.string.download_dialog_title);
        dialog.setMessage(context.getString(R.string.download_dialog_msg));
        dialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public void show() {
        if (!dialog.isShowing()) {
            dialog.show();
            isShowing = true;
        }
    }

    @Override
    public void hide() {
        if (dialog.isShowing()) {
            dialog.hide();
            isShowing = false;
        }
    }


    @Override
    public boolean isShowing() {
        return isShowing;
    }

    @Override
    public void destroy() {
        context = null;
    }
}
