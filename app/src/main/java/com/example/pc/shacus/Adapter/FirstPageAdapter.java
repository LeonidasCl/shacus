package com.example.pc.shacus.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.pc.shacus.Activity.OtherUserActivity;
import com.example.pc.shacus.Data.Cache.ACache;
import com.example.pc.shacus.Data.Model.ItemModel;
import com.example.pc.shacus.Data.Model.LoginDataModel;
import com.example.pc.shacus.Data.Model.UserModel;
import com.example.pc.shacus.Fragment.HomeFragment;
import com.example.pc.shacus.Network.NetRequest;
import com.example.pc.shacus.Network.StatusCode;
import com.example.pc.shacus.R;
import com.example.pc.shacus.Util.CommonUrl;
import com.example.pc.shacus.View.CircleImageView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cuicui on 2016/9/8.
 */
public class FirstPageAdapter extends BaseAdapter {
    List<ItemModel> itemList;
    ViewHolder viewHolder;
    HomeFragment context;
    ACache aCache;
    NetRequest netRequest;

    public FirstPageAdapter(List<ItemModel> list, HomeFragment homeFragment ){
        this.context = homeFragment;
        itemList = list;
        aCache = ACache.get(context.getActivity());
        netRequest = new NetRequest(context,context.getActivity());
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
    public View getView(final int position, View view, ViewGroup parent) {
        if(view == null){
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(context.getActivity()).inflate(R.layout.item_firstpage,parent,false);
            viewHolder.bigImage = (ImageView) view.findViewById(R.id.first_bigimage);
            viewHolder.userImage = (CircleImageView) view.findViewById(R.id.firstpage_user_image);
            viewHolder.title = (TextView) view.findViewById(R.id.firstpage_user_title);
            viewHolder.liken = (TextView) view.findViewById(R.id.first_likenum);
            viewHolder.commentn = (TextView) view.findViewById(R.id.first_commentnum);
            viewHolder.time = (TextView) view.findViewById(R.id.first_time);
            viewHolder.detial = (TextView) view.findViewById(R.id.first_detail);
            viewHolder.likebtn = (ImageView) view.findViewById(R.id.first_likebtn);

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

        viewHolder.userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemModel next = itemList.get(position);
                Intent intent = new Intent(context.getActivity(),OtherUserActivity.class);
                intent.putExtra("id", next.getUserId());
                Log.d("sssssssssssssssssssss",String.valueOf(next.getUserId()));
                context.startActivity(intent);
            }
        });
        viewHolder.likebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginDataModel loginModel = (LoginDataModel)aCache.getAsObject("loginModel");
                UserModel user = loginModel.getUserModel();
                String uid = user.getId();
                String authkey = user.getAuth_key();
                Map map = new HashMap();
                map.put("uid",uid);
                map.put("authkey",authkey);
                map.put("trendid",itemList.get(position).getId());
                Log.d("aaaaaaaaaaaa",String.valueOf(itemList.get(position).getId()));
                if(itemList.get(position).getIndex() == 1){
                    //已收藏
                    int num = Integer.valueOf((String) viewHolder.liken.getText()) - 1;
                    viewHolder.liken.setText(String.valueOf(num));
                    map.put("type", StatusCode.REQUEST_CANCEL_FAVORDONGTAI);
                    netRequest.httpRequest(map, CommonUrl.aboutFavorDongTai);
                }else if (itemList.get(position).getIndex() == 0){
                    //未收藏
                    int num = Integer.valueOf((String) viewHolder.liken.getText()) + 1;
                    viewHolder.liken.setText(String.valueOf(num));
                    map.put("type",StatusCode.REQUEST_ADD_FAVORDONGTAI);
                    netRequest.httpRequest(map,CommonUrl.aboutFavorDongTai);
                }
            }
        });

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
        ImageView likebtn;

    }
}
