package com.example.pc.shacus.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerHolderView holder, int position) {
        Glide.with(context)
                .load(courseModelList.get(position).getImage())
                .placeholder(R.drawable.holder)
                .error(R.drawable.holder)
                .into(holder.imageView);
        holder.title.setText(courseModelList.get(position).getTitle());
        holder.read.setText(Integer.toString(courseModelList.get(position).getReadNum()));

    }



    @Override
    public int getItemCount() {
        return courseModelList.size();
    }

    public static class RecyclerHolderView extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView title;
        TextView read;



        public RecyclerHolderView(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.ItemImage);
            title = (TextView) itemView.findViewById(R.id.ItemName);
            read = (TextView) itemView.findViewById(R.id.ItemViewNum);
        }
    }
}
