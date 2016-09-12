/*
 * The MIT License (MIT)
 * Copyright (c) [2015] [chiemy]
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.example.pc.shacus.View.Custom;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ListAdapter;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

/**
 * @author chiemy
 * 
 */
public class CardView extends FrameLayout {

	private static final int ITEM_SPACE = 30;
	private static final int DEF_MAX_VISIBLE = 10;

	private int mMaxVisible = DEF_MAX_VISIBLE;
	private int itemSpace = ITEM_SPACE;
	private float mTouchSlop;
	private ListAdapter mListAdapter;
	private int adapterPosition;//这个数值减一是当前顶部卡片标号
	private SparseArray<View> viewHolder = new SparseArray<View>();
    //private Map<Integer,SparseArray<View>> viewMap;
	private OnCardClickListener mListener;
	//private int topPosition;
	private Rect topRect;

	public interface OnCardClickListener {
		void onCardClick(View view, int position);
	}

	public CardView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public CardView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public CardView(Context context) {
		super(context);
		init();
	}

	private void init() {
		topRect = new Rect();
		ViewConfiguration con = ViewConfiguration.get(getContext());
		mTouchSlop = con.getScaledTouchSlop();
	}

	public void setMaxVisibleCount(int count) {
		mMaxVisible = count;
	}

	public int getMaxVisibleCount() {
		return mMaxVisible;
	}

	public void setItemSpace(int itemSpace) {
		this.itemSpace = itemSpace;
	}

	public int getItemSpace() {
		return itemSpace;
	}

	public ListAdapter getAdapter() {
		return mListAdapter;
	}

	public void setAdapter(ListAdapter adapter) {
		if (mListAdapter != null) {
			mListAdapter.unregisterDataSetObserver(mDataSetObserver);
		}
		adapterPosition = 0;
		mListAdapter = adapter;
		adapter.registerDataSetObserver(mDataSetObserver);
		removeAllViews();
		ensureFull();
	}

	public void setOnCardClickListener(OnCardClickListener listener) {
		mListener = listener;
	}

	private void ensureFull() {
		while (adapterPosition < mListAdapter.getCount()
				&& getChildCount() < mMaxVisible) {
			int index = adapterPosition % mMaxVisible;
			View convertView = viewHolder.get(index);
			final View view = mListAdapter.getView(adapterPosition, convertView, this);
			view.setOnClickListener(null);
			viewHolder.put(index, view);
            //viewMap.put(index, viewHolder);
			// 添加剩余的View时，始终处在最后
			index = Math.min(adapterPosition, mMaxVisible - 1);
            if (index!=0)
			ViewHelper.setScaleX(view, ((mMaxVisible - index - 1) / (float) mMaxVisible) * 0.2f + 0.8f);
			//ViewHelper.setScaleY(view, ((mMaxVisible - index - 1) / (float) mMaxVisible) * 0.2f + 0.7f);
			int topMargin = (mMaxVisible - index - 1) * 4;
			ViewHelper.setTranslationY(view, topMargin);
			ViewHelper.setAlpha(view, adapterPosition == 0 ? 1 : 0.5f);

			LayoutParams params = (LayoutParams) view.getLayoutParams();
			if (params == null) {
				params = new LayoutParams(LayoutParams.MATCH_PARENT,
						LayoutParams.WRAP_CONTENT);
			}
			addViewInLayout(view, 0, params);

			adapterPosition += 1;
		}

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec)+40;//临时矩形修正

