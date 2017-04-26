package com.example.pc.shacus.Activity;

import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.pc.shacus.Adapter.OrderFriendsAdapter;
import com.example.pc.shacus.Adapter.YuePaiAdapter;
import com.example.pc.shacus.Data.Model.OrderFriendsModel;
import com.example.pc.shacus.Data.Model.PhotographerModel;
import com.example.pc.shacus.Network.NetRequest;
import com.example.pc.shacus.Network.NetworkCallbackInterface;
import com.example.pc.shacus.Network.StatusCode;
import com.example.pc.shacus.R;
import com.example.pc.shacus.Util.CommonUrl;
import com.example.pc.shacus.View.RefreshView;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.example.pc.shacus.R.id.random;

public class OrderfriendsActivity extends AppCompatActivity implements NetworkCallbackInterface.NetRequestIterface{
    private RefreshView refreshLayout;
    private ListView listView;
    private TextView back,title;
    private List<OrderFriendsModel> mList = new ArrayList<>();
    private OrderFriendsAdapter personAdapter;
    private NetRequest requestFragment;
    private int type = 10904;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            switch(msg.what){
                case 10904:
                    personAdapter.refresh(mList);
                    personAdapter.notifyDataSetChanged();
                    refreshLayout.setRefreshing(false);
                    break;

            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderfriends);

        back=(TextView) findViewById(R.id.photoset_toolbar_back);
        back.setText("＜返回");
        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });

        title=(TextView)findViewById(R.id.photoset_toolbar_title);
        title.setText("约拍伴侣");

        refreshLayout = (RefreshView) findViewById(R.id.orderfriends_swiperefresh_layout);
        listView = (ListView)findViewById(R.id.orderfriends_list);

        personAdapter = new OrderFriendsAdapter(this,mList);
        listView.setAdapter(personAdapter);

        requestFragment = new NetRequest(this,this);

        doRefresh();

        refreshLayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
        refreshLayout.setColorSchemeResources(R.color.zero_black);

        refreshLayout.setOnRefreshListener(new RefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);
                doRefresh();
            }
        });

        refreshLayout.setOnLoadListener(new RefreshView.OnLoadListener() {
            @Override
            public void onLoad() {
                refreshLayout.setRefreshing(true);
                doRefresh();
            }
        });
    }

    private void doRefresh(){
        Map<String, Object> map = new HashMap<>();
        map.put("type",type);
        requestFragment.httpRequest(map, CommonUrl.companionList);
    }

    @Override
    public void requestFinish(String result, String requestUrl) throws JSONException {
        JSONObject json = new JSONObject(result);
        String code = json.getString("code");
        JSONArray array = json.getJSONArray("contents");
        if(code.equals("10904")){
            mList = new ArrayList<>();
            for(int i =0;i<array.length();i++){
                JSONObject info = array.getJSONObject(i);
                Gson gson = new Gson();
                OrderFriendsModel companion = gson.fromJson(info.toString(), OrderFriendsModel.class);
                mList.add(companion);
            }
            Message msg=mHandler.obtainMessage();
            msg.what= 10904;
            mHandler.sendMessage(msg);
        }
    }

    @Override
    public void exception(IOException e, String requestUrl) {
    }
}
