package com.goodvin1709.corgigallery.activity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.goodvin1709.corgigallery.CorgiGallery;
import com.goodvin1709.corgigallery.R;
import com.goodvin1709.corgigallery.controller.GalleryController;
import com.goodvin1709.corgigallery.model.Image;
import com.goodvin1709.corgigallery.model.ImageStatus;

import java.util.ArrayList;
import java.util.List;

class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ImageHolder> {

    private final GalleryController controller;
    private List<Image> images;

    GalleryAdapter(Context context) {
        images = new ArrayList<>();
        controller = ((CorgiGallery) context.getApplicationContext()).getPresenter();
    }

    void addImages(List<Image> images) {
        this.images = images;
        notifyDataSetChanged();
    }

    @Override
    public ImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_loading_view, parent, false);
        return new ImageHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageHolder holder, int position) {
        if (isImageLoading(position)) {
            holder.progressView.setVisibility(View.VISIBLE);
            holder.imageView.setVisibility(View.GONE);
        } else if (isBrokenImage(position)) {
            holder.progressView.setVisibility(View.GONE);
            holder.imageView.setVisibility(View.VISIBLE);
            holder.imageView.setImageResource(R.drawable.ic_broken_image);
        } else {
            controller.loadImage(images.get(position), holder.imageView);
            holder.progressView.setVisibility(View.GONE);
            holder.imageView.setVisibility(View.VISIBLE);
        }
    }

    private boolean isImageLoading(int position) {
        ImageStatus status = images.get(position).getStatus();
        return status == ImageStatus.LOADING || status == ImageStatus.CACHING;
    }

    private boolean isBrokenImage(int position) {
        ImageStatus status = images.get(position).getStatus();
        return status == ImageStatus.CACHED_ERROR || status == ImageStatus.LOADING_ERROR;
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    private class ImageHolder extends RecyclerView.ViewHolder {

        private ProgressBar progressView;
        private ImageView imageView;

        ImageHolder(View itemView) {
            super(itemView);
            progressView = (ProgressBar) itemView.findViewById(R.id.image_progress_view);
            imageView = (ImageView) itemView.findViewById(R.id.image_image_view);
        }
    }
}
