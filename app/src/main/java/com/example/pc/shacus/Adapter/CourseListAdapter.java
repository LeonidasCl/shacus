package com.example.pc.shacus.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.example.pc.shacus.APP;
import com.example.pc.shacus.Data.Cache.ACache;
import com.example.pc.shacus.Data.Model.CoursesModel;
import com.example.pc.shacus.Data.Model.LoginDataModel;
import com.example.pc.shacus.Data.Model.UserModel;
import com.example.pc.shacus.Network.NetRequest;
import com.example.pc.shacus.Network.NetworkCallbackInterface;

import org.json.JSONException;
import com.example.pc.shacus.R;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 启凡 on 2016/9/5.
 */


public class CourseListAdapter extends RecyclerView.Adapter<CourseListAdapter.RecyclerHolderView> {


    private List<CoursesModel> courseModelList;
    private Context context;



    public CourseListAdapter(List<CoursesModel> list, Activity context){
        courseModelList = list;
        this.context=context;
    }

    @Override
    public CourseListAdapter.RecyclerHolderView onCreateViewHolder(ViewGroup viewGroup, int i){

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_courses_activity_item,null);
        RecyclerHolderView viewHolder = new RecyclerHolderView(view);



        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerHolderView holder, int position) {
        Glide.with(context)
                .load(courseModelList.get(position).getImage()).fitCenter()
                .placeholder(R.drawable.holder).dontAnimate().dontTransform()
                .error(R.drawable.holder)
                .into(holder.imageView);
        holder.title.setText(courseModelList.get(position).getTitle());
        holder.read.setText(Integer.toString(courseModelList.get(position).getReadNum()));
        //在分类和列表教程中可以收藏和取消收藏
       if (courseModelList.get(position).getKind()==2||courseModelList.get(position).getKind()==3) {
            if (courseModelList.get(position).getCollet() == 1) {
                holder.collectItem.setVisibility(View.VISIBLE);
                holder.cancel.setVisibility(View.INVISIBLE);
//                holder.collectItem.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        holder.cancel.setVisibility(View.VISIBLE);
//                        holder.collectItem.setVisibility(View.INVISIBLE);
//                    }
//                });
            } else {
                holder.cancel.setVisibility(View.VISIBLE);
                holder.collectItem.setVisibility(View.INVISIBLE);
//                holder.cancel.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        holder.collectItem.setVisibility(View.VISIBLE);
//                        holder.cancel.setVisibility(View.INVISIBLE);
//                    }
//                });
            }

       }

        //在课程表中标注已完成和未完成
        if (courseModelList.get(position).getKind()==2) {
            if (courseModelList.get(position).getSee() == 1) {
                holder.seeNum.setText("已看完");
                holder.seeNum.setTextColor(Color.WHITE);
            } else {
                holder.seeNum.setText("未完成");
                holder.seeNum.setTextColor(Color.WHITE);
            }
        }
        List list1 = new ArrayList();
        list1.add(1);
        list1.add(position);
        list1.add(courseModelList.get(position).getItemid());
        list1.add(courseModelList.get(position).getCollet());
        list1.add(holder.collectItem);
        list1.add(holder.cancel);
        holder.collectItem.setTag(list1);
        holder.cancel.setTag(list1);
        holder.cancel.setOnClickListener((View.OnClickListener) context);
        holder.collectItem.setOnClickListener((View.OnClickListener) context);
        List list2 = new ArrayList();
        list2.add(2);
        list2.add(position);
        list2.add(courseModelList.get(position).getItemid());
        holder.relativeLayout.setTag(list2);
        holder.relativeLayout.setOnClickListener((View.OnClickListener) context);



    }


    @Override
    public int getItemCount() {
        return courseModelList.size();
    }


    public static class RecyclerHolderView extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView title;
        TextView read;
        ImageView collectItem;
        ImageView cancel;

        RelativeLayout relativeLayout;
        TextView seeNum;
        android.support.v7.widget.CardView cardView;


        public RecyclerHolderView(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.ItemVideo);
            title = (TextView) itemView.findViewById(R.id.ItemName);
            read = (TextView) itemView.findViewById(R.id.ItemViewNum);
            collectItem=(ImageView)itemView.findViewById(R.id.CollectItem);
            seeNum=(TextView)itemView.findViewById(R.id.ItemSeeNum);
            cancel = (ImageView) itemView.findViewById(R.id.cancelCollectItem);
            relativeLayout=(RelativeLayout)itemView.findViewById(R.id.relativeLayout4);
        }
    }
}
