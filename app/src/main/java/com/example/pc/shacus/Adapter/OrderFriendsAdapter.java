package com.example.pc.shacus.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.pc.shacus.Activity.WebviewActivity;
import com.example.pc.shacus.Data.Model.OrderFriendsModel;
import com.example.pc.shacus.Data.Model.PhotographerModel;
import com.example.pc.shacus.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2017/4/12.
 */

public class OrderFriendsAdapter extends BaseAdapter {
    private List<OrderFriendsModel> orderfriendsList;
    private Activity activity;
    private LayoutInflater layoutInflater;
    private ViewHolder viewHolder;


    public OrderFriendsAdapter(Activity activity, List<OrderFriendsModel> list){
        this.orderfriendsList = list;
        this.activity = activity;
    }

    public void add(List<OrderFriendsModel> persons){
        this.orderfriendsList.addAll(persons);
    }
    public void refresh(List<OrderFriendsModel> persons) {
        this.orderfriendsList = persons;
    }

    @Override
    public int getCount() {
        return orderfriendsList.size();
    }

    public OrderFriendsModel getItem(int i) {
        return orderfriendsList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView ==null){
            layoutInflater = LayoutInflater.from(activity);
            convertView = layoutInflater.inflate(R.layout.item_orderfriends_layout, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else
            viewHolder = (ViewHolder) convertView.getTag();

        OrderFriendsModel item = getItem(position);
        viewHolder.setValues(item);

        return convertView;
    }
    private class ViewHolder{
        TextView OFTitle;
        TextView OFContent;
        ImageView Pic_1;
        ImageView Pic_2;
        ImageView Pic_3;
        LinearLayout singleItem;
        String OFurl;
        String OFid;


        public ViewHolder(View view){
            OFTitle =(TextView)view.findViewById(R.id.orderfriends_title);
            OFContent =(TextView)view.findViewById(R.id.orderfriends_content);
            Pic_1=(ImageView)view.findViewById(R.id.orderfriends_img_1);
            Pic_2=(ImageView)view.findViewById(R.id.orderfriends_img_2);
            Pic_3=(ImageView)view.findViewById(R.id.orderfriends_img_3);
            singleItem =  (LinearLayout) view.findViewById(R.id.orderfriends_item);
        }

        public void setValues(final OrderFriendsModel item){
            Resources res=activity.getResources();

            OFTitle.setText(item.getCompanionTitle());
            OFContent.setText(item.getCompanionContent());
            OFurl = item.getCompanionUrl();
            OFid = item.getCompanionId();

            ArrayList<String> picList = item.getCompanionPic();

            singleItem.setOnClickListener(new LinearLayout.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(activity,WebviewActivity.class);
                    intent.putExtra("Url",OFurl);
                    activity.startActivity(intent);
//                    Intent intent= new Intent();
//                    intent.setAction("android.intent.action.VIEW");
//                    Uri content_url = Uri.parse(OFurl);
//                    intent.setData(content_url);
//                    try {
//                        activity.startActivity(intent);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
                }
            });

            if(!picList.isEmpty()) {
                Glide.with(activity)
                        .load(picList.get(0))
                        .placeholder(R.drawable.holder)
                        .centerCrop()
                        .dontAnimate()
                        .error(R.drawable.loading_error)
                        .into(Pic_1);
                Glide.with(activity)
                        .load(picList.get(1))
                        .placeholder(R.drawable.holder)
                        .centerCrop()
                        .dontAnimate()
                        .error(R.drawable.loading_error)
                        .into(Pic_2);
                Glide.with(activity)
                        .load(picList.get(2))
                        .placeholder(R.drawable.holder)
                        .centerCrop()
                        .dontAnimate()
                        .error(R.drawable.loading_error)
                        .into(Pic_3);
            }
        }
    }
}
