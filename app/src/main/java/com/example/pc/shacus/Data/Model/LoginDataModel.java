package com.example.pc.shacus.Data.Model;

import java.util.List;

public class LoginDataModel {

	private UserModel userModel;
	private List<PhotographerModel> photoList;
	private List<NavigationModel> daohanglan;
	private String code;
	private List<PhotographerModel> modelList;

	public List<PhotographerModel> getPhotoList() {
		return photoList;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public List<NavigationModel> getDaohanglan() {
		return daohanglan;
	}

	public void setDaohanglan(List<NavigationModel> daohanglan){
		this.daohanglan = daohanglan;
	}

	public UserModel getUserModel() {
		return userModel;
	}

	public void setUserModel(UserModel userModel) {
		this.userModel = userModel;
	}

	public List<PhotographerModel> getModelList() {
		return modelList;
	}

	public void setModelList(List<PhotographerModel> modelList) {
		this.modelList = modelList;
	}

	public void setPhotoList(List<PhotographerModel> photoList) {
		this.photoList = photoList;
	}
}