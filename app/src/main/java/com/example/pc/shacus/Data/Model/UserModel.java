package com.example.pc.shacus.Data.Model;

/**
 * Created by pc on 2016/3/7.
 */
public class UserModel extends BaseDataModel {

    private String classType, phone, id, nickName, avatar, recentLook,
            signature, userId;
    private int age, fansNum, favoriteNum, followNum, themeNum,
            isMessageReceive, loginDays, sex;
    public int getThemeNum() {
        return themeNum;
    }

    public void setThemeNum(int themeNum) {
        this.themeNum = themeNum;
    }

    private Float latitude, longitude;
    private long lastLoginTime, registDate;

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getRecentLook() {
        return recentLook;
    }

    public void setRecentLook(String recentLook) {
        this.recentLook = recentLook;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getFansNum() {
        return fansNum;
    }

    public void setFansNum(int fansNum) {
        this.fansNum = fansNum;
    }

    public int getFavoriteNum() {
        return favoriteNum;
    }

    public void setFavoriteNum(int favoriteNum) {
        this.favoriteNum = favoriteNum;
    }

    public int getFollowNum() {
        return followNum;
    }

    public void setFollowNum(int followNum) {
        this.followNum = followNum;
    }


    public int getIsMessageReceive() {
        return isMessageReceive;
    }

    public void setIsMessageReceive(int isMessageReceive) {
        this.isMessageReceive = isMessageReceive;
    }

    public int getLoginDays() {
        return loginDays;
    }

    public void setLoginDays(int loginDays) {
        this.loginDays = loginDays;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }



    public long getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(long lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public long getRegistDate() {
        return registDate;
    }

    public void setRegistDate(long registDate) {
        this.registDate = registDate;
    }
}
