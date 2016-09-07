package com.example.pc.shacus.Activity;


import android.content.Intent;

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

public class SplashActivity extends BaseSplashActivity implements NetworkCallbackInterface.NetRequestIterface {

    private NetRequest requestFragment;

    @Override
    public void initNetworkData() {
//        ACache cache=ACache.get(APP.context);
//        LoginDataModel temp= (LoginDataModel) cache.getAsObject("loginModel");
//        UserModel data=temp.getUserModel();
//        requestFragment = new NetRequest(this, APP.context);
//        Map map = new HashMap();
//
//        requestFragment.httpRequest(map, CommonUrl.loginAccount);
    }


    @Override
    public void initCheck() {

    }

    @Override
    public int getLogoImageResource() {
        return R.drawable.start_logo;
    }

    @Override
    public Class getNextActivityClass() {
        return MainActivity.class;
    }

    @Override
    public void requestFinish(String result, String requestUrl) {

        if (requestUrl.equals(CommonUrl.loginAccount)) {//返回登录请求
            try {
                JSONObject object = null;

                object = new JSONObject(result);

                int code = Integer.valueOf(object.getString("code"));

                if (code== StatusCode.REQUEST_LOGIN_SUCCESS){
                    Gson gson=new Gson();
                    LoginDataModel loginModel=gson.fromJson(object.getJSONArray("contents").getJSONObject(0).toString(),LoginDataModel.class);
                    ACache cache=ACache.get(APP.context);
                    cache.put("loginModel",loginModel,ACache.TIME_WEEK*2);
                }else {
                    //Looper.prepare();CommonUtils.getUtilInstance().showToast(APP.context, content.toString());Looper.loop();
                    String str=object.getString("contents");
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("result","登录失败:"+str);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    return;
                    }
                } catch (JSONException e) {
                e.printStackTrace();
            }

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("result", "登录成功");
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
            finish();
            return;
        }
    }


    @Override
    public void exception(IOException e, String requestUrl) {

    }
}
