package com.example.pc.shacus.Activity;

import android.content.res.ColorStateList;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.pc.shacus.APP;
import com.example.pc.shacus.Adapter.YuePaiAdapter;
import com.example.pc.shacus.Adapter.YuePaiAdapter_new;
import com.example.pc.shacus.Data.Cache.ACache;
import com.example.pc.shacus.Data.Model.LoginDataModel;
import com.example.pc.shacus.Data.Model.PhotographerModel;
import com.example.pc.shacus.Data.Model.UserModel;
import com.example.pc.shacus.Data.Model.YuePaiGroupModel;
import com.example.pc.shacus.Network.NetRequest;
import com.example.pc.shacus.Network.NetworkCallbackInterface;
import com.example.pc.shacus.Network.StatusCode;
import com.example.pc.shacus.R;
import com.example.pc.shacus.Util.CToast;
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

import static android.support.design.widget.TabLayout.MODE_SCROLLABLE;
import static com.example.pc.shacus.Network.StatusCode.WANT_TO_PHOTOGRAPH;
import static com.example.pc.shacus.Network.StatusCode.WANT_TO_PHOTOGRAPH_MORE;

/*
* 李嘉文 2017.2.17
* 二级界面 想拍人
* （约拍列表：摄影师发布的约拍）
* */

