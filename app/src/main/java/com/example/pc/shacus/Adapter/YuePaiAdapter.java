package com.example.pc.shacus.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.example.pc.shacus.Activity.MainActivity;
import com.example.pc.shacus.Activity.YuePaiDetailActivity;
import com.example.pc.shacus.Data.Model.PhotographerModel;
import com.example.pc.shacus.Fragment.YuePaiFragment;
import com.example.pc.shacus.R;

import java.util.List;

/**
 * licl 2016.9.4
 */
public class YuePaiAdapter extends BaseAdapter{

    private List<PhotographerModel> rankList;
    private MainActivity activity;
    private LayoutInflater layoutInflater;
    private ViewHolder viewHolder;

    public YuePaiAdapter(Activity activity, List<PhotographerModel> list) {
        this.rankList = list;
        this.activity = (MainActivity)activity;
    }

    public void add(List<PhotographerModel> persons){
        this.rankList.addAll(persons);
    }
    public void refresh(List<PhotographerModel> persons) {
        this.rankList = persons;
    }

    @Override
    public int getCount() {
        return rankList.size();
    }

    @Override
    public PhotographerModel getItem(int i) {
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
            view = layoutInflater.inflate(R.layout.item_rank_layout, viewGroup, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }
        else
            viewHolder = (ViewHolder) view.getTag();

        PhotographerModel item = getItem(i);
        viewHolder.setValues(item);



        return view;
    }


    private class ViewHolder {
        ImageView userIamgeSrc;
        TextView APTitle;
        TextView APstartT;
        ImageView mainPicture;
        ImageButton APlike;
        ImageButton APjoin;
        TextView praiseNum;
        TextView commentNum;

        public ViewHolder(View view){
            //name = (TextView) view.findViewById(R.id.text_view_name);
            userIamgeSrc=(ImageView)view.findViewById(R.id.user_image);
            APTitle =(TextView)view.findViewById(R.id.APtitle);
            APstartT =(TextView)view.findViewById(R.id.APstartT);
            mainPicture=(ImageView)view.findViewById(R.id.APimgurl);
            APlike =(ImageButton)view.findViewById(R.id.APlikeBtn);
            APjoin =(ImageButton)view.findViewById(R.id.APjoinBtn);
            praiseNum=(TextView)view.findViewById(R.id.APlikeN);
            commentNum=(TextView)view.findViewById(R.id.APregistN);
        }

        public void setValues(final PhotographerModel item){
            //name.setText(item.getName());
            Resources res=activity.getResources();
            //Drawable usrimg=res.getDrawable(R.drawable.user_image);
            //userIamgeSrc.setImageDrawable(usrimg);
            String userimg=item.getAPimgurl();
            Glide.with(activity)
                    .load(userimg)
                    .placeholder(R.drawable.user_image)
                    .error(R.drawable.loading_error)
                    .into(userIamgeSrc);
            APTitle.setText(item.getAPtitle());
            APstartT.setText(item.getAPstartT());
            String mainimg=item.getAPimgurl();
            Glide.with(activity)
                    .load(mainimg)
                    .centerCrop()
                    .placeholder(R.drawable.holder)
                    .error(R.drawable.p1)
                    .into(mainPicture);
            //mainPicture.setImageDrawable(mainimg);
            praiseNum.setText(String.valueOf(item.getAPlikeN()));
            commentNum.setText(String.valueOf(item.getAPregistN()));
            APlike.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    //发起一个点赞请求
                }
            });
            APjoin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity, YuePaiDetailActivity.class);
                    intent.putExtra("detail",item.getAPid());
                    intent.putExtra("type","yuepai");
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    activity.startActivity(intent);
                }
            });

        }
    }

}
