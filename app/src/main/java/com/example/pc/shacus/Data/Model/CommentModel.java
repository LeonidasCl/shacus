package com.example.pc.shacus.Data.Model;

/**
 * Created by cuicui on 2017/5/1.
 */
public class CommentModel {
    private String ucomment;
    private String uheadimage;
    private int uid;
    private String ualias;
    private float uscore;

    public float getUscore() {
        return uscore;
    }

    public void setUscore(float uscore) {
        this.uscore = uscore;
    }

    public String getUalias() {
        return ualias;
    }

    public void setUalias(String ualias) {
        this.ualias = ualias;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getUheadimage() {
        return uheadimage;
    }

    public void setUheadimage(String uheadimage) {
        this.uheadimage = uheadimage;
    }

    public String getUcomment() {
        return ucomment;
    }

    public void setUcomment(String ucomment) {
        this.ucomment = ucomment;
    }
}
