package com.example.pc.shacus.Data.Model;

import java.io.Serializable;

/**
 *
 *
 */

public class HuoDongItemModel implements Serializable{

    private String Userimageurl;
    private String AClurl;
    private String ACstartT;
    private String ACcontent;
    private int AClikenumber;
    private int ACregistN;
    private int ACid;
    private int Userliked;



    public String getACcontent(){
        return ACcontent;
    }

    public void setACcontent(String ACcontent) {
        this.ACcontent = ACcontent;
    }

    public int getAClikenumber() {
        return AClikenumber;
    }

    public void setAClikenumber(int AClikenumber) {
        this.AClikenumber = AClikenumber;
    }

    public int getACregistN() {
        return ACregistN;
    }

    public void setACregistN(int ACregistN) {
        this.ACregistN = ACregistN;
    }

    public String getACstartT() {
        return ACstartT;
    }

    public void setACstartT(String ACstartT) {
        this.ACstartT = ACstartT;
    }

    public String getUserimageurl() {
        return Userimageurl;
    }

    public void setUserimageurl(String userimageurl) {
        this.Userimageurl = userimageurl;
    }

    public String getAClurl() {
        return AClurl;
    }

    public void setAClurl(String AClurl) {
        this.AClurl = AClurl;
    }

    public int getACid() {
        return ACid;
    }

    public void setACid(int ACid) {
        this.ACid = ACid;
    }

    public int getUserliked() {
        return Userliked;
    }

    public void setUserliked(int userliked) {
        Userliked = userliked;
    }
}
