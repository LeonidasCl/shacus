package com.example.pc.shacus.Network;

/**
 * Created by pc on 2016/3/6.
 */
public interface StatusCode {
    int HTTP_GET=1;
    int HTTP_POST=2;

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

    //注册请求类别的成功返回码
    int REQUEST_LOGIN=10106;

    int REQUEST_LOGIN_SUCCESS =10101 ;
}
