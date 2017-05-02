package com.example.pc.shacus.Data.Model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 李嘉文 on 2016/7/13.
 */
public class YuePaiDataModel implements Serializable {

    private String APtitle;
    private String ACtitle;
    private String APlocation;
    private String AClocation;
    private String APendT;
    private int APid;
    private int ACid;
    private int Userliked;
    private int AP_issponsor;//自己是否为发起者  0为不是  1为是
    private int AC_issponsor;//自己是否为发起者  0为不是  1为是
    private int APstatus;//0是报名中 1是选择完成 2是结束了但是两方都没有评价 3是结束了一方给出了评价 4是两方都评价了
    private int ACstatus;//0为报名中 1是进行中 2为完成
    private String APcontent;
    private int APlikeN;
    private int AClikenumber;
    private int APfree;
    private int ACfree;
    private int APtype;
    private String APjoinT;
    private String ACjoinT;
    private int APvalid;
    private int ACvalid;
    private int APsponsorid;//发起者id
    private int ACsponsorid;//发起者id
    private List<UserModel> APregisters;
    private List<UserModel> ACregister;
    private List<CommentModel> comment;
    private String ACprice;
    private String ACtag;
    private List<String> APimgurl;
    private List<String> ACimageurl;
    private String APcreateT;
    private String ACendT;
    private String ACcreateT;
    private int ACminp;
    private int ACmaxp;
    private String APstartT;
    private String ACstartT;
    private int APregistN;
    private int APclosed;
    private int APaddallowed;
    private int ACscore;
    private int ACclosed;
    private String ACcontent;
    private String image;
    private String APtime;
    private String APprice;
    private int APtag;
    private int APgroup;
    private int APpricetag;
    private String Useralais;
    private String Userlocation;
    private String Userimg;
    private String Userage;
    private String Usex;
    private int Useregistd;
    private int Usercommented; //0是没有评价，1是已经评价

    public YuePaiDataModel() {
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    private int rank;

    public String getImagecard() {
        return imagecard;
    }

    public void setImagecard(String imagecard) {
        this.imagecard = imagecard;
    }

    private String imagecard;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

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



    public List<UserModel> getAPregisters() {
        return APregisters;
    }

    public void setAPregisters(List<UserModel> APregisters) {
        this.APregisters = APregisters;
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

    public String getAPprice() {
        return APprice;
    }

    public void setAPprice(String APprice) {
        this.APprice = APprice;
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

    public int getACid() {
        return ACid;
    }

    public void setACid(int ACid) {
        this.ACid = ACid;
    }

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

    public String getACstartT() {
        return ACstartT;
    }

    public void setACstartT(String ACstartT) {
        this.ACstartT = ACstartT;
    }

    public String getACprice() {
        return ACprice;
    }

    public void setACprice(String ACprice) {
        this.ACprice = ACprice;
    }

    public int getAClikenumber() {
        return AClikenumber;
    }

    public void setAClikenumber(int AClikenumber) {
        this.AClikenumber = AClikenumber;
    }

    public int getACfree() {
        return ACfree;
    }

    public void setACfree(int ACfree) {
        this.ACfree = ACfree;
    }

    public int getACstatus() {
        return ACstatus;
    }

    public void setACstatus(int ACstatus) {
        this.ACstatus = ACstatus;
    }

    public String getACtag() {
        return ACtag;
    }

    public void setACtag(String ACtag) {
        this.ACtag = ACtag;
    }

    public int getACvalid() {
        return ACvalid;
    }

    public void setACvalid(int ACvalid) {
        this.ACvalid = ACvalid;
    }

    public String getACcreateT() {
        return ACcreateT;
    }

    public void setACcreateT(String ACcreateT) {
        this.ACcreateT = ACcreateT;
    }

    public int getACminp() {
        return ACminp;
    }

    public void setACminp(int ACminp) {
        this.ACminp = ACminp;
    }

    public List<String> getACimageurl() {
        return ACimageurl;
    }

    public void setACimageurl(List<String> ACimageurl) {
        this.ACimageurl = ACimageurl;
    }

    public String getACendT() {
        return ACendT;
    }

    public void setACendT(String ACendT) {
        this.ACendT = ACendT;
    }

    public int getACmaxp() {
        return ACmaxp;
    }

    public void setACmaxp(int ACmaxp) {
        this.ACmaxp = ACmaxp;
    }

    public List<UserModel> getACregister() {
        return ACregister;
    }

    public void setACregister(List<UserModel> ACregister) {
        this.ACregister = ACregister;
    }

    public int getACscore() {
        return ACscore;
    }

    public void setACscore(int ACscore) {
        this.ACscore = ACscore;
    }

    public int getACsponsorid() {
        return ACsponsorid;
    }

    public void setACsponsorid(int ACsponsorid) {
        this.ACsponsorid = ACsponsorid;
    }

    public String getACjoinT() {
        return ACjoinT;
    }

    public void setACjoinT(String ACjoinT) {
        this.ACjoinT = ACjoinT;
    }

    public int getACclosed() {
        return ACclosed;
    }

    public void setACclosed(int ACclosed) {
        this.ACclosed = ACclosed;
    }

    public String getACcontent() {
        return ACcontent;
    }

    public void setACcontent(String ACcontent) {
        this.ACcontent = ACcontent;
    }

    public int getAC_issponsor() {
        return AC_issponsor;
    }

    public void setAC_issponsor(int AC_issponsor) {
        this.AC_issponsor = AC_issponsor;
    }

    public int getUserliked() {
        return Userliked;
    }

    public void setUserliked(int userliked) {
        Userliked = userliked;
    }

    @Override
    public String toString() {
        return getRank()+"";
    }

    public String getAPtime() {
        return APtime;
    }

    public void setAPtime(String APtime) {
        this.APtime = APtime;
    }

    public int getAPgroup() {
        return APgroup;
    }

    public void setAPgroup(int APgroup) {
        this.APgroup = APgroup;
    }

    public int getAPtag() {
        return APtag;
    }

    public void setAPtag(int APtag) {
        this.APtag = APtag;
    }

    public int getAPpricetag() {
        return APpricetag;
    }

    public void setAPpricetag(int APpricetag) {
        this.APpricetag = APpricetag;
    }

    public String getUseralais() {
        return Useralais;
    }

    public void setUseralais(String useralais) {
        Useralais = useralais;
    }

    public String getUserlocation() {
        return Userlocation;
    }

    public void setUserlocation(String userlocation) {
        Userlocation = userlocation;
    }

    public String getUserimg() {
        return Userimg;
    }

    public void setUserimg(String userimg) {
        Userimg = userimg;
    }

    public String getUserage() {
        return Userage;
    }

    public void setUserage(String userage) {
        Userage = userage;
    }

    public String getUsex() {
        return Usex;
    }

    public void setUsex(String usex) {
        Usex = usex;
    }

    public int getUseregistd() {
        return Useregistd;
    }

    public void setUseregistd(int useregistd) {
        Useregistd = useregistd;
    }

    public List<CommentModel> getComment() {
        return comment;
    }

    public void setComment(List<CommentModel> comment) {
        this.comment = comment;
    }

    public int getUsercommented() {
        return Usercommented;
    }

    public void setUsercommented(int usercommented) {
        Usercommented = usercommented;
    }
}
