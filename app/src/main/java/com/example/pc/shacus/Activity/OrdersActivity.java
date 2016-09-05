package com.example.pc.shacus.Activity;

import android.os.Handler;
import android.os.Message;
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

import android.widget.TabHost;
import android.widget.TabWidget;

import com.example.pc.shacus.Adapter.RecyclerViewAdapter;
import com.example.pc.shacus.Data.Cache.ACache;
import com.example.pc.shacus.Data.Model.ItemModel;
import com.example.pc.shacus.Data.Model.LoginDataModel;
import com.example.pc.shacus.Data.Model.UserModel;
import com.example.pc.shacus.Network.NetRequest;
import com.example.pc.shacus.Network.NetworkCallbackInterface;
import com.example.pc.shacus.Network.StatusCode;
import com.example.pc.shacus.R;
import com.example.pc.shacus.Util.CommonUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 崔颖华 on 2016/9/3.
 */

public class OrdersActivity extends AppCompatActivity implements  NetworkCallbackInterface.NetRequestIterface{


    private ViewPager viewPager = null;
    private List<View> viewContainter = new ArrayList<View>();   //存放容器
    private ViewPagerAdapter viewPagerAdapter = null;   //声明适配器

    private TabHost mTabHost;
    private TabWidget mTabWidget = null;
    private RecyclerView recyclerView1;
    private RecyclerViewAdapter recyclerViewAdapter1;
    List<ItemModel> ordersItemList1;
    RecyclerView.LayoutManager layoutManager1;

    private RecyclerView recyclerView2;
    private RecyclerViewAdapter recyclerViewAdapter2;
    List<ItemModel> ordersItemList2;
    RecyclerView.LayoutManager layoutManager2;

    private RecyclerView recyclerView3;
    private RecyclerViewAdapter recyclerViewAdapter3;
    List<ItemModel> ordersItemList3;
    RecyclerView.LayoutManager layoutManager3;

    private ACache aCache;
    private NetRequest netRequest;
    public int index = StatusCode.REQUEST_REGIST_ORDER ;
    int spanCount = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        netRequest = new NetRequest(OrdersActivity.this,OrdersActivity.this);
        aCache = ACache.get(OrdersActivity.this);

