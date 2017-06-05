package com.goodvin1709.corgigallery.activity.pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.goodvin1709.corgigallery.model.Image;

import java.util.List;

class ImagePagerAdapter extends FragmentStatePagerAdapter {

    private List<Image> images;

    ImagePagerAdapter(FragmentManager fm, List<Image> images) {
        super(fm);
        this.images = images;
    }

    @Override
    public Fragment getItem(int position) {
        return ImageFragment.getInstance(position);
    }

    @Override
    public int getCount() {
        return images.size();
    }
}
