package com.goodvin1709.corgigallery.model;

import java.util.Locale;

public class Image {

    private final String url;
    private ImageStatus status;

    public Image(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public ImageStatus getStatus() {
        return status;
    }

    public void setStatus(ImageStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format(Locale.ENGLISH, "Image [%s]", url);
    }
}
