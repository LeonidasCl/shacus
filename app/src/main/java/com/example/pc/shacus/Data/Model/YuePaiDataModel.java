package com.example.pc.shacus.Data.Model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by pc on 2016/7/13.
 */
public class YuePaiDataModel implements Serializable {

    private String APtitle;
    private String APlocation;
    private String APendT;
    private int APid;
    private int APstatus;
    private int AP_issponsor;
    private String APcontent;
    private int APlikeN;
    private int APfree;
    private int APtype;
    private String APjoinT;
    private int APvalid;
    private int APsponsorid;
    private String Userimg;
    private int APprice;
    private int APtag;
    private List<String> APimgurl;
    private String APcreateT;
    private String APstartT;
    private int APregistN;
    private int APclosed;
    private int APaddallowed;

    public String getAPtitle() {
        return APtitle;
    }

    public void setAPtitle(String APtitle) {
        this.APtitle = APtitle;
    }

    public String getAPlocation() {
        return APlocation;
    }

    public void setAPlocation(String APlocation) {
        this.APlocation = APlocation;
    }

    public String getAPendT() {
        return APendT;
    }

    public void setAPendT(String APendT) {
        this.APendT = APendT;
    }


    public String getAPcontent() {
        return APcontent;
    }

    public void setAPcontent(String APcontent) {
        this.APcontent = APcontent;
    }


    public String getAPjoinT() {
        return APjoinT;
    }

    public void setAPjoinT(String APjoinT) {
        this.APjoinT = APjoinT;
    }



    public String getUserimg() {
        return Userimg;
    }

    public void setUserimg(String userimg) {
        Userimg = userimg;
    }


    public List<String> getAPimgurl() {
        return APimgurl;
    }

    public void setAPimgurl(List<String> APimgurl) {
        this.APimgurl = APimgurl;
    }

    public String getAPcreateT() {
        return APcreateT;
    }

    public void setAPcreateT(String APcreateT) {
        this.APcreateT = APcreateT;
    }

    public String getAPstartT() {
        return APstartT;
    }

    public void setAPstartT(String APstartT) {
        this.APstartT = APstartT;
    }

    public int getAPid() {
        return APid;
    }

    public void setAPid(int APid) {
        this.APid = APid;
    }

    public int getAPlikeN() {
        return APlikeN;
    }

    public void setAPlikeN(int APlikeN) {
        this.APlikeN = APlikeN;
    }

    public int getAPfree() {
        return APfree;
    }

    public void setAPfree(int APfree) {
        this.APfree = APfree;
    }

    public int getAPtype() {
        return APtype;
    }

    public void setAPtype(int APtype) {
        this.APtype = APtype;
    }

    public int getAPvalid() {
        return APvalid;
    }

    public void setAPvalid(int APvalid) {
        this.APvalid = APvalid;
    }

    public int getAPsponsorid() {
        return APsponsorid;
    }

    public void setAPsponsorid(int APsponsorid) {
        this.APsponsorid = APsponsorid;
    }

    public int getAPprice() {
        return APprice;
    }

    public void setAPprice(int APprice) {
        this.APprice = APprice;
    }

    public int getAPtag() {
        return APtag;
    }

    public void setAPtag(int APtag) {
        this.APtag = APtag;
    }

    public int getAPregistN() {
        return APregistN;
    }

    public void setAPregistN(int APregistN) {
        this.APregistN = APregistN;
    }

    public int getAPclosed() {
        return APclosed;
    }

    public void setAPclosed(int APclosed) {
        this.APclosed = APclosed;
    }

    public int getAPaddallowed() {
        return APaddallowed;
    }

    public void setAPaddallowed(int APaddallowed) {
        this.APaddallowed = APaddallowed;
    }

    public int getAPstatus() {
        return APstatus;
    }

    public void setAPstatus(int APstatus) {
        this.APstatus = APstatus;
    }

    public int getAP_issponsor() {
        return AP_issponsor;
    }

    public void setAP_issponsor(int AP_issponsor) {
        this.AP_issponsor = AP_issponsor;
    }
}
