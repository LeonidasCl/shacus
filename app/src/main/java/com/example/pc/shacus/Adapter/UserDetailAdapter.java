package com.example.pc.shacus.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.preference.DialogPreference;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.pc.shacus.APP;
import com.example.pc.shacus.Activity.SelectUserActivity;
import com.example.pc.shacus.Activity.YuePaiDetailActivity;
import com.example.pc.shacus.Data.Cache.ACache;
import com.example.pc.shacus.Data.Model.LoginDataModel;
import com.example.pc.shacus.Data.Model.UserModel;
import com.example.pc.shacus.Network.NetRequest;
import com.example.pc.shacus.Network.StatusCode;
import com.example.pc.shacus.R;
import com.example.pc.shacus.Util.CommonUrl;
import com.example.pc.shacus.Util.CommonUtils;
import com.example.pc.shacus.View.CircleImageView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cuicui on 2016/9/7.
 */
public class UserDetailAdapter extends BaseAdapter{

    List<UserModel> userModelList;
    ViewHolder viewHolder;
    SelectUserActivity selectUserActivity;
    NetRequest netRequest;
    Map map = new HashMap<>();

    public UserDetailAdapter(SelectUserActivity c,List<UserModel> list){
        selectUserActivity = c;
        userModelList = list;
        netRequest = new NetRequest(selectUserActivity,selectUserActivity);
    }

    @Override
    public int getCount() {
        return userModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return userModelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        if(view == null){
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(selectUserActivity).inflate(R.layout.item_user_baoming,parent,false);
            viewHolder.userImageSrc = (CircleImageView) view.findViewById(R.id.baoming_user_image);
            viewHolder.userNameText = (TextView) view.findViewById(R.id.baoming_user_name);
            viewHolder.usersignatureText = (TextView) view.findViewById(R.id.baoming_user_signature);
            viewHolder.baoming = (Button) view.findViewById(R.id.baomingbtn);

            Glide.with(selectUserActivity)
                    .load(userModelList.get(position).getHeadImage()).fitCenter()
                    .placeholder(R.drawable.holder)
                    .into(viewHolder.userImageSrc);
            viewHolder.userNameText.setText(userModelList.get(position).getNickName());
            viewHolder.usersignatureText.setText(userModelList.get(position).getSign());

            if(selectUserActivity.getType().equals("yuepai")){
                if(userModelList.get(position).getIndex()){
                    viewHolder.baoming.setText("已选择");
                }else
                    viewHolder.baoming.setText("选择");
                viewHolder.baoming.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(userModelList.get(position).getIndex()){
                            CommonUtils.getUtilInstance().showToast(APP.context, "已选择不能更改");
                        }  else {
                            new AlertDialog.Builder(selectUserActivity).setTitle("确定选择")
                                    .setMessage(userModelList.get(position).getNickName() +"\n为约拍对象吗？一旦设置，不可更改")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {//确定按钮响应事件
                                            ACache aCache = ACache.get(selectUserActivity);
                                            LoginDataModel loginDataModel = (LoginDataModel) aCache.getAsObject("loginModel");
                                            UserModel content = loginDataModel.getUserModel();
                                            String myid = content.getId();
                                            String authkey = content.getAuth_key();
                                            int ap = selectUserActivity.getId();
                                            map.put("apid",ap);
                                            map.put("chooseduid",userModelList.get(position).getId());
                                            map.put("authkey",authkey);
                                            map.put("uid",myid);
                                            map.put("type", StatusCode.REQUEST_SELECT_YUEPAIUSER);
                                            Log.d("aaaaaaaaaaa", map.toString());
                                            dialog.dismiss();
                                            viewHolder.baoming.setText("已选择");
                                            netRequest.httpRequest(map, CommonUrl.getOrdersInfo);
                                        }
                                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
                        }
                    }
                });


            }else if(selectUserActivity.getType().equals("huodong")){
                viewHolder.baoming.setVisibility(View.INVISIBLE);
            }

            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }

        return view;
    }

    private class ViewHolder{
        CircleImageView userImageSrc;
        TextView userNameText;
        TextView usersignatureText;
        Button baoming; //报名或已报名

    }
}
