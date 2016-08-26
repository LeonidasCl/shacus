package com.example.pc.shacus.Data.Model;

public class LoginDataModel {
	private String authKey = null;
    private String content;
	private UserModel user;
	private String errorCode;

	public String getAuthKey() {
		return authKey;
	}

	public void setAuthKey(String authKey) {
		this.authKey = authKey;
	}


	public UserModel getUser() {
		return user;
	}

	public void setUser(UserModel user) {
		this.user = user;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}


    public String getContent() {
        return content;
    }
}