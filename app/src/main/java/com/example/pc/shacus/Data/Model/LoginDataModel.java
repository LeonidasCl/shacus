package com.example.pc.shacus.Data.Model;

import java.io.Serializable;
import java.util.List;
/*
* 李嘉文 注册数据模型
* */
public class LoginDataModel implements Serializable {

	private UserModel userModel;
	private List<NavigationModel> daohanglan;
	private List<YuePaiGroupModel> groupList;
	private List<PhotosetItemModel> CollectionList;
	private String code;

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

	public List<YuePaiGroupModel> getGroupList() {
		return groupList;
	}

	public void setGroupList(List<YuePaiGroupModel> groupList) {
		this.groupList = groupList;
	}
}