package com.example.pc.shacus.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.pc.shacus.R;

/**
 * A simple {@link Fragment} subclass.
 */
//Author:李前
//time:8.30
public class SetAdviceFragment extends Fragment implements View.OnClickListener{

    private EditText connectWay,content;
    public SetAdviceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_set_advice, container, false);
        //初始化
        ImageButton btn_back = (ImageButton) view.findViewById(R.id.btn_back);
        connectWay= (EditText) view.findViewById(R.id.advice_connectType);
        content= (EditText) view.findViewById(R.id.advice_content);
        TextView sendMessage = (TextView) view.findViewById(R.id.btn_sendMessage);

        //添加监听

        btn_back.setOnClickListener(this);
        sendMessage.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                getFragmentManager().popBackStack();
                break;
            case R.id.btn_sendMessage:
                fsendMessage();
                break;
        }
    }

    //点击发送后的响应事件
    private void fsendMessage() {

    }

    public EditText getContent() {
        return content;
    }
}
