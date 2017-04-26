package com.example.pc.shacus.Data.Model;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by fengchi on 2017/4/25.
 */

public class RecommandModel implements Serializable{
    private ArrayList<String> Ucimg;
    private String UcFirstimg;
    private String headimg;
    private UserModel userpublish;

    public void setUcimg(ArrayList<String> ucimg) {
        Ucimg = ucimg;
    }

    public void setUserpublish(UserModel userpublish) {
        this.userpublish = userpublish;
    }

    public UserModel getUserpublish() {

        return userpublish;
    }

    public ArrayList<String> getUcimg() {

        return Ucimg;
    }

    public void setUcFirstimg(String ucFirstimg) {
        UcFirstimg = ucFirstimg;
    }

    public String getUcFirstimg() {

        return UcFirstimg;
    }

    public void setHeadimg(String headimg) {
        this.headimg = headimg;
    }

    public String getHeadimg() {

        return headimg;
    }
}
