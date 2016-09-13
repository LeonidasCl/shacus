package com.example.pc.shacus.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * 活动页面头部View
 * 李嘉文
 */
public class PageHeadView extends MoveView {

	public PageHeadView(Context context) {

		super(context);
	}

	public PageHeadView(Context context, AttributeSet attrs) {

		super(context, attrs);
	}

	public PageHeadView(Context context, AttributeSet attrs, int defStyleAttr) {

		super(context, attrs, defStyleAttr);
	}

	public synchronized void onShowAnimation(float step) {

		if(isShowFinish()) {
			return;
		}
		updateMarginTop(getShowMoveStep(step));
	}

	public synchronized void onHideAnimation(float step) {

		if(isHideFinish()) {
			return;
		}
		updateMarginTop(-getHideMoveStep(step));
	}

	public void init(View view){

	}
}
