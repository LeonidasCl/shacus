package com.example.pc.shacus.Data.Model;

import java.io.Serializable;
import java.security.PrivateKey;
import java.util.List;

/**
 * Created by licl on 2017/3/24.
 */
public class PhotosetItemModel implements Serializable{

    private String UCtitle;
    private String UCcontent;
    private int UCpicnum;
    private String UCusername;
    private int UCuser;
    private int UCusergender;
    private int UCid;
    private String UserIsFriend;
    private String UserlikeNum;
    private List<UserModel> UserlikeList;
    private UserModel UserPublish;
    private List<ImageData> UCsimpleimg;
    private String UCcreateT;

    public String getUCtitle() {
        return UCtitle;
    }

    public void setUCtitle(String UCtitle) {
        this.UCtitle = UCtitle;
    }

    public String getUCcontent() {
        return UCcontent;
    }

    public void setUCcontent(String UCcontent) {
        this.UCcontent = UCcontent;
    }

    public int getUCpicnum() {
        return UCpicnum;
    }

    public void setUCpicnum(int UCpicnum) {
        this.UCpicnum = UCpicnum;
    }

    public String getUCusername() {
        return UCusername;
    }

    public void setUCusername(String UCusername) {
        this.UCusername = UCusername;
    }

    public int getUCuser() {
        return UCuser;
    }

    public void setUCuser(int UCuser) {
        this.UCuser = UCuser;
    }

    public int getUCid() {
        return UCid;
    }

    public void setUCid(int UCid) {
        this.UCid = UCid;
    }

    public List<UserModel> getUserlikeList() {
        return UserlikeList;
    }

    public void setUserlikeList(List<UserModel> userlikeList) {
        UserlikeList = userlikeList;
    }

    public UserModel getUserHeadimg() {
        return UserPublish;
    }

    public void setUserHeadimg(UserModel userHeadimg) {
        UserPublish = userHeadimg;
    }

    public List<ImageData> getUCsimpleimg() {
        return UCsimpleimg;
    }

    public void setUCsimpleimg(List<ImageData> UCsimpleimg) {
        this.UCsimpleimg = UCsimpleimg;
    }

    public int getUCusergender() {
        return UCusergender;
    }

    public void setUCusergender(int UCusergender) {
        this.UCusergender = UCusergender;
    }

    public String getUserIsFriend() {
        return UserIsFriend;
    }

    public void setUserIsFriend(String userIsFriend) {
        UserIsFriend = userIsFriend;
    }

    public String getUserlikeNum() {
        return UserlikeNum;
    }

    public void setUserlikeNum(String userlikeNum) {
        UserlikeNum = userlikeNum;
    }

    public String getUCcreateT() {
        return UCcreateT;
    }

    public void setUCcreateT(String UCcreateT) {
        this.UCcreateT = UCcreateT;
    }
}
