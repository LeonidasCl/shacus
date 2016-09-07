package com.example.pc.shacus.Data.Model;

import java.io.Serializable;

/**
 * Created by 启凡 on 2016/9/5.
 */
public class CoursesModel  implements Serializable {
    private String image;
    private int readNum;
    private String title;
    private Boolean collet;
    //private String courseurl;

    public Boolean getCollet() {
        return collet;
    }

    public void setCollet(Boolean collet) {
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



//    public String getCourseurl() {
//        return courseurl;
//    }
//
//    public void setCourseurl(String courseurl) {
//        this.courseurl = courseurl;
//    }


}
