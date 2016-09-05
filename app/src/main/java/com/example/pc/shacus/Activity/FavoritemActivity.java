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
import android.widget.Toast;

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
    List<ItemModel> favorItemList1;
    RecyclerView.LayoutManager layoutManager1;


    private RecyclerView recyclerView2;
    private RecyclerViewAdapter recyclerViewAdapter2;
    List<ItemModel> favorItemList2;
    RecyclerView.LayoutManager layoutManager2;

    private ACache aCache;
    private NetRequest netRequest;
    public int index = StatusCode.REQUEST_FAVOR_YUEPAI;
    int spanCount = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritem);

        netRequest = new NetRequest(FavoritemActivity.this,FavoritemActivity.this);
        aCache = ACache.get(FavoritemActivity.this);

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
                if(position == 0) index = StatusCode.REQUEST_FAVOR_YUEPAI;
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
                if (tabId.equals("yuepai")){
                    viewPager.setCurrentItem(0);
                    index = StatusCode.REQUEST_FAVOR_YUEPAI;
                }else if (tabId.equals("huodong")){
                    viewPager.setCurrentItem(1);
                    //index =
                }/*else{
                    viewPager.setCurrentItem(2);
                }*/
            }
        });

        //解决开始时初始化不显示viewPager
//        mTabHost.setCurrentTab(1);
//        mTabHost.setCurrentTab(0);

    }

    //初始化viewPager
    private void initViewPagerContainter() {
        //建立三个view，并找到对应的
        View view_1 = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_recyclerview_container,null);
        View view_2 = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_recyclerview_container,null);
//        View view3 = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_favoritem_list,null);
        //加入ViewPager的容器
        viewContainter.add(view_1);
        viewContainter.add(view_2);
//        viewContainter.add(view3);

        recyclerView1 = (RecyclerView) view_1.findViewById(R.id.recyclerView);

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
        mTabHost.addTab(mTabHost.newTabSpec("huodong").setContent(R.id.favoritem_tab2).setIndicator("作品"));
        //mTabHost.addTab(mTabHost.newTabSpec("works").setContent(R.id.favoritem_works).setIndicator("作品"));
    }

    //获得收藏信息
    private void initFavorInfo(){
        favorItemList1 = new ArrayList<>();
        favorItemList2 = new ArrayList<>();

        LoginDataModel loginModel = (LoginDataModel)aCache.getAsObject("loginModel");
        UserModel content = null;
        Map map = new HashMap<>();
        String userId = null;
        String authkey = null;

        content = loginModel.getUserModel();
        userId = content.getId();
        authkey = content.getAuth_key();
        map.put("authkey",authkey);
        map.put("uid",userId);


        switch (index){
            case StatusCode.REQUEST_FAVOR_YUEPAI:
            {
                map.put("type",StatusCode.REQUEST_FAVOR_YUEPAI);
                netRequest.httpRequest(map, CommonUrl.getFavorInfo);
            }
        }

    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case StatusCode.REQUEST_FAVORYUEPAI_SUCCESS:
                {
                    layoutManager1 = new StaggeredGridLayoutManager(spanCount,StaggeredGridLayoutManager.VERTICAL);
                    recyclerView1.setLayoutManager(layoutManager1);

                    recyclerViewAdapter1 = new RecyclerViewAdapter(favorItemList1,FavoritemActivity.this);
                    recyclerView1.setAdapter(recyclerViewAdapter1);
                }
            }
        }
    };

    @Override
    public void onClick(View v) {
        int tag = (int) v.getTag();
        ACache aCache = ACache.get(FavoritemActivity.this);
        LoginDataModel loginDataModel = (LoginDataModel) aCache.getAsObject("loginModel");
        UserModel content = null;
        Map map = new HashMap<>();
        content = loginDataModel.getUserModel();
        String userId = content.getId();
        String authkey = content.getAuth_key();

        map.put("uid",userId);
        map.put("authkey", authkey);
        map.put("type",StatusCode.REQUEST_CANCEL_FAVORYUEPAI);
        map.put("typeid",favorItemList1.get(tag).getId());
        netRequest.httpRequest(map, CommonUrl.getFavorInfo);
        favorItemList1.remove(tag);
        recyclerViewAdapter1.notifyDataSetChanged();
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
        if(requestUrl.equals(CommonUrl.getFavorInfo)){//返回收藏信息
            JSONObject object = new JSONObject(result);
            int code = Integer.valueOf(object.getString("code"));
            Message msg = new Message();

            switch (code){
                case StatusCode.REQUEST_FAVORYUEPAI_SUCCESS://请求收藏的约拍成功
                {
                    JSONArray content = object.getJSONArray("contents");
                    for(int i = 0;i < content.length();i++){
                        JSONObject favor = content.getJSONObject(i);
                        ItemModel itemModel = new ItemModel();
                        itemModel.setTitle(favor.getString("APtitle"));
                        itemModel.setId(favor.getInt("APid"));
                        itemModel.setUserImage(favor.getString("Userimg"));
                        itemModel.setStartTime(favor.getString("APstartT"));
                        itemModel.setLikeNum(favor.getInt("APlikeN"));
                        itemModel.setImage(favor.getString("APimgurl"));
                        itemModel.setRegistNum(favor.getInt("APregistN"));
                        favorItemList1.add(itemModel);
                    }
                    msg.what = StatusCode.REQUEST_FAVORYUEPAI_SUCCESS;
                    handler.sendMessage(msg);
                    break;
                }
                /*case StatusCode.REQUEST_CANCELYUEPAI_SUCCESS://请求取消收藏约拍成功
                {
                    msg.what = StatusCode.REQUEST_CANCELYUEPAI_SUCCESS;
                    handler.sendMessage(msg);
                    break;
                }*/
            }

        }

    }

    @Override
    public void exception(IOException e, String requestUrl) {

    }

}
