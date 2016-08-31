package com.example.pc.shacus.Activity;


import com.example.pc.shacus.APP;
import com.example.pc.shacus.Data.Model.LoginDataModel;
import com.example.pc.shacus.Network.NetworkCallbackInterface;
import com.example.pc.shacus.Network.NetRequest;
import com.example.pc.shacus.R;
import com.example.pc.shacus.Util.CommonUrl;
import com.example.pc.shacus.Util.UserInfoUtil;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SplashActivity extends BaseSplashActivity implements NetworkCallbackInterface.NetRequestIterface {

    private NetRequest requestFragment;

    @Override
    public void initNetworkData() {

      /*   user=new UserModel();
        String username=user.getUsername();
        String usertoken=user.getToken();

        //该用户登录状态未过期，开始获取网络数据
       if (username!=null){
            LoginReciever response=new LoginReciever();
            HashMap loginParams=new HashMap<String,String>();
            loginParams.put("username",username);
            loginParams.put("usertoken", usertoken);
            String result= BaseNetworkRequestor.getInstance().sendRequest(StatusCode.HTTP_POST,
                    "http://www.airserverseu.applinzi.com/userlogin.php", loginParams, response, getApplicationContext());

            if (result.equals(response.RETURN_LOGIN_SUCCESS)){
                //登陆成功
            }
        }

        if (true){
            LoginReciever response=new LoginReciever();
            HashMap loginParams=new HashMap<>();
            loginParams.put("username","shacus");
            loginParams.put("usertoken", "5AA765D61D8327DE");
            String result= BaseNetworkRequestor.getInstance().sendRequest(StatusCode.HTTP_POST,
                    "http://223.3.67.125:800/huodong/ask?type=askCommentId&topicId=1", loginParams, response, getApplicationContext());

            if (result.equals(response.RETURN_LOGIN_SUCCESS)){
                //登陆成功
            }
        }*/
        requestFragment = new NetRequest(this, APP.context);
        Map map = new HashMap();
        map.put("userName", "15652009705");
        map.put("pwd", "123456");
        map.put("thirdPart", "");
        //requestFragment.httpRequest(map, CommonUrl.loginAccount);
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
        if (requestUrl.equals(CommonUrl.loginAccount)) {

            Gson gson = new Gson();
            LoginDataModel loginStatusInfoObject = gson.fromJson(result, LoginDataModel.class);
}
    }

    @Override
    public void exception(IOException e, String requestUrl) {

    }
}
