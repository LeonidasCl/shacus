package com.example.pc.shacus.Activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.pc.shacus.Network.NetworkCallbackInterface;
import com.example.pc.shacus.R;

import java.io.IOException;

//孙启凡 2016.08.30
//用户签到界面（二级）

public class SignInActivity extends AppCompatActivity implements  NetworkCallbackInterface.NetRequestIterface{
    private ImageButton returnButton;
    private Switch aswitch;
    private Button signInButton;
    private TextView signInText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        aswitch=(Switch)findViewById(R.id.aswitch);
        returnButton=(ImageButton)findViewById(R.id.header_left_btn);
        signInButton=(Button)findViewById(R.id.signInButton);
        signInText=(TextView)findViewById(R.id.signInText);
        returnButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              SignInActivity.this.finish();
          }
      });
        aswitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        signInText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


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
