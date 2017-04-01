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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.pc.shacus.APP;
import com.example.pc.shacus.Adapter.PhotosetListAdapter;
import com.example.pc.shacus.Data.Cache.ACache;
import com.example.pc.shacus.Data.Model.LoginDataModel;
import com.example.pc.shacus.Data.Model.PhotosetItemModel;
import com.example.pc.shacus.Data.Model.UserModel;
import com.example.pc.shacus.Network.NetRequest;
import com.example.pc.shacus.Network.NetworkCallbackInterface;
import com.example.pc.shacus.Network.StatusCode;
import com.example.pc.shacus.R;
import com.example.pc.shacus.Util.CommonUrl;
import com.example.pc.shacus.Util.CommonUtils;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.pc.shacus.Network.StatusCode.REQUESTRECOMMENDED_PHOTOSET_LIST;
import static com.example.pc.shacus.Network.StatusCode.REQUEST_FAVOR_PHOTOSET_LIST;

/**
 * 李嘉文 2016.9.3
 * @Deprecated 重写的排行榜fragment
 *
 * @Deprecated 2016.9.12 lq修改
 *
 * 2017.2.6 李嘉文 检查
 * @Deprecated 原约拍列表页面（约拍一级页面）
 *
 * 2017.2.16 李嘉文 第二次重写
 * 修改为动态（作品集）列表页面
 *
 * 2017.3.20 李嘉文 第三次重写
 * 重新加上分类按钮并更换listview膜板
 *
 */

public class YuePaiFragmentD extends android.support.v4.app.Fragment{

    private Activity yuepai;

    final int FAVOR = 0;
    final int RECOMMEND = 1;
    final int INIT=2;

    private RelativeLayout button_favor;
    private RelativeLayout button_recommand;
    boolean isFavor;//是否显示摄影师

    boolean refreshing=false;
    private SwipeRefreshLayout refreshLayout;
    private PhotosetListAdapter personAdapter;
    private LinearLayout btn_favor_photoset;
    private LinearLayout btn_recommended_photoset;
    private ListView listView;
    private int bootCounter=0;
    private int maxRecords = 400;

    private boolean getYuePaiFlag=false;
    private boolean mainScrollControl=true;


