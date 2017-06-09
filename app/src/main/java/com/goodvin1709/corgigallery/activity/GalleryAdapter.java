package com.goodvin1709.corgigallery.activity;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.goodvin1709.corgigallery.R;
import com.goodvin1709.corgigallery.controller.GalleryController;
import com.goodvin1709.corgigallery.controller.ImageLoadingHandler;
import com.goodvin1709.corgigallery.controller.LoadingListener;
import com.goodvin1709.corgigallery.model.Image;

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
    public void onBindViewHolder(final ImageHolder holder, int position) {
        holder.imageView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                holder.imageView.getViewTreeObserver().removeOnPreDrawListener(this);
                controller.loadImage(holder.getAdapterPosition(), holder.imageView, new ImageLoadingHandler(holder));
                return false;
            }
        });
    }

    class ImageHolder extends RecyclerView.ViewHolder implements LoadingListener {

        private ProgressBar progressView;
        private ImageView imageView;

        ImageHolder(View itemView, int imageSize) {
            super(itemView);
            progressView = (ProgressBar) itemView.findViewById(R.id.image_progress_view);
            imageView = (ImageView) itemView.findViewById(R.id.image_image_view);
            itemView.getLayoutParams().height = imageSize;
            itemView.getLayoutParams().width = imageSize - 6;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onImageClick(getAdapterPosition());
                }
            });
        }

        @Override
        public void onLoadComplete() {
            progressView.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
            notifyItemChanged(getAdapterPosition());
        }

        @Override
        public void onLoadFail() {
            imageView.setImageResource(R.drawable.ic_broken_image_black);
            progressView.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
        }
    }

    interface OnImageClickListener {

        void onImageClick(int position);
    }
}
