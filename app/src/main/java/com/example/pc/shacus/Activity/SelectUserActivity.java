package com.example.pc.shacus.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.pc.shacus.Adapter.UserDetailAdapter;
import com.example.pc.shacus.Data.Cache.ACache;
import com.example.pc.shacus.Data.Model.LoginDataModel;
import com.example.pc.shacus.Data.Model.UserModel;
import com.example.pc.shacus.Network.NetRequest;
import com.example.pc.shacus.Network.NetworkCallbackInterface;
import com.example.pc.shacus.R;

import org.json.JSONException;

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

        title.setText("");
        for(int i = 0 ; i <10; i++){
            UserModel userModel = new UserModel();
            userModel.setNickName("崔崔");
            userModel.setSign("哈哈哈");
            userModelList.add(userModel);
        }
        userDetailAdapter = new UserDetailAdapter(SelectUserActivity.this,userModelList);
        listView.setAdapter(userDetailAdapter);
    }

    //获得报名用户的信息
    private void initUserInfo(){
        Map map = new HashMap<>();
        LoginDataModel loginDataModel = (LoginDataModel) aCache.getAsObject("loginModel");
        UserModel content = loginDataModel.getUserModel();
        String userId = content.getId();
        String authkey = content.getAuth_key();

    }

    private Handler handler = new Handler(){

    };

    @Override
    public void requestFinish(String result, String requestUrl) throws JSONException {

    }

    @Override
    public void exception(IOException e, String requestUrl) {

    }
}
