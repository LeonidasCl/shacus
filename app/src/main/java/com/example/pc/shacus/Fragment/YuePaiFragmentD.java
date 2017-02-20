package com.example.pc.shacus.Fragment;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.pc.shacus.APP;
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

import java.util.logging.LogRecord;

/**
 * 李嘉文 2016.9.3
 * @Deprecated 重写的排行榜fragment
 *
 * 2016.9.12 lq修改
 *
 * 2017.2.6 李嘉文 检查
 * 原约拍列表页面（约拍一级页面）
 *
 * 2017.2.16 李嘉文 第二次重写
 * 修改为动态（作品集）列表页面
 */

public class YuePaiFragmentD extends android.support.v4.app.Fragment{

    private Activity yuepai;
    boolean refreshing=false;
    private SwipeRefreshLayout refreshLayout;
    private YuePaiAdapter personAdapter;
    private ListView listView;
    private int bootCounter=0;
    private int maxRecords = 400;
    private boolean getYuePaiFlag=false;
    private boolean mainScrollControl=true;
    private View rankView;
    private RelativeLayout mSideZoomBanner;
    private ACache cache;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            switch(msg.what){
                case StatusCode.REQUEST_YUEPAI_GRAPH_LIST_SUCCESS:
                    personAdapter.notifyDataSetChanged();
                    getYuePaiFlag=true;
                    break;
                case StatusCode.REQUEST_YUEPAI_MODEL_LIST_SUCCESS:
                    personAdapter.notifyDataSetChanged();
                    getYuePaiFlag=true;
                    break;
                case StatusCode.REQUEST_YUEPAI_MORE_MODEL_LIST_SUCCESS:
                    personAdapter.notifyDataSetChanged();
                    getYuePaiFlag=true;
                    break;
                case StatusCode.REQUEST_YUEPAI_MORE_GRAPH_LIST_SUCCESS:
                    personAdapter.notifyDataSetChanged();
                    getYuePaiFlag=true;
                    break;
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        bootData();
        refreshLayout.setRefreshing(true);
        bootCounter = 0;
        personAdapter.refresh(new ArrayList<PhotographerModel>());
        personAdapter.notifyDataSetChanged();
        refreshLayout.setRefreshing(false);
        refreshing = true;
    }


    public ListView getListView(){
        return listView;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        yuepai=this.getActivity();
        refreshing=false;
        rankView = inflater.inflate(R.layout.fragment_rank, container, false);
        cache= ACache.get(getActivity());
        personAdapter = new YuePaiAdapter(yuepai,bootData());
        listView = (ListView) rankView.findViewById(R.id.rank_list);
        refreshLayout = (SwipeRefreshLayout) rankView.findViewById(R.id.swipe_refresh_layout);
        listView.setAdapter(personAdapter);

        onScrollListener();
        onRefreshListener();

        return rankView;
    }

