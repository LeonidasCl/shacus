package com.example.pc.shacus.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.pc.shacus.Network.NetworkCallbackInterface;
import com.example.pc.shacus.R;

import java.io.IOException;

//TODO：把这一行换成作者名
//订单界面（二级）
public class OrdersActivity extends AppCompatActivity implements  NetworkCallbackInterface.NetRequestIterface{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
    }

    @Override
    public void requestFinish(String result, String requestUrl) {

    }

    @Override
    public void exception(IOException e, String requestUrl) {

    }
}
