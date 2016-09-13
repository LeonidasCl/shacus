package com.example.pc.shacus.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
/*
* 活动列表视图
* 李嘉文
* */
public class TouchMoveView extends MoveView {

	private TouchMoveListener mTouchMoveListener;
	
	public TouchMoveView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public TouchMoveView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public TouchMoveView(Context context) {
		super(context);
	}

	@Override
	public boolean isTouchEventEnable() {
		return true;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		if(isTouchEventEnable()) {
			if(mTouchMoveListener != null) {
				mTouchMoveListener.onTouchMoveEvent(event);
			}
		}
		return isTouchEventEnable() || super.onTouchEvent(event);
	}

    /*
    * 李嘉文 2016.8.27
    * 留个接口这样写，是因为将来会有一些布局只需要用到此类的一部分方法
    * 那个时候他们就不必继承这个类了，但可以通过实现该接口来调用这个类的部分方法
    * */
	public interface TouchMoveListener {
		int onTouchMoveEvent(MotionEvent event);
	}


	public void setTouchMoveListener(TouchMoveListener touchMoveListener) {

		this.mTouchMoveListener = touchMoveListener;
	}
}
