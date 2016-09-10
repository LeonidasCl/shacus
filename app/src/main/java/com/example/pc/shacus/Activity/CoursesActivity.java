package com.example.pc.shacus.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;

import com.bumptech.glide.Glide;
import com.example.pc.shacus.APP;
import com.example.pc.shacus.Adapter.CommonPagerAdapter;
import com.example.pc.shacus.Adapter.CourseListAdapter;
import com.example.pc.shacus.Data.Cache.ACache;
import com.example.pc.shacus.Data.Model.CoursesModel;
import com.example.pc.shacus.Data.Model.LoginDataModel;
import com.example.pc.shacus.Data.Model.UserModel;
import com.example.pc.shacus.Fragment.FinishedCourseFragment;
import com.example.pc.shacus.Fragment.UndoCourseFragment;
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
 * Created by 孙启凡
 */
//课程界面（二级）

public class CoursesActivity extends AppCompatActivity implements  NetworkCallbackInterface.NetRequestIterface,TabLayout.OnTabSelectedListener,View.OnClickListener{


    private ImageButton returnButton;
    private ImageButton imageButton1;
    private ViewPager mPager;
    private int itemid;
    private ACache aCache;
    private NetRequest netRequest;
    private NetRequest netRequest1;
    String userId = null;
    String authkey = null;

    UserModel user = null;
    String url=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);

        returnButton=(ImageButton)findViewById(R.id.return1);
        imageButton1=(ImageButton)findViewById(R.id.imagebutton);

        returnButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
          finish();
      }
  });
        imageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        aCache=ACache.get(this);


        LoginDataModel loginModel = (LoginDataModel)aCache.getAsObject("loginModel");
        netRequest=new NetRequest(this,this);




        user = loginModel.getUserModel();
        userId = user.getId();
        authkey = user.getAuth_key();
        TabLayout tabLayout = (TabLayout)findViewById(R.id.discover_tab_bar);
        mPager = (ViewPager) findViewById(R.id.discover_viewPager);
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new UndoCourseFragment());
        fragments.add(new FinishedCourseFragment());
        CommonPagerAdapter adapter = new CommonPagerAdapter(getSupportFragmentManager(), fragments);
        mPager.setAdapter(adapter);

        TabLayout.Tab tab = tabLayout.newTab();
        tab.setText("课程表");
        tabLayout.addTab(tab);
        tab = tabLayout.newTab();
        tab.setText("完成课程");
        tabLayout.addTab(tab);
        tabLayout.setTabTextColors(Color.WHITE, Color.BLACK);
        tabLayout.setSelectedTabIndicatorColor(Color.TRANSPARENT);




        mPager.addOnPageChangeListener(
                new TabLayout.TabLayoutOnPageChangeListener(tabLayout)
        );
        tabLayout.setOnTabSelectedListener(this);
    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        mPager.setCurrentItem(tab.getPosition(), false);


    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }


    @Override
    public void requestFinish (String result, String requestUrl)throws JSONException {

        if(requestUrl.equals(CommonUrl.courseInfo)){//返回收藏信息
            JSONObject object = new JSONObject(result);
            int code = Integer.valueOf(object.getString("code"));
            Message msg = new Message();

            switch (code){
                case StatusCode.REQUEST_UNDO_FAIL:
                {
                    msg.what=StatusCode.REQUEST_UNDO_FAIL;
                    handler.sendMessage(msg);
                    break;
                }

                case StatusCode.REQUEST_DETAIL_SECCESS: {
                    JSONObject object1 = object.getJSONObject("contents");
                    JSONObject object2=object1.getJSONObject("course");
                    url = object2.getString("Curl");
                    msg.what=StatusCode.REQUEST_DETAIL_SECCESS;
                    handler.sendMessage(msg);
                    break;
                }
                case StatusCode.REQUSET_DETAIL_INVALID:
                {
                    msg.what=StatusCode.REQUSET_DETAIL_INVALID;
                    handler.sendMessage(msg);
                    break;

                    }
                    case StatusCode.REQUEST_DISCOLLECT_SUCCESS:
                    {
                        msg.what=StatusCode.REQUEST_DISCOLLECT_SUCCESS;
                        handler.sendMessage(msg);
                        break;
                    }
                    case StatusCode.REQUEST_DISCOLLECT_ALREADY:
                    {
                        msg.what=StatusCode.REQUEST_DISCOLLECT_ALREADY;
                        handler.sendMessage(msg);
                        break;
                    }
                    case StatusCode.REQUEST_DISCOLLECT_EVER:
                    {
                        msg.what=StatusCode.REQUEST_DISCOLLECT_EVER;
                        handler.sendMessage(msg);
                        break;
                    }
                    case StatusCode.REQUEST_SEVER_FAIL:
                    {
                        msg.what=StatusCode.REQUEST_SEVER_FAIL;
                        handler.sendMessage(msg);
                        break;
                    }
                    case StatusCode.REQUEST_COURSE_INVALID:
                    {

                        msg.what=StatusCode.REQUEST_COURSE_INVALID;
                        handler.sendMessage(msg);
                        break;
                    }
                    case StatusCode.REQUEST_COLLECT_AUTHKEYWRONG:
                    {
                        msg.what=StatusCode.REQUEST_COLLECT_AUTHKEYWRONG;
                        handler.sendMessage(msg);
                        break;
                    }
                    case StatusCode.REQUEST_COLLECT_SUCCESS:
                    {
                        msg.what=StatusCode.REQUEST_COLLECT_SUCCESS;
                        handler.sendMessage(msg);
                        break;
                    }
                    case StatusCode.REQUEST_COLLECT_ALREADY:
                    {
                        msg.what=StatusCode.REQUEST_COLLECT_ALREADY;
                        handler.sendMessage(msg);
                        break;
                    }
                }
            }


        }
