package com.example.pc.shacus.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.pc.shacus.Activity.CreateYuePaiActivity;
import com.example.pc.shacus.R;
import com.example.pc.shacus.View.MoveHideView;
import com.example.pc.shacus.View.PageHeadView;

/**
 * 活动列表界面(二级)
 */
public class YuePaiFragmentC extends Fragment {

    private Activity yuepai;
    private View navibar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        yuepai=this.getActivity();
        View view = inflater.inflate(R.layout.fragment_yue_pai_c, container, false);
        /*MoveHideView moveHideView = (MoveHideView) view.findViewById(R.id.ucindexview);
        PageHeadView pageHeadView = moveHideView.getChildAt();*/

//        Button toPhotograph=(Button)view.findViewById(R.id.button_yaopai);
//        toPhotograph.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(yuepai, CreateYuePaiActivity.class));
//            }
//        });
//
//        Button toModel=(Button)view.findViewById(R.id.button_beipai);
//        /*toModel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(yuepai, ModelActivity.class));
//            }
//        });*/
//
//        Button toActivity=(Button)view.findViewById(R.id.button_activity);
        /*toActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(yuepai, SetHuodongActivity.class));
            }
        });*/

        navibar=yuepai.findViewById(R.id.fragment_list);
        navibar.setVisibility(View.GONE);
        
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        navibar.setVisibility(View.VISIBLE);
    }

}