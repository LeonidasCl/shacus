package com.example.pc.shacus.Adapter;


import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pc.shacus.Activity.FollowActivity;
import com.example.pc.shacus.Data.Cache.ACache;
import com.example.pc.shacus.Data.Model.LoginDataModel;
import com.example.pc.shacus.Data.Model.UserModel;
import com.example.pc.shacus.Network.NetRequest;
import com.example.pc.shacus.Network.StatusCode;
import com.example.pc.shacus.R;
import com.example.pc.shacus.Util.CommonUrl;
import com.example.pc.shacus.View.CircleImageView;

import junit.framework.Test;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by 崔颖华 on 2016/8/29.
 *
 * 关注 我的粉丝 列表适配器
 */
public class FollowItemAdapter extends BaseAdapter{

    private List<UserModel> followlist;
    private FollowActivity activity;
    private NetRequest netRequest;
    private Button button1;
//    private Button button2;
    ViewHolder viewHolder;

    String type = null;
    String index = null;

    public FollowItemAdapter(FollowActivity activity, List<UserModel> list) {
        this.followlist = list;
        this.activity = activity;
        type = activity.getType();
        index = activity.getIndex();
        netRequest = new NetRequest(activity,activity);
    }

    @Override
    public int getCount() {
        return followlist.size();
    }

    @Override
    public UserModel getItem(int position) {
        return followlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {

        if(view == null){
            viewHolder = new ViewHolder();
            //点击我的关注
            if(type.equals("following")){
                view = LayoutInflater.from(activity).inflate(R.layout.item_following_layout, viewGroup, false);
                viewHolder.usersignatureText = (TextView) view.findViewById(R.id.following_user_signature);
                viewHolder.userNameText = (TextView) view.findViewById(R.id.following_user_name);
                viewHolder.userImageSrc = (CircleImageView) view.findViewById(R.id.following_user_image);
                //viewHolder.follow = (Button) view.findViewById(R.id.followedbtn);
                button1 = (Button) view.findViewById(R.id.followedbtn);
                if (index.equals("myself")){
                    button1.setOnClickListener(new FollowListener(position));
                    button1.setTag(StatusCode.REQUEST_CANCEL_FOLLOWING);
                }else if (index.equals("other")){
                    button1.setVisibility(View.INVISIBLE);
                }
            }else if(type.equals("follower")){
                view = LayoutInflater.from(activity).inflate(R.layout.item_follower_layout, viewGroup, false);
                viewHolder.userNameText = (TextView) view.findViewById(R.id.follower_user_name);
                viewHolder.userImageSrc = (CircleImageView) view.findViewById(R.id.follower_user_image);
                viewHolder.follow = (Button) view.findViewById(R.id.followingbtn);
                if (index.equals("myself")){
                    viewHolder.follow.setOnClickListener(new FollowListener(position));
                    if(followlist.get(position).getIndex()) {
                        viewHolder.follow.setTag(StatusCode.REQUEST_CANCEL_FOLLOWING);
                    }
                    else {
                        viewHolder.follow.setText("关注");
                        viewHolder.follow.setTag(StatusCode.REQUEST_FOLLOW_USER);
                    }
                }else if (index.equals("other")){
                    viewHolder.follow.setVisibility(View.INVISIBLE);
                }
            }

            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }

        UserModel userModel = followlist.get(position);
        if(type.equals("following"))
            viewHolder.usersignatureText.setText(userModel.getSign());
        viewHolder.userNameText.setText(userModel.getNickName());
        //获取用户头像
        Glide.with(activity)
                .load(followlist.get(position).getHeadImage())
                .placeholder(R.drawable.holder)
                .error(R.drawable.holder)
                .into(viewHolder.userImageSrc);

        if (type.equals("follower")){
            if (userModel.getIndex())
                viewHolder.follow.setText("已关注");
            else viewHolder.follow.setText("关 注");
        }

        return view;
    }

    private class ViewHolder{
        CircleImageView userImageSrc;
        TextView userNameText;
        TextView usersignatureText;
        Button follow; //已关注或关注

    }

    class FollowListener implements View.OnClickListener{

        int position;
        public FollowListener(int p){
            position = p;
        }

        @Override
        public void onClick(View v) {
            int tag = (int) v.getTag();
            switch (tag){
                case StatusCode.REQUEST_CANCEL_FOLLOWING:
                {   ACache aCache = ACache.get(activity);
                    LoginDataModel loginDataModel = (LoginDataModel) aCache.getAsObject("loginModel");
                    UserModel content = null;
                    Map map = new HashMap<>();

                    content = loginDataModel.getUserModel();
                    String userId = content.getId();
                    String authkey = content.getAuth_key();
                    String followerid = activity.getData().get(position).getId();
                    map.put("uid", userId);
                    map.put("authkey", authkey);
                    map.put("followerid", followerid);
                    map.put("type", StatusCode.REQUEST_CANCEL_FOLLOWING);
                    if(type.equals("following")){
                        followlist.remove(position);
                        notifyDataSetChanged();
                    }
                    else{
                        followlist.clear();
                        v.setTag(StatusCode.REQUEST_FOLLOW_USER);
                    }
                    netRequest.httpRequest(map, CommonUrl.getFollowInfo);
                    break;
                }
                case StatusCode.REQUEST_FOLLOW_USER:
                {
                    ACache aCache = ACache.get(activity);
                    LoginDataModel loginDataModel = (LoginDataModel) aCache.getAsObject("loginModel");
                    UserModel content = null;
                    content = loginDataModel.getUserModel();
                    String userid = content.getId();
                    String authkey = content.getAuth_key();
                    String followerid = activity.getData().get(position).getId();
                    Map map = new HashMap<>();
                    map.put("uid", userid);
                    map.put("authkey", authkey);
                    map.put("followerid", followerid);
                    map.put("type", StatusCode.REQUEST_FOLLOW_USER);
                    followlist.clear();
                    v.setTag(StatusCode.REQUEST_CANCEL_FOLLOWING);
                    netRequest.httpRequest(map, CommonUrl.getFollowInfo);
                    break;
                }

            }
        }
    }
}
