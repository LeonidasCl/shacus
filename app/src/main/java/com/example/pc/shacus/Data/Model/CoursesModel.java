package com.example.pc.shacus.Data.Model;

import java.io.Serializable;

/**
 * Created by 启凡 on 2016/9/5.
 */
public class CoursesModel  implements Serializable {

    private int kind;
    private int itemid;
    private int see;
    private String image;
    private int readNum;
    private String title;
    private int likeNum;
    private int collet;
    private int valid;
    //private String courseurl;

    public int getKind() {
        return kind;
    }

    public void setKind(int kind) {
        this.kind = kind;
    }
    public int getValid() {
        return valid;
    }

    public void setValid(int valid) {
        this.valid = valid;
    }



    public int getSee() {
        return see;
    }

    public void setSee(int see) {
        this.see = see;
    }

    public int getCollet() {
        return collet;
    }

    public void setCollet(int collet) {
        this.collet = collet;
    }


    public int getReadNum() {
        return readNum;
    }

    public void setReadNum(int readNum) {
        this.readNum = readNum;
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


    public int getItemid() {
        return itemid;
    }

    public void setItemid(int itemid) {
        this.itemid = itemid;
    }

    public int getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(int likeNum) {
        this.likeNum = likeNum;
    }

//    public String getCourseurl() {
//        return courseurl;
//    }
//
//    public void setCourseurl(String courseurl) {
//        this.courseurl = courseurl;
//    }


}
