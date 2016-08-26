package com.example.pc.shacus.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import android.view.View;

import android.widget.Button;
import android.widget.TextView;

import com.example.pc.shacus.Network.StatusCode;
import com.example.pc.shacus.R;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;
import cn.bingoogolapple.bgabanner.BGABannerUtil;


//引导界面
//黄鑫晨 2016.03.16
//licl 优化了销毁动画-2016.06.07
public class GuideActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView mSkipTv;
    private BGABanner mContentBanner;

    Button btn_login,btn_regist;//登录，注册按钮
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPreference();
        setContentView(R.layout.activity_guide);
        initialWidgets();//初始化控件
    }

    private void checkPreference() {
                SharedPreferences settings = getSharedPreferences("setting", 0);
                SharedPreferences.Editor editor = settings.edit();
                int time = settings.getInt("time", 0);
                if (time==0){
                    editor.putInt("time",++time);
                    editor.apply();
                }else{
                    finish();
                    //要关掉销毁Activity的动画，不然很丑
                    overridePendingTransition(0,0);
                    Intent intent=new Intent("android.intent.action.SPLASHACTIVITY");
                    startActivity(intent);
                }

    }


    //初始化控件
    public void initialWidgets(){

        mSkipTv = (TextView) findViewById(R.id.tv_guide_skip);

        mContentBanner = (BGABanner) findViewById(R.id.banner_guide_content);
        List<View> topViews = new ArrayList<>();
        topViews.add(BGABannerUtil.getItemImageView(this, R.drawable.spl1));
        topViews.add(BGABannerUtil.getItemImageView(this, R.drawable.spl2));
        topViews.add(BGABannerUtil.getItemImageView(this, R.drawable.spl3));
        topViews.add(BGABannerUtil.getItemImageView(this, R.drawable.spl4));
        mContentBanner.setViews(topViews);
        mContentBanner.setOverScrollMode(View.OVER_SCROLL_NEVER);


        View v = findViewById(R.id.guidebtn_login);//将按钮设置为透明
        btn_login= (Button)v;//注意，因为button在View4中，所以
        v.getBackground().setAlpha(120);//0~255透明度值

        View v2 = findViewById(R.id.guidebtn_regist);//将按钮设置为透明
        btn_regist= (Button) v2;
        v2.getBackground().setAlpha(120);//0~255透明度值

        mSkipTv.setOnClickListener(this);

        btn_login.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                finish();
                Intent intent = new Intent("android.intent.action.LOGINACTIVITY");
                ///////***********2016-3-8 added by licl****************////
                //intent.putExtra("father", "GuideActivity");/////////////
                intent.putExtra("method", StatusCode.STATUS_LOGIN);
                startActivity(intent);
            }
        });
        btn_regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent("android.intent.action.LOGINACTIVITY");
                ///////***********2016-3-8 added by licl****************////
                //intent.putExtra("father", "GuideActivity");/////////////
                intent.putExtra("method", StatusCode.STATUS_REGISTER);
                startActivity(intent);
            }
        });

        mContentBanner.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == mContentBanner.getItemCount() - 2) {
                    ViewCompat.setAlpha(btn_login, positionOffset);
                    ViewCompat.setAlpha(btn_regist, positionOffset);
                    ViewCompat.setAlpha(mSkipTv, 1.0f - positionOffset);

                    if (positionOffset > 0.5f) {
                        btn_login.setVisibility(View.VISIBLE);
                        btn_regist.setVisibility(View.VISIBLE);
                        mSkipTv.setVisibility(View.GONE);
                    } else {
                        btn_login.setVisibility(View.GONE);
                        btn_regist.setVisibility(View.GONE);
                        mSkipTv.setVisibility(View.VISIBLE);
                    }
                } else if (position == mContentBanner.getItemCount() - 1) {
                    mSkipTv.setVisibility(View.VISIBLE);
                    btn_login.setVisibility(View.VISIBLE);
                    btn_regist.setVisibility(View.VISIBLE);
                    ViewCompat.setAlpha(btn_login, 1.0f);
                } else {
                    mSkipTv.setVisibility(View.VISIBLE);
                    ViewCompat.setAlpha(mSkipTv, 1.0f);
                    btn_login.setVisibility(View.GONE);
                    btn_regist.setVisibility(View.GONE);
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_guide_skip) {
            startActivity(new Intent(GuideActivity.this, MainActivity.class));
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mContentBanner.setBackgroundResource(android.R.color.white);
    }
}



