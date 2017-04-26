package com.example.pc.shacus.Data.Model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 李嘉文 on 2016/9/2.
 */

//数据模型：摄影师发布的约拍
public class PhotographerModel implements Serializable {

    private String Userimg;
    private String APtitle;
    private int APid;
//    private String APimgurl;
    private String APstartT;
    private int APlikeN;
    private String APregistN;
    private int Userliked;
    private String APtime;
    private UserModel userModel;
    private List<String> APimgurl;
    private String APcontent;
    private int APpricetype;
    private String APprice;
    private int APgroup;


    public String getAPtitle() {
        return APtitle;
    }

    public void setAPtitle(String APtitle) {
        this.APtitle = APtitle;
    }

    public int getAPid() {
        return APid;
    }

    public void setAPid(int APid) {
        this.APid = APid;
    }


    public String getAPstartT() {
        return APstartT;
    }

    public void setAPstartT(String APstartT) {
        this.APstartT = APstartT;
    }

    public int getAPlikeN() {
        return APlikeN;
    }

    public void setAPlikeN(int APlikeN) {
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

    public String getAPtime() {
        return APtime;
    }

    public void setAPtime(String APtime) {
        this.APtime = APtime;
    }


    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    public List<String> getAPimgurl() {
        return APimgurl;
    }

    public void setAPimgurl(List<String> APimgurl) {
        this.APimgurl = APimgurl;
    }

    public String getAPcontent() {
        return APcontent;
    }

    public void setAPcontent(String APcontent) {
        this.APcontent = APcontent;
    }

    public int getAPpricetype() {
        return APpricetype;
    }

    public void setAPpricetype(int APpricetype) {
        this.APpricetype = APpricetype;
    }

    public String getAPprice() {
        return APprice;
    }

    public void setAPprice(String APprice) {
        this.APprice = APprice;
    }

    public int getAPgroup() {
        return APgroup;
    }

    public void setAPgroup(int APgroup) {
        this.APgroup = APgroup;
    }
}
