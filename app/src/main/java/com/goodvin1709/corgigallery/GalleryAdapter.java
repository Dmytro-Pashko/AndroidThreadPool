package com.goodvin1709.corgigallery;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

class GalleryAdapter extends BaseAdapter {

    private Context context;
    private List<Image> images;

    GalleryAdapter(Context context) {
        this.context = context;
        this.images = new ArrayList<Image>();
    }

    void updateList(List<Image> images) {
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
        ImageView image;
        int imageSize = parent.getWidth() / ((GridView) parent).getNumColumns();
        if (convertView == null) {
            image = new ImageView(context);
            image.setLayoutParams(new GridView.LayoutParams(imageSize, imageSize));
            image.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            image = (ImageView) convertView;
            image.setLayoutParams(new GridView.LayoutParams(imageSize, imageSize));
        }
        image.setImageBitmap(images.get(position).getBitmap());
        return image;
    }
}