public class WantToPhotographActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private ACache cache;
    private List<YuePaiGroupModel> apTypes;
    LoginDataModel logindata;
    private TextView group_description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_want_to_photograph);

        cache=ACache.get(this);
        logindata=(LoginDataModel)cache.getAsObject("loginModel");
        apTypes=logindata.getGroupList();

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.zero_black));
        setSupportActionBar(toolbar);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);


        group_description=(TextView)findViewById(R.id.group_description);
        group_description.setText("以下是所有的约拍");

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        //ColorStateList colorList = tabLayout.getTabTextColors();
        //tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.main_green));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if (position > 0)
                    group_description.setText(apTypes.get(position - 1).getDescription());
                else
                    group_description.setText("以下是所有分类");
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setTabMode(MODE_SCROLLABLE);

        final ImageButton finish = (ImageButton) findViewById(R.id.backbtn);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends android.support.v4.app.Fragment implements NetworkCallbackInterface.NetRequestIterface{

        private static final String ARG_SECTION_NUMBER = "section_number";
        private SwipeRefreshLayout refreshLayout;
        private YuePaiAdapter_new personAdapter;
        private ListView listView;
        List<PhotographerModel> subList = new ArrayList<>();
        private int type=0;//0为全部约拍，其它数字为类型
        private int bootCounter=0;
        private int maxRecords = 400;
        private int last = 6;
        private LoginDataModel userModel;
        private UserModel userData;
        private NetRequest requestFragment;
        List<PhotographerModel> yuepaiList = new ArrayList<>();
        private boolean isloading=false;
        private LinearLayout invis;
        private Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg){
                super.handleMessage(msg);
                switch(msg.what){
                    case StatusCode.REQUEST_YUEPAI_GRAPH_LIST_SUCCESS:
                        last = 6;
                        personAdapter.refresh(yuepaiList);
                        personAdapter.notifyDataSetChanged();
                        refreshLayout.setRefreshing(false);
                        break;
                    case StatusCode.REQUEST_YUEPAI_MORE_GRAPH_LIST_SUCCESS:
                        List<PhotographerModel> addList= (List<PhotographerModel>) msg.obj;
                        personAdapter.add(addList);
                        personAdapter.notifyDataSetChanged();
                        isloading=false;
                        break;
                }
            }
        };



        public PlaceholderFragment(){
        }

        public static PlaceholderFragment newInstance(int sectionNumber){
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            fragment.type=sectionNumber;
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_activity_want_to_photograph, container, false);


            personAdapter = new YuePaiAdapter_new(getActivity(),subList);
            listView = (ListView) rootView.findViewById(R.id.person_list);
            invis = (LinearLayout) rootView.findViewById(R.id.invis);
            refreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
            listView.setAdapter(personAdapter);

            ACache cache=ACache.get(getActivity());
            userModel = (LoginDataModel) cache.getAsObject("loginModel");
            userData= userModel.getUserModel();
            requestFragment=new NetRequest(this,getActivity());
            Map<String, Object> map = new HashMap<>();
            map.put("type", WANT_TO_PHOTOGRAPH);//??10231想拍人的约拍列表（摄影师发布的约拍）
            map.put("authkey", userData.getAuth_key());
            map.put("group",type);
            map.put("uid", userData.getId());
            requestFragment.httpRequest(map, CommonUrl.getYuePaiInfo);

            onScrollListener();
            onRefreshListener();
            invis.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    personAdapter.notifyDataSetChanged();
                    listView.setSelection(0);
                }
            });
            return rootView;
        }

        private void doRefresh(){
            Map<String, Object> map = new HashMap<>();
            map.put("type", WANT_TO_PHOTOGRAPH);//??10231想拍人的约拍列表（摄影师发布的约拍）
            map.put("authkey", userData.getAuth_key());
            map.put("uid", userData.getId());
            map.put("group", type);
            requestFragment.httpRequest(map, CommonUrl.getYuePaiInfo);
        }

        private void doLoadmore(){
            if (bootCounter<6||isloading||personAdapter.getCount()==0||last<6)//如果数据小于五说明是初始化，不读加载更多
                return;
            isloading=true;
            Map<String, Object> map = new HashMap<>();
            map.put("type", WANT_TO_PHOTOGRAPH_MORE);
            map.put("group",type);
            map.put("authkey", userData.getAuth_key());
            map.put("uid", userData.getId());
            map.put("offsetapid", personAdapter.getItem(bootCounter - 1).getAPid());
            requestFragment.httpRequest(map, CommonUrl.getYuePaiInfo);
            Log.d("=======================", String.valueOf(bootCounter));
            Log.d("=======================", String.valueOf(personAdapter.getItem(bootCounter - 1).getAPid()));
        }

        private void onRefreshListener(){
            refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    refreshLayout.setRefreshing(true);
                    bootCounter = 0;
                    doRefresh();
                }
            });
        }

        private void onScrollListener(){
            listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    // 当不滚动时
                    /*if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                        // 判断是否滚动到底部
                        if (view.getLastVisiblePosition() == view.getCount() - 1) {
                            doLoadmore();
                            //加载更多功能的代码
                        }
                    }*/
                }

                @Override
                public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    if ((firstVisibleItem + visibleItemCount > totalItemCount - 2) && (totalItemCount < maxRecords)) {
                        doLoadmore();
                    }
                    if (firstVisibleItem >= 1){
                        invis.setVisibility(View.VISIBLE);
                    } else{
                        invis.setVisibility(View.GONE);
                    }

                    if(firstVisibleItem == 0){
                        refreshLayout.setEnabled(true);
                    }else{
                        refreshLayout.setEnabled(false);
                    }
                }
            });
        }

        @Override
        public void requestFinish(String result, String requestUrl) throws JSONException {
            JSONObject json = new JSONObject(result);
            Log.d("CCCCCCCCCCCCC",json.toString());
            String code = json.getString("code");
            if (code.equals("10251")){//第一次加载/刷新的返回
                JSONArray array = json.getJSONArray("contents");
                yuepaiList=new ArrayList<>();
                for (int i = 0; i < array.length(); i++) {
                    /*JSONObject info = array.getJSONObject(i);
                    Gson gson = new Gson();
                    PhotographerModel photo = gson.fromJson(info.toString(), PhotographerModel.class);
                    yuepaiList.add(photo);*/
                    JSONObject info = array.getJSONObject(i);
                    PhotographerModel photographerModel = new PhotographerModel();
                    UserModel userModel = new UserModel();
                    userModel.setId(String.valueOf(info.get("sponsorid")));
                    userModel.setLocation(info.getString("Userlocation"));
                    userModel.setNickName(info.getString("Useralais"));
                    userModel.setAge(info.getString("Userage"));
                    userModel.setHeadImage(info.getString("Userimg"));
                    userModel.setSex(info.getString("Usex"));
                    photographerModel.setUserModel(userModel);
                    List<String>  list = new ArrayList<String>();
                    JSONArray jsonArray = info.getJSONArray("APimgurl");
                    for (int j = 0; j < jsonArray.length();j++){
                        list.add(jsonArray.getString(j));
                    }
                    photographerModel.setAPimgurl(list);
                    photographerModel.setAPtime(info.getString("APtime"));
                    photographerModel.setAPgroup(info.getInt("APgroup"));
                    photographerModel.setAPcontent(info.getString("APcontent"));
                    photographerModel.setAPstartT(info.getString("APcreatetime"));
                    photographerModel.setAPpricetype(info.getInt("APpricetype"));
                    photographerModel.setAPprice(info.getString("APprice"));
                    photographerModel.setAPlikeN(info.getInt("APlikeN"));
                    photographerModel.setAPid(info.getInt("APid"));
                    yuepaiList.add(photographerModel);
                }
                bootCounter=array.length();
                Message msg=handler.obtainMessage();
                msg.what= StatusCode.REQUEST_YUEPAI_GRAPH_LIST_SUCCESS;
                handler.sendMessage(msg);
            }
            if (code.equals("10262")){
                //暂无更多
            }
            if (code.equals("10253") || code.equals("10263")){//加载更多的返回
                Log.d("KKKKKKKKKKKKKKKKKKKK","AAA3");
                JSONArray array = json.getJSONArray("contents");
                List<PhotographerModel> addList = new ArrayList<>();
                for (int i = 0; i < array.length(); i++) {
                    /*JSONObject info = array.getJSONObject(i);
                    Gson gson = new Gson();
                    PhotographerModel photo = gson.fromJson(info.toString(), PhotographerModel.class);
                    yuepaiList.add(photo);
                    addList.add(photo);*/
                    JSONObject info = array.getJSONObject(i);
                    PhotographerModel photographerModel = new PhotographerModel();
                    UserModel userModel = new UserModel();
                    userModel.setId(String.valueOf(info.get("sponsorid")));
                    userModel.setLocation(info.getString("Userlocation"));
                    userModel.setNickName(info.getString("Useralais"));
                    userModel.setAge(info.getString("Userage"));
                    userModel.setHeadImage(info.getString("Userimg"));
                    userModel.setSex(info.getString("Usex"));
                    photographerModel.setUserModel(userModel);
                    List<String>  list = new ArrayList<String>();
                    JSONArray jsonArray = info.getJSONArray("APimgurl");
                    for (int j = 0; j < jsonArray.length();j++){
                        list.add(jsonArray.getString(j));
                    }
                    photographerModel.setAPimgurl(list);
                    photographerModel.setAPgroup(info.getInt("APgroup"));
                    photographerModel.setAPcontent(info.getString("APcontent"));
                    photographerModel.setAPstartT(info.getString("APcreatetime"));
                    photographerModel.setAPpricetype(info.getInt("APpricetype"));
                    photographerModel.setAPprice(info.getString("APprice"));
                    photographerModel.setAPlikeN(info.getInt("APlikeN"));
                    photographerModel.setAPid(info.getInt("APid"));
                    photographerModel.setAPtime(info.getString("APtime"));
                    yuepaiList.add(photographerModel);
                    addList.add(photographerModel);
                }
                last = array.length();
                bootCounter += array.length();
                //personAdapter.add(addList);
                Message msg = handler.obtainMessage();
                msg.what = StatusCode.REQUEST_YUEPAI_MORE_GRAPH_LIST_SUCCESS;
                msg.obj=addList;
                handler.sendMessage(msg);
            }
        }

        @Override
        public void exception(IOException e, String requestUrl) {

        }

    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm){
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            PlaceholderFragment frag=PlaceholderFragment.newInstance(position);
            frag.type=position;
            if (position!=0)
                group_description.setText(apTypes.get(position-1).getDescription());
            return frag;
        }

        @Override
        public int getCount(){
            return apTypes.size()+1;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position==0){
                return "全部";
            }
            else
            {
                return apTypes.get(position-1).getName();
            }
        }
    }



}
