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
    private boolean UCusergender;
    private int UCid;
    private List<String> UserlikeList;
    private String UserHeadimg;
    private List<String> UCsimpleimg;

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

    public boolean isUCusergender() {
        return UCusergender;
    }

    public void setUCusergender(boolean UCusergender) {
        this.UCusergender = UCusergender;
    }

    public int getUCid() {
        return UCid;
    }

    public void setUCid(int UCid) {
        this.UCid = UCid;
    }

    public List<String> getUserlikeList() {
        return UserlikeList;
    }

    public void setUserlikeList(List<String> userlikeList) {
        UserlikeList = userlikeList;
    }

    public String getUserHeadimg() {
        return UserHeadimg;
    }

    public void setUserHeadimg(String userHeadimg) {
        UserHeadimg = userHeadimg;
    }

    public List<String> getUCsimpleimg() {
        return UCsimpleimg;
    }

    public void setUCsimpleimg(List<String> UCsimpleimg) {
        this.UCsimpleimg = UCsimpleimg;
    }
}
