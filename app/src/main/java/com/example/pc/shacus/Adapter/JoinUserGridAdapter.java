package com.example.pc.shacus.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.pc.shacus.Activity.OtherUserActivity;
import com.example.pc.shacus.Activity.OtherUserDisplayActivity;
import com.example.pc.shacus.Data.Model.UserModel;
import com.example.pc.shacus.R;

import java.util.List;

/**
 * Created by 李嘉文 on 2016/9/2.
 */
public class JoinUserGridAdapter extends BaseAdapter {

    int count;
    Context context;
    private List<UserModel> usrlist;
    //private boolean hasMore;

    public JoinUserGridAdapter(Context context, List<UserModel> list,boolean hasmore) {

        this.context = context;

        usrlist=list;
       /* hasMore=hasmore;
        if (hasmore)
        {
            UserModel user=new UserModel();
            user.setId("more");
            usrlist.add(user);
        }*/
        this.count = list.size();
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public UserModel getItem(int position) {
        return usrlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (null == convertView) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.grid_user_avatar, null);
            holder.ibv=(ImageView)convertView.findViewById(R.id.imgbtn_user_avatar);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //holder.ibv.setImageDrawable(context.getResources().getDrawable(R.drawable.user_image));

        Glide.with(context)
                .load(usrlist.get(position).getHeadImage()).centerCrop()
//                .placeholder(R.drawable.user_image)
                .error(R.drawable.avatarholder)
                .into(holder.ibv);
        holder.ibv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(context, OtherUserDisplayActivity.class);
                in.putExtra("id", usrlist.get(position).getId());
                context.startActivity(in);
            }
        });

        return convertView;
    }


    class ViewHolder {
        ImageView ibv;
    }

}