    View rankView;
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
                case StatusCode.REQUEST_FAILURE:
                    CommonUtils.getUtilInstance().showToast(getActivity(),"拉取作品集列表失败");
                    break;
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        bootData(isFavor ? FAVOR : RECOMMEND);
        refreshLayout.setRefreshing(true);
        bootCounter = 0;
        personAdapter.refresh(new ArrayList<PhotosetItemModel>());
        personAdapter.notifyDataSetChanged();
        refreshLayout.setRefreshing(false);
        refreshing = true;
    }

    public YuePaiFragmentD(){
        isFavor =true;
    }

    public ListView getListView(){
        return listView;
    }

    public boolean isRefreshing(){
        return refreshing;
    }

    public void setFreshing(boolean bool){
        refreshing=bool;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        yuepai=this.getActivity();
        refreshing=false;
        rankView = inflater.inflate(R.layout.fragment_rank, container, false);
        cache= ACache.get(getActivity());
        button_favor = (RelativeLayout) rankView.findViewById(R.id.button_favor);
        button_recommand = (RelativeLayout) rankView.findViewById(R.id.button_recommend);

        personAdapter = new PhotosetListAdapter(yuepai,bootData(INIT));
        listView = (ListView) rankView.findViewById(R.id.rank_list);
        refreshLayout = (SwipeRefreshLayout) rankView.findViewById(R.id.swipe_refresh_layout);
        listView.setAdapter(personAdapter);
        btn_favor_photoset =(LinearLayout)rankView.findViewById(R.id.ll_favor_photoset);
        btn_recommended_photoset=(LinearLayout)rankView.findViewById(R.id.ll_recommended_photoset);

        onScrollListener();
        onRefreshListener();

        setListener();

        return rankView;
    }

    private void setListener(){
        button_favor.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event){
                if (isFavor){
                }else {
                    getYuePaiFlag=false;
                    isFavor = true;
                    //button_favor.setImageResource(R.drawable.button_grapher_down);
                    //button_recommand.setImageResource(R.drawable.button_model_up);
                    btn_favor_photoset.setBackgroundColor(getResources().getColor(R.color.main_green));
                    btn_recommended_photoset.setBackgroundColor(getResources().getColor(R.color.transparent));

                    refreshLayout.setRefreshing(true);
                    bootCounter=0;
                    personAdapter.refresh(bootData(INIT));
                    personAdapter.notifyDataSetChanged();//直接调用BaseAdapter的notify
                    refreshLayout.setRefreshing(false);
                }
                return false;
            }
        });

        button_recommand.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (isFavor) {
                    getYuePaiFlag=false;
                    isFavor = false;
                    btn_favor_photoset.setBackgroundColor(getResources().getColor(R.color.transparent));
                    btn_recommended_photoset.setBackgroundColor(getResources().getColor(R.color.main_green));

                    refreshLayout.setRefreshing(true);
                    bootCounter = 0;
                    personAdapter.refresh(bootData(INIT));
                    personAdapter.notifyDataSetChanged();
                    refreshLayout.setRefreshing(false);

                }
                return false;
            }
        });

    }
    private void onRefreshListener(){
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh() {
                bootData(isFavor ? FAVOR : RECOMMEND);
                refreshLayout.setRefreshing(true);
                bootCounter = 0;
                personAdapter.refresh(new ArrayList<PhotosetItemModel>());
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
                //停止的时候必然会调用这里，滑动到顶端的时候必然停止，所以在这里用判断top来实现showHeader
//                ViewGroup.MarginLayoutParams layoutParam = (ViewGroup.MarginLayoutParams) mSideZoomBanner.getLayoutParams();
//                int firstVisibleItem = absListView.getFirstVisiblePosition();
//                boolean onTop = firstVisibleItem == 0 && absListView.getChildAt(0) != null && absListView.getChildAt(0).getTop() < -150;
//                Log.d("LQ1111", "firstVisibleItem:" + firstVisibleItem + " \nabsListView.getChildAt(0):" + absListView.getChildAt(0) + "" +
//                        "\nabsListView.getChildAt(0).getTop():" + absListView.getChildAt(0).getTop()+
//                        "\nlayoutParam.topMargin:"+layoutParam.topMargin+"\n mSideZoomBanner.getHeight():"+ mSideZoomBanner.getHeight());
//                if (onTop && -layoutParam.topMargin == mSideZoomBanner.getHeight()) {//showHeader
//                    ValueAnimator anim = ValueAnimator.ofInt(-mSideZoomBanner.getHeight(), 0);
//                    anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                        @Override
//                        public void onAnimationUpdate(ValueAnimator animation) {
//                            //mSideZoomBanner.setPadding(0,(Integer)animation.getAnimatedValue(),0,0);
//                            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) mSideZoomBanner.getLayoutParams();
//                            layoutParams.topMargin = (Integer) animation.getAnimatedValue();
//                            mSideZoomBanner.setLayoutParams(layoutParams);
//                            mSideZoomBanner.invalidate();
//                        }
//                    });
//                    anim.setDuration(150);
//                    anim.start();
//                }
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount > totalItemCount - 1 && totalItemCount < maxRecords && totalItemCount != 0 && getYuePaiFlag) {
                    loadData(isFavor ? FAVOR : RECOMMEND);
                    getYuePaiFlag = false;
                }
                if (!mainScrollControl && firstVisibleItem == 0 && absListView.getChildAt(0) != null && absListView.getChildAt(0).getTop() > -10) {
                    ViewGroup.MarginLayoutParams layoutParam = (ViewGroup.MarginLayoutParams) mSideZoomBanner.getLayoutParams();
                    Log.d("LQ1111", "firstVisibleItem:" + firstVisibleItem + " \nabsListView.getChildAt(0):" + absListView.getChildAt(0) + "" +
                            "\nabsListView.getChildAt(0).getTop():" + absListView.getChildAt(0).getTop());
                    if (-layoutParam.topMargin == mSideZoomBanner.getHeight()) {//showHeader
                        ValueAnimator anim = ValueAnimator.ofInt(-mSideZoomBanner.getHeight(), 0);
                        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                //mSideZoomBanner.setPadding(0,(Integer)animation.getAnimatedValue(),0,0);
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


//                    Log.e("logout.topmargin", layoutParam.topMargin + "");
                if (mainScrollControl && absListView.getChildAt(0) != null && absListView.getChildAt(0).getTop() < -290 && absListView.getChildAt(0).getTop() != 0) {//hide
                    ViewGroup.MarginLayoutParams layoutParam = (ViewGroup.MarginLayoutParams) mSideZoomBanner.getLayoutParams();
                    Log.d("LQ1111", "firstVisibleItem:" + firstVisibleItem + " \nabsListView.getChildAt(0):" + absListView.getChildAt(0) + "" +
                            "\nabsListView.getChildAt(0).getTop():" + absListView.getChildAt(0).getTop());
                    if (layoutParam.topMargin >= 0) {
                        ValueAnimator anim = ValueAnimator.ofInt(0, -mSideZoomBanner.getHeight());
                        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                //mSideZoomBanner.setPadding(0,(Integer)animation.getAnimatedValue(),0,0);
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

            private List<PhotosetItemModel> loadData(int type) {
                final List<PhotosetItemModel> list = new ArrayList<>();
                final LoginDataModel model = (LoginDataModel) cache.getAsObject("loginModel");
                final UserModel data = model.getUserModel();
                NetRequest requestFragment = new NetRequest(new NetworkCallbackInterface.NetRequestIterface() {
                    @Override
                    public void requestFinish(String result, String requestUrl) throws JSONException {
                        JSONObject json = new JSONObject(result);
                        String code = json.getString("code");
                        if (code.equals(String.valueOf(StatusCode.RETURN_FAVOR_PHOTOSET_LIST)) && isFavor) {//加载更多的返回
                            JSONArray array = json.getJSONArray("contents");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject info = array.getJSONObject(i);
                                Gson gson = new Gson();
                                PhotosetItemModel photo = gson.fromJson(info.toString(), PhotosetItemModel.class);
                                Log.d("LQQQQQ", info.getString("APid"));
                                list.add(photo);
                            }
                            bootCounter += array.length();
                            personAdapter.add(list);
                            Message msg = handler.obtainMessage();
                            msg.what = StatusCode.REQUEST_YUEPAI_MORE_GRAPH_LIST_SUCCESS;
                            handler.sendMessage(msg);
                        }
                        if (code.equals(String.valueOf(StatusCode.RETURN_RECOMMENDED_PHOTOSET_LIST)) && !isFavor) {//加载更多的返回
                            JSONArray array = json.getJSONArray("contents");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject info = array.getJSONObject(i);
                                Gson gson = new Gson();
                                PhotosetItemModel photo = gson.fromJson(info.toString(), PhotosetItemModel.class);
                                Log.d("LQQQQQ", info.getString("APid"));
                                list.add(photo);
                            }
                            bootCounter += array.length();
                            personAdapter.add(list);
                            Message msg = handler.obtainMessage();
                            msg.what = StatusCode.REQUEST_YUEPAI_MORE_MODEL_LIST_SUCCESS;
                            handler.sendMessage(msg);
                        } else if (code.equals("10262")) {
                            Log.d("LQQQQQ", "加载失败");
                        }

                    }

                    @Override
                    public void exception(IOException e, String requestUrl) {
                    }

                }, APP.context);

                if (type == FAVOR){
                    Map<String, Object> map = new HashMap<>();
                    map.put("type", StatusCode.REQUEST_MORE_FAVOR_PHOTOSET_LIST);
                    map.put("authkey", data.getAuth_key());
                    map.put("index", personAdapter.getItem(bootCounter - 1).getUCid());
                    requestFragment.httpRequest(map, CommonUrl.imgSelfAndSets);
                } else if (type == RECOMMEND) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("type", StatusCode.REQUEST_MORE_RECOMMENDED_PHOTOSET_LIST);
                    map.put("authkey", data.getAuth_key());
                    map.put("index", personAdapter.getItem(bootCounter - 1).getUCid());
                    requestFragment.httpRequest(map, CommonUrl.imgSelfAndSets);
                }

                return list;
            }
        });
    }

    private List<PhotosetItemModel> bootData(int type){

        if(type==INIT){
            if(isFavor){
                LoginDataModel model=(LoginDataModel)cache.getAsObject("loginModel");
                List<PhotosetItemModel> persons=null;
                persons =model.getCollectionList();
                bootCounter+=persons.size();
                getYuePaiFlag=true;
                return persons;
            } else if(!isFavor){
                LoginDataModel model=(LoginDataModel)cache.getAsObject("loginModel");
                List<PhotosetItemModel> persons=null;
                persons =model.getCollectionList();
                bootCounter+=persons.size();
                getYuePaiFlag=true;
                return persons;
            }
        }



        final List<PhotosetItemModel> list = new ArrayList<>();
        final LoginDataModel model = (LoginDataModel) cache.getAsObject("loginModel");
        final UserModel data = model.getUserModel();

        NetRequest requestFragment = new NetRequest(new NetworkCallbackInterface.NetRequestIterface() {
            @Override
            public void requestFinish(String result, String requestUrl) throws JSONException {
                JSONObject json = new JSONObject(result);
                String code = json.getString("code");
                JSONArray array = json.getJSONArray("contents");
                if (code.equals(String.valueOf(REQUEST_FAVOR_PHOTOSET_LIST))) {
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject info = array.getJSONObject(i);
                        Gson gson = new Gson();
                        PhotosetItemModel photo = gson.fromJson(info.toString(), PhotosetItemModel.class);

                        list.add(photo);

                    }
                    bootCounter+=array.length();
                    model.setCollectionList(list);
                    cache.put("loginModel", model);
                    personAdapter.add(list);
                    Message msg=handler.obtainMessage();
                    msg.what=StatusCode.REQUEST_YUEPAI_GRAPH_LIST_SUCCESS;
                    handler.sendMessage(msg);
                    return;
                }else if (code.equals(String.valueOf(REQUESTRECOMMENDED_PHOTOSET_LIST))) {
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject info = array.getJSONObject(i);
                        Gson gson = new Gson();
                        PhotosetItemModel photo = gson.fromJson(info.toString(), PhotosetItemModel.class);

                        list.add(photo);
                    }
                    bootCounter+=array.length();
                    model.setCollectionList(list);
                    cache.put("loginModel", model);
                    personAdapter.add(list);
                    Message msg=handler.obtainMessage();
                    msg.what=StatusCode.REQUEST_YUEPAI_MODEL_LIST_SUCCESS;
                    handler.sendMessage(msg);
                    return;
                }else {
                    Message msg=handler.obtainMessage();
                    msg.what=StatusCode.REQUEST_FAILURE;
                    handler.sendMessage(msg);
                }
//                else if(code.equals("10261")){}
//                else if(code.equals("10262")){}
            }

            @Override
            public void exception(IOException e, String requestUrl) {
            }

        }, APP.context);

        if(type== FAVOR){
            Map<String, Object> map = new HashMap<>();
            map.put("type", REQUEST_FAVOR_PHOTOSET_LIST);
            map.put("authkey", data.getAuth_key());
            requestFragment.httpRequest(map, CommonUrl.imgSelfAndSets);
        }else if(type== RECOMMEND){
            Map<String, Object> map = new HashMap<>();
            map.put("type", REQUESTRECOMMENDED_PHOTOSET_LIST);
            map.put("authkey", data.getAuth_key());
            requestFragment.httpRequest(map, CommonUrl.imgSelfAndSets);
        }
        return list;

    }

    public void setmSideZoomBanner(RelativeLayout mSideZoomBanner){
        this.mSideZoomBanner = mSideZoomBanner;
    }

    public void doRefresh(){
        refreshLayout.setRefreshing(true);
        bootCounter=0;
        personAdapter.refresh(bootData(isFavor ? FAVOR : RECOMMEND));
        personAdapter.notifyDataSetChanged();
        refreshLayout.setRefreshing(false);
    }
}