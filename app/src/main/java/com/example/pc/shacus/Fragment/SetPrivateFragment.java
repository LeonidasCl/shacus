package com.example.pc.shacus.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import com.example.pc.shacus.R;

/**
 * A simple {@link Fragment} subclass.
 */
//Author:李前
//time:8.30
public class SetPrivateFragment extends Fragment implements View.OnClickListener{

    private Button btn_back;
    private CheckBox showArticle,showConcern,showFan;
    public SetPrivateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_set_private, container, false);
        //初始化
        btn_back= (Button) view.findViewById(R.id.btn_back);
        showArticle= (CheckBox) view.findViewById(R.id.btn_likeArticle);
        showConcern= (CheckBox) view.findViewById(R.id.btn_myConcern);
        showFan= (CheckBox) view.findViewById(R.id.btn_myFan);
        //监听
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

    public CheckBox getShowArticle() {
        return showArticle;
    }

    public CheckBox getShowConcern() {
        return showConcern;
    }

    public CheckBox getShowFan() {
        return showFan;
    }
}
