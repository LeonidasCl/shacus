package com.example.pc.shacus.Fragment;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.example.pc.shacus.APP;
import com.example.pc.shacus.R;
import com.example.pc.shacus.Util.CommonUtils;
import com.example.pc.shacus.Util.UploadPhotoUtil;
import com.example.pc.shacus.View.Custom.PhotoView;


public class ImageDisplayFragment extends Fragment implements OnClickListener{

	private PhotoView display_big_image;
	private LinearLayout display_image_fragment;
	public static boolean showNetImg=true;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		View view = inflater.inflate(R.layout.display_image_fragment, null);
		initView(view);
		return view;
	}

	public  ImageDisplayFragment create(String url,int position) {
		//在这里设置好要返回的Fragment
		ImageDisplayFragment imageDisplayFragment= new ImageDisplayFragment();
		//设置Bundle并放入图片地址
		Bundle bundle = new Bundle();  
		bundle.putString("url", url);
        imageDisplayFragment.setArguments(bundle); 
		return imageDisplayFragment;
	}

	private void initView(View view) {

		display_big_image = (PhotoView) view.findViewById(R.id.display_big_image);
		display_image_fragment= (LinearLayout) view.findViewById(R.id.display_image_fragment);
		display_image_fragment.setOnClickListener(this);
		//拿到放入的图片地址
		String imageUrl=getArguments().getString("url");

//		if(showNetImg){
//		CommonUtils.getUtilInstance().displayNetworkImage(imageUrl, display_big_image);
//		}else{
//			Bitmap bitmap= UploadPhotoUtil.getInstance().trasformToZoomBitmapAndLessMemory(imageUrl);
//			display_big_image.setImageDrawable(new BitmapDrawable(getActivity().getResources(),bitmap));
//		}
		Glide.with(APP.context).load(imageUrl).into(display_big_image);
	}

	@Override
	public void onClick(View view) {

		switch(view.getId()){
//		case R.id.display_image_fragment:
//			 CommunityFragment.display_big_image_layout.setVisibility(View.GONE);
//			CommunityFragment.isBigImageShow=false;
//			if(CommunityFragment.isBigImageShowFrom==0){
//			CommunityFragment.isIntoThemeContent=true;
//			}else{
//				CommunityFragment.isIntoAddTheme=true;
//			}
//			break;
		}
	}
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

}