		int childCount = getChildCount();
		int maxHeight = 0;
		int maxWidth = 0;
		for (int i = 0; i < childCount; i++) {
			View child = getChildAt(i);
			this.measureChild(child, widthMeasureSpec, heightMeasureSpec);
			int height = child.getMeasuredHeight();
			int width = child.getMeasuredWidth();
			if (height > maxHeight) {
				maxHeight = height;
			}
			if (width > maxWidth) {
				maxWidth = width;
			}
		}
		int desireWidth = widthSize;
		int desireHeight = heightSize;
		if (widthMode == MeasureSpec.AT_MOST) {
			desireWidth = maxWidth + getPaddingLeft() + getPaddingRight();
		}
		if (heightMode == MeasureSpec.AT_MOST) {
			desireHeight = maxHeight + (mMaxVisible - 1) * itemSpace + getPaddingTop() + getPaddingBottom();
		}
		setMeasuredDimension(desireWidth, desireHeight);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		View topView = getChildAt(getChildCount() - 1);
		if (topView != null) {
			topView.setOnClickListener(listener);
		}
	}

	float downX, downY;

	@Override
	public boolean onTouchEvent(MotionEvent event) {

        float currentY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float distance = currentY - downY;
                if (distance > mTouchSlop*2||distance<=-mTouchSlop) {//垂直滑动距离大于限定就拦截在本层，会导致不打开fragment，执行卡片切换
                    if (distance>0)
                        return goDown();
                    else
                        return goUp();
                }
                break;
        }

		return super.onTouchEvent(event);
	}

    /**
     * 上移视图
     */
    public boolean goUp() {
        final View topView = getChildAt(getChildCount() - 1);
        final int index=viewHolder.indexOfValue(topView);

        topView.setEnabled(false);

        ViewPropertyAnimator anim = ViewPropertyAnimator
                .animate(topView).scaleX(((mMaxVisible - index - 1) / (float) mMaxVisible) * 0.2f + 0.8f)
                .alpha(0.5f)
                .setListener(null).setDuration(100);

        //防止重复监听
        final int[] flag = {0};
        anim.setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (flag[0]==1)return;
                topView.setEnabled(true);
                View view=viewHolder.get((index-1)<0?9:index-1);
                bringToTop(view);
                flag[0] =1;
            }
        });
        return true;
    }

	public boolean scrollCards(int order) throws InterruptedException {
        final View topView = getChildAt(getChildCount() - 1);
        final int index=viewHolder.indexOfValue(topView);
        order-=3;
        topView.setEnabled(false);

        ViewPropertyAnimator anim = ViewPropertyAnimator
                .animate(topView).scaleX(((mMaxVisible - index - 1) / (float) mMaxVisible) * 0.2f + 0.8f)
                .alpha(adapterPosition == 0 ? 1 : 0.5f)
                .setListener(null).setDuration(100);

        final int front=order;
        final int[] flag = {0};
        anim.setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (flag[0]==1)return;
                super.onAnimationEnd(animation);
                View view=viewHolder.get(front);
                view.setEnabled(true);
                bringToTop(view);
                flag[0]=1;
            }
        });


        return true;
    }

	/**
	 * 下移视图
	 */
	public boolean goDown() {
		final View topView = getChildAt(getChildCount() - 1);
        final int index=viewHolder.indexOfValue(topView);

		if(!topView.isEnabled()){
			return false;
		}
		topRect = getHitRect(topRect, topView);
		// 如果按下的位置不在顶部视图上，则不移动
		if (!topRect.contains((int) downX, (int) downY)) {
			return false;
		}

		topView.setEnabled(false);

        ViewPropertyAnimator anim = ViewPropertyAnimator
                .animate(topView).scaleX(((mMaxVisible - index - 1) / (float) mMaxVisible) * 0.2f + 0.8f)
                .alpha(adapterPosition == 0 ? 1 : 0.5f)
                .setListener(null).setDuration(100);

        //防止重复监听
        final int[] flag = {0};
		anim.setListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
                if (flag[0]==1)return;
				topView.setEnabled(true);
                View view=viewHolder.get((index+1)==10?0:index+1);
                bringToTop(view);
                flag[0] =1;
			}
		});
		return true;
	}

	/**
	 * 将下一个视图移到前边
	 * 
	 * @param view
	 */
	private void bringToTop(final View view) {
		//topPosition++;
        ViewPropertyAnimator.animate(view)
                .scaleY(1).scaleX(1).setDuration(100).alpha(1)
                .setInterpolator(new AccelerateInterpolator());
        view.bringToFront();
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		float currentY = ev.getY();
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			downX = ev.getX();
			downY = ev.getY();
			break;
        case MotionEvent.ACTION_MOVE:
            float distance = currentY - downY;
            if (distance > mTouchSlop*2||distance<=-mTouchSlop) {//垂直滑动距离大于限定就拦截在本层，会导致不打开fragment，执行卡片切换
                return true;
            }
            break;
        }
		return false;
	}

	public static Rect getHitRect(Rect rect, View child) {
		rect.left = child.getLeft();
		rect.right = child.getRight();
		rect.top = (int) (child.getTop() + ViewHelper.getTranslationY(child));
		rect.bottom = (int) (child.getBottom() + ViewHelper.getTranslationY(child));
		return rect;
	}

	private final DataSetObserver mDataSetObserver = new DataSetObserver() {
		@Override
		public void onChanged() {
			super.onChanged();
		}

		@Override
		public void onInvalidated() {
			super.onInvalidated();
		}
	};

    //未被拦截时子控件可获得触摸事件并执行监听
	private OnClickListener listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (mListener != null) {
				mListener.onCardClick(v,0);
			}
		}
	};
}
