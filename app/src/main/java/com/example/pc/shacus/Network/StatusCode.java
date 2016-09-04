package com.example.pc.shacus.Network;

/**
 * Created by pc on 2016/3/6.
 */
public interface StatusCode {

    int STATUS_ERROR= 0;
    int STATUS_LOGIN=10100;
    int STATUS_REGISTER=10000;

    //请求获取验证码
    int REQUEST_REGISTER_VERIFYA=10001;
    //请求验证验证码
    int REQUEST_REGISTER_VERIFYB=10002;
    //请求注册
    int REQUEST_REGISTER=10003;
    //注册请求类别的成功返回码
    int RECIEVE_REGISTER_SUCCESS=10004;
    //请求登录的请求码
    int REQUEST_LOGIN=10106;
    //登录请求类别成功的返回码
    int REQUEST_LOGIN_SUCCESS =10101 ;

    int REQUEST_YUEPAI_SUCCESS=10200;
    int REQUEST_CREATE_YUEPAIA=10201;
    int REQUEST_CREATE_YUEPAIB=10202;
    int REQUEST_CREATE_HUODONG=10203;
    int REQUEST_SEND_YUEPAI=10205;


    int REQUEST_FOLLOW_ERROR = 10419; //服务器错误
    //请求我关注的用户（我的关注）
    int REQUEST_INFO_FOLLOWING = 10403;
    int REQUEST_FOLLOWING_SUCCESS = 10430; //请求我的关注成功
    int REQUEST_FOLLOWING_NONE = 10431; //用户不存在粉丝
    int REQUEST_USER_ILLEGAL = 10412; //用户不合法

    //请求取消关注
    int REQUEST_CANCEL_FOLLOWING = 10402;
    int REQUEST_CANCEL_SUCCESS = 10420; //请求取消关注成功
    int REQUEST_CANCEL_NONE = 10421; //未关注该用户

    //请求关注
    int REQUEST_FOLLOW_USER = 10401;
    int REQUEST_FOLLOW_SUCCESS = 10411; //请求关注成功

    //请求我的粉丝
    int REQUEST_INFO_FOLLOWER = 10404;
    int REQUEST_FOLLOWER_SUCCESS = 10440; //请求我的粉丝成功

    int REQUEST_REGIST_ORDER = 10901; //请求用户所有的已报名的约拍和活动
    int REQUEST_ORDER_ERROR = 10391; //请求订单授权码不正确

    int REQUEST_REGIST_SUCCESS = 10392; //请求用户所有的已报名的约拍和活动成功

    int REQUEST_DOING_ORDER = 10902; //请求用户所有的正在进行的约拍和活动
    int REQUEST_DOING_SUCCESS = 10393; //请求进行中成功

    int REQUEST_DONE_ORDER = 10903; //请求用户所有的已完成的约拍和活动
    int REQUEST_DONE_SUCCESS = 10394; //请求已完成成功

    int REQUEST_FAVOR_YUEPAI = 10541; //请求所有收藏的约拍

    int REQUEST_ADD_FAVORYUEPAI = 10501; //请求添加收藏

    int REQUEST_FAVOR_HUODONG = 10502; //请求收藏的活动
    int REQUEST_FAVOR_ZUOPIN = 10503; //请求收藏的作品
    int REQUEST_FAVOR_JIAOCHENG = 10504; //请求收藏的教程
    int REQUEST_CANCEL_FAVORYUEPAI = 10523; //取消收藏约拍
    int REQUEST_CANCEL_FAVORHUODONG = 10511; //取消收藏活动
    //int REQUEST_CANCEL_FAVORZUOPIN = 10512; //取消收藏作品
    //int REQUEST_CANCEL_FAVORJIAOCHENG = 10

    int REQUEST_FAVORYUEPAI_SUCCESS = 10550; //请求所有收藏约拍成功
}
