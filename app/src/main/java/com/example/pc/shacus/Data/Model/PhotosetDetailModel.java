package com.example.pc.shacus.Data.Model;

import java.util.ArrayList;

/**
 * Created by licl on 2017/2/12.
 */

public class PhotosetDetailModel {

    private String UCcreateT;
    private String UCcontent;
    private int UCid;
    private int UCuser;
    private String UCtitle;
    private ArrayList<String> UCimg;
    private ArrayList<ImageData> UCsimpleimg;


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

    public ArrayList<ImageData> getUCsimpleimg() {
        return UCsimpleimg;
    }

    public void setUCsimpleimg(ArrayList<ImageData> UCimg) {
        this.UCsimpleimg = UCimg;
    }

    public String getTitle() {
        return UCtitle;
    }

    public void setTitle(String title) {
        this.UCtitle = title;
    }

    public String getUCcontent() {
        return UCcontent;
    }

    public void setUCcontent(String UCcontent) {
        this.UCcontent = UCcontent;
    }

    public int getUCuser() {
        return UCuser;
    }

    public void setUCuser(int UCuser) {
        this.UCuser = UCuser;
    }

    public ArrayList<String> getUCimg() {
        return UCimg;
    }

    public void setUCimg(ArrayList<String> UCimg) {
        this.UCimg = UCimg;
    }
}
