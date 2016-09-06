package com.example.pc.shacus.Fragment;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.pc.shacus.APP;
import com.example.pc.shacus.Adapter.YuePaiAdapter;
import com.example.pc.shacus.Data.Cache.ACache;
import com.example.pc.shacus.Data.Model.LoginDataModel;
import com.example.pc.shacus.Data.Model.PhotographerModel;
import com.example.pc.shacus.Network.NetRequest;
import com.example.pc.shacus.Network.NetworkCallbackInterface;
import com.example.pc.shacus.R;
import com.example.pc.shacus.Util.CommonUrl;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * licl 2016.9.3
 * 重写的排行榜fragment
 */
public class YuePaiFragmentD extends android.support.v4.app.Fragment{

    private Activity yuepai;

    final int GRAPHER = 0;
    final int MODEL = 1;

    private ImageButton button_grapher;
    private ImageButton button_model;
    boolean isGrapher;//是否显示摄影师

    boolean refreshing=false;
    private SwipeRefreshLayout refreshLayout;
    private YuePaiAdapter personAdapter;
    private ListView listView;
    private int bootCounter=0;
    private int maxRecords = 400;


    View rankView;
    private RelativeLayout mSideZoomBanner;
    private ACache cache;

    public YuePaiFragmentD() {
        isGrapher=true;
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
        button_grapher = (ImageButton) rankView.findViewById(R.id.button_grapher);
        button_model = (ImageButton) rankView.findViewById(R.id.button_model);

        personAdapter = new YuePaiAdapter(yuepai,bootData(GRAPHER));
        listView = (ListView) rankView.findViewById(R.id.rank_list);
        refreshLayout = (SwipeRefreshLayout) rankView.findViewById(R.id.swipe_refresh_layout);
        listView.setAdapter(personAdapter);

        onScrollListener();
        onRefreshListener();

        setListener();

        return rankView;
    }

    private void setListener(){
        button_grapher.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (isGrapher){
                }else {
                    isGrapher = true;
                    button_grapher.setImageResource(R.drawable.button_grapher_down);
                    button_model.setImageResource(R.drawable.button_model_up);

                    refreshLayout.setRefreshing(true);
                    bootCounter=0;
                    personAdapter.refresh(bootData(GRAPHER));
                    personAdapter.notifyDataSetChanged();//直接调用BaseAdapter的notify
                    refreshLayout.setRefreshing(false);

                }
                return false;
            }
        });

        button_model.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (isGrapher) {
                    isGrapher = false;
                    button_grapher.setImageResource(R.drawable.button_grapher_up);
                    button_model.setImageResource(R.drawable.button_model_down);

                    refreshLayout.setRefreshing(true);
                    bootCounter = 0;
                    personAdapter.refresh(bootData(MODEL));
                    personAdapter.notifyDataSetChanged();
                    refreshLayout.setRefreshing(false);

                }
                return false;
            }
        });

    }
    private void onRefreshListener(){
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);
                bootCounter = 0;
                personAdapter.refresh(bootData(isGrapher ? GRAPHER : MODEL));
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
                ViewGroup.MarginLayoutParams layoutParam = (ViewGroup.MarginLayoutParams) mSideZoomBanner.getLayoutParams();
                int firstVisibleItem = absListView.getFirstVisiblePosition();
                boolean onTop = firstVisibleItem == 0 && absListView.getChildAt(0) != null && absListView.getChildAt(0).getTop() == 0;
                if (onTop && -layoutParam.topMargin == mSideZoomBanner.getHeight()) {//showHeader
                    ValueAnimator anim = ValueAnimator.ofInt(-mSideZoomBanner.getHeight(), 0);
                    anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            //mSideZoomBanner.setPadding(0,(Integer)animation.getAnimatedValue(),0,0);
                            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) mSideZoomBanner.getLayoutParams();
                            layoutParams.topMargin = (Integer) animation.getAnimatedValue();
                            mSideZoomBanner.setLayoutParams(layoutParams);
                            mSideZoomBanner.invalidate();
                        }
                    });
                    anim.setDuration(150);
                    anim.start();
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount-1 > totalItemCount - 1 && totalItemCount < maxRecords) {
                    personAdapter.add(loadData(isGrapher ? GRAPHER : MODEL));
                    personAdapter.notifyDataSetChanged();
                }
            }

            private List<PhotographerModel> loadData(int i) {

                final  List<PhotographerModel> list=new ArrayList<>();

                NetRequest requestFragment = new NetRequest(new NetworkCallbackInterface.NetRequestIterface() {
                    @Override
                    public void requestFinish(String result, String requestUrl) throws JSONException {
                        list.add(0,null);
                    }

                    @Override
                    public void exception(IOException e, String requestUrl) {}

                }, APP.context);

                Map<String,Object> map=new HashMap<>();
                requestFragment.httpRequest(map, CommonUrl.url);

                return list;
            }
        });
    }

    private List<PhotographerModel> bootData(int type){

        LoginDataModel model=(LoginDataModel)cache.getAsObject("loginModel");
        List<PhotographerModel> persons=null;
        if (type==GRAPHER){
            persons =model.getPhotoList();
        }
        if (type==MODEL){
           persons = model.getModelList();
        }
        return persons;
    }

    public void setmSideZoomBanner(RelativeLayout mSideZoomBanner){
        this.mSideZoomBanner = mSideZoomBanner;
    }

    public void doRefresh(){
        refreshLayout.setRefreshing(true);
        bootCounter=0;
        personAdapter.refresh(bootData(isGrapher ? GRAPHER : MODEL));
        personAdapter.notifyDataSetChanged();
        refreshLayout.setRefreshing(false);
    }
}
