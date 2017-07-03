package com.goodvin1709.corgigallery.activity;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.goodvin1709.corgigallery.R;

class GalleryImageHolder extends RecyclerView.ViewHolder {

    ProgressBar progressView;
    GalleryImageView imageView;
    private GalleryAdapter.OnImageClickListener clickListener;

    GalleryImageHolder(View itemView, int imageSize, GalleryAdapter.OnImageClickListener listener) {
        super(itemView);
        clickListener = listener;
        progressView = (ProgressBar) itemView.findViewById(R.id.image_progress_view);
        imageView = (GalleryImageView) itemView.findViewById(R.id.image_image_view);
        itemView.getLayoutParams().height = imageSize;
        itemView.getLayoutParams().width = imageSize - 6;
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onImageClick(getAdapterPosition());
            }
        });
    }
}
