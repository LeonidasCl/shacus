package com.example.pc.shacus.Adapter;


import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.pc.shacus.Activity.FollowActivity;
import com.example.pc.shacus.Data.Model.UserModel;
import com.example.pc.shacus.R;

import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by 崔颖华 on 2016/8/29.
 *
 * 关注 我的粉丝 列表适配器
 */
public class FollowItemAdapter extends BaseAdapter {

    private List<UserModel> followlist;
    private Activity activity;

    String type = null;

    public FollowItemAdapter(FollowActivity activity, List<UserModel> list) {
        this.followlist = list;
        this.activity = activity;
        type = activity.getType();
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
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        ViewHolder viewHolder;
        if(view == null){
            viewHolder = new ViewHolder();
            //点击我的关注
            if(type.equals("following")){
                view = LayoutInflater.from(activity).inflate(R.layout.item_following_layout, viewGroup, false);
                viewHolder.usersignatureText = (TextView) view.findViewById(R.id.following_user_signature);
                viewHolder.userNameText = (TextView) view.findViewById(R.id.following_user_name);
                viewHolder.userImageSrc = (ImageButton) view.findViewById(R.id.following_user_image);
            }else if(type.equals("follower")){
                view = LayoutInflater.from(activity).inflate(R.layout.item_follower_layout, viewGroup, false);
                viewHolder.userNameText = (TextView) view.findViewById(R.id.follower_user_name);
                viewHolder.userImageSrc = (ImageButton) view.findViewById(R.id.follower_user_image);
            }

            viewHolder.userNameText = (TextView) view.findViewById(R.id.following_user_name);
            viewHolder.userImageSrc = (ImageButton) view.findViewById(R.id.following_user_image);

            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }

        UserModel userModel = followlist.get(position);
        if(type.equals("following"))
            viewHolder.usersignatureText.setText(userModel.getSign());
        viewHolder.userNameText.setText(userModel.getNickName());
        //获取用户头像
        //viewHolder.userImageSrc.setImageResource(userModel.get);

        return view;
    }

    private class ViewHolder{
        /*关注已关注监听事件*/
        ImageButton userImageSrc;
        TextView userNameText;
        TextView usersignatureText;
        ImageButton follow; //已关注或关注

/*        public ViewHolder(View view){
            if(type.equals("following")){
                userImageSrc = (ImageButton) view.findViewById(R.id.following_user_image);
                APTitle = (TextView) view.findViewById(R.id.following_user_name);
                usersignatureText = (TextView) view.findViewById(R.id.following_user_signature);
                follow = (ImageButton) view.findViewById(R.id.followedbtn);
            }else if(type.equals("follower")){
                userImageSrc = (ImageButton) view.findViewById(R.id.follower_user_image);
                APTitle = (TextView) view.findViewById(R.id.follower_user_name);
                follow = (ImageButton) view.findViewById(R.id.followingbtn);
            }
        }

        public void setValues(UserModel item){
            Resources res = activity.getResources();
            Drawable usrimg=res.getDrawable(R.drawable.user_image);
            userImageSrc.setImageDrawable(usrimg);

            APTitle.setText(item.getNickName());
            if (type.equals("following"))
                usersignatureText.setText(item.getNickName());
        }*/

    }
}
