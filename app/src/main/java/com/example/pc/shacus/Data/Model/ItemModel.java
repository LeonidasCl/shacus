package com.example.pc.shacus.Data.Model;

/**
 * Created by cuicui on 2016/9/3.
 */
public class ItemModel {

    private int id;
    private String image;
    private String startTime;
    private int likeNum;
    private String userImage;
    private int registNum;
    private String title;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public int getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(int likeNum) {
        this.likeNum = likeNum;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public int getRegistNum() {
        return registNum;
    }

    public void setRegistNum(int registNum) {
        this.registNum = registNum;
    }


}
