package com.example.pc.shacus.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.example.pc.shacus.Data.Model.CoursesModel;
import com.example.pc.shacus.R;

import java.util.List;

/**
 * Created by 启凡 on 2016/9/5.
 */


public class CourseListAdapter extends RecyclerView.Adapter<CourseListAdapter.RecyclerHolderView>{


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
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //进入网页xxxxxxxx
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerHolderView holder, int position) {
        Glide.with(context)
                .load(courseModelList.get(position).getImage()).fitCenter()
                .placeholder(R.drawable.holder).dontAnimate().dontTransform()
                .error(R.drawable.holder)
                .into(holder.imageView);
        holder.title.setText(courseModelList.get(position).getTitle());
        holder.read.setText(Integer.toString(courseModelList.get(position).getReadNum()));
        if (courseModelList.get(position).getKind()==1||courseModelList.get(position).getKind()==3) {
            if (courseModelList.get(position).getCollet() == 1) {
                holder.collectItem.setImageResource(R.drawable.button_pop_down);
            } else {
                holder.collectItem.setImageResource(R.drawable.button_pop_down);
            }
        }
        if (courseModelList.get(position).getKind()==2) {
            if (courseModelList.get(position).getSee() == 1) {
                holder.seeNum.setText("已看完");
                holder.seeNum.setTextColor(Color.BLUE);
            } else {
                holder.seeNum.setText("未完成");
                holder.seeNum.setTextColor(Color.RED);
            }
        }

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

        RelativeLayout relativeLayout;
        TextView seeNum;


        public RecyclerHolderView(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.ItemVideo);
            title = (TextView) itemView.findViewById(R.id.ItemName);
            read = (TextView) itemView.findViewById(R.id.ItemViewNum);
            collectItem=(ImageView)itemView.findViewById(R.id.CollectItem);
            seeNum=(TextView)itemView.findViewById(R.id.ItemSeeNum);
            relativeLayout=(RelativeLayout)itemView.findViewById(R.id.relativeLayout4);
            String[] stringcolor={"#ECF5FF","#FFECF5","#ECF5FF","#ECECFF","#FFFCEC","#FFE6D9","#F2E6E6","#F3F3FA","#D1E9E9","#F5FFE8"};
            int index=(int)(Math.random()*stringcolor.length);
            relativeLayout.setBackgroundColor(Color.parseColor(stringcolor[index]));
        }
    }
}
