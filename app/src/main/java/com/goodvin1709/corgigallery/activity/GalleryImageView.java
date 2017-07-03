package com.goodvin1709.corgigallery.activity;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.goodvin1709.corgigallery.controller.GalleryController;
import com.goodvin1709.corgigallery.controller.LoadingListener;
import com.goodvin1709.corgigallery.model.Image;

public class GalleryImageView extends AppCompatImageView {

    private Image image;
    private GalleryController controller;
    private LoadingListener loadingListener;

    public GalleryImageView(Context context) {
        super(context);
    }

    public GalleryImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GalleryImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        this.loadImageIfNecessary();
    }

    public void setImageUrl(Image image, GalleryController controller, LoadingListener loadingListener) {
        this.image = image;
        this.controller = controller;
        this.loadingListener = loadingListener;
        this.loadImageIfNecessary();
    }


    void loadImageIfNecessary() {
        int width = this.getWidth();
        int height = this.getHeight();
        boolean wrapWidth = false;
        boolean wrapHeight = false;
        if (this.getLayoutParams() != null) {
            wrapWidth = this.getLayoutParams().width == -2;
            wrapHeight = this.getLayoutParams().height == -2;
        }

        boolean isFullyWrapContent = wrapWidth && wrapHeight;
        if (width != 0 || height != 0 || isFullyWrapContent) {
            controller.loadImage(image, this, loadingListener);
        }
    }

    protected void onDetachedFromWindow() {
        controller = null;
        super.onDetachedFromWindow();
    }
}
