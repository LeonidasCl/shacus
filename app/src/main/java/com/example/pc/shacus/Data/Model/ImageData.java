package com.example.pc.shacus.Data.Model;

/**
 * Created by licl on 2017/2/4.
 */

public class ImageData {
    private String imageUrl;
    //宽高比
    private float aspectRatio;
    //是否被选中
    private boolean checked=false;
    //是否可选
    private  boolean checkable=false;
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
        float ar = (float)width / height;
        setAspectRatio(ar);
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

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isCheckable() {
        return checkable;
    }

    public void setCheckable(boolean checkable) {
        this.checkable = checkable;
    }
}