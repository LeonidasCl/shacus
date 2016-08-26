package com.example.pc.shacus.Util;

public class CommonUrl {

	private static final String netUrl="http://114.215.94.95:800/";

	public static final String getYuepaiNavigateurl = netUrl+"xxxxxxxxxx/xxxxxxxx/";
	public static final String url = netUrl;
	public static final String imageUrl ="xxxx";
	public static final String loginAccount= url + "login";//登录接口，登陆之后才能更换头像、发表主题
	public static final String registerAccount = url + "regist";//注册申请验证码接口/注册验证验证码接口/注册提交信息接口（最终注册接口）

	public static final String saveThemeInfo    = url + "xxxx";//将主题上传到这个路径（上传图片第一次握手）
	public static final String saveThemeImgNew    = url + "xxxx";//将图片上传到这个路径（上传图片第三次握手）

}
