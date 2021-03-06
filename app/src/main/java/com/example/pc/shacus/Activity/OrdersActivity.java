package com.example.pc.shacus.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.example.pc.shacus.APP;
import com.example.pc.shacus.Adapter.RecyclerViewAdapter;
import com.example.pc.shacus.Data.Cache.ACache;
import com.example.pc.shacus.Data.Model.ItemModel;
import com.example.pc.shacus.Data.Model.LoginDataModel;
import com.example.pc.shacus.Data.Model.UserModel;
import com.example.pc.shacus.Data.Model.YuePaiDataModel;
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

import static com.example.pc.shacus.R.id.recyclerView;

/**
 * Created by 崔颖华 on 2016/9/3.
 */

public class OrdersActivity extends AppCompatActivity implements NetworkCallbackInterface.NetRequestIterface ,View.OnClickListener{


    private ViewPager viewPager = null;
    private List<View> viewContainter = new ArrayList<View>();   //存放容器
    private ViewPagerAdapter viewPagerAdapter = null;   //声明适配器

    private TabHost mTabHost;
    private TabWidget mTabWidget = null;
    private RecyclerView recyclerView1;
    private RecyclerViewAdapter recyclerViewAdapter1;
    List<YuePaiDataModel> ordersItemList1;
    RecyclerView.LayoutManager layoutManager1;

    private RecyclerView recyclerView2;
    private RecyclerViewAdapter recyclerViewAdapter2;
    List<YuePaiDataModel> ordersItemList2;
    RecyclerView.LayoutManager layoutManager2;

    private RecyclerView recyclerView3;
    private RecyclerViewAdapter recyclerViewAdapter3;
    List<YuePaiDataModel> ordersItemList3;
    RecyclerView.LayoutManager layoutManager3;

    private RecyclerView recyclerView4;
    private RecyclerViewAdapter recyclerViewAdapter4;
    List<YuePaiDataModel> ordersItemList4;
    RecyclerView.LayoutManager layoutManager4;

    private ACache aCache;
    private NetRequest netRequest;
    public int index = StatusCode.REQUEST_REGIST_ORDER ;
    private FrameLayout loading1;
    private FrameLayout loading2;
    private FrameLayout loading3;
    private FrameLayout loading4;

    int spanCount = 2;
    View view_1;
    View view_2;
    View view_3;
    View view_4;

    private LinearLayout invis1;
    private LinearLayout invis2;
    private LinearLayout invis3;
    private LinearLayout invis4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        TextView textView = (TextView) findViewById(R.id.orders_title);
        textView.setText("我的订单");

//        invis = (LinearLayout) findViewById(R.id.invis);
//        invis.setVisibility(View.INVISIBLE);

        netRequest = new NetRequest(OrdersActivity.this,OrdersActivity.this);
        aCache = ACache.get(OrdersActivity.this);

