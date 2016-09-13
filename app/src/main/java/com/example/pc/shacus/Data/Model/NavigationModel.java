package com.example.pc.shacus.Data.Model;

import java.io.Serializable;

/**
 * Created by 李嘉文 on 2016/9/3.
 */
public class NavigationModel implements Serializable {

    private String weburl;
    private String imgurl;

    public String getWeburl() {
        return weburl;
    }

    public void setWeburl(String weburl) {
        this.weburl = weburl;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }
}
