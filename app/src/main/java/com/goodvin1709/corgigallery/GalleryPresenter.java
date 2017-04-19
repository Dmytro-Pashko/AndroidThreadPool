package com.goodvin1709.corgigallery;

import android.os.Handler;

import java.util.List;

public interface GalleryPresenter {

    void attachHandler(Handler handler);

    List<Image> getImages();

    void loadBitmap(Image image, int imageSize);


}