//
    //}

    @Override
    public void exception (IOException e, String requestUrl){

    }

private int itemCollect;
ImageView collect;
    ImageView cancel;
    @Override
    public void onClick(View v) {
        List list = new ArrayList();
        list = (List) v.getTag();
        int i = (int) list.get(0);
        if( i == 2){
            int position = (int) list.get(1);
            itemid=(int)list.get(2);
            Message msg = new Message();
            msg.what = StatusCode.REQUEST_DETAIL_COURSE;
            handler.sendMessage(msg);


        }
        if(i==1){
            int position = (int) list.get(1);
            itemid=(int)list.get(2);
            itemCollect=(int)list.get(3);
            collect=(ImageView)list.get(4);
            cancel=(ImageView)list.get(5);
            if (itemCollect == 1) {

            //    collect.setImageResource(R.drawable.button_pop_down);
                Message msg = new Message();
                msg.what = StatusCode.REQUEST_DISCOLLECT_COURSE;
                handler.sendMessage(msg);
            }
            if(itemCollect == 0){

              //  collect.setImageResource(R.drawable.button_model_up);
                Message msg = new Message();
                msg.what = StatusCode.REQUEST_COLLECT_COURSE;
                handler.sendMessage(msg);
            }

        }

    }
    private Handler handler=new Handler(){

        @Override
        public void handleMessage(Message msg) {
            if (msg.what==StatusCode.REQUSET_FINISHED_FAIL){
                CommonUtils.getUtilInstance().showToast(APP.context, "请求失败!");
            }

            if (msg.what==StatusCode.REQUEST_DETAIL_COURSE){
                LoginDataModel loginModel = (LoginDataModel)aCache.getAsObject("loginModel");
                user = loginModel.getUserModel();
                userId = user.getId();
                authkey = user.getAuth_key();
                Map map1=new HashMap();
                map1.put("uid",userId);
                map1.put("authkey",authkey);
                map1.put("type",StatusCode.REQUEST_DETAIL_COURSE);
                map1.put("cid", itemid);
                netRequest.httpRequest(map1, CommonUrl.courseInfo);
            }
            if (msg.what==StatusCode.REQUEST_DETAIL_SECCESS){
                Intent intent = new Intent(CoursesActivity.this,CourseWebViewActivity.class);
                intent.putExtra("detail", url);
                startActivity(intent);

            }
            if (msg.what==StatusCode.REQUSET_DETAIL_INVALID){
                CommonUtils.getUtilInstance().showToast(APP.context, "教程不存在！");
            }
            if (msg.what==StatusCode.REQUEST_DISCOLLECT_COURSE){
                aCache = ACache.get(CoursesActivity.this);
                LoginDataModel loginModel = (LoginDataModel)aCache.getAsObject("loginModel");
                netRequest = new NetRequest(CoursesActivity.this,CoursesActivity.this);
                user = loginModel.getUserModel();
                userId = user.getId();
                authkey = user.getAuth_key();
                Map map1=new HashMap();
                map1.put("uid",userId);
                map1.put("authkey",authkey);
                map1.put("cid", itemid);
                map1.put("type", StatusCode.REQUEST_DISCOLLECT_COURSE);
                netRequest.httpRequest(map1, CommonUrl.courseFav);
                finish();
                Intent intent=new Intent(CoursesActivity.this,CoursesActivity.class);
                startActivity(intent);




            }
            if (msg.what==StatusCode.REQUEST_DISCOLLECT_SUCCESS){
                CommonUtils.getUtilInstance().showToast(APP.context, "已取消收藏");

            }
            if (msg.what==StatusCode.REQUEST_DISCOLLECT_ALREADY){
                CommonUtils.getUtilInstance().showToast(APP.context, "已取消过收藏");
            }
            if (msg.what==StatusCode.REQUEST_DISCOLLECT_EVER){
                CommonUtils.getUtilInstance().showToast(APP.context, "你没有收藏过次课程");

            }
            if(msg.what==StatusCode.REQUEST_SEVER_FAIL){
                CommonUtils.getUtilInstance().showToast(APP.context, "服务器错误");
            }
            if(msg.what==StatusCode.REQUEST_COURSE_INVALID){
                CommonUtils.getUtilInstance().showToast(APP.context, "该教程无效");
            }
            if(msg.what==StatusCode.REQUEST_COLLECT_AUTHKEYWRONG){
                CommonUtils.getUtilInstance().showToast(APP.context, "授权码不正确");
            }
            if(msg.what==StatusCode.REQUEST_COLLECT_COURSE){
                aCache = ACache.get(CoursesActivity.this);
                LoginDataModel loginModel = (LoginDataModel)aCache.getAsObject("loginModel");
                netRequest = new NetRequest(CoursesActivity.this,CoursesActivity.this);
                user = loginModel.getUserModel();
                userId = user.getId();
                authkey = user.getAuth_key();
                Map map1=new HashMap();
                map1.put("uid",userId);
                map1.put("authkey",authkey);
                map1.put("cid", itemid);
                map1.put("type",StatusCode.REQUEST_COLLECT_COURSE);
                netRequest.httpRequest(map1, CommonUrl.courseFav);
                finish();
                Intent intent=new Intent(CoursesActivity.this,CoursesActivity.class);
                startActivity(intent);

            }
            if(msg.what==StatusCode.REQUEST_COLLECT_SUCCESS){
                CommonUtils.getUtilInstance().showToast(APP.context, "收藏成功");
            }
            if(msg.what==StatusCode.REQUEST_COLLECT_ALREADY){
                CommonUtils.getUtilInstance().showToast(APP.context, "已收藏过此课程");
            }
        }
    };

}
