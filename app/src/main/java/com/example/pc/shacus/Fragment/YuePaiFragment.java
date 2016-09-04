package com.example.pc.shacus.Fragment;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pc.shacus.APP;
import com.example.pc.shacus.Activity.CreateYuePaiActivity;
import com.example.pc.shacus.Data.Cache.ACache;
import com.example.pc.shacus.Data.Model.LoginDataModel;
import com.example.pc.shacus.Data.Model.NavigationModel;
import com.example.pc.shacus.Data.Model.PhotographerModel;
import com.example.pc.shacus.Data.Model.PictureModel;
import com.example.pc.shacus.Network.NetworkCallbackInterface;
import com.example.pc.shacus.Network.NetRequest;
import com.example.pc.shacus.R;
import com.example.pc.shacus.Util.CommonUrl;
import com.google.gson.Gson;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.view.ViewPropertyAnimator;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bingoogolapple.bgabanner.BGABanner;
import cn.bingoogolapple.bgabanner.BGABannerUtil;

/**
 * 约拍主界面（一级）
 * Created by pc on 2016/7/7.
 */
public class YuePaiFragment extends android.support.v4.app.Fragment implements NetworkCallbackInterface.NetRequestIterface{

    private BGABanner mSideZoomBanner;
    private NetRequest requestFragment;
    FragmentManager fragmentManager;
    private FragmentTransaction fragmentTrs;
    private YuePaiFragmentC yuepaiFragment;
    private YuePaiFragmentAB yuePaiFragmentAB;
    private ArrayList tips=new ArrayList();
    private ImageButton btn_yuepai_a;
    private ImageButton btn_yuepai_b;
    private ImageButton btn_yuepai_c;
    private ImageButton btn_create_yuepai;
    private float mLastTouchY;
    private float mDelY;
    private YuePaiFragmentD rankFrag;
    private List<NavigationModel> navigationBar;
    private ACache cache;

    @Override
    public void onResume() {
        //如果之前动过还原动画flag
        super.onResume();
    }

    public YuePaiFragmentD getRankFrag(){
        return rankFrag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_yuepai, container, false);

        rankFrag =new YuePaiFragmentD();

