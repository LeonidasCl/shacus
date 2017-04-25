package com.example.pc.shacus.swipecards.swipe;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.pc.shacus.Activity.CoursesActivity;
import com.example.pc.shacus.Activity.LoginActivity;
import com.example.pc.shacus.Activity.MainActivity;
import com.example.pc.shacus.Activity.OtherUserActivity;
import com.example.pc.shacus.Data.Cache.ACache;
import com.example.pc.shacus.Data.Model.LoginDataModel;
import com.example.pc.shacus.Data.Model.PhotosetItemModel;
import com.example.pc.shacus.Data.Model.UserModel;
import com.example.pc.shacus.Network.NetRequest;
import com.example.pc.shacus.Network.NetworkCallbackInterface;
import com.example.pc.shacus.Network.StatusCode;
import com.example.pc.shacus.Util.CommonUrl;
import com.example.pc.shacus.Util.CommonUtils;
import com.example.pc.shacus.View.Custom.RoundImageView;
import com.example.pc.shacus.swipecards.SwipeFlingView;
import com.example.pc.shacus.swipecards.test.TestData;
import com.example.pc.shacus.swipecards.util.BaseModel;
import com.example.pc.shacus.swipecards.util.CardEntity;
import com.example.pc.shacus.swipecards.util.RetrofitHelper;
import com.example.pc.shacus.swipecards.view.SwipeFlingBottomLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit2.Call;

import com.example.pc.shacus.R;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.pc.shacus.APP.context;


/**
 * 卡片Fragment
 *
 * @author zc
 */
