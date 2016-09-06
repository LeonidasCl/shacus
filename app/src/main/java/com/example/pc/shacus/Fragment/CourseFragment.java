package com.example.pc.shacus.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.example.pc.shacus.R;
//LQ
//9.5
public class CourseFragment extends Fragment {
    //推荐列表
    private LinearLayout recommendLine;
    //推荐图
    private ImageView recommendImage;
    private ImageButton myCourseImage;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //9.5缺少布局文件
        //9.6已更正
        View view=inflater.inflate(R.layout.fragment_course, container, false);
        recommendLine= (LinearLayout) view.findViewById(R.id.recommendCourse_Linear);

        initView();

        return view;
    }

    private void initView() {
        LayoutInflater mInflater=LayoutInflater.from(this.getActivity());
        for (int i = 0; i < 10; i++) {
            View view=mInflater.inflate(R.layout.item_fragment_course,null);
            recommendImage= (ImageView) view.findViewById(R.id.recommendCourse_image);
            Glide.with(this.getActivity())
                    .load("http://img15.3lian.com/2015/f1/41/d/8"+i+".jpg").centerCrop()
                    .placeholder(R.drawable.holder)
                    .error(R.drawable.loading_error)
                    .into(recommendImage);
            recommendLine.addView(view);
        }
    }
}
