package com.example.pc.shacus.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.pc.shacus.APP;
import com.example.pc.shacus.Data.Cache.ACache;
import com.example.pc.shacus.Data.Model.LoginDataModel;
import com.example.pc.shacus.Data.Model.UserModel;
import com.example.pc.shacus.R;

/**
 * 李嘉文
 * 导航布局
 * 修改 李前
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

		CircleImageView imgview=(CircleImageView)view.findViewById(R.id.huodong_header_avatar);
		TextView tvname=(TextView)view.findViewById(R.id.huodong_header_name);
		TextView tvsign=(TextView)view.findViewById(R.id.huodong_header_sign);
		ACache cache=ACache.get(APP.context);
		LoginDataModel model=(LoginDataModel)cache.getAsObject("loginModel");
		UserModel user=model.getUserModel();
		Glide.with(APP.context)
				.load(user.getHeadImage()).centerCrop()
//				.placeholder(R.drawable.user_image)
				.error(R.drawable.personal_default_photo)
				.into(imgview);
		tvname.setText(user.getNickName());
		tvsign.setText("苟利国家生死以，岂因祸福避趋之");

	}

}
