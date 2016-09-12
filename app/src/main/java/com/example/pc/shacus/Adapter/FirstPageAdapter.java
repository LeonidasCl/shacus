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
import com.example.pc.shacus.Activity.MainActivity;
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
 * 崔颖华
 * Created by cuicui on 2016/9/8.
 */
public class FirstPageAdapter extends  BaseAdapter{
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
            view = LayoutInflater.from(context.getActivity()).inflate(R.layout.item_firstpage,parent,false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }

        ItemModel itemModel = itemList.get(position);
        viewHolder.setValues(itemModel);

        viewHolder.userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemModel next = itemList.get(position);
                Intent intent = new Intent(context.getActivity(), OtherUserActivity.class);
                intent.putExtra("id", next.getUserId());
                context.startActivity(intent);
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
        public ViewHolder(View view){
            bigImage = (ImageView) view.findViewById(R.id.first_bigimage);
            userImage = (CircleImageView) view.findViewById(R.id.firstpage_user_image);
            title = (TextView) view.findViewById(R.id.firstpage_user_title);
            liken = (TextView) view.findViewById(R.id.first_likenum);
            commentn = (TextView) view.findViewById(R.id.first_commentnum);
            time = (TextView) view.findViewById(R.id.first_time);
            detial = (TextView) view.findViewById(R.id.first_detail);
            likebtn = (ImageView) view.findViewById(R.id.first_likebtn);

        }

        public void setValues(final ItemModel itemModel){
            Glide.with(context)
                    .load(itemModel.getImage()).fitCenter()
                    .into(viewHolder.bigImage);
            Glide.with(context)
                    .load(itemModel.getUserImage()).fitCenter()
                    .into(viewHolder.userImage);
            detial.setText(itemModel.getDetial());
            liken.setText(Integer.toString(itemModel.getLikeNum()));
            commentn.setText(Integer.toString(itemModel.getCommentNum()));
            time.setText(itemModel.getStartTime());
            title.setText(itemModel.getTitle());

            if(itemModel.getIndex() == 1){
                //已收藏
                likebtn.setImageResource(R.drawable.likedafter);
            }else if (itemModel.getIndex() == 0){
                //未收藏
                likebtn.setImageResource(R.drawable.heart);
            }

            final Handler handler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if (msg.what == StatusCode.REQUEST_ADD_FAVORDONGTAI_SUCCESS){
                        liken.setText(String.valueOf(itemModel.getLikeNum()));

                        likebtn.setImageResource(R.drawable.likedafter);
                        CommonUtils.getUtilInstance().showToast(APP.context, "收藏成功");
                    }else if(msg.what == StatusCode.REQUEST_CANCEL_FAVORDONGTAI_SUCCESS){
                        liken.setText(String.valueOf(itemModel.getLikeNum()));
                        likebtn.setImageResource(R.drawable.heart);
                        CommonUtils.getUtilInstance().showToast(APP.context, "取消收藏");
                    }
                }
            };

            likebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //发起一个收藏请求：收藏或取消收藏
                    NetRequest netRequest = new NetRequest(new NetworkCallbackInterface.NetRequestIterface() {
                        @Override
                        public void requestFinish(String result, String requestUrl) throws JSONException {
                            if(requestUrl.equals(CommonUrl.aboutFavorDongTai)){
                                JSONObject object = new JSONObject(result);
                                int code  = Integer.valueOf(object.getString("code"));
                                switch (code){
                                    case StatusCode.REQUEST_ADD_FAVORDONGTAI_SUCCESS:{
                                        itemModel.setIndex(1);
                                        Message message = handler.obtainMessage();
                                        message.what = StatusCode.REQUEST_ADD_FAVORDONGTAI_SUCCESS;
                                        itemModel.setLikeNum(Integer.valueOf(liken.getText().toString())+1);
                                        handler.sendMessage(message);
                                        break;
                                    }
                                    case StatusCode.REQUEST_CANCEL_FAVORDONGTAI_SUCCESS:{
                                        itemModel.setIndex(0);
                                        Message message = handler.obtainMessage();
                                        message.what = StatusCode.REQUEST_CANCEL_FAVORDONGTAI_SUCCESS;
                                        itemModel.setLikeNum(Integer.valueOf(liken.getText().toString())-1);
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
                    map.put("trendid", itemModel.getId());
                    if(itemModel.getIndex() == 1){
                        //已收藏
                        likebtn.setImageResource(R.drawable.likedafter);
                        map.put("type", StatusCode.REQUEST_CANCEL_FAVORDONGTAI);
                    }else if (itemModel.getIndex() == 0){
                        //未收藏
                        likebtn.setImageResource(R.drawable.heart);
                        map.put("type", StatusCode.REQUEST_ADD_FAVORDONGTAI);
                    }
                    netRequest.httpRequest(map, CommonUrl.aboutFavorDongTai);
                }
            });

        }

    }
}
