package com.goodvin1709.corgigallery.activity.pager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.goodvin1709.corgigallery.CorgiGallery;
import com.goodvin1709.corgigallery.R;
import com.goodvin1709.corgigallery.controller.GalleryController;
import com.goodvin1709.corgigallery.controller.LoadingListener;
import com.goodvin1709.corgigallery.model.Image;
import com.goodvin1709.corgigallery.utils.Logger;

public class ImageFragment extends Fragment implements LoadingListener {

    public static final String IMAGE_POSITION_KEY = "image_position";
    private ImageView imageView;
    private ProgressBar progress;
    private Image image;
    private int position;
    private GalleryController controller;

    public static ImageFragment getInstance(int position) {
        ImageFragment fragment = new ImageFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(IMAGE_POSITION_KEY, position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.image_frament, container, false);
        controller = CorgiGallery.getInstance().getPresenter();
        position = getImagePosition(savedInstanceState);
        image = controller.getImages().get(position);
        imageView = (ImageView) fragment.findViewById(R.id.image_fragment_image);
        progress = (ProgressBar) fragment.findViewById(R.id.image_fragment_progress);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        controller.loadImage(image, imageView, this);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(IMAGE_POSITION_KEY, position);
        super.onSaveInstanceState(outState);
    }

    private int getImagePosition(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            return getArguments().getInt(IMAGE_POSITION_KEY);
        } else {
            return savedInstanceState.getInt(IMAGE_POSITION_KEY);
        }
    }

    @Override
    public void onLoadComplete() {
        Logger.log("%s loaded from memory into pager fragment.", image);
        imageView.setVisibility(View.VISIBLE);
        progress.setVisibility(View.GONE);
    }

    @Override
    public void onLoadFail() {
        imageView.setVisibility(View.VISIBLE);
        imageView.setImageResource(R.drawable.ic_broken_image_white);
        progress.setVisibility(View.GONE);
    }
}
