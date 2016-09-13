package com.example.pc.shacus.Data.Model;

import com.example.pc.shacus.Network.NetworkCallbackInterface;

import org.json.JSONException;

import java.io.IOException;
import java.io.Serializable;

/**
 * Created by 李前 on 2016/9/2.
 */
public class SettingDataModel implements Serializable{
    private String userPhone;
    private boolean phoneVisible;
    private boolean messageInform;
    private String userID;

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public boolean isPhoneVisible() {
        return phoneVisible;
    }

    public void setPhoneVisible(boolean phoneVisible) {
        this.phoneVisible = phoneVisible;
    }

    public boolean isMessageInform() {
        return messageInform;
    }

    public void setMessageInform(boolean messageInform) {
        this.messageInform = messageInform;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

}
