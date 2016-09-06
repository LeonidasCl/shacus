package com.example.pc.shacus.Data.Model;

import java.util.List;

/**
 * Created by pc on 2016/9/5.
 */
public class HuoDongDataModel {

    private String ACtitle;
    private String AClocation;
    private String ACendT;
    private int ACid;
    private int AC_issponsor;//是否为参与者0为不是 1为是
    private int ACstatus;//1是报名中 2是选择完成 3是已结束
    private String ACcontent;
    private int AClikeN;
    private int ACfree;
    private int ACtype;
    private String ACjoinT;
    private int ACvalid;
    private int ACsponsorid;//发起者id
    private List<UserModel> Userimg;
    private int ACprice;
    private int ACtag;
    private List<String> ACimgurl;
    private String ACcreateT;
    private String ACstartT;
    private int ACregistN;
    private int ACclosed;
    private int ACaddallowed;

    public String getACtitle() {
        return ACtitle;
    }

    public void setACtitle(String ACtitle) {
        this.ACtitle = ACtitle;
    }

    public String getAClocation() {
        return AClocation;
    }

    public void setAClocation(String AClocation) {
        this.AClocation = AClocation;
    }

    public String getACendT() {
        return ACendT;
    }

    public void setACendT(String ACendT) {
        this.ACendT = ACendT;
    }


    public String getACcontent() {
        return ACcontent;
    }

    public void setACcontent(String ACcontent) {
        this.ACcontent = ACcontent;
    }


    public String getACjoinT() {
        return ACjoinT;
    }

    public void setACjoinT(String ACjoinT) {
        this.ACjoinT = ACjoinT;
    }



    public List<UserModel> getUserimg() {
        return Userimg;
    }

    public void setUserimg(List<UserModel> userimg) {
        Userimg = userimg;
    }


    public List<String> getACimgurl() {
        return ACimgurl;
    }

    public void setACimgurl(List<String> ACimgurl) {
        this.ACimgurl = ACimgurl;
    }

    public String getACcreateT() {
        return ACcreateT;
    }

    public void setACcreateT(String ACcreateT) {
        this.ACcreateT = ACcreateT;
    }

    public String getACstartT() {
        return ACstartT;
    }

    public void setACstartT(String ACstartT) {
        this.ACstartT = ACstartT;
    }

    public int getACid() {
        return ACid;
    }

    public void setACid(int ACid) {
        this.ACid = ACid;
    }

    public int getAClikeN() {
        return AClikeN;
    }

    public void setAClikeN(int AClikeN) {
        this.AClikeN = AClikeN;
    }

    public int getACfree() {
        return ACfree;
    }

    public void setACfree(int ACfree) {
        this.ACfree = ACfree;
    }

    public int getACtype() {
        return ACtype;
    }

    public void setACtype(int ACtype) {
        this.ACtype = ACtype;
    }

    public int getACvalid() {
        return ACvalid;
    }

    public void setACvalid(int ACvalid) {
        this.ACvalid = ACvalid;
    }

    public int getACsponsorid() {
        return ACsponsorid;
    }

    public void setACsponsorid(int ACsponsorid) {
        this.ACsponsorid = ACsponsorid;
    }

    public int getACprice() {
        return ACprice;
    }

    public void setACprice(int ACprice) {
        this.ACprice = ACprice;
    }

    public int getACtag() {
        return ACtag;
    }

    public void setACtag(int ACtag) {
        this.ACtag = ACtag;
    }

    public int getACregistN() {
        return ACregistN;
    }

    public void setACregistN(int ACregistN) {
        this.ACregistN = ACregistN;
    }

    public int getACclosed() {
        return ACclosed;
    }

    public void setACclosed(int ACclosed) {
        this.ACclosed = ACclosed;
    }

    public int getACaddallowed() {
        return ACaddallowed;
    }

    public void setACaddallowed(int ACaddallowed) {
        this.ACaddallowed = ACaddallowed;
    }

    public int getACstatus() {
        return ACstatus;
    }

    public void setACstatus(int ACstatus) {
        this.ACstatus = ACstatus;
    }

    public int getAC_issponsor() {
        return AC_issponsor;
    }

    public void setAC_issponsor(int AC_issponsor) {
        this.AC_issponsor = AC_issponsor;
    }
    
}
