package com.example.pc.shacus.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.pc.shacus.R;

/**
 * A simple {@link Fragment} subclass.
 */
//Author:李前
//time:8.30
public class SetGeneralFragment extends Fragment implements View.OnClickListener{

    private Button btn_back;
    private CheckBox autoDownload;
    private TextView language,fontSize;
    public SetGeneralFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_set_general, container, false);
        //初始化
        btn_back= (Button) view.findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);
        autoDownload= (CheckBox) view.findViewById(R.id.btn_autoDownload);
        language= (TextView) view.findViewById(R.id.textData_language);
        fontSize=(TextView)view.findViewById(R.id.textData_fontSize);


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

    public CheckBox getAutoDownload() {
        return autoDownload;
    }

    public TextView getLanguage() {
        return language;
    }

    public TextView getFontSize() {
        return fontSize;
    }
}
