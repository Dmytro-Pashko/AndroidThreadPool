package com.goodvin1709.corgigallery.activity;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
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
import com.goodvin1709.corgigallery.utils.Logger;

import java.util.ArrayList;
import java.util.List;

class GalleryAdapter extends BaseAdapter {

    private final GalleryController controller;
    private List<Image> images;
    private LayoutInflater inflater;

    GalleryAdapter(Activity activity) {
        images = new ArrayList<>();
        controller = ((CorgiGallery) activity.getApplicationContext()).getPresenter();
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    void addImages(List<Image> images) {
        this.images = images;
        notifyDataSetInvalidated();
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
        Logger.log("Position [%d]", position);
        ImageHolder holder;
        Image image = images.get(position);
        int size = parent.getWidth() / ((GridView) parent).getNumColumns();
        if (convertView == null) {
            holder = new ImageHolder();
            convertView = inflater.inflate(R.layout.image_loading_view, null);
            convertView.setLayoutParams(new AbsListView.LayoutParams(size, size));
            holder.image = (ImageView) convertView.findViewById(R.id.image_image_view);
            holder.progress = (ProgressBar) convertView.findViewById(R.id.image_progress_view);
            holder.image.setLayoutParams(new RelativeLayout.LayoutParams(size, size));
            convertView.setTag(holder);
        } else {
            holder = (ImageHolder) convertView.getTag();
        }
        setImage(position, holder, image);
        checkHolderVisibility(holder, image);
        return convertView;
    }

    private void setImage(int position, ImageHolder holder, Image image) {
        if (isImageBroken(image)) {
            setBrokenIcon(holder.image);
        } else {
            controller.loadImage(images.get(position), holder.image);
        }
    }

    private boolean isImageBroken(Image image) {
        return image.getStatus() == ImageStatus.CACHED_ERROR || image.getStatus() == ImageStatus.LOADING_ERROR;
    }

    private void setBrokenIcon(ImageView view) {
        view.setImageResource(R.drawable.ic_broken_image);
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