package com.example.pc.shacus.Fragment;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pc.shacus.R;

import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 */
//Author:LQ
//Time:8.29
    //change1:完成剩余部分
    //Worker:LQ
    //Time:8.30
public class SetUserManageFragment extends Fragment implements View.OnClickListener{


    private Button btn_back;
    private TextView userName,phoneNumber, weiBo,weiChat,QQ;
    private View btn_userName,btn_phoneNumber,btn_weiBo,btn_weiChat,btn_QQ;

    public SetUserManageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_set_user_manage, container, false);
        //初始化
        btn_back= (Button) view.findViewById(R.id.btn_back);
        btn_userName=view.findViewById(R.id.layout_UserName);
        btn_phoneNumber=view.findViewById(R.id.layout_phoneNumber);
        btn_weiBo=view.findViewById(R.id.layout_weibo);
        btn_weiChat=view.findViewById(R.id.layout_weiChat);
        btn_QQ=view.findViewById(R.id.layout_QQ);

        userName= (TextView) view.findViewById(R.id.textData_UserName);
        phoneNumber= (TextView) view.findViewById(R.id.textData_phoneNumber);
        weiBo = (TextView) view.findViewById(R.id.textData_weiBo);
        weiChat= (TextView) view.findViewById(R.id.textData_weiChat);
        QQ= (TextView) view.findViewById(R.id.textData_QQ);

        //监听
        btn_back.setOnClickListener(this);
        btn_userName.setOnClickListener(this);
        btn_phoneNumber.setOnClickListener(this);
        btn_weiBo.setOnClickListener(this);
        btn_weiChat.setOnClickListener(this);
        btn_QQ.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                getFragmentManager().popBackStack();
                break;
            case R.id.layout_UserName://点击修改用户昵称
                final EditText UN=new EditText(this.getActivity());
                new AlertDialog.Builder(this.getActivity())
                        .setTitle("请输入")
                        .setIcon(R.drawable.ic_launcher)
                        .setView(UN)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {//监听
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SetUserManageFragment.this.getUserName().setText(UN.getText());
                                Toast.makeText(SetUserManageFragment.this.getActivity(),"修改成功",Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
                break;
            case R.id.layout_phoneNumber:
                final EditText UN2=new EditText(this.getActivity());
                new AlertDialog.Builder(this.getActivity())
                        .setTitle("请输入")
                        .setIcon(R.drawable.ic_launcher)
                        .setView(UN2)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SetUserManageFragment.this.getPhoneNumber().setText(UN2.getText());
                                Toast.makeText(SetUserManageFragment.this.getActivity(),"修改成功",Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
                break;
            case R.id.layout_weibo:
                break;
            case R.id.layout_weiChat:
                break;
            case R.id.layout_QQ:
                break;
        }
    }

    public TextView getUserName() {
        return userName;
    }

    public TextView getPhoneNumber() {
        return phoneNumber;
    }

    public TextView getWeiBo() {
        return weiBo;
    }

    public TextView getWeiChat() {
        return weiChat;
    }

    public TextView getQQ() {
        return QQ;
    }

}
