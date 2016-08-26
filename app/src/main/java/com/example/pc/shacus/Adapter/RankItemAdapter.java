package com.example.pc.shacus.Adapter;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


import com.example.pc.shacus.Data.Model.RankItemModel;
import com.example.pc.shacus.R;

import java.util.List;

/**
 * licl 2016.7.18
 * 这个适配器写法大量参照ekansh在2015.10.20的一个开源例子
 */
public class RankItemAdapter extends BaseAdapter{

    private List<RankItemModel> rankList;
    private Activity activity;
    private LayoutInflater layoutInflater;
    private ViewHolder viewHolder;

    public RankItemAdapter(Activity activity, List<RankItemModel> list) {
        this.rankList = list;
        this.activity = activity;
    }

    public void add(List<RankItemModel> persons){
        this.rankList.addAll(persons);
    }
    public void refresh(List<RankItemModel> persons) {
        this.rankList = persons;
    }

    @Override
    public int getCount() {
        return rankList.size();
    }

    @Override
    public RankItemModel getItem(int i) {
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

        RankItemModel item = getItem(i);
        viewHolder.setValues(item);

        return view;
    }


    private class ViewHolder {
        ImageButton userIamgeSrc;
        Button rank;
        TextView userNameText;
        TextView userAddressText;
        ImageButton mainPicture;
        ImageButton rankPraise;
        ImageButton rankComment;
        TextView praiseNum;
        TextView commentNum;

        public ViewHolder(View view) {
            //name = (TextView) view.findViewById(R.id.text_view_name);
            userIamgeSrc=(ImageButton)view.findViewById(R.id.user_image);
            rank=(Button)view.findViewById(R.id.rank_image);
            userNameText=(TextView)view.findViewById(R.id.userInfo);
            userAddressText=(TextView)view.findViewById(R.id.userAddress);
            mainPicture=(ImageButton)view.findViewById(R.id.main_picture);
            rankPraise=(ImageButton)view.findViewById(R.id.rank_praise);
            rankComment=(ImageButton)view.findViewById(R.id.rank_comment);
            praiseNum=(TextView)view.findViewById(R.id.rank_praise_num);
            commentNum=(TextView)view.findViewById(R.id.rank_comment_num);
        }

        public void setValues(RankItemModel item) {
           // name.setText(item.getName());
            Resources res=activity.getResources();
            Drawable usrimg=res.getDrawable(R.drawable.user_image);
            userIamgeSrc.setImageDrawable(usrimg);
            int rank=item.getRank();
            if(rank<=3){
                if(rank==1)
                    this.rank.setBackgroundResource(R.drawable.rank1);
                if(rank==2)
                    this.rank.setBackgroundResource(R.drawable.rank2);
                if(rank==3)
                    this.rank.setBackgroundResource(R.drawable.rank3);
            }else {
                this.rank.setBackgroundResource(R.drawable.transparent);
                this.rank.setText(String.valueOf(rank));
            }
            userNameText.setText(item.getUserNameText());
            userAddressText.setText(item.getUserAddressText());
            Drawable mainimg=res.getDrawable(R.drawable.main_picture1);
            mainPicture.setImageDrawable(mainimg);
            Drawable praise=res.getDrawable(R.drawable.praise);
            rankPraise.setImageDrawable(praise);
            Drawable comment=res.getDrawable(R.drawable.comment_image);
            rankPraise.setImageDrawable(comment);
            praiseNum.setText(String.valueOf(item.getFavorNum()));
            commentNum.setText(String.valueOf(item.getCommentNum()));

        }
    }

}
