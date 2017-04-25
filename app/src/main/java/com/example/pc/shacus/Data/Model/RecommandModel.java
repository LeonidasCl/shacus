package com.example.pc.shacus.Data.Model;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by fengchi on 2017/4/25.
 */

public class RecommandModel implements Serializable{
    private ArrayList<String> Ucimg;
    private String Fisrtimg;
    private String Uheadimg;
    private UserModel recommandUser;

    public void setUcimg(ArrayList<String> ucimg) {
        Ucimg = ucimg;
    }

    public void setFisrtimg(String fisrtimg) {
        Fisrtimg = fisrtimg;
    }

    public void setUheadimg(String uheadimg) {
        Uheadimg = uheadimg;
    }

    public void setRecommandUser(UserModel recommandUser) {
        this.recommandUser = recommandUser;
    }

    public ArrayList<String> getUcimg() {
        return Ucimg;
    }

    public String getFisrtimg() {
        return Fisrtimg;
    }

    public String getUheadimg() {
        return Uheadimg;
    }

    public UserModel getRecommandUser() {
        return recommandUser;
    }
}
