package com.goodvin1709.corgigallery.activity;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.goodvin1709.corgigallery.R;
import com.goodvin1709.corgigallery.controller.GalleryController;
import com.goodvin1709.corgigallery.model.Image;
import com.goodvin1709.corgigallery.model.ImageStatus;

import java.util.List;

class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ImageHolder> {

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
    public ImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_view, parent, false);
        return new ImageHolder(view, parent.getMeasuredWidth() / rowsCount);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    @Override
    public void onBindViewHolder(ImageHolder holder, int position) {
        if (isBrokenImage(position)) {
            holder.imageView.setImageResource(R.drawable.ic_broken_image);
            holder.progressView.setVisibility(View.GONE);
            holder.imageView.setVisibility(View.VISIBLE);
        } else if (isLoaded(position)) {
            controller.loadImage(images.get(position), holder.imageSize, holder.imageView);
            holder.progressView.setVisibility(View.GONE);
            holder.imageView.setVisibility(View.VISIBLE);
        } else {
            controller.loadImage(images.get(position), holder.imageSize, holder.imageView);
        }
    }

    private boolean isBrokenImage(int position) {
        ImageStatus status = images.get(position).getStatus();
        return status == ImageStatus.CACHED_ERROR || status == ImageStatus.LOADING_ERROR;
    }

    private boolean isLoaded(int position) {
        ImageStatus status = images.get(position).getStatus();
        return status == ImageStatus.IDLE;
    }

    class ImageHolder extends RecyclerView.ViewHolder {

        private ProgressBar progressView;
        private ImageView imageView;
        private int imageSize;

        ImageHolder(View itemView, int imageSize) {
            super(itemView);
            this.imageSize = imageSize;
            progressView = (ProgressBar) itemView.findViewById(R.id.image_progress_view);
            imageView = (ImageView) itemView.findViewById(R.id.image_image_view);
            itemView.getLayoutParams().height = imageSize;
            itemView.getLayoutParams().width = imageSize - 6;
            imageView.getLayoutParams().width = imageSize - 6;
            imageView.getLayoutParams().height = imageSize;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onImageClick(getAdapterPosition());
                }
            });
        }
    }

    interface OnImageClickListener {

        void onImageClick(int position);
    }
}
