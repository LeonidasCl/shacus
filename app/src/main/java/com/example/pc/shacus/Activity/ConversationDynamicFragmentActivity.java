package com.example.pc.shacus.Activity;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.example.pc.shacus.APP;
import com.example.pc.shacus.Data.Cache.ACache;
import com.example.pc.shacus.Data.Model.LoginDataModel;
import com.example.pc.shacus.Network.NetRequest;
import com.example.pc.shacus.Network.NetworkCallbackInterface;
import com.example.pc.shacus.Network.StatusCode;
import com.example.pc.shacus.R;
import com.example.pc.shacus.Util.CommonUrl;
import com.example.pc.shacus.Util.CommonUtils;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import io.rong.imkit.RongIM;
import io.rong.imkit.utils.StringUtils;
import io.rong.imlib.model.UserInfo;


/**
 * Created by 李嘉文
 * 单聊的Activity
 */
public class ConversationDynamicFragmentActivity extends AppCompatActivity{

    private TextView back,title;
    private String nickname;
    private Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversation_dynamic_fr);
        back=(TextView) findViewById(R.id.conversation_toolbar_back);
        back.setText("＜返回");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        title=(TextView)findViewById(R.id.conversation_toolbar_title);
        String tid=getIntent().getData().getQueryParameter("targetId");

        ACache cache = ACache.get(getApplicationContext());
        LoginDataModel loginModel = (LoginDataModel) cache.getAsObject("loginModel");
        String uid = loginModel.getUserModel().getId();

        Map map = new HashMap();
        //向业务服务器发请求
        map.put("uid", uid);
        map.put("seeid",tid);
        map.put("authkey", loginModel.getUserModel().getAuth_key());
        map.put("type", StatusCode.GET_OTHER_USER_INFO);

        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what==StatusCode.REQUEST_OTHER_INFORMATION){
                    title.setText(nickname);
                    return;
                }
            }
        };

        new NetRequest(new NetworkCallbackInterface.NetRequestIterface(){
            @Override
            public void requestFinish(String result, String requestUrl) throws JSONException {
                if (requestUrl.equals(CommonUrl.getOtherUser)){//if#2 收到业务服务器回复

                    JSONObject object = new JSONObject(result);
                    int code = Integer.valueOf(object.getString("code"));
                    if (code==StatusCode.REQUEST_OTHER_USER_SUCCESS){//if#3
                        JSONObject content=object.getJSONObject("contents");
                        String id=content.getString("uid");
                        nickname=content.getString("ualais");
                        String avatar=content.getString("uheadimage");
                        //把用户信息传回给融云
                        RongIM.getInstance().refreshUserInfoCache(new UserInfo(id, nickname, Uri.parse(avatar)));

                        Message msg=handler.obtainMessage();
                        msg.what= StatusCode.REQUEST_OTHER_INFORMATION;
                        handler.sendMessage(msg);

                        return;
                    }else{//请求失败

                    }//end if#3
                }//end if#2
            }
            @Override
            public void exception(IOException e, String requestUrl) {}
        }, APP.context).httpRequest(map, CommonUrl.getOtherUser);


    }
}
