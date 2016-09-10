package com.example.pc.shacus.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.pc.shacus.Activity.OtherUserActivity;
import com.example.pc.shacus.Activity.YuePaiDetailActivity;
import com.example.pc.shacus.Data.Cache.ACache;
import com.example.pc.shacus.Data.Model.HuoDongItemModel;
import com.example.pc.shacus.Data.Model.LoginDataModel;
import com.example.pc.shacus.Network.NetRequest;
import com.example.pc.shacus.Network.NetworkCallbackInterface;
import com.example.pc.shacus.Network.StatusCode;
import com.example.pc.shacus.R;
import com.example.pc.shacus.Util.CommonUrl;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * licl 2016.7.18
 *
 */
public class HuodongItemAdapter extends BaseAdapter{

    private List<HuoDongItemModel> rankList;
    private Context activity;
    private LayoutInflater layoutInflater;
    private ViewHolder viewHolder;



    public HuodongItemAdapter(Context activity, List<HuoDongItemModel> list) {
        this.rankList = list;
        this.activity = activity;
    }

    public void add(List<HuoDongItemModel> persons){
        this.rankList.addAll(persons);
    }
    public void refresh(List<HuoDongItemModel> persons) {
        this.rankList = persons;
    }

    @Override
    public int getCount() {
         int size=rankList.size();
        return size;
    }

    @Override
    public HuoDongItemModel getItem(int i) {
        return rankList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup){

        if(view ==null){
            layoutInflater = LayoutInflater.from(activity);
            view = layoutInflater.inflate(R.layout.item_huodong_layout, viewGroup, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }
        else
            viewHolder = (ViewHolder) view.getTag();

        HuoDongItemModel item = getItem(i);

        viewHolder.setValues(item);

        return view;
    }


    private class ViewHolder {
        ImageView userIamgeSrc;
        ImageView mainPicture;
        TextView praiseNum;
        TextView joinNum;
        TextView userName;
        TextView setTime;
        TextView describe;
        ImageButton huodongJoin;
        ImageButton huodongPraise;

        public ViewHolder(View view) {
            userIamgeSrc=(ImageView)view.findViewById(R.id.huodong_avatar);
            mainPicture=(ImageView)view.findViewById(R.id.huodong_mainimg);
            praiseNum=(TextView)view.findViewById(R.id.huodong_praise_num);
            joinNum=(TextView)view.findViewById(R.id.huodong_join_num);
            userName=(TextView)view.findViewById(R.id.huodong_username);
            setTime=(TextView)view.findViewById(R.id.huodong_settime);
            describe=(TextView)view.findViewById(R.id.huodong_describe);
            huodongJoin=(ImageButton)view.findViewById(R.id.huodong_join);
            huodongPraise=(ImageButton)view.findViewById(R.id.huodong_praise);
        }

        public void setValues(final HuoDongItemModel item){
            Glide.with(activity)
                    .load(item.getAClurl()).centerCrop()
                    .placeholder(R.drawable.holder)
                    .error(R.drawable.loading_error)
                    .into(mainPicture);
            Glide.with(activity)
                    .load(item.getUserimageurl()).centerCrop()
//                    .placeholder(R.drawable.holder)
                    .error(R.drawable.loading_error)
                    .into(userIamgeSrc);
//            userIamgeSrc.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent in = new Intent(activity, OtherUserActivity.class);
//                    in.putExtra("id", item.getACid());
//                    activity.startActivity(in);
//                }
//            });
            Resources res=activity.getResources();
            Drawable usrimg=res.getDrawable(R.drawable.personal_default_photo);
//            userIamgeSrc.setImageDrawable(usrimg);
            //Drawable mainpic=res.getDrawable(R.drawable.huodong_loading);
            praiseNum.setText(String.valueOf(item.getAClikenumber()));
            joinNum.setText(String.valueOf(item.getACregistN()));
            userName.setText(item.getACtitle()+"");
            setTime.setText(item.getACstartT());
            describe.setText(item.getACcontent());

            final Handler handler=new Handler(){
                @Override
                public void handleMessage(Message msg){
                    super.handleMessage(msg);

                    if (msg.what== StatusCode.PRAISE_HUODONG_SUCCESS){
                        huodongPraise.setSelected(true);
                        praiseNum.setText(item.getAClikenumber()+"");
                    }
                    if (msg.what == StatusCode.PRAISE_HUODONG_CANCEL_SUCCESS){

                        huodongPraise.setSelected(false);
                        praiseNum.setText(item.getAClikenumber()+"");
                    }
                }
            };

            huodongJoin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //进入细节
                    Intent intent = new Intent(activity, YuePaiDetailActivity.class);
                    intent.putExtra("detail",item.getACid()+"");
                    intent.putExtra("type","huodong");
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    activity.startActivity(intent);
                }
            });
            huodongPraise.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //点赞
                    NetRequest netRequest = new NetRequest(new NetworkCallbackInterface.NetRequestIterface() {
                        @Override
                        public void requestFinish(String result, String requestUrl) throws JSONException {
                            if (requestUrl.equals(CommonUrl.praiseActivity)) {
                                JSONObject object = new JSONObject(result);
                                int code = Integer.valueOf(object.getString("code"));
                                if (code == StatusCode.PRAISE_HUODONG_SUCCESS) {
                                    item.setUserliked(1);
                                    Message msg=handler.obtainMessage();
                                    msg.what= StatusCode.PRAISE_HUODONG_SUCCESS;
                                    item.setAClikenumber(Integer.valueOf(praiseNum.getText().toString()) + 1);

                                    handler.sendMessage(msg);
                                    return;
                                }
                                if (code == StatusCode.PRAISE_HUODONG_CANCEL_SUCCESS){
                                    item.setUserliked(0);
                                    Message msg=handler.obtainMessage();
                                    msg.what= StatusCode.PRAISE_HUODONG_CANCEL_SUCCESS;
                                    item.setAClikenumber(Integer.valueOf(praiseNum.getText().toString()) - 1 );
                                    handler.sendMessage(msg);
                                    return;
                                }
                            }
                        }

                        @Override
                        public void exception(IOException e, String requestUrl){

                        }
                    }, activity);

                    Map map = new HashMap();
                    if (item.getUserliked()==1)
                        map.put("type", StatusCode.CANCEL_HUODONH_PRAISE);
                    else
                        map.put("type", StatusCode.PRAISE_HUODONG);
                    map.put("aclacid", item.getACid());
                    ACache cache = ACache.get(activity);
                    LoginDataModel model = (LoginDataModel) cache.getAsObject("loginModel");
                    map.put("uid", model.getUserModel().getId());
                    map.put("authkey", model.getUserModel().getAuth_key());
                    netRequest.httpRequest(map, CommonUrl.praiseActivity);
                }
            });
        }
    }

}
