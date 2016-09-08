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
import com.example.pc.shacus.Data.Cache.ACache;
import com.example.pc.shacus.Data.Model.CoursesModel;
import com.example.pc.shacus.Data.Model.LoginDataModel;
import com.example.pc.shacus.Data.Model.UserModel;
import com.example.pc.shacus.Network.NetRequest;
import com.example.pc.shacus.Network.NetworkCallbackInterface;
import com.example.pc.shacus.R;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 启凡 on 2016/9/5.
 */


public class CourseListAdapter extends RecyclerView.Adapter<CourseListAdapter.RecyclerHolderView> implements  NetworkCallbackInterface.NetRequestIterface{


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
    public void onBindViewHolder(RecyclerHolderView holder, int position) {
        Glide.with(context)
                .load(courseModelList.get(position).getImage()).fitCenter()
                .placeholder(R.drawable.holder).dontAnimate().dontTransform()
                .error(R.drawable.holder)
                .into(holder.imageView);
        holder.title.setText(courseModelList.get(position).getTitle());
        holder.read.setText(Integer.toString(courseModelList.get(position).getReadNum()));
        //在分类和已完成教程中可以收藏和取消收藏
        if (courseModelList.get(position).getKind()==1||courseModelList.get(position).getKind()==3) {
            if (courseModelList.get(position).getCollet() == 1) {
                holder.collectItem.setImageResource(R.drawable.button_pop_down);
            } else {
                holder.collectItem.setImageResource(R.drawable.button_pop_down);
            }

        }

        //在课程表中标注已完成和未完成
        if (courseModelList.get(position).getKind()==2) {
            if (courseModelList.get(position).getSee() == 1) {
                holder.seeNum.setText("已看完");
                holder.seeNum.setTextColor(Color.BLUE);
            } else {
                holder.seeNum.setText("未完成");
                holder.seeNum.setTextColor(Color.RED);
            }
        }
        List list1 = new ArrayList();
        list1.add(1);
        list1.add(position);
        holder.collectItem.setTag(list1);
        holder.collectItem.setOnClickListener((View.OnClickListener) context);
        List list2 = new ArrayList();
        list2.add(2);
        list2.add(position);
        holder.relativeLayout.setTag(list2);
        holder.relativeLayout.setOnClickListener((View.OnClickListener) context);



    }


    @Override
    public int getItemCount() {
        return courseModelList.size();
    }

    @Override
    public void requestFinish(String result, String requestUrl) throws JSONException {

    }

    @Override
    public void exception(IOException e, String requestUrl) {

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
