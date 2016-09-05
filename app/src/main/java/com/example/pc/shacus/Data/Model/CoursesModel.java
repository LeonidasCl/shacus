package com.example.pc.shacus.Data.Model;

import java.io.Serializable;

/**
 * Created by 启凡 on 2016/9/5.
 */
public class CoursesModel  implements Serializable {
    private String image;

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

    private int readNum;
    private String title;

    public String getCoureseurl() {
        return coureseurl;
    }

    public void setCoureseurl(String coureseurl) {
        this.coureseurl = coureseurl;
    }

    private String coureseurl;


}
