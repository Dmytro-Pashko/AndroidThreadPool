package com.goodvin1709.corgigallery.activity.pager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.goodvin1709.corgigallery.R;

public class PagerFragment extends Fragment {

    private static final String IMAGE_POSITION_KEY = "image_position";
    private int imagePosition;

    public static PagerFragment getInstance(int position) {
        PagerFragment fragment = new PagerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(IMAGE_POSITION_KEY, position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.pager_fragment, container, false);
        imagePosition = getImagePosition(savedInstanceState);
        ViewPager pagerView = (ViewPager) fragment.findViewById(R.id.image_fragment_pager);
        ImagePagerAdapter pagerAdapter = new ImagePagerAdapter(getFragmentManager());
        pagerView.setAdapter(pagerAdapter);
        pagerView.setCurrentItem(imagePosition);
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
        outState.putInt(IMAGE_POSITION_KEY, imagePosition);
        super.onSaveInstanceState(outState);
    }
}
