package com.example.pc.shacus.Util;

public class UserInfoUtil {
	public static UserInfoUtil userInfoUtil;
	private String authKey =null;

	public String getAuthKey() {
		return authKey;
	}

	public void setAuthKey(String authKey) {
		this.authKey = authKey;
	}


	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
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

	public int getFollowNum() {
		return followNum;
	}

	public void setFollowNum(int followNum) {
		this.followNum = followNum;
	}


	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}


	private String   nickName, photoUrl, signature, userId="";
	private int age, fansNum,  followNum, sex;


	public static UserInfoUtil getInstance() {
		if (userInfoUtil == null) {
			userInfoUtil = new UserInfoUtil();
		}
		return userInfoUtil;
	}

}
