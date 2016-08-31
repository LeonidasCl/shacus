package com.example.pc.shacus.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.pc.shacus.Adapter.FollowItemAdapter;
import com.example.pc.shacus.Data.Model.UserModel;
import com.example.pc.shacus.Network.NetworkCallbackInterface;
import com.example.pc.shacus.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//崔颖华
//粉丝与关注界面（二级）
public class FollowActivity extends AppCompatActivity implements  NetworkCallbackInterface.NetRequestIterface{

    private String type = null;
    private ImageButton backbtn;
    private ListView followListview;
    private List<UserModel> userList = new ArrayList<>();
    private TextView follow;
    private FollowItemAdapter followItemAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow);

        backbtn = (ImageButton) findViewById(R.id.backbtn);
        follow = (TextView) findViewById(R.id.follow);
        followListview = (ListView) findViewById(R.id.follow_listview);

        //获得个人主界面点击的按钮名称 关注|粉丝
        Intent intent = getIntent();
        type = intent.getStringExtra("activity");
        initUserInfo(); //初始化关注或粉丝数据

        if (type.equals("following")){
            follow.setText("我的关注");
        }else if(type.equals("follower")){
            follow.setText("我的粉丝");
        }

        backbtn.setOnClickListener(new BackButton());

        followItemAdapter = new FollowItemAdapter(FollowActivity.this,userList);
        followListview.setAdapter(followItemAdapter);
    }

    //初始化关注following或粉丝follower数据
    private void initUserInfo() {

        if(type.equals("following")){
            /*向UserList中添加获取到的关注信息*/

        }else if(type.equals("follower")){
            /*向UserList中添加获取到的粉丝信息*/

        }
    }

    //在适配器中调用，确定单项布局
    public String getType() {
        return type;
    }

    //返回一级我的个人主界面
    class BackButton implements View.OnClickListener{

        @Override
        public void onClick(View v) {
        //           Intent intent = new Intent(FollowActivity.this,/*上级界面*/);
        }
    }

    @Override
    public void requestFinish(String result, String requestUrl) {

    }

    @Override
    public void exception(IOException e, String requestUrl) {

    }
}