        mSideZoomBanner = (BGABanner) view.findViewById(R.id.banner_main_zoom);
        setListener();
        loadData();
        FragmentManager childfragManager=getChildFragmentManager();
        childfragManager.beginTransaction().replace(R.id.fragment_rank, rankFrag).commit();
        mSideZoomBanner.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                int x = mSideZoomBanner.getCurrentItem();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        btn_yuepai_a=(ImageButton)view.findViewById(R.id.btn_yuepai_a);
        btn_yuepai_a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                ViewPropertyAnimator.animate(v).scaleY(1.4f).scaleX(1.4f).setDuration(80)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                ViewPropertyAnimator.animate(v).scaleY(1.2f).scaleX(1.2f)
                                        .setListener(new AnimatorListenerAdapter() {
                                            @Override
                                            public void onAnimationEnd(Animator animation) {
                                            if (yuePaiFragmentAB == null) {
                                                yuePaiFragmentAB = new YuePaiFragmentAB();
                                                yuePaiFragmentAB.setEventflag(1);
                                            }
                                            fragmentTrs = fragmentManager.beginTransaction();
                                            fragmentTrs.replace(R.id.fl_content, yuePaiFragmentAB);
                                            fragmentTrs.addToBackStack(null);
                                            fragmentTrs.commit();
                                            }
                                        }).setDuration(30);
                            }
                        });
            }
        });

        btn_yuepai_b=(ImageButton)view.findViewById(R.id.btn_yuepai_b);
        btn_yuepai_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                ViewPropertyAnimator.animate(v).scaleY(1.4f).scaleX(1.4f).setDuration(80)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                ViewPropertyAnimator.animate(v).scaleY(1.2f).scaleX(1.2f)
                                        .setListener(new AnimatorListenerAdapter() {
                                            @Override
                                            public void onAnimationEnd(Animator animation) {
                                                if (yuePaiFragmentAB == null) {
                                                    yuePaiFragmentAB = new YuePaiFragmentAB();
                                                    yuePaiFragmentAB.setEventflag(2);
                                                }
                                                fragmentTrs = fragmentManager.beginTransaction();
                                                fragmentTrs.replace(R.id.fl_content, yuePaiFragmentAB);
                                                fragmentTrs.addToBackStack(null);
                                                fragmentTrs.commit();
                                            }
                                        }).setDuration(30);
                            }
                        });
            }
        });

        btn_yuepai_c=(ImageButton)view.findViewById(R.id.btn_yuepai_c);
        btn_yuepai_c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                ViewPropertyAnimator.animate(v).scaleY(1.4f).scaleX(1.4f).setDuration(80)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                ViewPropertyAnimator.animate(v).scaleY(1.2f).scaleX(1.2f)
                                        .setListener(new AnimatorListenerAdapter() {
                                            @Override
                                            public void onAnimationEnd(Animator animation) {
                                                if (yuepaiFragment == null) {
                                                    yuepaiFragment = new YuePaiFragmentC();
                                                }
                                                fragmentTrs = fragmentManager.beginTransaction();
                                                fragmentTrs.replace(R.id.fl_content, yuepaiFragment);
                                                fragmentTrs.addToBackStack(null);
                                                fragmentTrs.commit();
                                            }
                                        }).setDuration(30);
                            }
                        });
            }
        });

        btn_create_yuepai=(ImageButton)view.findViewById(R.id.btn_create_yuepai);
        btn_create_yuepai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CreateYuePaiActivity.class));
            }
        });

        //touchMove=rankView.findViewById(R.id.fragment_rank)
        rankFrag.setmSideZoomBanner(mSideZoomBanner);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        int x=2;
        x++;
    }

    @Override
    public void onStart(){

       rankFrag.getListView().setOnTouchListener(new View.OnTouchListener() {
           private float downY;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
            int firstVisibleItem = rankFrag.getListView().getFirstVisiblePosition();
            boolean onTop = firstVisibleItem == 0 &&rankFrag.getListView().getChildAt(0) != null && rankFrag.
                    getListView().getChildAt(0).getTop() == 0;
                Log.w("logout.gettop",rankFrag.getListView().getChildAt(0).getTop()+"");
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        ViewGroup.MarginLayoutParams layoutParam = (ViewGroup.MarginLayoutParams)mSideZoomBanner.getLayoutParams();
                        Log.e("logout.topmargin",layoutParam.topMargin+"");
                        if (layoutParam.topMargin>=0&&!onTop){//hide
                            ValueAnimator anim=ValueAnimator.ofInt(0,-mSideZoomBanner.getHeight());
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
                            anim.setDuration(300);
                            anim.start();
                            break;
                        }
                        break;
                    case MotionEvent.ACTION_UP:

                        break;
                }

                return false;
            }
        });

        super.onStart();
    }



    private void loadData() {
        fragmentManager=getFragmentManager();
        tips.clear();
        cache= ACache.get(getActivity());
        Gson gson=new Gson();
        JSONObject userStr=cache.getAsJSONObject("loginModel");
        LoginDataModel model=gson.fromJson(userStr.toString(), LoginDataModel.class);
        navigationBar=model.getDaohanglan();
        mSideZoomBanner.setViews(getViews(navigationBar.size()));
        loadData(navigationBar.size());

    }

    private void loadData(int i) {
        //requestFragment = new NetRequest(this, APP.context);
//        Map map = new HashMap();
//        String count=String.valueOf(i);
//        map.put("userID", "15652009705");
//        map.put("usetToken", "123456");
//        map.put("requestNum", "100XX");
//        map.put("pictureCount", count);
        //requestFragment.httpRequest(map, CommonUrl.getYuepaiNavigateurl);
        for (int j=0;j<i;j++){
            ImageView view=mSideZoomBanner.getItemImageView(j);
            Glide.with(YuePaiFragment.this)
                    .load(navigationBar.get(j).getImgurl())
                    .placeholder(R.drawable.holder)
                    .error(R.drawable.holder)
                    .into(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //打开活动webView
                }
            });
        }
    }


    private void setListener() {
        mSideZoomBanner.setDelegate(new BGABanner.Delegate() {
            @Override
            public void onClickBannerItem(int position) {

                if (position==0){
                }

                if (position==1){
                }

               if(position==2) {
               }

                if (position==3){
                }
            }
        });
    }


    @Override
    public void requestFinish(String result, String requestUrl) {
        if (requestUrl.equals(CommonUrl.getYuepaiNavigateurl)) {
            Gson gson = new Gson();
            PictureModel picStatusInfoObject = gson.fromJson(result, PictureModel.class);
            String errorCode = picStatusInfoObject.getErrorCode();
            if (errorCode.equals("0")){
                /*for (int i=0;i<naviContent;i++){
                    String currentCount="pic"+String.valueOf(i);
                    String picUrl=picStatusInfoObject.getPicUrl(currentCount);
                    Glide.with(YuePaiFragment.this)
                            .load(picUrl)
                            .placeholder(R.drawable.holder)
                            .error(R.drawable.holder)
                            .into(mSideZoomBanner.getItemImageView(i));
                }*/
            }
        }
    }

    @Override
    public void exception(IOException e, String requestUrl) {
        Toast.makeText(APP.getInstance(), "获取网络数据失败！请检查网络连接！", Toast.LENGTH_SHORT).show();
    }

    private List<ImageView> getViews(int count) {
        List<ImageView> views = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            views.add(BGABannerUtil.getItemImageView(getContext(), R.drawable.holder));
        }
        return views;
    }
}
