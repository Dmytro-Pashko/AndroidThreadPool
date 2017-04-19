package com.goodvin1709.corgigallery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

class GalleryAdapter extends BaseAdapter {

    private List<Image> images;
    private GalleryPresenter presenter;
    private LayoutInflater inflater;

    GalleryAdapter(Context context) {
        images = new ArrayList<Image>();
        presenter = ((CorgiGallery) context.getApplicationContext()).getPresenter();
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        ImageViewHolder view;
        Image image = images.get(position);
        int imageSize = parent.getWidth() / ((GridView) parent).getNumColumns();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.image_loading_view, parent, false);
            view = new ImageViewHolder();
            view.image = (ImageView) convertView.findViewById(R.id.image);
            view.progressBar = (ProgressBar) convertView.findViewById(R.id.image_progress_view);
            convertView.setTag(view);
            convertView.setLayoutParams(new GridView.LayoutParams(imageSize, imageSize));
        } else {
            view = (ImageViewHolder) convertView.getTag();
        }
        setImageIntoView(image, view, imageSize);
        return convertView;
    }

    private void setImageIntoView(Image image, ImageViewHolder viewHolder, int imageSize) {
        if (image.getBitmap() == null) {
            presenter.loadBitmap(image, imageSize);
        } else {
            viewHolder.image.setImageBitmap(image.getBitmap());
            viewHolder.image.setScaleType(ImageView.ScaleType.CENTER_CROP);
            viewHolder.progressBar.setVisibility(View.GONE);
        }
    }

    private static class ImageViewHolder {
        ImageView image;
        ProgressBar progressBar;
    }
}