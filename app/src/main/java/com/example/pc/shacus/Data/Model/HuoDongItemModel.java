package com.example.pc.shacus.Data.Model;

import android.graphics.drawable.Drawable;

import java.io.Serializable;
import java.util.Date;

/**
 *
 *
 */

public class HuoDongItemModel implements Serializable{

    private String userHeader;
    private String huodongMainpic;
    private String usrName;
    private String usrtoken;
    private String setTime;
    private String describe;
    private int praiseNum;
    private int joinNum;


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

    public String getSetTime() {
        return setTime;
    }

    public void setSetTime(String setTime) {
        this.setTime = setTime;
    }

    public String getUserHeader() {
        return userHeader;
    }

    public void setUserHeader(String userHeader) {
        this.userHeader = userHeader;
    }

    public String getHuodongMainpic() {
        return huodongMainpic;
    }

    public void setHuodongMainpic(String huodongMainpic) {
        this.huodongMainpic = huodongMainpic;
    }
}