public class CardFragment extends Fragment implements SwipeFlingView.OnSwipeFlingListener,
        SwipeFlingBottomLayout.OnBottomItemClickListener, SwipeFlingView.OnItemClickListener,
        NetworkCallbackInterface.NetRequestIterface {

    private final static String TAG = CardFragment.class.getSimpleName();
    private final static boolean DEBUG = true;
    private NetRequest requestFragment;
    private ACache acache;
    private UserModel user;
    private String authkey;
    private String category;
    private PhotosetItemModel photosetItemModel;

    private int requestTime = 1; //第几次请求
    private static final int MSG_SUCCESS = 0;//获取数据成功的标识
    private static final int MSG_FAILURE = 1;//获取数据失败的标识

    @InjectView(R.id.frame)
    SwipeFlingView mSwipeFlingView;

    @InjectView(R.id.self_main)
    RoundImageView mImageView;

    @InjectView(R.id.swipe_fling_bottom)
    SwipeFlingBottomLayout mBottomLayout;

    private UserAdapter mAdapter;
    private UserModel userModel;
    private View view;
    private CheckBox checkbox_people_photogragher;
    private CheckBox checkbox_people_model;
    private CheckBox checkbox_people_all;


    private int mPageIndex = 0;
    private boolean mIsRequestGirlList;
    private ArrayList<CardEntity> mGrilList = new ArrayList<>();//这个list不用了，但暂时先保留
    private ArrayList<CardEntity> mOurList = new ArrayList<>();//自己的list


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.card_layout, null);
        ButterKnife.inject(this, rootView);
        initView();
        requestOurList();
        return rootView;
    }

    private void initView() {

        mAdapter = new UserAdapter(getActivity(), mGrilList);
        mSwipeFlingView.setAdapter(mAdapter);
        mSwipeFlingView.setOnSwipeFlingListener(this);//SimpleOnSwipeListener/OnSwipeListener
        mSwipeFlingView.setOnItemClickListener(this);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acache = ACache.get(getActivity());
                PhotosetItemModel model = (PhotosetItemModel) acache.getAsObject("PhotosetItemModel");
                Intent intent = new Intent(getActivity(), OtherUserActivity.class);
                intent.putExtra("id", model.getUCid());
                startActivity(intent);
            }
        });

        mBottomLayout.setOnBottomItemClickListener(this);
    }

    //筛选之后调用这个方法
    private void updateListView(ArrayList<CardEntity> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        mGrilList.addAll(list);
        mAdapter.notifyDataSetChanged();
    }



    private void requestOurList() {
        requestFragment = new NetRequest(CardFragment.this, getContext());
        acache = ACache.get(getActivity());
        LoginDataModel loginModel = (LoginDataModel) acache.getAsObject("loginModel");
        userModel = loginModel.getUserModel();
        user = loginModel.getUserModel();
        authkey = user.getAuth_key();
        Map map = new HashMap();
        if (mIsRequestGirlList) {
            return;
        }
        mIsRequestGirlList = true;
        if(requestTime == 3 || requestTime == 1){   //之后的请求次数多，把3放在前面提高效率
            map.put("type", StatusCode.RECOMMAND_PHOTOGRAPHER_MODEL_LIST);  //10850
            map.put("authkey", authkey);
            requestFragment.httpRequest(map, CommonUrl.requestModel);
        }
//        else if(requestTime == 2){
//            category = user.getCategory();
//            map.put("type", StatusCode.CHANGE_USER_CATEGORY);   //10852
//            map.put("authkey", authkey);
//            if (category == "摄影师") map.put("category", 1);
//            if (category == "模特") map.put("category", 2);
//            requestFragment.httpRequest(map, CommonUrl.requestModel);
//        }
    }

    private void alertDialogShow(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialog);
        builder.setPositiveButton("确定", null);
        // 通过LayoutInflater来加载一个xml的布局文件作为一个View对象
        view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_selector_category, null);

        // 设置我们自己定义的布局文件作为弹出框的Content
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        checkbox_people_photogragher = (CheckBox) view.findViewById(R.id.people_photogragher);
        checkbox_people_model = (CheckBox) view.findViewById(R.id.people_model);
        checkbox_people_all = (CheckBox) view.findViewById(R.id.people_all);

        checkbox_people_photogragher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkbox_people_model.isChecked()) checkbox_people_model.setChecked(false);
                if(checkbox_people_all.isChecked()) checkbox_people_all.setChecked(false);
            }
        });
        checkbox_people_model.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkbox_people_photogragher.isChecked()) checkbox_people_photogragher.setChecked(false);
                if(checkbox_people_all.isChecked()) checkbox_people_all.setChecked(false);
            }
        });
        checkbox_people_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkbox_people_photogragher.isChecked()) checkbox_people_photogragher.setChecked(false);
                if(checkbox_people_model.isChecked()) checkbox_people_model.setChecked(false);
            }
        });

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acache = ACache.get(getActivity());
                LoginDataModel loginModel = (LoginDataModel) acache.getAsObject("loginModel");
                userModel = loginModel.getUserModel();
                if((!checkbox_people_all.isChecked()) && (!checkbox_people_model.isChecked()) &&
                        (!checkbox_people_photogragher.isChecked())) {
                    CommonUtils commonUtils = new CommonUtils();
                    commonUtils.showToast(getContext(), "尚未选择！");
                }


                else if (checkbox_people_all.isChecked()) {
                    userModel.setCategory("两者都是");
                    alertDialog.dismiss();
                } else if (checkbox_people_photogragher.isChecked()){
                    userModel.setCategory("摄影师");
                    alertDialog.dismiss();
                }
                else if (checkbox_people_model.isChecked()) {
                    userModel.setCategory("模特");
                    alertDialog.dismiss();
                }

                requestFragment = new NetRequest(CardFragment.this, getContext());
                authkey = userModel.getAuth_key();
                Map map = new HashMap();
                category = userModel.getCategory();
                map.put("type", StatusCode.CHANGE_USER_CATEGORY);   //10852
                map.put("authkey", authkey);
                if (category == "摄影师") map.put("category", 1);
                if (category == "模特") map.put("category", 2);
                if (category == "两者都是") map.put("category", 12);
                requestFragment.httpRequest(map, CommonUrl.requestModel); //调试弹框需要注释掉这一句话，否则传到服务器后就不弹了
            }
        });
        Button btnPositive =
                alertDialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE);
        btnPositive.setTextColor(getResources().getColor(R.color.ff_white));
        btnPositive.setTextSize(15);
        requestOurList();
    }

    private Handler mHandler = new Handler() {
        public void handleMessage (Message msg) {//此方法在ui线程运行
            switch(msg.what) {
                case MSG_SUCCESS:
                    alertDialogShow();
                case MSG_FAILURE:
            }
        }
    };

    @Override
    public void requestFinish(String result, String requestUrl) throws JSONException {
        //Message msg = new Message();
        if (requestUrl.equals(CommonUrl.requestModel)) {
            mIsRequestGirlList = false;
            JSONObject object = new JSONObject(result);
            Gson gson = new Gson();
            //Log.d("CradFragment", object.getJSONArray("contents").getJSONObject(0).toString());
            int code = Integer.valueOf(object.getString("code"));
            switch (code){
                case 10850://返回了推荐的卡片
                    photosetItemModel = gson.fromJson(object.getJSONArray("contents").getJSONObject(0).toString(), PhotosetItemModel.class);
                    break;
                case 10851://设置字段
                    requestTime++;
                    mHandler.obtainMessage(MSG_SUCCESS, requestTime).sendToTarget();
                    break;
                case 10852://设置成功返回字段
                    String contents = String.valueOf(object.getString("contents"));
                    if(contents == "修改用户类型成功") requestTime++;//添加错误逻辑
                    requestOurList();
                    break;

            }
            Log.d("CardFragment", "已接受到返回数据");

        }
    }

    private void requestGirlList() {
        if (mIsRequestGirlList) {
            return;
        }
        mIsRequestGirlList = true;
        Call<BaseModel<ArrayList<CardEntity>>> call = RetrofitHelper.api().getGirlList(mPageIndex);
        RetrofitHelper.call(call, new RetrofitHelper.ApiCallback<ArrayList<CardEntity>>() {
            @Override
            public void onLoadSucceed(ArrayList<CardEntity> result) {
                updateListView(result);
                ++mPageIndex;
                mIsRequestGirlList = false;
            }

            @Override
            public void onLoadFail(int statusCode) {
                mIsRequestGirlList = false;
                Toast.makeText(getActivity(), "API服务器请求失败,使用默认测试数据填充", Toast.LENGTH_LONG).show();
                addTestData();
            }

            @Override
            public void onForbidden() {
                mIsRequestGirlList = false;
            }
        });
    }

    private void addTestData() {
        updateListView(TestData.getApiData(getActivity()));
    }

    @Override
    public void onStartDragCard() {
        if (DEBUG) {
            Log.d(TAG, "SwipeFlingView onStartDragCard");
        }
    }

    @Override
    public void onPreCardExit() {
        if (DEBUG) {
            Log.d(TAG, "SwipeFlingView onPreCardExit");
        }
    }

    @Override
    public void onTopCardViewFinish() {
        if (DEBUG) {
            Log.d(TAG, "SwipeFlingView onTopCardViewFinish");
        }
    }

    @Override
    public boolean canLeftCardExit() {
        if (DEBUG) {
            Log.d(TAG, "SwipeFlingView canLeftCardExit");
        }
        return true;
    }

    @Override
    public boolean canRightCardExit() {
        if (DEBUG) {
            Log.d(TAG, "SwipeFlingView canRightCardExit");
        }
        return true;
    }

    /*
    * 向左滑或点击不喜欢按钮
    * */
    @Override
    public void onLeftCardExit(View view, Object dataObject, boolean triggerByTouchMove) {
        if (DEBUG) {
            Log.d(TAG, "SwipeFlingView onLeftCardExit");
            int cur = Integer.valueOf(dataObject.toString());
            CardEntity card = mAdapter.getItem(cur);
            String excited = card.url;
            Log.d("excited", "不喜欢 :" + excited);
        }
        if (triggerByTouchMove)
            mBottomLayout.getUnLikeView().animateDragAnimation();
    }

    /*
    * 向右滑或点击感兴趣按钮
    * */
    @Override
    public void onRightCardExit(View view, Object dataObject, boolean triggerByTouchMove) {
        if (DEBUG) {
            Log.d(TAG, "SwipeFlingView onRightCardExit");
            int cur = Integer.valueOf(dataObject.toString());
            CardEntity card = mAdapter.getItem(cur);
            String excited = card.url;
            Log.d("excited", "感兴趣 :" + excited);
        }
        if (triggerByTouchMove)
            mBottomLayout.getLikeView().animateDragAnimation();
    }

    @Override
    public void onSelfChat(View view, Object dataObject, boolean triggerByTouchMove) {
        if (DEBUG) {
            Log.d(TAG, "SwipeFlingView onSuperLike");
            int cur = Integer.valueOf(dataObject.toString());
            CardEntity card = mAdapter.getItem(cur);
            String excited = card.url;
            Log.d("excited", "关注 :" + excited);
        }
        if (triggerByTouchMove)
            mBottomLayout.getSuperLikeView().animateDragAnimation();
    }

    /*
    * 从服务器加载新的卡片数据
    * */
    @Override
    public void onAdapterAboutToEmpty(int itemsInAdapter) {
        if (DEBUG) {
            Log.d(TAG, "SwipeFlingView onAdapterAboutToEmpty");
        }
        requestOurList();
    }

    @Override
    public void onAdapterEmpty() {
        if (DEBUG) {
            Log.d(TAG, "SwipeFlingView onAdapterEmpty");
        }
    }

    @Override
    public void onScroll(View selectedView, float scrollProgressPercent) {
        if (DEBUG) {
            Log.d(TAG, "SwipeFlingView onScroll " + scrollProgressPercent);
        }
    }

    @Override
    public void onEndDragCard() {
        if (DEBUG) {
            Log.d(TAG, "SwipeFlingView onEndDragCard");
        }
    }

    /*@Override
    public void onComeBackClick() {
        //参数决定动画开始位置是从左边还是右边出现
        mSwipeFlingView.selectComeBackCard(true);
    }*/

    @Override
    public void onSelfChatClick() {
        if (mSwipeFlingView.isAnimationRunning()) {
            return;
        }
        mSwipeFlingView.selectSuperLike(false);
    }

    @Override
    public void onLikeClick() {
        if (mSwipeFlingView.isAnimationRunning()) {
            return;
        }
        mSwipeFlingView.selectRight(false);
    }

    @Override
    public void onUnLikeClick() {
        if (mSwipeFlingView.isAnimationRunning()) {
            return;
        }
        mSwipeFlingView.selectLeft(false);
    }

    @Override
    public void onItemClicked(int itemPosition, Object dataObject) {
        if (DEBUG) {
            Log.d("excited", "onItemClicked itemPosition:" + itemPosition);
            CardEntity card = mAdapter.getItem(itemPosition);
            String excited = card.url;
            Log.d("excited", "clicked url :" + excited);
        }
    }

    @Override
    public void exception(IOException e, String requestUrl) {

    }
}
