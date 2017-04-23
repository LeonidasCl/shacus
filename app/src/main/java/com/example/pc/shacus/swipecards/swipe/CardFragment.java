package com.example.pc.shacus.swipecards.swipe;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private PhotosetItemModel photosetItemModel;

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

    private int mPageIndex = 0;
    private boolean mIsRequestGirlList;
    private ArrayList<CardEntity> mGrilList = new ArrayList<>();


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

        //判断是否设置了自己是摄影师还是模特
        ACache cache = ACache.get(getActivity());
        LoginDataModel loginModel = (LoginDataModel) cache.getAsObject("loginModel");
        userModel = loginModel.getUserModel();
        String category = userModel.getCategory();
        if (category == null) {
            AlertDialog builder = new AlertDialog.Builder(getContext(), R.style.AlertDialog).create();
            builder.setTitle("请选择您的职业：");
            // 通过LayoutInflater来加载一个xml的布局文件作为一个View对象
            view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_selector_category, null);
            // 设置我们自己定义的布局文件作为弹出框的Content
            builder.setView(view);
            checkbox_people_photogragher = (CheckBox) view.findViewById(R.id.people_photogragher);
            checkbox_people_model = (CheckBox) view.findViewById(R.id.people_model);
            builder.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (checkbox_people_model.isChecked() && checkbox_people_photogragher.isChecked()) {
                        userModel.setCategory("摄影师和模特");
                    } else if (checkbox_people_photogragher.isChecked())
                        userModel.setCategory("摄影师");
                    else if (checkbox_people_model.isChecked()) userModel.setCategory("模特");
                }
            });
        }

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
        user = loginModel.getUserModel();
        authkey = user.getAuth_key();   //authkey npe
        Map map = new HashMap();
        if (mIsRequestGirlList) {
            return;
        }
        mIsRequestGirlList = true;
        map.put("type", StatusCode.RECOMMAND_PHOTOGRAPHER_MODEL_LIST);
        map.put("authkey", authkey);
        requestFragment.httpRequest(map, CommonUrl.requestModel);
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

    @Override
    public void requestFinish(String result, String requestUrl) throws JSONException {
        //Message msg = new Message();
        if (requestUrl.equals(CommonUrl.requestModel)) {
            JSONObject object = new JSONObject(result);
            Gson gson = new Gson();
            Log.d("CradFragment", object.getJSONArray("contents").getJSONObject(0).toString());
            //int code = Integer.valueOf(object.getString("code"));
            Log.d("CardFragment", "已接受到返回数据");
            photosetItemModel = gson.fromJson(object.getJSONArray("contents").getJSONObject(0).toString(), PhotosetItemModel.class);
        }
    }
}
