package com.example.pc.shacus.swipecards.swipe;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.pc.shacus.Activity.MainActivity;
import com.example.pc.shacus.Activity.OtherUserActivity;
import com.example.pc.shacus.Data.Cache.ACache;
import com.example.pc.shacus.Data.Model.LoginDataModel;
import com.example.pc.shacus.Data.Model.UserModel;
import com.example.pc.shacus.View.Custom.RoundImageView;
import com.example.pc.shacus.swipecards.SwipeFlingView;
import com.example.pc.shacus.swipecards.test.TestData;
import com.example.pc.shacus.swipecards.util.BaseModel;
import com.example.pc.shacus.swipecards.util.CardEntity;
import com.example.pc.shacus.swipecards.util.RetrofitHelper;
import com.example.pc.shacus.swipecards.view.SwipeFlingBottomLayout;
import java.util.ArrayList;
import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit2.Call;
import com.example.pc.shacus.R;

import static com.example.pc.shacus.APP.context;


/**
 * 卡片Fragment
 * @author zc
 */
public class CardFragment extends Fragment implements SwipeFlingView.OnSwipeFlingListener,
        SwipeFlingBottomLayout.OnBottomItemClickListener, SwipeFlingView.OnItemClickListener {

    private final static String TAG = CardFragment.class.getSimpleName();
    private final static boolean DEBUG = true;
    private ACache acache;

    @InjectView(R.id.frame)
    SwipeFlingView mSwipeFlingView;

    @InjectView(R.id.self_main)
    RoundImageView mImageView;

    @InjectView(R.id.swipe_fling_bottom)
    SwipeFlingBottomLayout mBottomLayout;

    private UserAdapter mAdapter;

    private int mPageIndex = 0;
    private boolean mIsRequestGirlList;
    private ArrayList<CardEntity> mGrilList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.card_layout, null);
        ButterKnife.inject(this, rootView);
        initView();
        requestGirlList();
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
                UserModel model=(UserModel) acache.getAsObject("userModel");
                Intent intent = new Intent(getActivity(),OtherUserActivity.class);
                intent.putExtra("id", model.getId());
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

    private void requestGirlList(){
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
            int cur= Integer.valueOf(dataObject.toString());
            CardEntity card=mAdapter.getItem(cur);
            String excited=card.url;
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
            int cur= Integer.valueOf(dataObject.toString());
            CardEntity card=mAdapter.getItem(cur);
            String excited=card.url;
            Log.d("excited", "感兴趣 :" + excited);
        }
        if (triggerByTouchMove)
            mBottomLayout.getLikeView().animateDragAnimation();
    }

    @Override
    public void onSelfChat(View view, Object dataObject, boolean triggerByTouchMove) {
        if (DEBUG) {
            Log.d(TAG, "SwipeFlingView onSuperLike");
            int cur= Integer.valueOf(dataObject.toString());
            CardEntity card=mAdapter.getItem(cur);
            String excited=card.url;
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
        requestGirlList();
    }

    @Override
    public void onAdapterEmpty(){
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
        if (mSwipeFlingView.isAnimationRunning()){
            return;
        }
        mSwipeFlingView.selectLeft(false);
    }

    @Override
    public void onItemClicked(int itemPosition, Object dataObject){
        if (DEBUG) {
            Log.d("excited", "onItemClicked itemPosition:" + itemPosition);
            CardEntity card=mAdapter.getItem(itemPosition);
            String excited=card.url;
            Log.d("excited", "clicked url :" + excited);
        }
    }
}
