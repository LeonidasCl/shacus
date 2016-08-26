package com.example.pc.shacus.Data.Model;

import android.graphics.drawable.Drawable;

import java.util.Date;

/**
 *
 *
 */

public class HuoDongItemModel {

    private Drawable userHeader;
    private Drawable huodongMainpic;
    private String usrName;
    private String usrtoken;
    private Date starttime;
    private Date endtime;
    private Date setTime;
    private double price;
    private String location;
    private String describe;
    private int praiseNum;
    private int joinNum;

    public Drawable getUserHeader() {
        return userHeader;
    }

    public void setUserHeader(Drawable userHeader) {
        this.userHeader = userHeader;
    }

    public Drawable getHuodongMainpic() {
        return huodongMainpic;
    }

    public void setHuodongMainpic(Drawable huodongMainpic) {
        this.huodongMainpic = huodongMainpic;
    }

    public String getUsrName() {
        return usrName;
    }

    public void setUsrName(String usrName) {
        this.usrName = usrName;
    }

    public String getUsrtoken() {
        return usrtoken;
    }

    public void setUsrtoken(String usrtoken) {
        this.usrtoken = usrtoken;
    }

    public Date getStarttime() {
        return starttime;
    }

    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }

    public Date getEndtime() {
        return endtime;
    }

    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

    public Date getSetTime() {
        return setTime;
    }

    public void setSetTime(Date setTime) {
        this.setTime = setTime;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public int getPraiseNum() {
        return praiseNum;
    }

    public void setPraiseNum(int praiseNum) {
        this.praiseNum = praiseNum;
    }

    public int getJoinNum() {
        return joinNum;
    }

    public void setJoinNum(int joinNum) {
        this.joinNum = joinNum;
    }
}
