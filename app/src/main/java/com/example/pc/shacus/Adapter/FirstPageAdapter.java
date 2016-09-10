package com.example.pc.shacus.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.pc.shacus.APP;
import com.example.pc.shacus.Activity.OtherUserActivity;
import com.example.pc.shacus.Data.Cache.ACache;
import com.example.pc.shacus.Data.Model.ItemModel;
import com.example.pc.shacus.Data.Model.LoginDataModel;
import com.example.pc.shacus.Data.Model.UserModel;
import com.example.pc.shacus.Fragment.HomeFragment;
import com.example.pc.shacus.Network.NetRequest;
import com.example.pc.shacus.Network.NetworkCallbackInterface;
import com.example.pc.shacus.Network.StatusCode;
import com.example.pc.shacus.R;
import com.example.pc.shacus.Util.CommonUrl;
import com.example.pc.shacus.Util.CommonUtils;
import com.example.pc.shacus.View.CircleImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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

    public FirstPageAdapter(List<ItemModel> list, HomeFragment homeFragment ){
        this.context = homeFragment;
        itemList = list;
        aCache = ACache.get(context.getActivity());
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
                context.startActivity(intent);
            }
        });

        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Log.d("aaaaaaaaaaass", String.valueOf(position));
                if (msg.what == StatusCode.REQUEST_ADD_FAVORDONGTAI_SUCCESS){
                    viewHolder.liken.setText(String.valueOf(itemList.get(position).getLikeNum()));
                    CommonUtils.getUtilInstance().showToast(APP.context, "收藏成功");
                }else if(msg.what == StatusCode.REQUEST_CANCEL_FAVORDONGTAI_SUCCESS){
                    viewHolder.liken.setText(String.valueOf(itemList.get(position).getLikeNum()));
                    CommonUtils.getUtilInstance().showToast(APP.context, "取消收藏");
                }
            }
        };

        viewHolder.likebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //发起一个收藏请求：收藏或取消收藏
                NetRequest netRequest = new NetRequest(new NetworkCallbackInterface.NetRequestIterface() {
                    @Override
                    public void requestFinish(String result, String requestUrl) throws JSONException {
                        if(requestUrl.equals(CommonUrl.aboutFavorDongTai)){
                            Log.d("aaaaaaaaaaaa", String.valueOf(position));
                            JSONObject object = new JSONObject(result);
                            int code  = Integer.valueOf(object.getString("code"));
                            switch (code){
                                case StatusCode.REQUEST_ADD_FAVORDONGTAI_SUCCESS:{
                                    itemList.get(position).setIndex(1);
                                    Message message = handler.obtainMessage();
                                    message.what = StatusCode.REQUEST_ADD_FAVORDONGTAI_SUCCESS;
                                    itemList.get(position).setLikeNum(Integer.valueOf(viewHolder.liken.getText().toString())+1);
                                    handler.sendMessage(message);
                                    break;
                                }
                                case StatusCode.REQUEST_CANCEL_FAVORDONGTAI_SUCCESS:{
                                    itemList.get(position).setIndex(0);
                                    Message message = handler.obtainMessage();
                                    message.what = StatusCode.REQUEST_CANCEL_FAVORDONGTAI_SUCCESS;
                                    itemList.get(position).setLikeNum(Integer.valueOf(viewHolder.liken.getText().toString())-1);
                                    handler.sendMessage(message);
                                    break;
                                }
                            }
                        }
                    }

                    @Override
                    public void exception(IOException e, String requestUrl) {

                    }
                },context.getActivity());

                LoginDataModel loginModel = (LoginDataModel)aCache.getAsObject("loginModel");
                UserModel user = loginModel.getUserModel();
                String uid = user.getId();
                String authkey = user.getAuth_key();
                Map map = new HashMap();
                map.put("uid",uid);
                map.put("authkey",authkey);
                map.put("trendid", itemList.get(position).getId());
                Log.d("aaaaaaaaaaaeeeee",String.valueOf(itemList.get(position).getIndex()));
                if(itemList.get(position).getIndex() == 1){
                    //已收藏
                    Log.d("aaaaaaaaaaaa", "取消");
                    map.put("type", StatusCode.REQUEST_CANCEL_FAVORDONGTAI);
                }else if (itemList.get(position).getIndex() == 0){
                    //未收藏
                    Log.d("aaaaaaaaaaaa", "收藏");
                    map.put("type", StatusCode.REQUEST_ADD_FAVORDONGTAI);
                }
                netRequest.httpRequest(map, CommonUrl.aboutFavorDongTai);
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
