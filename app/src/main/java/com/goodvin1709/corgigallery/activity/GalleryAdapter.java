package com.goodvin1709.corgigallery.activity;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.goodvin1709.corgigallery.R;
import com.goodvin1709.corgigallery.controller.GalleryController;
import com.goodvin1709.corgigallery.controller.LoadingListener;
import com.goodvin1709.corgigallery.model.Image;

import java.util.List;

class GalleryAdapter extends RecyclerView.Adapter<GalleryImageHolder> {

    private GalleryController controller;
    private List<Image> images;
    private int rowsCount;
    private OnImageClickListener clickListener;

    GalleryAdapter(GalleryController controller, OnImageClickListener listener, int rowsCount) {
        this.clickListener = listener;
        this.rowsCount = rowsCount;
        this.controller = controller;
        images = controller.getImages();
    }

    @Override
    public GalleryImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_view, parent, false);
        return new GalleryImageHolder(view, parent.getMeasuredWidth() / rowsCount, clickListener);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    @Override
    public void onBindViewHolder(final GalleryImageHolder holder, int position) {
        final Image image = images.get(position);
        holder.imageView.setImageUrl(image,controller,new LoadingListener() {
            @Override
            public void onLoadComplete() {
                holder.imageView.setVisibility(View.VISIBLE);
                holder.progressView.setVisibility(View.GONE);
            }

            @Override
            public void onLoadFail() {
                holder.imageView.setImageResource(R.drawable.ic_broken_image_black);
                holder.imageView.setVisibility(View.VISIBLE);
                holder.progressView.setVisibility(View.GONE);
            }
        });
    }

    interface OnImageClickListener {

        void onImageClick(int position);
    }
}
