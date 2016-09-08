package com.example.pc.shacus.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.pc.shacus.APP;
import com.example.pc.shacus.Data.Model.UserModel;
import com.example.pc.shacus.R;
import com.example.pc.shacus.Util.CommonUtils;
import com.example.pc.shacus.View.CircleImageView;

import java.util.List;

/**
 * Created by cuicui on 2016/9/7.
 */
public class UserDetailAdapter extends BaseAdapter{

    List<UserModel> userModelList;
    ViewHolder viewHolder;
    Context context;

    public UserDetailAdapter(Context c,List<UserModel> list){
        context = c;
        userModelList = list;
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
            view = LayoutInflater.from(context).inflate(R.layout.item_user_baoming,parent,false);
            viewHolder.userImageSrc = (CircleImageView) view.findViewById(R.id.baoming_user_image);
            viewHolder.userNameText = (TextView) view.findViewById(R.id.baoming_user_name);
            viewHolder.usersignatureText = (TextView) view.findViewById(R.id.baoming_user_signature);
            viewHolder.baoming = (Button) view.findViewById(R.id.baomingbtn);

            Glide.with(context)
                    .load(userModelList.get(position).getHeadImage()).fitCenter()
                    .placeholder(R.drawable.holder)
                    .into(viewHolder.userImageSrc);
            viewHolder.userNameText.setText(userModelList.get(position).getNickName());
            viewHolder.usersignatureText.setText(userModelList.get(position).getSign());

            viewHolder.baoming.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(userModelList.get(position).getIndex()){
                        CommonUtils.getUtilInstance().showToast(APP.context, "已取消关注");

                    }
                }
            });

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
