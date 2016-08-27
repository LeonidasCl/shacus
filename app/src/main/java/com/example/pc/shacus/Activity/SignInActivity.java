package com.example.pc.shacus.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.pc.shacus.Network.NetworkCallbackInterface;
import com.example.pc.shacus.R;

import java.io.IOException;

//TODO：把这一行换成作者名
//用户签到界面（二级）
public class SignInActivity extends AppCompatActivity implements  NetworkCallbackInterface.NetRequestIterface{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
    }

    @Override
    public void requestFinish(String result, String requestUrl) {
        //在这里接收所有网络请求
    }

    @Override
    public void exception(IOException e, String requestUrl) {
        //处理网络请求的异常信息，一般用不到
    }

}