        String page = null;
        Intent intent = getIntent();
        page = intent.getStringExtra("page");

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
                mTabHost.setCurrentTab(position);
                updateTab(mTabHost);
                if (position == 0) {
                    loading1.setVisibility(View.VISIBLE);
                    index = StatusCode.REQUEST_REGIST_ORDER;
                    initOrderInfo();
                } else if (position == 1) {
                    loading2.setVisibility(View.VISIBLE);
                    index = StatusCode.REQUEST_DOING_ORDER;
                    initOrderInfo();
                } else if (position == 2) {
                    loading3.setVisibility(View.VISIBLE);
                    index = StatusCode.REQUEST_DONE_ORDER;
                    initOrderInfo();
                } else if (position == 3){
                    loading4.setVisibility(View.VISIBLE);
                    index = StatusCode.REQUEST_PJ_DONE_ORDER;
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
                    loading1.setVisibility(View.VISIBLE);
                    index = StatusCode.REQUEST_REGIST_ORDER;
                    updateTab(mTabHost);
                } else if (tabId.equals("tab2")) { //进行中
                    viewPager.setCurrentItem(1);
                    loading2.setVisibility(View.VISIBLE);
                    index = StatusCode.REQUEST_DOING_ORDER;
                    updateTab(mTabHost);
                } else if (tabId.equals("tab3")){  //已完成,未评价
                    viewPager.setCurrentItem(2);
                    loading3.setVisibility(View.VISIBLE);
                    index = StatusCode.REQUEST_DONE_ORDER;
                    updateTab(mTabHost);
                } else{
                    viewPager.setCurrentItem(3);
                    loading4.setVisibility(View.VISIBLE);
                    index = StatusCode.REQUEST_PJ_DONE_ORDER;
                    updateTab(mTabHost);
                }
            }

        });
        if(page == null){
            mTabHost.setCurrentTab(0);
        }else if (page.equals("3")){
            mTabHost.setCurrentTab(2);
        }else if(page.equals("2")){
            mTabHost.setCurrentTab(1);
        }

        ImageButton imageButton = (ImageButton) findViewById(R.id.orders_backbtn);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
        mTabHost.addTab(mTabHost.newTabSpec("tab3").setContent(R.id.order_tab3).setIndicator("未评价"));
        mTabHost.addTab(mTabHost.newTabSpec("tab4").setContent(R.id.order_tab3).setIndicator("已评价"));
        mTabHost.setCurrentTab(0);
        //初始化Tab的颜色，和字体的颜色
        for (int i = 0;i < mTabHost.getTabWidget().getChildCount();i++){
            TextView tv = (TextView) mTabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            tv.setTextColor(Color.parseColor("#ffffff"));
        }
        updateTab(mTabHost);

    }

    private void updateTab(TabHost tabHost){
        for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++)
        {
            View view = tabHost.getTabWidget().getChildAt(i);
            if (tabHost.getCurrentTab() == i)
            {
                //选中
                //view.setBackground(getResources().getDrawable(R.drawable.nepal));//选中后的背景
                view.setBackgroundColor(Color.parseColor("#44000000"));

            }
            else
            {
                //不选中
                //view.setBackground(getResources().getDrawable(R.drawable.sea));//非选择的背景
                view.setBackgroundColor(Color.parseColor("#1a1a1a"));
            }
        }
    }

    //初始化viewPager
    public void initViewPagerContainter(){

        //建立两个view的样式，并找到他们
        view_1 = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_recyclerview_container,null);
        view_2 = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_recyclerview_container,null);
        view_3 = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_recyclerview_container,null);
        view_4 = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_recyclerview_container,null);

        //加入ViewPage的容器
        viewContainter.add(view_1);
        viewContainter.add(view_2);
        viewContainter.add(view_3);
        viewContainter.add(view_4);

        recyclerView1 = (RecyclerView) view_1.findViewById(recyclerView);
        recyclerView2 = (RecyclerView) view_2.findViewById(recyclerView);
        recyclerView3 = (RecyclerView) view_3.findViewById(recyclerView);
        recyclerView4 = (RecyclerView) view_4.findViewById(recyclerView);

        loading1 = (FrameLayout) view_1.findViewById(R.id.wait_loading_layout);
        loading2 = (FrameLayout) view_2.findViewById(R.id.wait_loading_layout);
        loading3 = (FrameLayout) view_3.findViewById(R.id.wait_loading_layout);
        loading4 = (FrameLayout) view_4.findViewById(R.id.wait_loading_layout);

        invis1 = (LinearLayout) view_1.findViewById(R.id.invis);
        invis2 = (LinearLayout) view_2.findViewById(R.id.invis);
        invis3 = (LinearLayout) view_3.findViewById(R.id.invis);
        invis4 = (LinearLayout) view_4.findViewById(R.id.invis);

        invis1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView1.scrollToPosition(0);
            }
        });

        invis2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView2.scrollToPosition(0);
            }
        });

        invis3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView3.scrollToPosition(0);
            }
        });

        invis4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView4.scrollToPosition(0);
            }
        });

        initOrderInfo();
    }


    //获得订单信息
    private void initOrderInfo(){

        ordersItemList1 = new ArrayList<>();
        ordersItemList2 = new ArrayList<>();
        ordersItemList3 = new ArrayList<>();
        ordersItemList4 = new ArrayList<>();

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
                break;
            }
            case StatusCode.REQUEST_DOING_ORDER:
            {
                map.put("uid",userId);
                map.put("authkey",authkey);
                map.put("type", StatusCode.REQUEST_DOING_ORDER);
                netRequest.httpRequest(map, CommonUrl.getOrdersInfo);
                break;
            }
            case StatusCode.REQUEST_DONE_ORDER:
            {
                map.put("uid",userId);
                map.put("authkey",authkey);
                map.put("type",StatusCode.REQUEST_DONE_ORDER);
                netRequest.httpRequest(map, CommonUrl.getOrdersInfo);
                break;
            }
            case StatusCode.REQUEST_PJ_DONE_ORDER:
            {
                map.put("uid",userId);
                map.put("authkey",authkey);
                map.put("type",StatusCode.REQUEST_PJ_DONE_ORDER);
                netRequest.httpRequest(map, CommonUrl.getOrdersInfo);
                break;
            }
        }


    }

