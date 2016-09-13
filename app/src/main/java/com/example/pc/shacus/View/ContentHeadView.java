package com.example.pc.shacus.View;

import android.content.Context;
import android.util.AttributeSet;

/**
 * 内容视图头部
 * 李嘉文
 */
public class ContentHeadView extends MoveView {

	public ContentHeadView(Context context) {
		super(context);
	}

	public ContentHeadView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ContentHeadView(Context context, AttributeSet attrs, int defStyleAttr) {
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
}
