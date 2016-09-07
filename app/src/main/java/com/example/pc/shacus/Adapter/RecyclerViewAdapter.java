package com.example.pc.shacus.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.pc.shacus.Data.Model.ItemModel;
import com.example.pc.shacus.Network.NetRequest;
import com.example.pc.shacus.R;

import java.util.List;

/**
 * Created by cuicui on 2016/9/3.
 * 瀑布流布局适配器
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerHolderView>{


    private List<ItemModel> itemModelList;
    private Context context;
    private NetRequest netRequest;

    public RecyclerViewAdapter(List<ItemModel> list, Activity activity){
        itemModelList = list;
        this.context = activity;
    }

    @Override
    public RecyclerHolderView onCreateViewHolder(ViewGroup viewGroup, int i){

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_orders_layout,null);
        RecyclerHolderView viewHolder = new RecyclerHolderView(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerHolderView holder, int position) {
        Glide.with(context)
                .load(itemModelList.get(position).getImage()).fitCenter()
                .placeholder(R.drawable.holder).dontAnimate().dontTransform()
                .error(R.drawable.holder)
                .into(holder.imageView);
        holder.title.setText(itemModelList.get(position).getTitle());
        holder.time.setText(itemModelList.get(position).getStartTime());
        holder.like.setText(Integer.toString(itemModelList.get(position).getLikeNum()));
        holder.regist.setText(Integer.toString(itemModelList.get(position).getRegistNum()));
        holder.likebtn.setTag(position);
        holder.likebtn.setOnClickListener((View.OnClickListener) context);

    }


    @Override
    public int getItemCount() {
        return itemModelList.size();
    }


    public static class RecyclerHolderView extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView title;
        TextView time;
        TextView like;
        TextView regist;
        ImageButton likebtn;
        ImageButton registbtn;


        public RecyclerHolderView(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.item_img);
            title = (TextView) itemView.findViewById(R.id.item_title);
            time = (TextView) itemView.findViewById(R.id.item_time);
            like = (TextView) itemView.findViewById(R.id.item_like);
            regist = (TextView) itemView.findViewById(R.id.item_regist);
            likebtn = (ImageButton) itemView.findViewById(R.id.item_like_btn);
            registbtn = (ImageButton) itemView.findViewById(R.id.item_regist_btn);

        }
    }
}
