package com.example.pc.shacus.Data.Model;

/**
 * Created by licl on 2017/2/10.
 */

public class PhotosetModel {
    private String UCcreateT;
    private int UCid;
    private String title;
    private ImageData UCimg;

    public String getUCcreateT() {
        return UCcreateT;
    }

    public void setUCcreateT(String UCcreateT) {
        this.UCcreateT = UCcreateT;
    }

    public int getUCid() {
        return UCid;
    }

    public void setUCid(int UCid) {
        this.UCid = UCid;
    }

    public ImageData getUCimg() {
        return UCimg;
    }

    public void setUCimg(ImageData UCimg) {
        this.UCimg = UCimg;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