    private void onRefreshListener(){
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                bootData();
                refreshLayout.setRefreshing(true);
                bootCounter = 0;
                personAdapter.refresh(new ArrayList<PhotographerModel>());
                personAdapter.notifyDataSetChanged();
                refreshLayout.setRefreshing(false);
                refreshing = true;
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
                if (firstVisibleItem + visibleItemCount > totalItemCount - 1 && totalItemCount < maxRecords && totalItemCount != 0 && getYuePaiFlag) {
                    loadData();
                    getYuePaiFlag = false;
                }
                if (!mainScrollControl && firstVisibleItem == 0 && absListView.getChildAt(0) != null && absListView.getChildAt(0).getTop() > -10) {
                    ViewGroup.MarginLayoutParams layoutParam = (ViewGroup.MarginLayoutParams) mSideZoomBanner.getLayoutParams();

                    if (-layoutParam.topMargin == mSideZoomBanner.getHeight()) {//showHeader
                        ValueAnimator anim = ValueAnimator.ofInt(-mSideZoomBanner.getHeight(), 0);
                        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                Log.d("LQQQQQQ", "onAnimationUpdate: ");
                                ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) mSideZoomBanner.getLayoutParams();
                                layoutParams.topMargin = (Integer) animation.getAnimatedValue();
                                mSideZoomBanner.setLayoutParams(layoutParams);
                                mSideZoomBanner.invalidate();
                                mainScrollControl = true;
                            }
                        });
                        anim.setDuration(150);
                        anim.start();
                    }
                }

                if (mainScrollControl && absListView.getChildAt(0) != null && absListView.getChildAt(0).getTop() < -290 && absListView.getChildAt(0).getTop() != 0) {//hide
                    ViewGroup.MarginLayoutParams layoutParam = (ViewGroup.MarginLayoutParams) mSideZoomBanner.getLayoutParams();

                    if (layoutParam.topMargin >= 0) {
                        ValueAnimator anim = ValueAnimator.ofInt(0, -mSideZoomBanner.getHeight());
                        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) mSideZoomBanner.getLayoutParams();
                                layoutParams.topMargin = (Integer) animation.getAnimatedValue();
                                mSideZoomBanner.setLayoutParams(layoutParams);
                                mSideZoomBanner.invalidate();
                                mainScrollControl = false;
                            }
                        });
                        anim.setDuration(300);
                        anim.start();
                    }
                }
            }

            private List<PhotographerModel> loadData() {
                final List<PhotographerModel> list = new ArrayList<>();
                final LoginDataModel model = (LoginDataModel) cache.getAsObject("loginModel");
                final UserModel data = model.getUserModel();
                NetRequest requestFragment = new NetRequest(new NetworkCallbackInterface.NetRequestIterface() {
                    @Override
                    public void requestFinish(String result, String requestUrl) throws JSONException {
                        JSONObject json = new JSONObject(result);
                        String code = json.getString("code");
                        if (code.equals("10253")) {
                            JSONArray array = json.getJSONArray("contents");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject info = array.getJSONObject(i);
                                Gson gson = new Gson();
                                PhotographerModel photo = gson.fromJson(info.toString(), PhotographerModel.class);
                                list.add(photo);
                            }
                            bootCounter += array.length();
                            personAdapter.add(list);
                            Message msg = handler.obtainMessage();
                            msg.what = StatusCode.REQUEST_YUEPAI_MORE_GRAPH_LIST_SUCCESS;
                            handler.sendMessage(msg);
                        }else if (code.equals("10262")) {
                        }

                    }

                    @Override
                    public void exception(IOException e, String requestUrl) {
                    }

                }, APP.context);

                    Map<String, Object> map = new HashMap<>();
                    map.put("type", "10243");
                    map.put("authkey", data.getAuth_key());
                    map.put("uid", data.getId());
                    map.put("offsetapid", personAdapter.getItem(bootCounter - 1).getAPid());
                    requestFragment.httpRequest(map, CommonUrl.getYuePaiInfo);
                    Log.d("LQQQQQQQQQ", "request map");


                return list;
            }
        });
    }

    private List<PhotographerModel> bootData(){

        final List<PhotographerModel> list = new ArrayList<>();
        final LoginDataModel model = (LoginDataModel) cache.getAsObject("loginModel");
        final UserModel data = model.getUserModel();

        NetRequest requestFragment = new NetRequest(new NetworkCallbackInterface.NetRequestIterface() {
            @Override
            public void requestFinish(String result, String requestUrl) throws JSONException {
                JSONObject json = new JSONObject(result);
                String code = json.getString("code");
                Log.d("LQQQQQQ", "code:" + code);
                JSONArray array = json.getJSONArray("contents");
                if (code.equals("10251")) {
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject info = array.getJSONObject(i);
                        Gson gson = new Gson();
                        PhotographerModel photo = gson.fromJson(info.toString(), PhotographerModel.class);
                        Log.d("LQQQQQ", info.getString("APid"));
                        list.add(photo);

                    }
                    bootCounter+=array.length();
                    model.setPhotoList(list);
                    cache.put("loginModel", model);
                    personAdapter.add(list);
                    Message msg=handler.obtainMessage();
                    msg.what=StatusCode.REQUEST_YUEPAI_GRAPH_LIST_SUCCESS;
                    handler.sendMessage(msg);
                }else if (code.equals("10252")) {
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject info = array.getJSONObject(i);
                        Gson gson = new Gson();
                        PhotographerModel photo = gson.fromJson(info.toString(), PhotographerModel.class);
                        Log.d("LQQQQQ", info.getString("APid"));
                        list.add(photo);
                    }
                    bootCounter+=array.length();
                    model.setModelList(list);
                    cache.put("loginModel", model);
                    personAdapter.add(list);
                    Message msg=handler.obtainMessage();
                    msg.what=StatusCode.REQUEST_YUEPAI_MODEL_LIST_SUCCESS;
                    handler.sendMessage(msg);
                }
                else if(code.equals("10261")){}
                else if(code.equals("10262")){}

            }

            @Override
            public void exception(IOException e, String requestUrl) {
            }

        }, APP.context);

            Map<String, Object> map = new HashMap<>();
            map.put("type", "10231");
            map.put("authkey", data.getAuth_key());
            map.put("uid", data.getId());
            requestFragment.httpRequest(map, CommonUrl.getYuePaiInfo);
            Log.d("LQQQQQQQQQ", "request map");

        return list;

    }

    public void setmSideZoomBanner(RelativeLayout mSideZoomBanner){
        this.mSideZoomBanner = mSideZoomBanner;
    }

    public void doRefresh(){
        refreshLayout.setRefreshing(true);
        bootCounter=0;
        Log.d("LQQQQQQQQQ", "dorefresh");
        personAdapter.refresh(bootData());
        personAdapter.notifyDataSetChanged();
        refreshLayout.setRefreshing(false);
    }
}
