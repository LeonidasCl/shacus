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
        TextView location;
        TextView time;
        TextView setTime;
        TextView describe;
        TextView price;

        public ViewHolder(View view) {
            userIamgeSrc=(ImageView)view.findViewById(R.id.huodong_avatar);
            mainPicture=(ImageView)view.findViewById(R.id.huodong_mainimg);
            praiseNum=(TextView)view.findViewById(R.id.huodong_praise_num);
            joinNum=(TextView)view.findViewById(R.id.huodong_join_num);
            userName=(TextView)view.findViewById(R.id.huodong_username);
            location=(TextView)view.findViewById(R.id.huodong_location);
            time=(TextView)view.findViewById(R.id.huodong_time);
            setTime=(TextView)view.findViewById(R.id.huodong_settime);
            describe=(TextView)view.findViewById(R.id.huodong_describe);
            price=(TextView)view.findViewById(R.id.huodong_price);

        }

        public void setValues(HuoDongItemModel item) {
            Resources res=activity.getResources();
            Drawable usrimg=res.getDrawable(R.drawable.personal_default_photo);
            userIamgeSrc.setImageDrawable(usrimg);
            Drawable mainpic=res.getDrawable(R.drawable.huodong_loading);
            mainPicture.setImageDrawable(mainpic);
            praiseNum.setText(String.valueOf(item.getPraiseNum()));
            joinNum.setText(String.valueOf(item.getJoinNum()));
            userName.setText(item.getUsrName());
            location.setText(item.getLocation());
            time.setText(item.getStarttime().toString() + " - " + item.getEndtime().toString());
            setTime.setText(item.getSetTime().toString());
            describe.setText(item.getDescribe());
            price.setText(String.valueOf(item.getPrice()));
        }
    }

}
