package com.example.pc.shacus.Activity;


import android.content.Intent;
import android.util.Log;

import com.example.pc.shacus.APP;
import com.example.pc.shacus.Data.Cache.ACache;
import com.example.pc.shacus.Data.Model.LoginDataModel;
import com.example.pc.shacus.Data.Model.UserModel;
import com.example.pc.shacus.Network.NetworkCallbackInterface;
import com.example.pc.shacus.Network.NetRequest;
import com.example.pc.shacus.Network.StatusCode;
import com.example.pc.shacus.R;
import com.example.pc.shacus.Util.CommonUrl;
import com.example.pc.shacus.Util.UserInfoUtil;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/*
* 李嘉文 2016/8/23
* */
public class SplashActivity extends BaseSplashActivity implements NetworkCallbackInterface.NetRequestIterface {

    private NetRequest requestFragment;
    private int splashFlag=0;//动画页面FLAG,以是否登录判断进哪个Activity

    @Override
    public void initNetworkData() {
        ACache cache=ACache.get(APP.context);
        LoginDataModel login= (LoginDataModel) cache.getAsObject("loginModel");
        if (login==null){
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        UserModel data=login.getUserModel();
        requestFragment = new NetRequest(this, APP.context);
        Map map = new HashMap();
        map.put("uid",data.getId());
        map.put("authkey",data.getAuth_key());
        map.put("askCode",StatusCode.REQUEST_AUTO_LOGIN);
        requestFragment.httpRequest(map, CommonUrl.loginAccount);
    }


    @Override
    public void initCheck() {

    }

    @Override
    public int getLogoImageResource() {
        return R.drawable.start_logo;
    }

    @Override
    public int getFrontImageResource() {
        return R.drawable.start_logo2;
    }

    @Override
    public Class getNextActivityClass() {
        if (splashFlag==0)
            return LoginActivity.class;
        if (splashFlag==1)
            return MainActivity.class;
        else
            return null;
    }

    @Override
    public void requestFinish(String result, String requestUrl) {

        if (requestUrl.equals(CommonUrl.loginAccount)){//返回登录请求
            try {
                JSONObject object = null;

                object = new JSONObject(result);

                int code = Integer.valueOf(object.getString("code"));

                if (code== StatusCode.REQUEST_LOGIN_SUCCESS){
                    splashFlag=1;
                    Gson gson=new Gson();
                    LoginDataModel loginModel=gson.fromJson(object.getJSONArray("contents").getJSONObject(0).toString(),LoginDataModel.class);
                    ACache cache=ACache.get(APP.context);
                    cache.put("loginModel",loginModel,ACache.TIME_WEEK*2);
                }else {
//                    String str=object.getString("contents");
//                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//                    intent.putExtra("result","自动登录失败:"+str+"\n请手动登录");
//                    startActivity(intent);
//                    finish();
                    return;
                    }
                } catch (JSONException e){
                e.printStackTrace();
            }
//
//            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//            intent.putExtra("result", "登录成功");
//            startActivity(intent);
//            finish();
            return;
        }
    }


    @Override
    public void exception(IOException e, String requestUrl) {
        finish();
    }
}
