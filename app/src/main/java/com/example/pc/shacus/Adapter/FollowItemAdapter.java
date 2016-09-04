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
import com.example.pc.shacus.Data.Model.UserModel;
import com.example.pc.shacus.Network.NetRequest;
import com.example.pc.shacus.Network.StatusCode;
import com.example.pc.shacus.R;
import com.example.pc.shacus.Util.CommonUrl;

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
    private Button button2;

    String type = null;

    public FollowItemAdapter(FollowActivity activity, List<UserModel> list) {
        this.followlist = list;
        this.activity = activity;
        type = activity.getType();
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

        ViewHolder viewHolder;
        if(view == null){
            viewHolder = new ViewHolder();
            //点击我的关注
            if(type.equals("following")){
                view = LayoutInflater.from(activity).inflate(R.layout.item_following_layout, viewGroup, false);
                viewHolder.usersignatureText = (TextView) view.findViewById(R.id.following_user_signature);
                viewHolder.userNameText = (TextView) view.findViewById(R.id.following_user_name);
                viewHolder.userImageSrc = (ImageButton) view.findViewById(R.id.following_user_image);
                //viewHolder.follow = (Button) view.findViewById(R.id.followedbtn);
                button1 = (Button) view.findViewById(R.id.followedbtn);
                button1.setOnClickListener(new FollowListener(position));
                button1.setTag(StatusCode.REQUEST_CANCEL_FOLLOWING);
            }else if(type.equals("follower")){
                view = LayoutInflater.from(activity).inflate(R.layout.item_follower_layout, viewGroup, false);
                viewHolder.userNameText = (TextView) view.findViewById(R.id.follower_user_name);
                viewHolder.userImageSrc = (ImageButton) view.findViewById(R.id.follower_user_image);
                viewHolder.follow = (Button) view.findViewById(R.id.followingbtn);
                button2 = (Button) view.findViewById(R.id.followingbtn);
                button2.setOnClickListener(new FollowListener(position));
                button2.setTag(StatusCode.REQUEST_FOLLOW_USER);
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
        /*关注已关注监听事件*/
        ImageButton userImageSrc;
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
                {
                    ACache aCache = ACache.get(activity);
                    JSONObject jsonObject = aCache.getAsJSONObject("loginModel");
                    try {

                        JSONObject content = jsonObject.getJSONObject("userModel");
                        String userid = content.getString("id");
                        String authkey = content.getString("auth_key");
                        String followerid = activity.getData().get(position).getId();
                        Map map = new HashMap<>();
                        map.put("uid",userid);
                        map.put("authkey", authkey);
                        map.put("followerid", followerid);
                        map.put("type", StatusCode.REQUEST_CANCEL_FOLLOWING);
                        netRequest.httpRequest(map, CommonUrl.getFollowInfo);
                        v.setTag(StatusCode.REQUEST_FOLLOW_USER);
                        followlist.remove(position);
                        notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case StatusCode.REQUEST_FOLLOW_USER:
                {
                    ACache aCache = ACache.get(activity);
                    JSONObject jsonObject = aCache.getAsJSONObject("loginModel");
                    try {
                        JSONObject content = jsonObject.getJSONObject("userModel");
                        String userid = content.getString("id");
                        String authkey = content.getString("auth_key");
                        String followerid = activity.getData().get(position).getId();
                        Map map = new HashMap<>();
                        map.put("uid", userid);
                        map.put("authkey", authkey);
                        map.put("followerid", followerid);
                        map.put("type", StatusCode.REQUEST_FOLLOW_USER);
                        netRequest.httpRequest(map, CommonUrl.getFollowInfo);
                        v.getResources();
                        v.setTag(StatusCode.REQUEST_CANCEL_FOLLOWING);
                        notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                }

            }
        }
    }
}
