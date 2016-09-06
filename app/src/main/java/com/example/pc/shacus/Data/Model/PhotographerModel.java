package com.example.pc.shacus.Data.Model;

import java.io.Serializable;

/**
 * Created by pc on 2016/9/2.
 */

//数据模型：摄影师发布的约拍
public class PhotographerModel implements Serializable {

    private String Userimg;
    private String APtitle;
    private String APid;
    private String APimgurl;
    private String APstartT;
    private String APlikeN;
    private String APregistN;
    private int Userliked;


    public String getAPtitle() {
        return APtitle;
    }

    public void setAPtitle(String APtitle) {
        this.APtitle = APtitle;
    }

    public String getAPid() {
        return APid;
    }

    public void setAPid(String APid) {
        this.APid = APid;
    }

    public String getAPimgurl() {
        return APimgurl;
    }

    public void setAPimgurl(String APimgurl) {
        this.APimgurl = APimgurl;
    }

    public String getAPstartT() {
        return APstartT;
    }

    public void setAPstartT(String APstartT) {
        this.APstartT = APstartT;
    }

    public String getAPlikeN() {
        return APlikeN;
    }

    public void setAPlikeN(String APlikeN) {
        this.APlikeN = APlikeN;
    }

    public String getAPregistN() {
        return APregistN;
    }

    public void setAPregistN(String APregistN) {
        this.APregistN = APregistN;
    }

    public String getUserimg() {
        return Userimg;
    }

    public void setUserimg(String userimg) {
        Userimg = userimg;
    }

    public int getUserliked() {
        return Userliked;
    }

    public void setUserliked(int userliked) {
        Userliked = userliked;
    }
}
