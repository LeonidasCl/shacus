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
    int REQUEST_CREATE_HUODONG=10301;
    int REQUEST_SEND_YUEPAI=10205;
    int REQUEST_HUODONG_SUCCESS=10313;
    int REQUEST_HUODONG_SUCCEED = 10323;
    int REQUEST_SEND_HUODONG=10302;
    int REQUEST_YUEPAI_SUCCEED = 10214;
}
