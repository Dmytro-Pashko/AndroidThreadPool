package com.goodvin1709.corgigallery.activity.pager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.goodvin1709.corgigallery.CorgiGallery;
import com.goodvin1709.corgigallery.R;
import com.goodvin1709.corgigallery.controller.GalleryController;

public class ImageFragment extends Fragment {

    public static final String IMAGE_POSITION_KEY = "image_position";
    private ImageView image;
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
        controller = ((CorgiGallery)getActivity().getApplicationContext()).getPresenter();
        position = getImagePosition(savedInstanceState);
        image = (ImageView) fragment.findViewById(R.id.image_fragment_image);
        controller.loadImage(controller.getImages().get(position),360,image);
        return fragment;
    }

    private int getImagePosition(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            return getArguments().getInt(IMAGE_POSITION_KEY);
        } else {
            return savedInstanceState.getInt(IMAGE_POSITION_KEY);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(IMAGE_POSITION_KEY, position);
        super.onSaveInstanceState(outState);
    }

}
