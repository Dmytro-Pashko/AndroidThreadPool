package com.goodvin1709.corgigallery.activity;

import android.app.Activity;
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

import java.util.ArrayList;
import java.util.List;

class GalleryAdapter extends BaseAdapter {

    private List<Image> images;
    private final GalleryController controller;
    private LayoutInflater inflater;
    private Activity activity;

    GalleryAdapter(Activity activity) {
        this.activity = activity;
        inflater = activity.getLayoutInflater();
        images = new ArrayList<Image>();
        controller = ((CorgiGallery) activity.getApplicationContext()).getPresenter();
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
        int size = parent.getWidth() / ((GridView) parent).getNumColumns();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.image_loading_view, parent, false);
            holder = new ImageHolder();
            convertView.setLayoutParams(new ViewGroup.LayoutParams(size, size));
            holder.image = (ImageView) convertView.findViewById(R.id.image_image_view);
            holder.image.setLayoutParams(new RelativeLayout.LayoutParams(size, size));
            convertView.setTag(holder);
        } else {
            holder = (ImageHolder) convertView.getTag();
        }
        controller.loadImage(images.get(position), holder.image);
        return convertView;
    }

    private static class ImageHolder {
        ImageView image;
        ProgressBar progress;
    }

}