package com.example.pc.shacus.Fragment;

import android.support.v4.app.Fragment;

import com.example.pc.shacus.Network.NetworkCallbackInterface;

import java.io.IOException;
//TODO：把这一行换成作者名
//用户个人界面（一级）
public class UserFragment extends android.support.v4.app.Fragment implements  NetworkCallbackInterface.NetRequestIterface{


    @Override
    public void requestFinish(String result, String requestUrl) {
        //在这里接收所有网络请求
    }

    @Override
    public void exception(IOException e, String requestUrl) {
        //处理网络请求的异常信息，一般用不到
    }
}
