package com.example.pc.shacus.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.pc.shacus.Network.NetworkCallbackInterface;
import com.example.pc.shacus.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

//Author:LQ
//Time:8.29
//订单界面（二级）
public class OrdersActivity extends AppCompatActivity implements  NetworkCallbackInterface.NetRequestIterface{

    //订单列表
    private ListView orderListView;
    //每一列Item
    ArrayList<HashMap<String,Object>> orderViewItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        initData();
    }

    //初始化ListView数据
    private void initData() {
        orderListView = (ListView) findViewById(R.id.listView_order);
        orderViewItem =new ArrayList<HashMap<String, Object>>();

        //TODO:连接服务器后修改,存入数据
        for (int i = 0; i < 10; i++) {
            HashMap<String,Object> map=new HashMap<String,Object>();
            map.put("ActivityName","活动名");
            map.put("Status","交易成功");
            map.put("UserPhoto",R.drawable.personal_default_photo);//存入图片
            map.put("UserID","I'm MrQ");
            map.put("PutUpTime","2016.8.29");
            map.put("ActivityTimeAndPlace","14:00-18:00  九龙湖校区计算机楼235 10000￥/人");
            map.put("ActivityContent","限定男，要帅");
            orderViewItem.add(map);
        }
        //使用SimpleAdapter绑定数据Item
        SimpleAdapter adapter=new SimpleAdapter(OrdersActivity.this, orderViewItem,R.layout.activity_orders_item
                ,new String[]{"ActivityName","Status","UserPhoto","UserID","PutUpTime","ActivityTimeAndPlace","ActivityContent"}
                , new int[]{R.id.text_ActivityName,R.id.text_Status,R.id.btn_UserPhoto,R.id.text_UserID,R.id.text_putUpTime
                    ,R.id.text_activityWhenAndWhere,R.id.text_ActivityContent});
        orderListView.setAdapter(adapter);

        //TODO:DELETE
        orderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(OrdersActivity.this, "你点击了第" + position + "条订单", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void requestFinish(String result, String requestUrl) {

    }

    @Override
    public void exception(IOException e, String requestUrl) {

    }
}
