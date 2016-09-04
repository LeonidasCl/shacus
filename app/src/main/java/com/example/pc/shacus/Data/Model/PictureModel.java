package com.example.pc.shacus.Data.Model;


import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * Created by pc on 2016/3/7.
 */
public class PictureModel  implements Serializable {

    private String authKey = null;
    private String errorCode;
    private Map map=new HashMap();

    public String getAuthKey() {
        return authKey;
    }

    public void setAuthKey(String authKey) {
        this.authKey = authKey;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getPicUrl(String i){
        String url= (String) map.get(i);
        return url;
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }
}


