package com.example.pc.shacus.Data.Model;

/**
 * Created by licl on 2017/2/4.
 */

public class ImageData {
    private String imageUrl;
    private float aspectRatio;
    private int width;
    private int height;

    public ImageData(String imageUrl, float aspectRatio) {
        setImageUrl(imageUrl);
        setAspectRatio(aspectRatio);
    }

    public ImageData(String imageUrl, int width, int height) {
        setImageUrl(imageUrl);
        this.width = width;
        this.height = height;
        float ar = (float)width / height;
        setAspectRatio(ar);
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public float getAspectRatio() {
        return aspectRatio;
    }

    public void setAspectRatio(float aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

}