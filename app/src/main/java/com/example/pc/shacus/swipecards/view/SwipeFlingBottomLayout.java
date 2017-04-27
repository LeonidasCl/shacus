package com.example.pc.shacus.swipecards.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.example.pc.shacus.R;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * SwipeFling底部区域
 *
 * @zc
 */
public class SwipeFlingBottomLayout extends LinearLayout {

    public final static int ANIMATION_DURATION = 50;

    /*@InjectView(R.id.comeback)
    BottomSwitchView mComeBackView;*/

    @InjectView(R.id.self_chat)
    //BottomSwitchView mSelfChatView;
    ImageView mSelfChatView;

    @InjectView(R.id.like)
    BottomSwitchView mLikeView;

    @InjectView(R.id.unlike)
    BottomSwitchView mUnlikeView;

    private OnBottomItemClickListener mListener;

    public SwipeFlingBottomLayout(Context context) {
        super(context);
    }

    public SwipeFlingBottomLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.inject(this);
        //mComeBackView.setEnabled(false);
    }

    /*public void show(int delay) {
        show(mLikeView, delay);
        show(mUnlikeView, delay);
        show(mComeBackView, delay);
        show(mSuperLikeView, delay);
    }*/

    /*public void hide() {
        int delay = 0;
        hide(mLikeView, delay);
        hide(mUnlikeView, delay);
        hide(mComeBackView, delay);
        hide(mSuperLikeView, delay);
    }*/

    /*private void show(View view, int delay) {
        view.animate()
                .setDuration(ANIMATION_DURATION)
                .setStartDelay(delay)
                .alpha(1);
    }*/

    /*private void hide(View view, int delay) {
        view.animate()
                .setDuration(ANIMATION_DURATION)
                .setStartDelay(delay)
                .alpha(0);
    }*/

    /*@OnClick(R.id.comeback)
    public void onComeBackClick() {
        if (mListener != null) {
            mListener.onComeBackClick();
        }
    }*/

    @OnClick(R.id.self_chat)
    public void onSelfChatClick() {
        if (mListener != null) {
            mListener.onSelfChatClick();
        }
    }

    @OnClick(R.id.like)
    public void onLikeClick() {
        if (mListener != null) {
            mListener.onLikeClick();
        }
    }

    @OnClick(R.id.unlike)
    public void onUnLikeClick() {
        if (mListener != null) {
            mListener.onUnLikeClick();
        }
    }

    public BottomSwitchView getLikeView() {
        return mLikeView;
    }

    public BottomSwitchView getUnLikeView() {
        return mUnlikeView;
    }

    public ImageView getSuperLikeView() {
        return mSelfChatView;
    }

   /* public void setEnableComeback(boolean isEnable) {
        mComeBackView.setEnabled(isEnable);
    }*/

    public void setEnableSuperLike(boolean isEnable) {
        mSelfChatView.setEnabled(isEnable);
    }

    /*public boolean isEnableComeback() {
        return mComeBackView.isEnabled();
    }*/

    public boolean isEnableSuperLike() {
        return mSelfChatView.isEnabled();
    }

    /*public void setComebackClickable(boolean clickable) {
        mComeBackView.setClickable(clickable);
    }*/

    public void setOnBottomItemClickListener(OnBottomItemClickListener listener) {
        this.mListener = listener;
    }

    public interface OnBottomItemClickListener {
        //public void onComeBackClick();

        public void onSelfChatClick();

        public void onLikeClick();

        public void onUnLikeClick();
    }

}
