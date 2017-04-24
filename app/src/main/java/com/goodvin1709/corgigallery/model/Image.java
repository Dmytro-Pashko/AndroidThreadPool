package com.goodvin1709.corgigallery.model;

public class Image {

    private final String url;
    private boolean isBroken;

    public Image(String url) {
        this.url = url;
    }

    public void setBroken(boolean broken) {
        isBroken = broken;
    }

    public boolean isBroken() {
        return isBroken;
    }

    public String getUrl() {
        return url;
    }
}
