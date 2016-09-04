package com.example.pc.shacus.Activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.pc.shacus.Data.Cache.ACache;
import com.example.pc.shacus.Data.Model.LoginDataModel;
import com.example.pc.shacus.Data.Model.UserModel;
import com.example.pc.shacus.Network.NetRequest;
import com.example.pc.shacus.Network.NetworkCallbackInterface;
import com.example.pc.shacus.Network.StatusCode;
import com.example.pc.shacus.R;
import com.example.pc.shacus.Util.CommonUrl;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
                Log.d("sssssssssssssss", "aaaa");
                Map map = new HashMap();
                String username = "17751030037";
                String pwd = "123456";
                map.put("phone",username);
                map.put("password",pwd);
                map.put("askCode", StatusCode.REQUEST_LOGIN);
                NetRequest request = new NetRequest(SignInActivity.this,SignInActivity.this);
                request.httpRequest(map, CommonUrl.loginAccount);

            }
        });
        signInText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("aaaaaaaaaaaaaaa", "kjsdka");
                ACache acache = ACache.get(SignInActivity.this);
                JSONObject jsonObject = acache.getAsJSONObject("loginModel");
                try {
                    JSONObject content = jsonObject.getJSONObject("userModel");

                    Log.d("aaaaaaaaaaaaaa",content.toString());
                    String a = content.getString("id");
                    Log.d("aaaaaaaaaaaaaaa",a);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    @Override
    public void requestFinish(String result, String requestUrl) throws JSONException {
        //在这里接收所有网络请求
        if(requestUrl.equals(CommonUrl.loginAccount)){
            JSONObject object = new JSONObject(result);
            int code = Integer.valueOf(object.getString("code"));
            JSONArray content=object.getJSONArray("contents");
            //还有其它的JSONArray比如榜单、广告栏等初始化数据未接
            String userJSON=content.getJSONObject(0).toString();
            Gson gson=new Gson();
            LoginDataModel loginDataModel=gson.fromJson(userJSON,LoginDataModel.class);

            UserModel user = loginDataModel.getUserModel();
            ACache cache=ACache.get(SignInActivity.this);
            cache.put("loginModel",content.getJSONObject(0),ACache.TIME_WEEK*2);


            if(code == StatusCode.REQUEST_LOGIN_SUCCESS){
                Log.d("sssssssssssssss", "登陆成功");
            } else{
                Log.d("sssssssssssssss","失败啦");
            }


        }
    }

    @Override
    public void exception(IOException e, String requestUrl) {
        //处理网络请求的异常信息，一般用不到
    }





}
