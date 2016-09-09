package com.example.pc.shacus.Util;

public class CommonUrl {

	private static final String netUrl="http://114.215.94.95:800/";

	public static final String getYuepaiNavigateurl = netUrl+"xxxxxxxxxx/xxxxxxxx/";
	public static final String url = netUrl;
	public static final String imageUrl ="xxxx";
	public static final String loginAccount= url + "login";//登录接口，登陆之后才能更换头像、发表主题
	public static final String registerAccount = url + "regist";//注册申请验证码接口/注册验证验证码接口/注册提交信息接口（最终注册接口）

	public static final String createYuePaiInfo = url + "appointment/create";//约拍立项申请路径（请求10201，上传图片第一次握手）
	public static final String saveThemeImgNew    = url + "xxxx";//将上传图片的结果与完整约拍信息回传业务服务器
	public static final String createActivityInfo    = url + "activity/create";//发布活动路径
	public static final String otherUserInfo =url+"user/homepager";//访问他人个人主页
	public static final String getYuePaiInfo    = url + "appointment/ask";//获得约拍详情
	public static final String settingChangeNetUrl=url+"PaswChange";

	public static final String getFollowInfo = url + "user/mylike";//获取关注和粉丝

	public static final String getFavorInfo = url + "user/favorite"; //获取收藏

	public static final String getOrdersInfo = url + "user/indent"; //获取用户订单
	public static final String courseInfo = url+"course/ask";//获取我的课程
    public static final String courseFav = url+"course/fav";//收藏与取消收藏课程
	public static final String courseHomePage=url+"course/homepage";//教程首页
	public static final String finishHuodong=url+"user/indent";
	public static final String getHuodongList=url+"Activity/ask";
	public static String joinYuepai=url+"appointment/regist";//约拍报名
	public static String joinHuodong=url+"activity/register";//约拍报名
	public static String praiseAppointment=url+"appointment/prase";
	public static String praiseActivity=url+"Activity/entry";
	public static String favouriteYuepai=url+"user/favorite";

	public static final String askYuepai = url+ "appointment/ask"; //获得约拍系列信息
	public static final String allDongtai = url + "trend/Trendspost"; //获得所有动态
}
