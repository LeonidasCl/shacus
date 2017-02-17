package com.example.pc.shacus.Activity;

import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AbsListView;
import android.widget.ListView;

import com.example.pc.shacus.Adapter.YuePaiAdapter;
import com.example.pc.shacus.Data.Cache.ACache;
import com.example.pc.shacus.Data.Model.LoginDataModel;
import com.example.pc.shacus.Data.Model.PhotographerModel;
import com.example.pc.shacus.Data.Model.UserModel;
import com.example.pc.shacus.Network.NetRequest;
import com.example.pc.shacus.Network.NetworkCallbackInterface;
import com.example.pc.shacus.Network.StatusCode;
import com.example.pc.shacus.R;
import com.example.pc.shacus.Util.CommonUrl;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
* 李嘉文 2017.2.17
* 二级界面 想拍人
* （约拍列表：摄影师发布的约拍）
* */

public class WantToPhotographActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_want_to_photograph);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends android.support.v4.app.Fragment implements NetworkCallbackInterface.NetRequestIterface{

        private static final String ARG_SECTION_NUMBER = "section_number";
        private SwipeRefreshLayout refreshLayout;
        private YuePaiAdapter personAdapter;
        private ListView listView;
        List<PhotographerModel> subList = new ArrayList<>();
        private int type=0;//0为全部约拍，其它数字为类型
        private int bootCounter=0;
        private int maxRecords = 400;
        private LoginDataModel userModel;
        private UserModel userData;
        private NetRequest requestFragment;
        List<PhotographerModel> yuepaiList = new ArrayList<>();
        private boolean isloading=false;
        private Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg){
                super.handleMessage(msg);
                switch(msg.what){
                    case StatusCode.REQUEST_YUEPAI_GRAPH_LIST_SUCCESS:
                        personAdapter.refresh(yuepaiList);
                        personAdapter.notifyDataSetChanged();
                        refreshLayout.setRefreshing(false);
                        break;
                    case StatusCode.REQUEST_YUEPAI_MORE_GRAPH_LIST_SUCCESS:
                        personAdapter.notifyDataSetChanged();
                        isloading=false;
                        break;
                }
            }
        };



        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_activity_want_to_photograph, container, false);


            personAdapter = new YuePaiAdapter(getActivity(),subList);
            listView = (ListView) rootView.findViewById(R.id.person_list);
            refreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
            listView.setAdapter(personAdapter);

            ACache cache=ACache.get(getActivity());
            userModel = (LoginDataModel) cache.getAsObject("loginModel");
            userData= userModel.getUserModel();
            requestFragment=new NetRequest(this,getActivity());
            Map<String, Object> map = new HashMap<>();
            map.put("type", "10231");//??10231想拍人的约拍列表（摄影师发布的约拍）
            map.put("authkey", userData.getAuth_key());
            map.put("uid", userData.getId());
            requestFragment.httpRequest(map, CommonUrl.getYuePaiInfo);

            onScrollListener();
            onRefreshListener();
            return rootView;
        }

        private void doRefresh(){
            Map<String, Object> map = new HashMap<>();
            map.put("type", "10231");//??10231想拍人的约拍列表（摄影师发布的约拍）
            map.put("authkey", userData.getAuth_key());
            map.put("uid", userData.getId());
            requestFragment.httpRequest(map, CommonUrl.getYuePaiInfo);
        }

        private void doLoadmore(){
            if (bootCounter<5||isloading)//如果数据小于五说明是初始化，不读加载更多
                return;
            isloading=true;
            Map<String, Object> map = new HashMap<>();
            map.put("type", "10243");
            map.put("authkey", userData.getAuth_key());
            map.put("uid", userData.getId());
            map.put("offsetapid", personAdapter.getItem(bootCounter - 1).getAPid());
            requestFragment.httpRequest(map, CommonUrl.getYuePaiInfo);
        }

        private void onRefreshListener(){
            refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    refreshLayout.setRefreshing(true);
                    bootCounter=0;
                    doRefresh();
                }
            });
        }

        private void onScrollListener(){
            listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView absListView, int i) {

                }
                @Override
                public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    if (firstVisibleItem + visibleItemCount > totalItemCount - 2 && totalItemCount < maxRecords){
                        doLoadmore();
                    }
                }
            });
        }

        @Override
        public void requestFinish(String result, String requestUrl) throws JSONException {
            JSONObject json = new JSONObject(result);
            String code = json.getString("code");
            JSONArray array = json.getJSONArray("contents");
            if (code.equals("10251")){//第一次加载/刷新的返回
                yuepaiList=new ArrayList<>();
                for (int i = 0; i < array.length(); i++) {
                    JSONObject info = array.getJSONObject(i);
                    Gson gson = new Gson();
                    PhotographerModel photo = gson.fromJson(info.toString(), PhotographerModel.class);
                    yuepaiList.add(photo);
                }
                bootCounter=array.length();
                Message msg=handler.obtainMessage();
                msg.what= StatusCode.REQUEST_YUEPAI_GRAPH_LIST_SUCCESS;
                handler.sendMessage(msg);
            }
            if (code.equals("10253")){//加载更多的返回
                List<PhotographerModel> addList = new ArrayList<>();
                for (int i = 0; i < array.length(); i++) {
                    JSONObject info = array.getJSONObject(i);
                    Gson gson = new Gson();
                    PhotographerModel photo = gson.fromJson(info.toString(), PhotographerModel.class);
                    yuepaiList.add(photo);
                    addList.add(photo);
                }
                bootCounter += array.length();
                personAdapter.add(addList);
                Message msg = handler.obtainMessage();
                msg.what = StatusCode.REQUEST_YUEPAI_MORE_GRAPH_LIST_SUCCESS;
                handler.sendMessage(msg);
            }
        }

        @Override
        public void exception(IOException e, String requestUrl) {

        }

    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            PlaceholderFragment frag=PlaceholderFragment.newInstance(position + 1);
            frag.type=position;
            return frag;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
    }



}
