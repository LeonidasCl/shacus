package com.example.pc.shacus.Activity;

import android.content.Intent;
import android.graphics.Color;
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
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

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

/**
 * Created by 崔颖华
 */
//收藏界面（二级）

public class FavoritemActivity extends AppCompatActivity implements  NetworkCallbackInterface.NetRequestIterface,View.OnClickListener{

    private ViewPager viewPager = null;
    private List<View> viewContainter = new ArrayList<>();   //存放容器
    private ViewPagerAdapter viewPagerAdapter = null;   //声明适配器

    private TabHost mTabHost = null;
    private TabWidget mTabWidget = null;

    private RecyclerView recyclerView1;
    private RecyclerViewAdapter recyclerViewAdapter1;
    List<YuePaiDataModel> favorItemList1;
    RecyclerView.LayoutManager layoutManager1;


    private RecyclerView recyclerView2;
    private RecyclerViewAdapter recyclerViewAdapter2;
    List<YuePaiDataModel> favorItemList2;
    RecyclerView.LayoutManager layoutManager2;

    private ACache aCache;
    private NetRequest netRequest;
    public int index = StatusCode.REQUEST_FAVOR_YUEPAI;
    int spanCount = 2;
    View view_1;
    View view_2;
    private FrameLayout loading1;
    private FrameLayout loading2;

    UserModel content = null;
    Map map = new HashMap<>();
    String userId = null;
    String authkey = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritem);

        netRequest = new NetRequest(FavoritemActivity.this,FavoritemActivity.this);
        aCache = ACache.get(FavoritemActivity.this);
        LoginDataModel loginModel = (LoginDataModel)aCache.getAsObject("loginModel");

        content = loginModel.getUserModel();
        userId = content.getId();
        authkey = content.getAuth_key();
        map.put("authkey",authkey);
        map.put("uid",userId);

        //初始化TabHost
        initTabHost();
        //绑定组件
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        //初始化viewPager
        initViewPagerContainter();
        viewPagerAdapter = new ViewPagerAdapter();
        //设置adapter适配器
        viewPager.setAdapter(viewPagerAdapter);

        //设置viewpager的监听器
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            //当滑动切换时
            @Override
            public void onPageSelected(int position) {
                mTabWidget.setCurrentTab(position);
                mTabHost.setCurrentTab(position);
                updateTab(mTabHost);
                if(position == 0){
                    loading1.setVisibility(View.VISIBLE);
                    index = StatusCode.REQUEST_FAVOR_YUEPAI;
                    initFavorInfo();
                }else if(position == 1){
                    loading2.setVisibility(View.VISIBLE);
                    index = StatusCode.REQUEST_FAVOR_DONGTAI;
                    initFavorInfo();
                }
                //else index =
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        //TabHost的监听事件
        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                updateTab(mTabHost);
                if (tabId.equals("yuepai")){
                    viewPager.setCurrentItem(0);
                    index = StatusCode.REQUEST_FAVOR_YUEPAI;
                    loading1.setVisibility(View.VISIBLE);
                    updateTab(mTabHost);
                }else if (tabId.equals("dongtai")){
                    viewPager.setCurrentItem(1);
                    index = StatusCode.REQUEST_FAVOR_DONGTAI;
                    loading2.setVisibility(View.VISIBLE);
                    updateTab(mTabHost);
                }/*else{
                    viewPager.setCurrentItem(2);
                }*/
            }
        });

        //解决开始时初始化不显示viewPager
//        mTabHost.setCurrentTab(1);
//        mTabHost.setCurrentTab(0);

        ImageButton imageButton = (ImageButton) findViewById(R.id.favor_backbtn);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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
                view.setBackgroundColor(Color.parseColor("#1A1A1A"));
            }
        }
    }

    //初始化viewPager
    private void initViewPagerContainter() {
        //建立三个view，并找到对应的
        view_1 = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_recyclerview_container,null);
        view_2 = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_recyclerview_container,null);
//        View view3 = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_favoritem_list,null);
        //加入ViewPager的容器
        viewContainter.add(view_1);
        viewContainter.add(view_2);
