package com.goodvin1709.corgigallery;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.goodvin1709.CorgiGallery;

import java.util.ArrayList;
import java.util.List;

class GalleryAdapter extends BaseAdapter {

    private Context context;
    private List<Image> images;
    private GalleryPresenter presenter;

    GalleryAdapter(Context context) {
        this.context = context;
        images = new ArrayList<Image>();
        presenter = ((CorgiGallery) context.getApplicationContext()).getPresenter();
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
        ImageView view;
        Image image = images.get(position);
        int imageSize = parent.getWidth() / ((GridView) parent).getNumColumns();
        if (convertView == null) {
            view = new ImageView(context);
            view.setLayoutParams(new GridView.LayoutParams(imageSize, imageSize));
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            view = (ImageView) convertView;
            view.setLayoutParams(new GridView.LayoutParams(imageSize, imageSize));
        }
        if (image.getBitmap() == null) {
            presenter.loadBitmap(image,imageSize,imageSize);
            view.setImageDrawable(context.getResources().getDrawable(R.drawable.download_load));
        } else {
            view.setImageBitmap(image.getBitmap());
        }
        return view;
    }
}
