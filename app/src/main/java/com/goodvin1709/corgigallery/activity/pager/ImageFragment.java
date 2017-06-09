package com.goodvin1709.corgigallery.activity.pager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.goodvin1709.corgigallery.CorgiGallery;
import com.goodvin1709.corgigallery.R;
import com.goodvin1709.corgigallery.controller.GalleryController;
import com.goodvin1709.corgigallery.controller.ImageLoadingHandler;
import com.goodvin1709.corgigallery.controller.LoadingListener;

public class ImageFragment extends Fragment implements LoadingListener {

    public static final String IMAGE_POSITION_KEY = "image_position";
    private ImageView image;
    private int position;
    private GalleryController controller;
    private final ImageLoadingHandler loadHandler = new ImageLoadingHandler(this);

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
        controller = ((CorgiGallery) getActivity().getApplicationContext()).getPresenter();
        position = getImagePosition(savedInstanceState);
        image = (ImageView) fragment.findViewById(R.id.image_fragment_image);
        image.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                image.getViewTreeObserver().removeOnPreDrawListener(this);
                loadImage();
                return false;
            }
        });
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(IMAGE_POSITION_KEY, position);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onLoadComplete() {
        loadImage();
    }

    @Override
    public void onLoadFail() {
        setBrokenImage();
    }

    private int getImagePosition(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            return getArguments().getInt(IMAGE_POSITION_KEY);
        } else {
            return savedInstanceState.getInt(IMAGE_POSITION_KEY);
        }
    }

    private void loadImage() {
        controller.loadImage(position, image, loadHandler);
    }

    private void setBrokenImage() {
        image.setImageResource(R.drawable.ic_broken_image_white);
    }
}
