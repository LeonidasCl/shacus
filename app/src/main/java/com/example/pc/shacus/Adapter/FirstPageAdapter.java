package com.example.pc.shacus.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.pc.shacus.Data.Model.ItemModel;
import com.example.pc.shacus.R;
import com.example.pc.shacus.View.CircleImageView;

import java.util.List;

/**
 * Created by cuicui on 2016/9/8.
 */
public class FirstPageAdapter extends BaseAdapter {
    List<ItemModel> itemList;
    ViewHolder viewHolder;
    Context context;

    public FirstPageAdapter(List<ItemModel> list, Context c ){
        this.context = c;
        itemList = list;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if(view == null){
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.item_firstpage,parent,false);
            viewHolder.bigImage = (ImageView) view.findViewById(R.id.first_bigimage);
            viewHolder.userImage = (CircleImageView) view.findViewById(R.id.firstpage_user_image);
            viewHolder.title = (TextView) view.findViewById(R.id.firstpage_user_title);
            viewHolder.liken = (TextView) view.findViewById(R.id.first_likenum);
            viewHolder.commentn = (TextView) view.findViewById(R.id.first_commentnum);
            viewHolder.time = (TextView) view.findViewById(R.id.first_time);
            viewHolder.detial = (TextView) view.findViewById(R.id.first_detail);

            Glide.with(context)
                    .load(itemList.get(position).getImage()).fitCenter()
                    .placeholder(R.drawable.test)
                    .into(viewHolder.bigImage);
            Glide.with(context)
                    .load(itemList.get(position).getUserImage()).fitCenter()
                    .placeholder(R.drawable.holder)
                    .into(viewHolder.userImage);
            viewHolder.detial.setText(itemList.get(position).getDetial());
            viewHolder.liken.setText(Integer.toString(itemList.get(position).getLikeNum()));
            viewHolder.commentn.setText(Integer.toString(itemList.get(position).getCommentNum()));
            viewHolder.time.setText(itemList.get(position).getStartTime());
            viewHolder.title.setText(itemList.get(position).getTitle());
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }
        return view;
    }

    private class ViewHolder{
        CircleImageView userImage;
        TextView title;
        ImageView bigImage;
        TextView time;
        TextView detial;
        TextView liken;
        TextView commentn;

    }
}