//        viewContainter.add(view3);
        loading1 = (FrameLayout) view_1.findViewById(R.id.wait_loading_layout);
        loading2 = (FrameLayout) view_2.findViewById(R.id.wait_loading_layout);

        recyclerView1 = (RecyclerView) view_1.findViewById(R.id.recyclerView);
        recyclerView2 = (RecyclerView) view_2.findViewById(R.id.recyclerView);

        initFavorInfo();

    }

    //初始化TabHost
    private void initTabHost() {
        //绑定id
        mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup();
        mTabWidget = mTabHost.getTabWidget();
        /**
         * newTabSpec（）   就是给每个Tab设置一个ID
         * setIndicator()   每个Tab的标题
         * setCount()       每个Tab的标签页布局
         */
        mTabHost.addTab(mTabHost.newTabSpec("yuepai").setContent(R.id.favoritem_tab1).setIndicator("约拍"));
        mTabHost.addTab(mTabHost.newTabSpec("dongtai").setContent(R.id.favoritem_tab2).setIndicator("动态"));
        //mTabHost.addTab(mTabHost.newTabSpec("works").setContent(R.id.favoritem_works).setIndicator("作品"));
        mTabHost.setCurrentTab(0);
        //初始化Tab的颜色，和字体的颜色
        for (int i = 0;i < mTabHost.getTabWidget().getChildCount();i++){
            TextView tv = (TextView) mTabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            tv.setTextColor(Color.parseColor("#ffffff"));
        }
        updateTab(mTabHost);
    }

    //获得收藏信息
    private void initFavorInfo(){
        favorItemList1 = new ArrayList<>();
        favorItemList2 = new ArrayList<>();


        switch (index){
            case StatusCode.REQUEST_FAVOR_YUEPAI:
            {
                map.put("type", StatusCode.REQUEST_FAVOR_YUEPAI);
                netRequest.httpRequest(map, CommonUrl.getFavorInfo);
                Log.d("sssssssssss", "aaaaa");
                break;
            }
            case StatusCode.REQUEST_FAVOR_DONGTAI:
            {
                map.put("type", StatusCode.REQUEST_FAVOR_DONGTAI);
                netRequest.httpRequest(map,CommonUrl.aboutFavorDongTai);
                break;
            }
        }

    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Message message = new Message();
            switch (msg.what){
                case StatusCode.REQUEST_FAVORHUODONG_SUCCESS:
                {
                    Log.d("ssssssssss","cccccc");
                    if(favorItemList1.size() == 0){
                        message.what = StatusCode.REQUEST_FAVORYUEPAI_NONE;
                        this.sendMessage(message);
                    }else {
                        view_1 = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_recyclerview_container,null);
                        layoutManager1 = new StaggeredGridLayoutManager(spanCount,StaggeredGridLayoutManager.VERTICAL);
                        recyclerView1.setLayoutManager(layoutManager1);
                        recyclerViewAdapter1 = new RecyclerViewAdapter(favorItemList1,FavoritemActivity.this);
                        recyclerView1.setAdapter(recyclerViewAdapter1);
                        message.what = 100;
                        this.sendMessageDelayed(message, 1000);
                    }
                    break;
                }
                case StatusCode.REQUEST_FAVORYUEPAI_NONE:{
                    TextView textView = (TextView) view_1.findViewById(R.id.none_item);
                    textView.setVisibility(View.VISIBLE);
                    recyclerView1.setVisibility(View.INVISIBLE);
                    textView.setText("没有收藏~快去“约拍”看看吧");
                    loading1.setVisibility(View.GONE);
                    break;
                }
                case StatusCode.REQUEST_FAVORYUEPAI_SUCCESS:
                {
                    map.put("type", StatusCode.REQUEST_FAVOR_HUODONG);
                    netRequest.httpRequest(map, CommonUrl.getFavorInfo);
                    Log.d("sssssssssss","bbbbb");
                    break;
                }
                case StatusCode.REQUEST_CANCELYUEPAI_SUCCESS:
                {
                    CommonUtils.getUtilInstance().showToast(APP.context, "已取消收藏");
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
                case StatusCode.REQUEST_FAVOR_DONGTAI_SUCCESS:
                {
                    view_2 = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_recyclerview_container,null);
                    layoutManager2 = new StaggeredGridLayoutManager(spanCount,StaggeredGridLayoutManager.VERTICAL);
                    recyclerView2.setLayoutManager(layoutManager2);
                    recyclerViewAdapter2 = new RecyclerViewAdapter(favorItemList2,FavoritemActivity.this);
                    recyclerView2.setAdapter(recyclerViewAdapter2);
                    message.what = 200;
                    this.sendMessageDelayed(message, 1000);
                    break;
                }
                case 200:
                {
                    loading2.setVisibility(View.GONE);
                    break;
                }
                case StatusCode.REQUEST_FAVOR_DONGTAI_NONE:
                {
                    TextView textView = (TextView) view_2.findViewById(R.id.none_item);
                    textView.setVisibility(View.VISIBLE);
                    recyclerView2.setVisibility(View.INVISIBLE);
                    textView.setText("没有收藏~快去“首页”看看吧");
                    loading2.setVisibility(View.GONE);
                    break;
                }
                case StatusCode.REQUEST_CANCEL_FAVORDONGTAI_SUCCESS:
                {
                    CommonUtils.getUtilInstance().showToast(APP.context, "已取消收藏");
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
        if( i == 1){
            int tag = (int) list.get(1);
            ACache aCache = ACache.get(FavoritemActivity.this);
            LoginDataModel loginDataModel = (LoginDataModel) aCache.getAsObject("loginModel");
            UserModel content = null;
            Map map = new HashMap<>();
            content = loginDataModel.getUserModel();
            String userId = content.getId();
            String authkey = content.getAuth_key();

            map.put("uid", userId);
            map.put("authkey", authkey);

            /*if(index == StatusCode.REQUEST_FAVOR_YUEPAI){
                if(favorItemList1.get(tag).getType().equals("yuepai")){
                    //取消收藏的约拍
                    map.put("type", StatusCode.REQUEST_CANCEL_FAVORYUEPAI);
                    map.put("typeid", favorItemList1.get(tag).getId());
                }else if(favorItemList1.get(tag).getType().equals("huodong")){
                    //取消收藏的活动
                    Log.d("aaaaaaa","取消活动");
                    map.put("type",StatusCode.REQUEST_CANCEL_FAVORHUODONG);
                    map.put("typeid", favorItemList1.get(tag).getId());
                }
                netRequest.httpRequest(map, CommonUrl.getFavorInfo);
                favorItemList1.remove(tag);
                recyclerViewAdapter1.notifyDataSetChanged();
            }else if(index == StatusCode.REQUEST_FAVOR_DONGTAI){
                //取消收藏的动态
                map.put("type",StatusCode.REQUEST_CANCEL_FAVORDONGTAI);
                map.put("trendid",favorItemList2.get(tag).getId());
                netRequest.httpRequest(map, CommonUrl.aboutFavorDongTai);
                favorItemList2.remove(tag);
                recyclerViewAdapter2.notifyDataSetChanged();
            }*/
        }
    }

    //内部类实现viewpager的适配器
    private class ViewPagerAdapter extends PagerAdapter{

        //该方法决定并返回viewPager中组件的数量
        @Override
        public int getCount() {
            return viewContainter.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }
        //滑动切换时，消除当前组件


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(viewContainter.get(position));
        }

        //每次滑动时生成的组件
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(viewContainter.get(position));
            return viewContainter.get(position);
        }
    }

    @Override
    public void requestFinish(String result, String requestUrl) throws JSONException {
        Message msg = new Message();
        if (requestUrl.equals(CommonUrl.getFavorInfo)) {//返回收藏信息
            JSONObject object = new JSONObject(result);
            int code = Integer.valueOf(object.getString("code"));
            Log.d("sssssssssssssss",object.toString());
           /* switch (code) {
                case StatusCode.REQUEST_FAVORYUEPAI_SUCCESS://请求收藏的约拍成功
                {
                    JSONArray content = object.getJSONArray("contents");
                    for (int i = 0; i < content.length(); i++) {
                        JSONObject favor = content.getJSONObject(i);
                        ItemModel itemModel = new ItemModel();
                        itemModel.setTitle(favor.getString("APtitle"));
                        itemModel.setId(favor.getInt("APid"));
                        itemModel.setUserImage(favor.getString("Userimg"));
                        itemModel.setStartTime(favor.getString("APstartT"));
                        itemModel.setLikeNum(favor.getInt("APlikeN"));
                        itemModel.setImage(favor.getString("APimgurl"));
                        itemModel.setRegistNum(favor.getInt("APregistN"));
                        itemModel.setType("yuepai");
                        favorItemList1.add(itemModel);
                    }
                    msg.what = StatusCode.REQUEST_FAVORYUEPAI_SUCCESS;
                    handler.sendMessage(msg);
                    break;
                }
                case StatusCode.REQUEST_FAVORHUODONG_SUCCESS://请求所有收藏的活动成功
                {
                    Log.d("sssssssssssssss","活动"+object.toString());
                    JSONObject jsonObject = object.getJSONObject("contents");
                    JSONArray content = jsonObject.getJSONArray("activity");
                    for (int i = 0; i < content.length(); i++) {
                        JSONObject favor = content.getJSONObject(i);
                        ItemModel itemModel = new ItemModel();
                        itemModel.setTitle(favor.getString("ACtitle"));
                        itemModel.setId(favor.getInt("ACid"));
                        itemModel.setUserImage(favor.getString("Userimageurl"));
                        itemModel.setStartTime(favor.getString("ACstartT"));
                        itemModel.setLikeNum(favor.getInt("AClikenumber"));
                        itemModel.setImage(favor.getString("AClurl"));
                        itemModel.setRegistNum(favor.getInt("ACregistN"));
                        itemModel.setType("huodong");
                        favorItemList1.add(itemModel);
                    }
                    msg.what = StatusCode.REQUEST_FAVORHUODONG_SUCCESS;
                    handler.sendMessage(msg);
                    break;
                }
                case StatusCode.REQUEST_CANCELYUEPAI_SUCCESS://请求取消收藏约拍成功
                {
                    msg.what = StatusCode.REQUEST_CANCELYUEPAI_SUCCESS;
                    handler.sendMessage(msg);
                    break;
                }
            }*/

        } else if (requestUrl.equals(CommonUrl.aboutFavorDongTai)) {
            JSONObject object = new JSONObject(result);
            Log.d("ooooooooooooo",object.toString());
            int code = Integer.valueOf(object.getString("code"));
            /*switch (code) {
                case StatusCode.REQUEST_FAVOR_DONGTAI_SUCCESS: {
                    JSONArray content = object.getJSONArray("contents");
                    if (content.length() != 0) {
                        for (int i = 0; i < content.length(); i++) {
                            JSONObject favor = content.getJSONObject(i);
                            ItemModel itemModel = new ItemModel();
                            itemModel.setTitle(favor.getString("Ttitle"));
                            itemModel.setId(favor.getInt("Tid"));
                            itemModel.setUserImage(favor.getString("Tsponsorimg"));
                            itemModel.setStartTime(favor.getString("TsponsT"));
                            itemModel.setLikeNum(favor.getInt("TlikeN"));
                            itemModel.setImage(favor.getString("TIimgurl"));
                            itemModel.setRegistNum(favor.getInt("TcommentN"));
                            favorItemList2.add(itemModel);
                        }
                        if(favorItemList2.size() == 0){
                            msg.what = 555;
                            handler.sendMessage(msg);
                        }else{
                            msg.what = StatusCode.REQUEST_FAVOR_DONGTAI_SUCCESS;
                            handler.sendMessage(msg);
                        }
                        break;
                    }
                }
                case StatusCode.REQUEST_FAVOR_DONGTAI_NONE:{
                    msg.what = StatusCode.REQUEST_FAVOR_DONGTAI_NONE;
                    handler.sendMessage(msg);
                    break;
                }
                case StatusCode.REQUEST_CANCEL_FAVORDONGTAI_SUCCESS:{
                    msg.what = StatusCode.REQUEST_CANCEL_FAVORDONGTAI_SUCCESS;
                    handler.sendMessage(msg);
                    break;
                }
            }*/

        }
    }

    @Override
    public void exception(IOException e, String requestUrl) {
        Message message = new Message();
        message.what = 88;
        handler.sendMessage(message);
    }

}