//    int pastVisiblesItems, visibleItemCount, totalItemCount;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Message message = new Message();
            switch (msg.what){
                case StatusCode.REQUEST_REGIST_SUCCESS:
                {
                    view_1 = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_recyclerview_container,null);
                    layoutManager1 = new StaggeredGridLayoutManager(spanCount,StaggeredGridLayoutManager.VERTICAL);
                    recyclerView1.setLayoutManager(layoutManager1);
                    recyclerViewAdapter1 = new RecyclerViewAdapter(ordersItemList1,OrdersActivity.this);
                    recyclerView1.setAdapter(recyclerViewAdapter1);
                    recyclerView1.setOnScrollListener(new MyRecyclerViewScrollListener(invis1));
                    message.what = 100;
                    this.sendMessageDelayed(message, 1000);
                    break;
                }
                case  StatusCode.REQUEST_DOING_SUCCESS:
                {
                    view_2 = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_recyclerview_container,null);
                    layoutManager2 = new StaggeredGridLayoutManager(spanCount,StaggeredGridLayoutManager.VERTICAL);
                    recyclerView2.setLayoutManager(layoutManager2);
                    recyclerViewAdapter2 = new RecyclerViewAdapter(ordersItemList2,OrdersActivity.this);
                    recyclerView2.setAdapter(recyclerViewAdapter2);
                    recyclerView2.setOnScrollListener(new MyRecyclerViewScrollListener(invis2));
                    message.what = 200;
                    this.sendMessageDelayed(message, 1000);
                    break;
                }
                case  StatusCode.REQUEST_DONE_SUCCESS:
                {
                    view_3 = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_recyclerview_container,null);
                    layoutManager3 = new StaggeredGridLayoutManager(spanCount,StaggeredGridLayoutManager.VERTICAL);
                    recyclerView3.setLayoutManager(layoutManager3);
                    recyclerViewAdapter3 = new RecyclerViewAdapter(ordersItemList3,OrdersActivity.this);
                    recyclerView3.setAdapter(recyclerViewAdapter3);
                    recyclerView3.setOnScrollListener(new MyRecyclerViewScrollListener(invis3));
                    message.what = 300;
                    this.sendMessageDelayed(message,1000);
                    break;
                }
                case StatusCode.REQUEST_PJ_DONE_SUCCESS:
                {
                    view_4 = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_recyclerview_container,null);
                    layoutManager4 = new StaggeredGridLayoutManager(spanCount,StaggeredGridLayoutManager.VERTICAL);
                    recyclerView4.setLayoutManager(layoutManager4);
                    recyclerViewAdapter4 = new RecyclerViewAdapter(ordersItemList4,OrdersActivity.this);
                    recyclerView4.setAdapter(recyclerViewAdapter4);
                    recyclerView4.setOnScrollListener(new MyRecyclerViewScrollListener(invis4));
                    message.what = 400;
                    this.sendMessageDelayed(message,1000);
                    break;
                }
                case StatusCode.REQUEST_ORDER_ERROR:
                {
                    CommonUtils.getUtilInstance().showToast(APP.context, "出错啦~请重试");
                    break;

                }
                case 111: //没有已报名
                {
                    TextView textView = (TextView) view_1.findViewById(R.id.none_item);
                    textView.setVisibility(View.VISIBLE);
                    recyclerView1.setVisibility(View.INVISIBLE);
                    textView.setText("没有报名的活动~快去“发现”看看吧");
                    loading1.setVisibility(View.GONE);
                    break;
                }
                case 222: //没有正在进行
                {
                    TextView textView = (TextView) view_2.findViewById(R.id.none_item);
                    textView.setVisibility(View.VISIBLE);
                    recyclerView2.setVisibility(View.INVISIBLE);
                    textView.setText("暂无正在进行的活动~快去“发现”看看吧");
                    loading2.setVisibility(View.GONE);
                    break;
                }
                case 333: //没有已完成
                {
                    TextView textView = (TextView) view_3.findViewById(R.id.none_item);
                    textView.setVisibility(View.VISIBLE);
                    recyclerView3.setVisibility(View.INVISIBLE);
                    textView.setText("暂无未评价");
                    loading3.setVisibility(View.GONE);
                    break;
                }
                case 444: //没有已完成
                {
                    TextView textView = (TextView) view_4.findViewById(R.id.none_item);
                    textView.setVisibility(View.VISIBLE);
                    recyclerView4.setVisibility(View.INVISIBLE);
                    textView.setText("暂无已评价完成");
                    loading4.setVisibility(View.GONE);
                    break;
                }

                case 88:{
                    CommonUtils.getUtilInstance().showToast(APP.context, "网络请求超时，请重试");
                    break;
                }
                case 100:{
                    loading1.setVisibility(View.GONE);
                    break;
                }
                case 200:{
                    loading2.setVisibility(View.GONE);
                    break;
                }
                case 300:{
                    loading3.setVisibility(View.GONE);
                    break;
                }
                case 400:{
                    loading4.setVisibility(View.GONE);
                    break;
                }
            }
        }
    };

    @Override
    public void onClick(View v) {
        List list = new ArrayList();
        list = (List) v.getTag();
        int i = (int) list.get(0);
        if( i == 2){
            int position = (int) list.get(1);
            Intent intent = new Intent(OrdersActivity.this,YuePaiDetailActivity_new.class);
            /*
            * Intent intent = new Intent(activity, YuePaiDetailActivity_new.class);
                    intent.putExtra("detail",String.valueOf(item.getAPid()));
                    intent.putExtra("type", "yuepai");
                    intent.putExtra("group", String.valueOf(item.getAPgroup()));
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    activity.startActivity(intent);*/
            if(index == StatusCode.REQUEST_REGIST_ORDER) {
                intent.putExtra("detail", String.valueOf(ordersItemList1.get(position).getAPid()));
                intent.putExtra("type", "yuepai");
                intent.putExtra("group",String.valueOf(ordersItemList1.get(position).getAPgroup()));
            } else if(index == StatusCode.REQUEST_DOING_ORDER){
                intent.putExtra("detail",String.valueOf(ordersItemList2.get(position).getAPid()));
                intent.putExtra("type", "yuepai");
                intent.putExtra("group",String.valueOf(ordersItemList2.get(position).getAPgroup()));
            } else if(index == StatusCode.REQUEST_DONE_ORDER){
                intent.putExtra("detail",String.valueOf(ordersItemList3.get(position).getAPid()));
                intent.putExtra("type", "yuepai");
                intent.putExtra("group",String.valueOf(ordersItemList3.get(position).getAPgroup()));
            } else if(index == StatusCode.REQUEST_PJ_DONE_ORDER){
                intent.putExtra("detail",String.valueOf(ordersItemList4.get(position).getAPid()));
                intent.putExtra("type", "yuepai");
                intent.putExtra("group",String.valueOf(ordersItemList4.get(position).getAPgroup()));
            }
            startActivity(intent);
        }
    }


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
            Log.d("======================",object.toString());
            int code = Integer.valueOf(object.getString("code"));
            Message msg = new Message();

            switch (code){
                case StatusCode.REQUEST_REGIST_SUCCESS: //请求已报名的成功
                {
                    int index = 0;
                    JSONObject jsonObject = object.getJSONObject("contents");

                    if(jsonObject.getJSONArray("myappointment").length() != 0){
                        JSONArray jsonArray1 = jsonObject.getJSONArray("myappointment");
                        for (int i = 0; i < jsonArray1.length();i++){
                            JSONObject myat = jsonArray1.getJSONObject(i);
                            YuePaiDataModel ordersModel = new YuePaiDataModel();
//                            ordersModel.setTitle(myat.getString("APtitle"));
                            ordersModel.setAPid(myat.getInt("APid"));
//                            ordersModel.setStartTime(myat.getString("APstartT"));
                            ordersModel.setAPlikeN(myat.getInt("APlikeN"));
                            ordersModel.setUserimg(myat.getString("Userimg"));
//                            ordersModel.setRegistNum(myat.getInt("APregistN"));
                            ordersModel.setAPcontent(myat.getString("APcontent"));
                            List<String>  list = new ArrayList<String>();
                            JSONArray jsonArray = myat.getJSONArray("APimgurl");
//                            Log.d("?????????????",jsonArray.toString());
                            for (int j = 0; j < jsonArray.length();j++){
                                list.add(jsonArray.getString(j));
                            }
                            ordersModel.setAPimgurl(list);
                            ordersItemList1.add(ordersModel);
                        }
                    }else{
                        index++;
                    }

                    if(jsonObject.getJSONArray("entryappointment").length()!= 0){
                        JSONArray jsonArray2 = jsonObject.getJSONArray("entryappointment");
                        for (int i = 0; i < jsonArray2.length();i++){
                            JSONObject myat = jsonArray2.getJSONObject(i);
                            YuePaiDataModel ordersModel = new YuePaiDataModel();
//                            ordersModel.setTitle(myat.getString("APtitle"));
                            ordersModel.setAPid(myat.getInt("APid"));
//                            ordersModel.setStartTime(myat.getString("APstartT"));
                            ordersModel.setAPlikeN(myat.getInt("APlikeN"));
                            ordersModel.setUserimg(myat.getString("Userimg"));
//                            ordersModel.setRegistNum(myat.getInt("APregistN"));
                            ordersModel.setAPcontent(myat.getString("APcontent"));
                            List<String>  list = new ArrayList<String>();
                            JSONArray jsonArray = myat.getJSONArray("APimgurl");
//                            Log.d("?????????????", jsonArray.toString());
                            for (int j = 0; j < jsonArray.length();j++){
                                list.add(jsonArray.getString(j));
                            }
                            ordersModel.setAPimgurl(list);
                            ordersItemList1.add(ordersModel);
                        }
                    }else{
                        index++;
                    }

/*                    if(jsonObject.getJSONArray("activity").length() != 0){
                        JSONArray jsonArray3 = jsonObject.getJSONArray("activity");
                        for (int i = 0; i < jsonArray3.length();i++){
                            JSONObject myat = jsonArray3.getJSONObject(i);
                            YuePaiDataModel ordersModel = new YuePaiDataModel();
//                            ordersModel.setTitle(myat.getString("APtitle"));
                            ordersModel.setAPid(myat.getInt("ACid"));
//                            ordersModel.setStartTime(myat.getString("APstartT"));
                            ordersModel.setAPlikeN(myat.getInt("AClikeN"));
                            ordersModel.setUserimg(myat.getString("Userimg"));
//                            ordersModel.setRegistNum(myat.getInt("APregistN"));
                            ordersModel.setAPcontent(myat.getString("APcontent"));
                            List<String>  list = new ArrayList<String>();
                            JSONArray jsonArray = myat.getJSONArray("APimgurl");
//                            Log.d("?????????????", jsonArray.toString());
                            for (int j = 0; j < jsonArray.length();j++){
                                list.add(jsonArray.getString(j));
                            }
                            ordersModel.setAPimgurl(list);
                            ordersItemList1.add(ordersModel);
                        }
                    }else{
                        index++;
                    }*/
                    index++;

                    if(index != 3){
                        Log.d("aaaaaaaa",ordersItemList1.toString());
                        msg.what = StatusCode.REQUEST_REGIST_SUCCESS;
                        handler.sendMessage(msg);
                        break;
                    }
                    else{
                        msg.what = 111;//没有已报名的订单
                        handler.sendMessage(msg);
                        break;
                    }

                }
                case StatusCode.REQUEST_DOING_SUCCESS: //请求正在进行中成功
                {
                    int index = 0;
                    JSONObject jsonObject = object.getJSONObject("contents");
                    if(jsonObject.getJSONArray("myappointment").length() != 0){
                        JSONArray jsonArray1 = jsonObject.getJSONArray("myappointment");
                        for (int i = 0; i < jsonArray1.length();i++){
                            JSONObject myat = jsonArray1.getJSONObject(i);
                            YuePaiDataModel ordersModel = new YuePaiDataModel();
//                            ordersModel.setTitle(myat.getString("APtitle"));
                            ordersModel.setAPid(myat.getInt("APid"));
//                            ordersModel.setStartTime(myat.getString("APstartT"));
                            ordersModel.setAPlikeN(myat.getInt("APlikeN"));
                            ordersModel.setUserimg(myat.getString("Userimg"));
//                            ordersModel.setRegistNum(myat.getInt("APregistN"));
                            ordersModel.setAPcontent(myat.getString("APcontent"));
                            List<String>  list = new ArrayList<String>();
                            JSONArray jsonArray = myat.getJSONArray("APimgurl");
//                            Log.d("?????????????", jsonArray.toString());
                            for (int j = 0; j < jsonArray.length();j++){
                                list.add(jsonArray.getString(j));
                            }
                            ordersModel.setAPimgurl(list);
                            ordersItemList2.add(ordersModel);
                        }
                    }else index++;

                    if(jsonObject.getJSONArray("entryappointment").length() != 0){
                        JSONArray jsonArray2 = jsonObject.getJSONArray("entryappointment");
                        for (int i = 0; i < jsonArray2.length();i++){
                            JSONObject myat = jsonArray2.getJSONObject(i);
                            YuePaiDataModel ordersModel = new YuePaiDataModel();
//                            ordersModel.setTitle(myat.getString("APtitle"));
                            ordersModel.setAPid(myat.getInt("APid"));
//                            ordersModel.setStartTime(myat.getString("APstartT"));
                            ordersModel.setAPlikeN(myat.getInt("APlikeN"));
                            ordersModel.setUserimg(myat.getString("Userimg"));
//                            ordersModel.setRegistNum(myat.getInt("APregistN"));
                            ordersModel.setAPcontent(myat.getString("APcontent"));
                            List<String>  list = new ArrayList<String>();
                            JSONArray jsonArray = myat.getJSONArray("APimgurl");
//                            Log.d("?????????????", jsonArray.toString());
                            for (int j = 0; j < jsonArray.length();j++){
                                list.add(jsonArray.getString(j));
                            }
                            ordersModel.setAPimgurl(list);
                            ordersItemList2.add(ordersModel);
                        }
                    }else index++;

/*                    if(jsonObject.getJSONArray("activity").length() != 0){
                        JSONArray jsonArray3 = jsonObject.getJSONArray("activity");
                        for (int i = 0; i < jsonArray3.length();i++){
                            JSONObject myat = jsonArray3.getJSONObject(i);
                            YuePaiDataModel ordersModel = new YuePaiDataModel();
//                            ordersModel.setTitle(myat.getString("APtitle"));
                            ordersModel.setAPid(myat.getInt("APid"));
//                            ordersModel.setStartTime(myat.getString("APstartT"));
                            ordersModel.setAPlikeN(myat.getInt("APlikeN"));
                            ordersModel.setUserimg(myat.getString("Userimg"));
//                            ordersModel.setRegistNum(myat.getInt("APregistN"));
                            ordersModel.setAPcontent(myat.getString("APcontent"));
                            List<String>  list = new ArrayList<String>();
                            JSONArray jsonArray = myat.getJSONArray("APimgurl");
//                            Log.d("?????????????", jsonArray.toString());
                            for (int j = 0; j < jsonArray.length();j++){
                                list.add(jsonArray.getString(j));
                            }
                            ordersModel.setAPimgurl(list);
                            ordersItemList2.add(ordersModel);
                        }
                    }else index++;*/
                    index++;
                    if(index != 3){
                        msg.what = StatusCode.REQUEST_DOING_SUCCESS;
                        handler.sendMessage(msg);
                        break;
                    }else{
                        msg.what = 222;//没有正在进行的订单
                        handler.sendMessage(msg);
                        break;
                    }


                }
                case StatusCode.REQUEST_DONE_SUCCESS: //请求已完成成功
                {
                    int index = 0;
                    JSONObject jsonObject = object.getJSONObject("contents");
                    if(jsonObject.getJSONArray("myappointment").length() != 0){
                        JSONArray jsonArray1 = jsonObject.getJSONArray("myappointment");
                        for (int i = 0; i < jsonArray1.length();i++){
                            JSONObject myat = jsonArray1.getJSONObject(i);
                            YuePaiDataModel ordersModel = new YuePaiDataModel();
//                            ordersModel.setTitle(myat.getString("APtitle"));
                            ordersModel.setAPid(myat.getInt("APid"));
//                            ordersModel.setStartTime(myat.getString("APstartT"));
                            ordersModel.setAPlikeN(myat.getInt("APlikeN"));
                            ordersModel.setUserimg(myat.getString("Userimg"));
//                            ordersModel.setRegistNum(myat.getInt("APregistN"));
                            ordersModel.setAPcontent(myat.getString("APcontent"));
                            List<String>  list = new ArrayList<String>();
                            JSONArray jsonArray = myat.getJSONArray("APimgurl");
//                            Log.d("?????????????", jsonArray.toString());
                            for (int j = 0; j < jsonArray.length();j++){
                                list.add(jsonArray.getString(j));
                            }
                            ordersModel.setAPimgurl(list);
                            ordersItemList3.add(ordersModel);
                        }
                    }else index++;

                    if (jsonObject.getJSONArray("entryappointment").length()!= 0){
                        JSONArray jsonArray2 = jsonObject.getJSONArray("entryappointment");
                        for (int i = 0; i < jsonArray2.length();i++){
                            JSONObject myat = jsonArray2.getJSONObject(i);
                            YuePaiDataModel ordersModel = new YuePaiDataModel();
//                            ordersModel.setTitle(myat.getString("APtitle"));
                            ordersModel.setAPid(myat.getInt("APid"));
//                            ordersModel.setStartTime(myat.getString("APstartT"));
                            ordersModel.setAPlikeN(myat.getInt("APlikeN"));
                            ordersModel.setUserimg(myat.getString("Userimg"));
//                            ordersModel.setRegistNum(myat.getInt("APregistN"));
                            ordersModel.setAPcontent(myat.getString("APcontent"));
                            List<String>  list = new ArrayList<String>();
                            JSONArray jsonArray = myat.getJSONArray("APimgurl");
//                            Log.d("?????????????", jsonArray.toString());
                            for (int j = 0; j < jsonArray.length();j++){
                                list.add(jsonArray.getString(j));
                            }
                            ordersModel.setAPimgurl(list);
                            ordersItemList3.add(ordersModel);
                        }

                    }else index++;

/*                    if(jsonObject.getJSONArray("activity").length() != 0){
                        JSONArray jsonArray3 = jsonObject.getJSONArray("activity");
                        for (int i = 0; i < jsonArray3.length();i++){
                            JSONObject myat = jsonArray3.getJSONObject(i);
                            YuePaiDataModel ordersModel = new YuePaiDataModel();
//                            ordersModel.setTitle(myat.getString("APtitle"));
                            ordersModel.setAPid(myat.getInt("APid"));
//                            ordersModel.setStartTime(myat.getString("APstartT"));
                            ordersModel.setAPlikeN(myat.getInt("APlikeN"));
                            ordersModel.setUserimg(myat.getString("Userimg"));
//                            ordersModel.setRegistNum(myat.getInt("APregistN"));
                            ordersModel.setAPcontent(myat.getString("APcontent"));
                            List<String>  list = new ArrayList<String>();
                            JSONArray jsonArray = myat.getJSONArray("APimgurl");
//                            Log.d("?????????????", jsonArray.toString());
                            for (int j = 0; j < jsonArray.length();j++){
                                list.add(jsonArray.getString(j));
                            }
                            ordersModel.setAPimgurl(list);
                            ordersItemList3.add(ordersModel);
                        }
                    }else index++;*/
                    index++;
                    if(index!=3) {
                        msg.what = StatusCode.REQUEST_DONE_SUCCESS;
                        handler.sendMessage(msg);
                        break;
                    }else{
                        msg.what = 333;
                        handler.sendMessage(msg);
                        break;
                    }

                }
                case StatusCode.REQUEST_PJ_DONE_SUCCESS: //请求已完成成功
                {
                    int index = 0;
                    JSONObject jsonObject = object.getJSONObject("contents");
                    if(jsonObject.getJSONArray("myappointment").length() != 0){
                        JSONArray jsonArray1 = jsonObject.getJSONArray("myappointment");
                        for (int i = 0; i < jsonArray1.length();i++){
                            JSONObject myat = jsonArray1.getJSONObject(i);
                            YuePaiDataModel ordersModel = new YuePaiDataModel();
//                            ordersModel.setTitle(myat.getString("APtitle"));
                            ordersModel.setAPid(myat.getInt("APid"));
//                            ordersModel.setStartTime(myat.getString("APstartT"));
                            ordersModel.setAPlikeN(myat.getInt("APlikeN"));
                            ordersModel.setUserimg(myat.getString("Userimg"));
//                            ordersModel.setRegistNum(myat.getInt("APregistN"));
                            ordersModel.setAPcontent(myat.getString("APcontent"));
                            List<String>  list = new ArrayList<String>();
                            JSONArray jsonArray = myat.getJSONArray("APimgurl");
//                            Log.d("?????????????", jsonArray.toString());
                            for (int j = 0; j < jsonArray.length();j++){
                                list.add(jsonArray.getString(j));
                            }
                            ordersModel.setAPimgurl(list);
                            ordersItemList4.add(ordersModel);
                        }
                    }else index++;

                    if (jsonObject.getJSONArray("entryappointment").length()!= 0){
                        JSONArray jsonArray2 = jsonObject.getJSONArray("entryappointment");
                        for (int i = 0; i < jsonArray2.length();i++){
                            JSONObject myat = jsonArray2.getJSONObject(i);
                            YuePaiDataModel ordersModel = new YuePaiDataModel();
//                            ordersModel.setTitle(myat.getString("APtitle"));
                            ordersModel.setAPid(myat.getInt("APid"));
//                            ordersModel.setStartTime(myat.getString("APstartT"));
                            ordersModel.setAPlikeN(myat.getInt("APlikeN"));
                            ordersModel.setUserimg(myat.getString("Userimg"));
//                            ordersModel.setRegistNum(myat.getInt("APregistN"));
                            ordersModel.setAPcontent(myat.getString("APcontent"));
                            List<String>  list = new ArrayList<String>();
                            JSONArray jsonArray = myat.getJSONArray("APimgurl");
//                            Log.d("?????????????", jsonArray.toString());
                            for (int j = 0; j < jsonArray.length();j++){
                                list.add(jsonArray.getString(j));
                            }
                            ordersModel.setAPimgurl(list);
                            ordersItemList4.add(ordersModel);
                        }

                    }else index++;

/*                    if(jsonObject.getJSONArray("activity").length() != 0){
                        JSONArray jsonArray3 = jsonObject.getJSONArray("activity");
                        for (int i = 0; i < jsonArray3.length();i++){
                            JSONObject myat = jsonArray3.getJSONObject(i);
                            YuePaiDataModel ordersModel = new YuePaiDataModel();
//                            ordersModel.setTitle(myat.getString("APtitle"));
                            ordersModel.setAPid(myat.getInt("APid"));
//                            ordersModel.setStartTime(myat.getString("APstartT"));
                            ordersModel.setAPlikeN(myat.getInt("APlikeN"));
                            ordersModel.setUserimg(myat.getString("Userimg"));
//                            ordersModel.setRegistNum(myat.getInt("APregistN"));
                            ordersModel.setAPcontent(myat.getString("APcontent"));
                            List<String>  list = new ArrayList<String>();
                            JSONArray jsonArray = myat.getJSONArray("APimgurl");
//                            Log.d("?????????????", jsonArray.toString());
                            for (int j = 0; j < jsonArray.length();j++){
                                list.add(jsonArray.getString(j));
                            }
                            ordersModel.setAPimgurl(list);
                            ordersItemList3.add(ordersModel);
                        }
                    }else index++;*/
                    index++;
                    if(index!=3) {
                        msg.what = StatusCode.REQUEST_PJ_DONE_SUCCESS;
                        handler.sendMessage(msg);
                        break;
                    }else{
                        msg.what = 444;
                        handler.sendMessage(msg);
                        break;
                    }

                }
                case StatusCode.REQUEST_ORDER_ERROR: //请求订单授权码错误
                {
                    msg.what = StatusCode.REQUEST_ORDER_ERROR;
                    handler.sendMessage(msg);
                    break;
                }
            }

        }

    }

    @Override
    public void exception(IOException e, String requestUrl) {
        Message message = new Message();
        message.what = 88;
        handler.sendMessage(message);
    }


    class MyRecyclerViewScrollListener extends RecyclerView.OnScrollListener{

        LinearLayout linearLayout;

        MyRecyclerViewScrollListener(LinearLayout l){
            linearLayout = l;
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            Log.d("?????????????","AAAAAAAAAAA");
            StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) recyclerView.getLayoutManager();
            int[] first = layoutManager.findFirstVisibleItemPositions(null);

            // 当不滚动时
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                // 判断是否滚动超过一屏
                if (layoutManager.getChildAt(0).getY()==0f && first[0]==0) {
                    linearLayout.setVisibility(View.GONE);
                } else {
                    linearLayout.setVisibility(View.VISIBLE);
                }

            } else if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {//拖动中
                linearLayout.setVisibility(View.GONE);
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            Log.d("?????????????", "BBBBBBBBBBBBBB");
            StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) recyclerView.getLayoutManager();
            int[] first = layoutManager.findFirstVisibleItemPositions(null);
            if(layoutManager.getChildAt(0).getY()==0f && first[0]==0){
                Log.d("?????????????","CCCCCCCCCCCCC");
                linearLayout.setVisibility(View.GONE);
            } else {
                Log.d("?????????????DDDDDDDDD",String.valueOf(first[0]));
                linearLayout.setVisibility(View.VISIBLE);
            }
        }
    }
}
