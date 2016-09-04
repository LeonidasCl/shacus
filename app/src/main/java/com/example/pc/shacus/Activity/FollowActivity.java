package com.example.pc.shacus.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pc.shacus.Adapter.FollowItemAdapter;
import com.example.pc.shacus.Data.Cache.ACache;
import com.example.pc.shacus.Data.Model.UserModel;
import com.example.pc.shacus.Network.NetRequest;
import com.example.pc.shacus.Network.NetworkCallbackInterface;
import com.example.pc.shacus.Network.StatusCode;
import com.example.pc.shacus.R;
import com.example.pc.shacus.Util.CommonUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/*
* @author:崔颖华
*
* */
//崔颖华
//粉丝与关注界面（二级）
public class FollowActivity extends AppCompatActivity implements  NetworkCallbackInterface.NetRequestIterface{

    private String type = null;
    private ImageButton backbtn;
    private ListView followListview;
    private List<UserModel> userList = new ArrayList<>();;
    private TextView follow;
    private FollowItemAdapter followItemAdapter;
    private NetRequest request = null;
    public  ACache aCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow);

        request = new NetRequest(FollowActivity.this,FollowActivity.this);
        aCache = ACache.get(FollowActivity.this);

        backbtn = (ImageButton) findViewById(R.id.backbtn);
        follow = (TextView) findViewById(R.id.follow);
        followListview = (ListView) findViewById(R.id.follow_listview);

        //获得个人主界面点击的按钮名称 关注|粉丝
        Intent intent = getIntent();
        type = intent.getStringExtra("activity");

        if (type.equals("following")){
            follow.setText("我的关注");
        }else if(type.equals("follower")){
            follow.setText("我的粉丝");
        }
        initUserInfo(); //初始化关注或粉丝数据

        backbtn.setOnClickListener(new BackButton());

        followListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //点击进入其他用户主界面
                //传入需要的参数
                UserModel next = userList.get(position);

            }
        });
    }

    //初始化关注following或粉丝follower数据
    private void initUserInfo() {
        JSONObject jsonObject = aCache.getAsJSONObject("loginModel");
        JSONObject content = null;
        Map map = new HashMap<>();

        try {
            content = jsonObject.getJSONObject("userModel");
            Log.d("aaaaaaaaaaaaaaaaa", jsonObject.toString());
            String userId = content.getString("id");
            String authkey = content.getString("auth_key");
            map.put("authkey",authkey);
            map.put("uid",userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(type.equals("following")){
            /*向UserList中添加获取到的关注信息*/
            map.put("type", StatusCode.REQUEST_INFO_FOLLOWING);
            request.httpRequest(map, CommonUrl.getFollowInfo);

        }else if(type.equals("follower")){
            /*向UserList中添加获取到的粉丝信息*/
            map.put("type", StatusCode.REQUEST_INFO_FOLLOWER);
            request.httpRequest(map,CommonUrl.getFollowInfo);

        }
    }

    //在适配器中调用，确定单项布局
    public String getType() {
        return type;
    }
    //在配适器中调用，用于关注和取消关注
    public List<UserModel> getData(){
        return userList;
    }

    private android.os.Handler handler = new android.os.Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case StatusCode.REQUEST_FOLLOWING_SUCCESS: //请求关注信息成功
                {
                    followItemAdapter = new FollowItemAdapter(FollowActivity.this,userList);
                    followListview.setAdapter(followItemAdapter);
                    break;
                }
                case StatusCode.REQUEST_CANCEL_SUCCESS:  //请求取消关注成功
                {
                    initUserInfo();
                    break;
                }
                case StatusCode.REQUEST_FOLLOW_SUCCESS: //请求关注成功
                {
                    initUserInfo();
                    break;
                }
                case StatusCode.REQUEST_FOLLOWER_SUCCESS: //请求关注信息成功
                {
                    followItemAdapter = new FollowItemAdapter(FollowActivity.this,userList);
                    followListview.setAdapter(followItemAdapter);
                    break;
                }
            }

        }
    };


 /*   @Override
    public void onClick(View v) {
        int id = (int) v.getTag();
        switch (id){
            case 1:
            {
                try {
                    JSONObject jsonObject = aCache.getAsJSONObject("loginModel");
                    JSONObject content = jsonObject.getJSONObject("userModel");
                    content = jsonObject.getJSONObject("userModel");
                    String userId = content.getString("id");
                    String authkey = content.getString("auth_key");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }*/

    //返回一级我的个人主界面
    class BackButton implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            //返回上级界面
        }
    }

    @Override
    public void requestFinish(String result, String requestUrl) throws JSONException {
        if(requestUrl.equals(CommonUrl.getFollowInfo)){//返回我的关注或粉丝信息
            JSONObject object = new JSONObject(result);
            int code = Integer.valueOf(object.getString("code"));
            Message msg = new Message();

            switch (code){
                case StatusCode.REQUEST_FOLLOWING_SUCCESS: //请求我的关注
                {
                    JSONArray content = object.getJSONArray("contents");
                    for (int i = 0;i < content.length();i++){
                        JSONObject following = content.getJSONObject(i);
                        UserModel userModel = new UserModel();
                        userModel.setHeadImage(following.getString("uimgurl"));
                        userModel.setId(following.getString("uid"));
                        userModel.setNickName(following.getString("ualais"));
                        Log.d("aaaaaaaaaaaaaaaaa", userModel.getId().toString());
                        userList.add(userModel);
                    }
                    msg.what = StatusCode.REQUEST_FOLLOWING_SUCCESS;
                    handler.sendMessage(msg);

                    break;
                }
                case StatusCode.REQUEST_FOLLOWER_SUCCESS: //请求我的粉丝
                {
                    JSONArray content = object.getJSONArray("contents");
                    for (int i = 0;i < content.length();i++){
                        JSONObject follower = content.getJSONObject(i);
                        UserModel userModel = new UserModel();
                        userModel.setHeadImage(follower.getString("uimgurl"));
                        userModel.setId(follower.getString("uid"));
                        userModel.setNickName(follower.getString("ualais"));
                        userModel.setIndex(follower.getBoolean("fansback"));
                        Log.d("aaaaaaaaaaaaaaaaa", userModel.getId().toString());
                        Log.d("aaaaaaaaaaaaaaaaa","hhhhhhhhhhhhhhh");
                        userList.add(userModel);
                    }
                    msg.what = StatusCode.REQUEST_FOLLOWING_SUCCESS;
                    handler.sendMessage(msg);

                    break;
                }
                case StatusCode.REQUEST_FOLLOWING_NONE:
                    //没有关注
                    break;
                case StatusCode.REQUEST_USER_ILLEGAL:
                    //用户非法
                    break;
                case StatusCode.REQUEST_CANCEL_SUCCESS: //取消关注成功
                {
                    msg.what = StatusCode.REQUEST_CANCEL_SUCCESS;
                    Log.d("aaaaaaaaaaaaaaaaa", "取消成功");
                    handler.sendMessage(msg);
                    break;
                }
                case StatusCode.REQUEST_FOLLOW_SUCCESS://请求关注成功
                {
                    msg.what = StatusCode.REQUEST_FOLLOW_SUCCESS;
                    Log.d("aaaaaaaaaaaaaaaaa", "请求关注成功");
                    handler.sendMessage(msg);
                    break;
                }
                case StatusCode.REQUEST_FOLLOW_ERROR://服务器错误
                {
                    //服务器错误
                }
            }

        }
    }

    @Override
    public void exception(IOException e, String requestUrl) {

    }

}
