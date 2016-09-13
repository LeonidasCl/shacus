package com.example.pc.shacus.Adapter;

import android.content.Context;
import android.graphics.PointF;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
/*
* 上传图片列表pager
* 李嘉文
* */
public class UploadViewPager extends ViewPager {

	PointF downPoint = new PointF();
	public static boolean ifIntercept = true;
	OnSingleTouchListener onSingleTouchListener;

	public UploadViewPager(Context context) {
		super(context);
	}

	public UploadViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public boolean onTouch(MotionEvent event) {

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			downPoint.x = event.getX();
			downPoint.y = event.getY();

			getParent().requestDisallowInterceptTouchEvent(true);

			break;
		case MotionEvent.ACTION_MOVE:

			getParent().requestDisallowInterceptTouchEvent(true);

			break;
		case MotionEvent.ACTION_UP:
			// 在up时判断是否按下和松手的坐标为一个点
			if (PointF.length(event.getX() - downPoint.x, event.getY()
					- downPoint.y) < (float) 5.0) {
				onSingleTouchListener.onSingleTouch(getCurrentItem());

			}
			break;
		}
		return super.onTouchEvent(event);
	}

	// 创建点击事件接口 
	 
	public interface OnSingleTouchListener {
		public void onSingleTouch(int position);
	}

	public void setOnSingleTouchListener(
			OnSingleTouchListener onSingleTouchListener) {
		this.onSingleTouchListener = onSingleTouchListener;
	}

}
