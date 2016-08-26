package com.example.pc.shacus.Data.Model;

import android.graphics.drawable.Drawable;

/**
 * Created by Mercer on 2016/7/12.
 */
public class RankItemModel {

    private Drawable userIamgeSrc;
    private int rank;
    private String userNameText;
    private String userAddressText;
    private Drawable mainPicture;
    private Drawable praisePicture;
    private Drawable comentPicture;
    private int favorNum;
    private int commentNum;


    public Drawable getUserIamgeSrc() {
        return userIamgeSrc;
    }

    public void setUserIamgeSrc(Drawable userIamgeSrc) {
        this.userIamgeSrc = userIamgeSrc;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getUserNameText() {
        return userNameText;
    }

    public void setUserNameText(String userNameText) {
        this.userNameText = userNameText;
    }

    public String getUserAddressText() {
        return userAddressText;
    }

    public void setUserAddressText(String userAddressText) {
        this.userAddressText = userAddressText;
    }

    public Drawable getMainPicture() {
        return mainPicture;
    }

    public void setMainPicture(Drawable mainPicture) {
        this.mainPicture = mainPicture;
    }

    public int getFavorNum() {
        return favorNum;
    }

    public void setFavorNum(int favorNum) {
        this.favorNum = favorNum;
    }

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }


    public Drawable getPraisePicture() {
        return praisePicture;
    }

    public void setPraisePicture(Drawable praisePicture) {
        this.praisePicture = praisePicture;
    }

    public Drawable getComentPicture() {
        return comentPicture;
    }

    public void setComentPicture(Drawable comentPicture) {
        this.comentPicture = comentPicture;
    }
}
