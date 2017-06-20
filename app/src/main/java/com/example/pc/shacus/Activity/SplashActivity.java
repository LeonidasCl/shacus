package com.example.pc.shacus.Activity;


import android.content.Intent;
import android.util.Log;

import com.example.pc.shacus.APP;
import com.example.pc.shacus.Data.Cache.ACache;
import com.example.pc.shacus.Data.Model.LoginDataModel;
import com.example.pc.shacus.Data.Model.RecommandModel;
import com.example.pc.shacus.Data.Model.UserModel;
import com.example.pc.shacus.Network.NetworkCallbackInterface;
import com.example.pc.shacus.Network.NetRequest;
import com.example.pc.shacus.Network.StatusCode;
import com.example.pc.shacus.R;
import com.example.pc.shacus.Util.CommonUrl;
import com.example.pc.shacus.Util.UserInfoUtil;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/*
* 李嘉文 2016/8/23
* */
public class SplashActivity extends BaseSplashActivity implements NetworkCallbackInterface.NetRequestIterface {

    private NetRequest requestFragment;
    private int splashFlag=0;//动画页面FLAG,以是否登录判断进哪个Activity,1表示登录成功进入Main

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

        Intent intent=getIntent();
        int type=intent.getIntExtra("result",0);//type为1表示是从LoginActivity登录成功跳转回来的初始化，不需要再登录

        UserModel data=login.getUserModel();
        requestFragment = new NetRequest(this, APP.context);
        Map map = new HashMap();
        if (type!=1){
            map.put("uid",data.getId());
            map.put("authkey",data.getAuth_key());
            map.put("askCode",StatusCode.REQUEST_AUTO_LOGIN);
            requestFragment.httpRequest(map, CommonUrl.loginAccount);
        }else {//已经登录成功，只要初始化即可
            splashFlag=1;
        }

        map.clear();
        map.put("type", StatusCode.RECOMMAND_PHOTOGRAPHER_MODEL_LIST);  //10850
        map.put("authkey", data.getAuth_key());
        requestFragment.httpRequest(map, CommonUrl.requestModel);
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
    public void requestFinish(String result, String requestUrl) throws JSONException {

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
                    return;
                    }
                } catch (JSONException e){
                e.printStackTrace();
            }
            return;
        }
        //请求摄影师模特列表
        if (requestUrl.equals(CommonUrl.requestModel)){
            JSONObject object = new JSONObject(result);
            int code = Integer.valueOf(object.getString("code"));
            if (code==StatusCode.RECOMMAND_PHOTOGRAPHER_MODEL_LIST){
                RecommandModel model = new RecommandModel();
                Gson gson = new Gson();
                JSONArray data = object.getJSONArray("contents");
                ArrayList<RecommandModel> mOurList = new ArrayList<>();
                for(int i = 0; i< data.length(); i++){
                    if(data.get(i)==null||data.get(i).toString().equals("null"))continue;
                    JSONObject info = data.getJSONObject(i);
                    model = gson.fromJson(info.toString(),RecommandModel.class);
                    mOurList.add(model);
                }
                //获得列表后先放入缓存
                ACache cache=ACache.get(APP.context);
                cache.put("mOurList",mOurList,ACache.TIME_HOUR);

            }
        }
    }


    @Override
    public void exception(IOException e, String requestUrl) {
        finish();
    }
}
