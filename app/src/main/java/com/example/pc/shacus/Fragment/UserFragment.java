package com.example.pc.shacus.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pc.shacus.Network.NetworkCallbackInterface;
import com.example.pc.shacus.R;

import java.io.IOException;
//TODO：把这一行换成作者名
//用户个人界面（一级）
public class UserFragment extends android.support.v4.app.Fragment implements  NetworkCallbackInterface.NetRequestIterface{


    @Override@Deprecated
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user, container, false);
        SearchView search=(SearchView)view.findViewById(R.id.test_search_tag);
        search.setIconifiedByDefault(false);
        search.setSubmitButtonEnabled(false);
        search.setQueryHint("查找或输入标签");
        return view;
    }

    @Override
    public void requestFinish(String result, String requestUrl) {
        //在这里接收所有网络请求
    }

    @Override
    public void exception(IOException e, String requestUrl) {
        //处理网络请求的异常信息，一般用不到
    }
}
