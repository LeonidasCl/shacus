package com.example.pc.shacus.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pc.shacus.R;

/**
 * A simple {@link Fragment} subclass.
 */
//Author:LQ
//Time:8.29
public class SetBaseFragment extends Fragment implements View.OnClickListener{

    private View userManage;
    private View privateManage;
    private View general;
    private View about;
    private View advice;
    private View cleanCache;
    private View logout;

    public SetBaseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_set_base, container, false);
        initData(view);

        return view;
    }

    private void initData(View view) {
        //初始化各部件
        userManage=view.findViewById(R.id.layout_UserManage);
        privateManage=view.findViewById(R.id.layout_private);
        general=view.findViewById(R.id.layout_general);
        about=view.findViewById(R.id.layout_about);
        advice=view.findViewById(R.id.layout_advice);
        cleanCache=view.findViewById(R.id.layout_clearCache);
        logout=view.findViewById(R.id.layout_Logout);
        //设置点击事件
        userManage.setOnClickListener(SetBaseFragment.this);
        privateManage.setOnClickListener(SetBaseFragment.this);
        general.setOnClickListener(SetBaseFragment.this);
        about.setOnClickListener(SetBaseFragment.this);
        advice.setOnClickListener(SetBaseFragment.this);
        cleanCache.setOnClickListener(SetBaseFragment.this);
        logout.setOnClickListener(SetBaseFragment.this);
    }


    @Override
    public void onClick(View v) {
        //跳转至各个Fragment
        switch (v.getId()){
            case R.id.layout_UserManage:
                SetUserManageFragment userManageFragment=new SetUserManageFragment();
                FragmentManager fm=getFragmentManager();
                FragmentTransaction tx=fm.beginTransaction();
                tx.replace(R.id.frameLayout,userManageFragment,"UserManage");
                tx.addToBackStack(null);
                tx.commit();
                break;
            case R.id.layout_private:
                break;
            case R.id.layout_general:
                break;
            case R.id.layout_about:
                break;
            case R.id.layout_advice:
                break;
            case R.id.layout_clearCache:
                break;
            case R.id.layout_Logout:
                break;
        }
    }
}
