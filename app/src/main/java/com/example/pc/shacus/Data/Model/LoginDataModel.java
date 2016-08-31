package com.example.pc.shacus.Data.Model;

public class LoginDataModel {

	private UserModel userModel;
	private Object modelList;
	private Object daohanglan;
	private String code;
	private Object photoList;

	public Object getPhotoList() {
		return photoList;
	}

	public void setPhotoList(Object photoList) {
		this.photoList = photoList;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Object getDaohanglan() {
		return daohanglan;
	}

	public void setDaohanglan(Object daohanglan) {
		this.daohanglan = daohanglan;
	}

	public Object getModelList() {
		return modelList;
	}

	public void setModelList(Object modelList) {
		this.modelList = modelList;
	}

	public UserModel getUserModel() {
		return userModel;
	}

	public void setUserModel(UserModel userModel) {
		this.userModel = userModel;
	}
}