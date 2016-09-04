package com.example.pc.shacus.Adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.pc.shacus.Data.Model.HuoDongItemModel;
import com.example.pc.shacus.R;

import java.util.List;

/**
 * licl 2016.7.18
 *
 */
public class HuodongItemAdapter extends BaseAdapter{

    private List<HuoDongItemModel> rankList;
    private Context activity;
    private LayoutInflater layoutInflater;
    private ViewHolder viewHolder;

    public HuodongItemAdapter(Context activity, List<HuoDongItemModel> list) {
        this.rankList = list;
        this.activity = activity;
    }

    public void add(List<HuoDongItemModel> persons){
        this.rankList.addAll(persons);
    }
    public void refresh(List<HuoDongItemModel> persons) {
        this.rankList = persons;
    }

    @Override
    public int getCount() {
         int size=rankList.size();
        return size;
    }

    @Override
    public HuoDongItemModel getItem(int i) {
        return rankList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if(view ==null){
            layoutInflater = LayoutInflater.from(activity);
            view = layoutInflater.inflate(R.layout.item_huodong_layout, viewGroup, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }
        else
            viewHolder = (ViewHolder) view.getTag();

        HuoDongItemModel item = getItem(i);

        viewHolder.setValues(item);

        return view;
    }


    private class ViewHolder {
        ImageView userIamgeSrc;
        ImageView mainPicture;
        TextView praiseNum;
        TextView joinNum;
        TextView userName;
        TextView setTime;
        TextView describe;

        public ViewHolder(View view) {
            userIamgeSrc=(ImageView)view.findViewById(R.id.huodong_avatar);
            mainPicture=(ImageView)view.findViewById(R.id.huodong_mainimg);
            praiseNum=(TextView)view.findViewById(R.id.huodong_praise_num);
            joinNum=(TextView)view.findViewById(R.id.huodong_join_num);
            userName=(TextView)view.findViewById(R.id.huodong_username);
            setTime=(TextView)view.findViewById(R.id.huodong_settime);
            describe=(TextView)view.findViewById(R.id.huodong_describe);

        }

        public void setValues(HuoDongItemModel item) {
            Glide.with(activity)
                    .load(item.getHuodongMainpic()).centerCrop()
                    .placeholder(R.drawable.holder)
                    .error(R.drawable.p1)
                    .into(mainPicture);
            Resources res=activity.getResources();
            Drawable usrimg=res.getDrawable(R.drawable.personal_default_photo);
            userIamgeSrc.setImageDrawable(usrimg);
            Drawable mainpic=res.getDrawable(R.drawable.huodong_loading);
            praiseNum.setText(String.valueOf(item.getPraiseNum()));
            joinNum.setText(String.valueOf(item.getJoinNum()));
            userName.setText(item.getUsrName());
            setTime.setText(item.getSetTime().toString());
            describe.setText(item.getDescribe());
        }
    }

}
