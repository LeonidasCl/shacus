package com.example.pc.shacus.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.pc.shacus.APP;
import com.example.pc.shacus.Adapter.UserDetailAdapter;
import com.example.pc.shacus.Data.Cache.ACache;
import com.example.pc.shacus.Data.Model.LoginDataModel;
import com.example.pc.shacus.Data.Model.UserModel;
import com.example.pc.shacus.Network.NetRequest;
import com.example.pc.shacus.Network.NetworkCallbackInterface;
import com.example.pc.shacus.Network.StatusCode;
import com.example.pc.shacus.R;
import com.example.pc.shacus.Util.CommonUrl;
import com.example.pc.shacus.Util.CommonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cuicui on 2016/9/7.
 * 选择用户
 */
public class SelectUserActivity extends AppCompatActivity implements NetworkCallbackInterface.NetRequestIterface{

    private List<UserModel> userModelList = new ArrayList<>();
    private ListView listView ;
    private NetRequest netRequest;
    private ACache aCache;
    private ImageButton back;
    private TextView title;
    private UserDetailAdapter userDetailAdapter;

    String type = null;

    String id = null;
    int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectuser);

        netRequest = new NetRequest(SelectUserActivity.this,SelectUserActivity.this);
        aCache = ACache.get(SelectUserActivity.this);

        listView = (ListView) findViewById(R.id.baoming_listview);
        title = (TextView) findViewById(R.id.baoming_title);
        back = (ImageButton) findViewById(R.id.baomimng_backbtn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        String t  = intent.getStringExtra("title");
        title.setText(t);
        type = intent.getStringExtra("type");
        if(type.equals("yuepai")){
            index = StatusCode.REQUEST_BAOMING_YUEPAI_USER;
            id = intent.getStringExtra("apid");
        }else if(type.equals("huodong")){
            //
            id = intent.getStringExtra("acid");
        }
        initUserInfo();
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }


    //获得报名用户的信息
    private void initUserInfo(){
        Map map = new HashMap<>();
        LoginDataModel loginDataModel = (LoginDataModel) aCache.getAsObject("loginModel");
        UserModel content = loginDataModel.getUserModel();
        String userId = content.getId();
        String authkey = content.getAuth_key();

        map.put("uid", userId);
        map.put("authkey",authkey);
        if(index == StatusCode.REQUEST_BAOMING_YUEPAI_USER){
            map.put("type", StatusCode.REQUEST_BAOMING_YUEPAI_USER);
            map.put("apid", id);
        }//else
        netRequest.httpRequest(map, CommonUrl.askYuepai);

    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case StatusCode.REQUEST_BAOMING_YUEPAI_USERSUCCESS: //成功返回报名人列表
                {
                    userDetailAdapter = new UserDetailAdapter(SelectUserActivity.this,userModelList);
                    listView.setAdapter(userDetailAdapter);
                    break;
                }
                case StatusCode.REQUEST_SELECT_YUEPAIUSER_SUCCESS: //成功
                {
                    CommonUtils.getUtilInstance().showToast(APP.context, "已成功选择");

                }
            }
        }
    };

    @Override
    public void requestFinish(String result, String requestUrl) throws JSONException {
        Message msg = new Message();
        if(requestUrl.equals(CommonUrl.askYuepai)){
            JSONObject object = new JSONObject(result);
            int code  = Integer.valueOf(object.getString("code"));
            Log.d("aaaaaaa",object.toString());
            switch (code){
                case StatusCode.REQUEST_BAOMING_YUEPAI_USERSUCCESS: //成功返回报名人列表
                {
                    JSONArray content = object.getJSONArray("contents");
                    for(int i = 0;i < content.length();i++){
                        JSONObject user = content.getJSONObject(i);
                        UserModel userModel = new UserModel();
                        userModel.setHeadImage(user.getString("uimage"));
                        userModel.setSign(user.getString("usign"));
                        userModel.setId(user.getString("uid"));
                        userModel.setNickName(user.getString("ualais"));
                        if(user.getInt("uchoosed")==1) userModel.setIndex(true);
                        else userModel.setIndex(false);
                        userModelList.add(userModel);
                    }
                    Log.d("sssssss",object.toString());
                    msg.what = StatusCode.REQUEST_BAOMING_YUEPAI_USERSUCCESS;
                    handler.sendMessage(msg);
                    break;
                }
            }
        }
        else if(requestUrl.equals(CommonUrl.getOrdersInfo)){
            JSONObject object = new JSONObject(result);
            int code  = Integer.valueOf(object.getString("code"));
            Log.d("aaaaaaa",object.toString());
            switch (code){
                case StatusCode.REQUEST_SELECT_YUEPAIUSER_SUCCESS:
                {
                    msg.what = StatusCode.REQUEST_SELECT_YUEPAIUSER_SUCCESS;
                    handler.sendMessage(msg);
                    break;
                }
            }
        }
    }

    @Override
    public void exception(IOException e, String requestUrl) {

    }
}
