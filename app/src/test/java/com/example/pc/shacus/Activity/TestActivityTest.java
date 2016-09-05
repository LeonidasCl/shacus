package com.example.pc.shacus.Activity;

import android.util.Log;

import com.example.pc.shacus.Data.Model.LoginDataModel;
import com.example.pc.shacus.Data.Model.UserModel;
import com.example.pc.shacus.Network.NetRequest;
import com.example.pc.shacus.Network.StatusCode;
import com.example.pc.shacus.Util.CommonUrl;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by 启凡 on 2016/9/1.
 */
public class TestActivityTest {

    NetRequest newRequst;
    @Test
    public void onCreat() throws Exception {
        Map map = new HashMap();

        map.put("phone","15850652378");
        map.put("password","00000000");
        map.put("askCode", StatusCode.REQUEST_LOGIN);
        newRequst.httpRequest(map, CommonUrl.loginAccount);
    }


    @Test
    public void requstFinish(String result, String requestUrl) throws Exception {
        if (requestUrl.equals(CommonUrl.loginAccount)) {//返回登录请求
            JSONObject object = new JSONObject(result);
            int code = Integer.valueOf(object.getString("code"));
            JSONArray content = object.getJSONArray("contents");
            String userJSON = content.getJSONObject(0).toString();
            Gson gson = new Gson();
            LoginDataModel loginDataModel = gson.fromJson(userJSON, LoginDataModel.class);

            UserModel user = loginDataModel.getUserModel();

            if (code == StatusCode.REQUEST_LOGIN_SUCCESS) {

                Log.d("login", "成功");
            } else {

                Log.d("login", "失败");


            }
        }

    }
}