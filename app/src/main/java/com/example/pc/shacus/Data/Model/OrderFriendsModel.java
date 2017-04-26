package com.example.pc.shacus.Data.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/12.
 */

public class OrderFriendsModel implements Serializable {
    private String CompanionTitle;
    private String CompanionContent;
    private String CompanionId;
    private String CompanionUrl;
    private ArrayList CompanionPic;

    public String getCompanionTitle() {
        return CompanionTitle;
    }
    public void setCompanionTitle(String OFTitle) {
        this.CompanionTitle = OFTitle;
    }

    public String getCompanionContent() {
        return CompanionContent;
    }
    public void setCompanionContent(String OFContent) {
        this.CompanionContent = OFContent;
    }

    public String getCompanionId(){ return CompanionId;}
    public void setCompanionId(String OFId){ this.CompanionId = OFId; }

    public String getCompanionUrl(){ return  CompanionUrl;}
    public void setCompanionUrl(String url){ this.CompanionUrl = url; }

    public ArrayList getCompanionPic() {
        return CompanionPic;
    }
    public void setCompanionPic(ArrayList PicList) {
        this.CompanionPic = PicList;
    }

}
