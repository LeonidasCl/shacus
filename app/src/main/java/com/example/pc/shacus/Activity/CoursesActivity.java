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
//使用ListView实现课程列表，
//我的学习界面（二级）
public class CoursesActivity extends AppCompatActivity implements  NetworkCallbackInterface.NetRequestIterface{


    //课程列表
    private ListView coursesListView;
    //存储每一列的Item
    ArrayList<HashMap<String,Object>> coursesViewItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);

        initData();
    }

    //初始化
    private void initData() {
        coursesListView = (ListView) findViewById(R.id.listView_course);
        coursesViewItem =new ArrayList<HashMap<String, Object>>();

        //存入数据//TODO:连接服务器后修改
        for (int i = 0; i < 10; i++) {
            HashMap<String,Object> map=new HashMap<String,Object>();
            map.put("ItemImage",R.drawable.ic_launcher);//存入图片
            map.put("ItemName",i+"号视频");//存入视频名字
            map.put("ItemStatus","没看过啊");//是否看过
            map.put("ItemViewNum",(i*1000)+"人看过");//观看人数
            coursesViewItem.add(map);
        }
        //使用SimpleAdapter绑定数据Item
        SimpleAdapter adapter=new SimpleAdapter(CoursesActivity.this, coursesViewItem,R.layout.activity_courses_activity_item
                ,new String[]{"ItemImage","ItemName","ItemStatus","ItemViewNum"},new int[]{R.id.ItemImage,R.id.ItemName,R.id.btn_ItemStatus,R.id.ItemViewNum});
        coursesListView.setAdapter(adapter);

        //TODO:DELETE
        coursesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(CoursesActivity.this, "你点击了第" + position + "条视频", Toast.LENGTH_SHORT).show();
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
