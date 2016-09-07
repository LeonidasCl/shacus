package com.example.pc.shacus.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.pc.shacus.R;

/**
 * A simple {@link Fragment} subclass.
 */
//Author:LQ
//time:8.30
//设置->关于我们界面
public class SetAboutFragment extends Fragment implements View.OnClickListener{

    private ImageButton btn_back;
    public SetAboutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_set_about, container, false);
        //初始化组件
        btn_back= (ImageButton) view.findViewById(R.id.btn_back);

        btn_back.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                getFragmentManager().popBackStack();
                break;
        }
    }
}