        initMyTabHost();  //初始化TabHost
        // 绑定组件
        viewPager = (ViewPager) findViewById(R.id.order_viewpager);
        initViewPagerContainter();  //初始viewPager
        viewPagerAdapter = new ViewPagerAdapter();
        //设置adapter的适配器
        viewPager.setAdapter(viewPagerAdapter);
        //设置viewPager的监听器
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            //当 滑动 切换时
            @Override
            public void onPageSelected(int position) {

                mTabWidget.setCurrentTab(position);
                if (position == 0) {
                    index = StatusCode.REQUEST_REGIST_ORDER;
                    initOrderInfo();
                } else if (position == 1) {
                    index = StatusCode.REQUEST_DOING_ORDER;
                    initOrderInfo();
                } else {
                    index = StatusCode.REQUEST_DONE_ORDER;
                    initOrderInfo();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //TabHost的监听事件
        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if (tabId.equals("tab1")) { //报名中
                    viewPager.setCurrentItem(0);
                    index = StatusCode.REQUEST_REGIST_ORDER;
                    initOrderInfo();
                } else if (tabId.equals("tab2")) { //进行中
                    viewPager.setCurrentItem(1);
                    index = StatusCode.REQUEST_DOING_ORDER;
                    initOrderInfo();
                } else {  //已完成
                    viewPager.setCurrentItem(2);
                    index = StatusCode.REQUEST_DONE_ORDER;
                    initOrderInfo();
                }
            }

        });

        //解决开始时不显示viewPager
        /*mTabHost.setCurrentTab(1);
        mTabHost.setCurrentTab(0);*/
    }

    //初始化TabHost
    public void initMyTabHost(){
        //绑定id
        mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup();
        mTabWidget = mTabHost.getTabWidget();
        /**
         * newTabSpec（）   就是给每个Tab设置一个ID
         * setIndicator()   每个Tab的标题
         * setCount()       每个Tab的标签页布局
         */
        mTabHost.addTab(mTabHost.newTabSpec("tab1").setContent(R.id.order_tab1).setIndicator("报名中"));
        mTabHost.addTab(mTabHost.newTabSpec("tab2").setContent(R.id.order_tab2).setIndicator("进行中"));
        mTabHost.addTab(mTabHost.newTabSpec("tab3").setContent(R.id.order_tab3).setIndicator("已完成"));

    }

    //初始化viewPager
    public void initViewPagerContainter(){

        //建立两个view的样式，并找到他们
        View view_1 = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_recyclerview_container,null);
        View view_2 = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_recyclerview_container,null);
        View view_3 = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_recyclerview_container,null);

        //加入ViewPage的容器
        viewContainter.add(view_1);
        viewContainter.add(view_2);
        viewContainter.add(view_3);

        recyclerView1 = (RecyclerView) view_1.findViewById(R.id.recyclerView);
        recyclerView2 = (RecyclerView) view_2.findViewById(R.id.recyclerView);
        recyclerView3 = (RecyclerView) view_3.findViewById(R.id.recyclerView);

        initOrderInfo();
    }


    //获得订单信息
    private void initOrderInfo(){

        ordersItemList1 = new ArrayList<>();
        ordersItemList2 = new ArrayList<>();
        ordersItemList3 = new ArrayList<>();

        LoginDataModel loginModel = (LoginDataModel)aCache.getAsObject("loginModel");
        UserModel user = null;
        Map map = new HashMap<>();
        String userId = null;
        String authkey = null;

        user = loginModel.getUserModel();
        userId = user.getId();
        authkey = user.getAuth_key();

        switch (index){
            case StatusCode.REQUEST_REGIST_ORDER:
            {
                map.put("uid",userId);
                map.put("authkey",authkey);
                map.put("type",StatusCode.REQUEST_REGIST_ORDER);
                netRequest.httpRequest(map, CommonUrl.getOrdersInfo);
            }
            case StatusCode.REQUEST_DOING_ORDER:
            {
                map.put("uid",userId);
                map.put("authkey",authkey);
                map.put("type",StatusCode.REQUEST_DOING_ORDER);
                netRequest.httpRequest(map, CommonUrl.getOrdersInfo);
            }
            case StatusCode.REQUEST_DONE_ORDER:
            {
                map.put("uid",userId);
                map.put("authkey",authkey);
                map.put("type",StatusCode.REQUEST_DONE_ORDER);
                netRequest.httpRequest(map, CommonUrl.getOrdersInfo);
            }
        }
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case StatusCode.REQUEST_REGIST_SUCCESS:
                {
                    recyclerViewAdapter1 = new RecyclerViewAdapter(ordersItemList1,OrdersActivity.this);
                    recyclerView1.setAdapter(recyclerViewAdapter1);
                    layoutManager1 = new StaggeredGridLayoutManager(spanCount,StaggeredGridLayoutManager.VERTICAL);
                    recyclerView1.setLayoutManager(layoutManager1);
                    break;
                }
                case  StatusCode.REQUEST_DOING_SUCCESS:
                {
                    recyclerViewAdapter2 = new RecyclerViewAdapter(ordersItemList2,OrdersActivity.this);
                    recyclerView2.setAdapter(recyclerViewAdapter2);
                    layoutManager2 = new StaggeredGridLayoutManager(spanCount,StaggeredGridLayoutManager.VERTICAL);
                    recyclerView2.setLayoutManager(layoutManager2);
                    break;
                }
                case  StatusCode.REQUEST_DONE_SUCCESS:
                {
                    recyclerViewAdapter3 = new RecyclerViewAdapter(ordersItemList3,OrdersActivity.this);
                    recyclerView3.setAdapter(recyclerViewAdapter3);
                    layoutManager3 = new StaggeredGridLayoutManager(spanCount,StaggeredGridLayoutManager.VERTICAL);
                    recyclerView3.setLayoutManager(layoutManager3);
                    break;
                }
            }
        }
    };


    //内部类实现viewpager的适配器
    private class ViewPagerAdapter extends PagerAdapter {


        //该方法 决定 并 返回 viewpager中组件的数量
        @Override
        public int getCount() {
            return viewContainter.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }
        //滑动切换的时候，消除当前组件
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(viewContainter.get(position));
        }
        //每次滑动的时候生成的组件
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(viewContainter.get(position));
            return viewContainter.get(position);
        }
    }


    @Override
    public void requestFinish(String result, String requestUrl) throws JSONException {
        if(requestUrl.equals(CommonUrl.getOrdersInfo)){//返回订单信息
            JSONObject object = new JSONObject(result);
            int code = Integer.valueOf(object.getString("code"));
            Message msg = new Message();

            switch (code){
                case StatusCode.REQUEST_REGIST_SUCCESS: //请求已报名的成功
                {
                    JSONObject jsonObject = object.getJSONObject("contents");

                    if(jsonObject.getJSONArray("myappointment") != null){
                        JSONArray jsonArray1 = jsonObject.getJSONArray("myappointment");
                        for (int i = 0; i < jsonArray1.length();i++){
                            JSONObject myat = jsonArray1.getJSONObject(i);
                            ItemModel ordersModel = new ItemModel();
                            ordersModel.setTitle(myat.getString("APtitle"));
                            ordersModel.setId(myat.getInt("APid"));
                            ordersModel.setImage(myat.getString("APimgurl"));
                            ordersModel.setStartTime(myat.getString("APstartT"));
                            ordersModel.setLikeNum(myat.getInt("APlikeN"));
                            ordersModel.setUserImage(myat.getString("Userimg"));
                            ordersModel.setRegistNum(myat.getInt("APregistN"));
                            Log.d("oooooooooooo", ordersModel.getTitle());
                            ordersItemList1.add(ordersModel);
                        }
                    }

                    if(jsonObject.getJSONArray("entryappointment")!= null){
                        JSONArray jsonArray2 = jsonObject.getJSONArray("entryappointment");
                        for (int i = 0; i < jsonArray2.length();i++){
                            JSONObject myat = jsonArray2.getJSONObject(i);
                            ItemModel ordersModel = new ItemModel();
                            ordersModel.setTitle(myat.getString("APtitle"));
                            ordersModel.setId(myat.getInt("APid"));
                            ordersModel.setImage(myat.getString("APimgurl"));
                            ordersModel.setStartTime(myat.getString("APstartT"));
                            ordersModel.setLikeNum(myat.getInt("APlikeN"));
                            ordersModel.setUserImage(myat.getString("Userimg"));
                            ordersModel.setRegistNum(myat.getInt("APregistN"));
                            ordersItemList1.add(ordersModel);
                        }
                    }

                    if(jsonObject.getJSONArray("activity") != null){
                        JSONArray jsonArray3 = jsonObject.getJSONArray("activity");
                        for (int i = 0; i < jsonArray3.length();i++){
                            JSONObject myat = jsonArray3.getJSONObject(i);
                            ItemModel ordersModel = new ItemModel();
                            ordersModel.setTitle(myat.getString("ACtitle"));
                            ordersModel.setId(myat.getInt("ACid"));
                            ordersModel.setImage(myat.getString("ACimgurl"));
                            ordersModel.setStartTime(myat.getString("ACstartT"));
                            ordersModel.setLikeNum(myat.getInt("AClikeN"));
                            ordersModel.setRegistNum(myat.getInt("ACregistN"));
                            ordersItemList1.add(ordersModel);
                        }
                    }
                    msg.what = StatusCode.REQUEST_REGIST_SUCCESS;
                    handler.sendMessage(msg);

                    break;



                }
                case StatusCode.REQUEST_DOING_SUCCESS: //请求正在进行中成功
                {
                    JSONObject jsonObject = object.getJSONObject("contents");
                    if(jsonObject.getJSONArray("myappointment") != null){
                        JSONArray jsonArray1 = jsonObject.getJSONArray("myappointment");
                        for (int i = 0; i < jsonArray1.length();i++) {
                            JSONObject myat = jsonArray1.getJSONObject(i);
                            ItemModel ordersModel = new ItemModel();
                            ordersModel.setTitle(myat.getString("APtitle"));
                            ordersModel.setId(myat.getInt("APid"));
                            ordersModel.setImage(myat.getString("APimgurl"));
                            ordersModel.setStartTime(myat.getString("APstartT"));
                            ordersModel.setLikeNum(myat.getInt("APlikeN"));
                            ordersModel.setUserImage(myat.getString("Userimg"));
                            ordersModel.setRegistNum(myat.getInt("APregistN"));
                            ordersItemList2.add(ordersModel);
                        }
                    }

                    if(jsonObject.getJSONArray("entryappointment") != null){
                        JSONArray jsonArray2 = jsonObject.getJSONArray("entryappointment");
                        for (int i = 0; i < jsonArray2.length();i++){
                            JSONObject myat = jsonArray2.getJSONObject(i);
                            ItemModel ordersModel = new ItemModel();
                            ordersModel.setTitle(myat.getString("APtitle"));
                            ordersModel.setId(myat.getInt("APid"));
                            ordersModel.setImage(myat.getString("APimgurl"));
                            ordersModel.setStartTime(myat.getString("APstartT"));
                            ordersModel.setLikeNum(myat.getInt("APlikeN"));
                            ordersModel.setUserImage(myat.getString("Userimg"));
                            ordersModel.setRegistNum(myat.getInt("APregistN"));
                            ordersItemList2.add(ordersModel);
                        }
                    }

                    if(jsonObject.getJSONArray("activity") != null){
                        JSONArray jsonArray3 = jsonObject.getJSONArray("activity");
                        for (int i = 0; i < jsonArray3.length();i++){
                            JSONObject myat = jsonArray3.getJSONObject(i);
                            ItemModel ordersModel = new ItemModel();
                            ordersModel.setTitle(myat.getString("ACtitle"));
                            ordersModel.setId(myat.getInt("ACid"));
                            ordersModel.setImage(myat.getString("ACimgurl"));
                            ordersModel.setStartTime(myat.getString("ACstartT"));
                            ordersModel.setLikeNum(myat.getInt("AClikeN"));
                            ordersModel.setRegistNum(myat.getInt("ACregistN"));
                            ordersItemList2.add(ordersModel);
                        }
                    }

                    msg.what = StatusCode.REQUEST_DOING_SUCCESS;
                    handler.sendMessage(msg);
                    break;

                }
                case StatusCode.REQUEST_DONE_SUCCESS: //请求已完成成功
                {
                    JSONObject jsonObject = object.getJSONObject("contents");
                    if(jsonObject.getJSONArray("myappointment") != null){
                        JSONArray jsonArray1 = jsonObject.getJSONArray("myappointment");
                        for (int i = 0; i < jsonArray1.length();i++){
                            JSONObject myat = jsonArray1.getJSONObject(i);
                            ItemModel ordersModel = new ItemModel();
                            ordersModel.setTitle(myat.getString("APtitle"));
                            ordersModel.setId(myat.getInt("APid"));
                            ordersModel.setImage(myat.getString("APimgurl"));
                            ordersModel.setStartTime(myat.getString("APstartT"));
                            ordersModel.setLikeNum(myat.getInt("APlikeN"));
                            ordersModel.setUserImage(myat.getString("Userimg"));
                            ordersModel.setRegistNum(myat.getInt("APregistN"));
                            ordersItemList3.add(ordersModel);
                        }
                    }

                    if (jsonObject.getJSONArray("entryappointment")!= null){
                        JSONArray jsonArray2 = jsonObject.getJSONArray("entryappointment");
                        for (int i = 0; i < jsonArray2.length();i++){
                            JSONObject myat = jsonArray2.getJSONObject(i);
                            ItemModel ordersModel = new ItemModel();
                            ordersModel.setTitle(myat.getString("APtitle"));
                            ordersModel.setId(myat.getInt("APid"));
                            ordersModel.setImage(myat.getString("APimgurl"));
                            ordersModel.setStartTime(myat.getString("APstartT"));
                            ordersModel.setLikeNum(myat.getInt("APlikeN"));
                            ordersModel.setUserImage(myat.getString("Userimg"));
                            ordersModel.setRegistNum(myat.getInt("APregistN"));
                            ordersItemList3.add(ordersModel);
                        }

                    }

                    if(jsonObject.getJSONArray("activity") != null){
                        JSONArray jsonArray3 = jsonObject.getJSONArray("activity");
                        for (int i = 0; i < jsonArray3.length();i++){
                            JSONObject myat = jsonArray3.getJSONObject(i);
                            ItemModel ordersModel = new ItemModel();
                            ordersModel.setTitle(myat.getString("ACtitle"));
                            ordersModel.setId(myat.getInt("ACid"));
                            ordersModel.setImage(myat.getString("ACimgurl"));
                            ordersModel.setStartTime(myat.getString("ACstartT"));
                            ordersModel.setLikeNum(myat.getInt("AClikeN"));
                            ordersModel.setRegistNum(myat.getInt("ACregistN"));
                            ordersItemList3.add(ordersModel);
                        }
                    }
                    msg.what = StatusCode.REQUEST_DONE_SUCCESS;
                    handler.sendMessage(msg);

                    break;

                }
                case StatusCode.REQUEST_ORDER_ERROR: //请求订单授权码错误
                {

                }
            }

        }

    }

    @Override
    public void exception(IOException e, String requestUrl) {

    }
}
