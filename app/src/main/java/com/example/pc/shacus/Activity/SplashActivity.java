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
//
//        requestFragment = new NetRequest(this, APP.context);
//        Map map = new HashMap();
//        map.put("userName", "15652009705");
//        map.put("pwd", "123456");
//        map.put("thirdPart", "");
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
        if (requestUrl.equals(CommonUrl.loginAccount)) {

            Gson gson = new Gson();
            LoginDataModel loginStatusInfoObject = gson.fromJson(result, LoginDataModel.class);
}
    }

    @Override
    public void exception(IOException e, String requestUrl) {

    }
}
