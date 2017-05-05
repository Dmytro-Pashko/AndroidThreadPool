package com.goodvin1709.corgigallery.activity;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.goodvin1709.corgigallery.CorgiGallery;
import com.goodvin1709.corgigallery.R;
import com.goodvin1709.corgigallery.controller.GalleryController;
import com.goodvin1709.corgigallery.model.Image;
import com.goodvin1709.corgigallery.model.ImageStatus;

import java.util.ArrayList;
import java.util.List;

class GalleryAdapter extends BaseAdapter {

    private final GalleryController controller;
    private List<Image> images;
    private LayoutInflater inflater;

    GalleryAdapter(Context context) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ImageHolder holder;
        final Image image = images.get(position);
        int size = parent.getWidth() / ((GridView) parent).getNumColumns();
        if (convertView == null) {
            holder = new ImageHolder();
            convertView = inflater.inflate(R.layout.image_loading_view, null);
            convertView.setLayoutParams(new ViewGroup.LayoutParams(size, size));
            holder.image = (ImageView) convertView.findViewById(R.id.image_image_view);
            holder.progress = (ProgressBar) convertView.findViewById(R.id.image_progress_view);
            holder.image.setLayoutParams(new RelativeLayout.LayoutParams(size, size));
            convertView.setTag(holder);
        } else {
            holder = (ImageHolder) convertView.getTag();
        }
        controller.loadImage(images.get(position), holder.image);
        checkHolderVisibility(holder, image);
        return convertView;
    }

    private void checkHolderVisibility(ImageHolder holder, Image image) {
        if (image.getStatus() == ImageStatus.LOADING) {
            holder.progress.setVisibility(View.VISIBLE);
            holder.image.setVisibility(View.GONE);
        } else {
            holder.progress.setVisibility(View.GONE);
            holder.image.setVisibility(View.VISIBLE);
        }
    }

    private static class ImageHolder {
        ImageView image;
        ProgressBar progress;
    }
}