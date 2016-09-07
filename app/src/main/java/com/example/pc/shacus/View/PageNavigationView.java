package com.example.pc.shacus.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * 导航按钮
 */
public class PageNavigationView extends TouchMoveView {

	public PageNavigationView(Context context) {

		super(context);
	}

	public PageNavigationView(Context context, AttributeSet attrs) {

		super(context, attrs);
	}

	public PageNavigationView(Context context, AttributeSet attrs,
			int defStyleAttr) {

		super(context, attrs, defStyleAttr);
	}

	public synchronized void onShowAnimation(float step) {

		if(isShowFinish()) {
			return;
		}
		updateMarginTop(-getShowMoveStep(step));
	}

	public synchronized void onHideAnimation(float step) {

		if(isHideFinish()) {
			return;
		}
		updateMarginTop(getHideMoveStep(step));
	}

	@Override
	public synchronized boolean isShowFinish() {

		return getMarginTop() <= mShowStopMarginTop ? true : false;
	}

	@Override
	public synchronized boolean isHideFinish() {
		return getMarginTop() >= mHideStopMarginTop ? true : false;
	}

	public void init(View view){

	}

}
