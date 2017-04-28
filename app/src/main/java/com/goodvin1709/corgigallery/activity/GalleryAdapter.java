package com.goodvin1709.corgigallery.activity;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.goodvin1709.corgigallery.CorgiGallery;
import com.goodvin1709.corgigallery.controller.GalleryController;
import com.goodvin1709.corgigallery.model.Image;

import java.util.ArrayList;
import java.util.List;

class GalleryAdapter extends BaseAdapter {

    private List<Image> images;
    private final GalleryController controller;
    private Context context;

    GalleryAdapter(Context context) {
        this.context = context;
        images = new ArrayList<Image>();
        controller = ((CorgiGallery) context.getApplicationContext()).getPresenter();
    }

    void addImages(List<Image> images) {
        this.images = images;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object getItem(int position) {
        return images.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        Image image = images.get(position);
        int imageSize = parent.getWidth() / ((GridView) parent).getNumColumns();
        if (convertView == null) {
            imageView = new ImageView(context);
            imageView.setPadding(4, 4, 4, 4);
            imageView.setLayoutParams(new GridView.LayoutParams(imageSize, imageSize));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            controller.loadImage(image, imageView);
        } else {
            imageView = (ImageView) convertView;
        }
        return imageView;
    }

}