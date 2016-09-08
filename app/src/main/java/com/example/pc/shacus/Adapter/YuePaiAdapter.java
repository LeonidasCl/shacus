package com.example.pc.shacus.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
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
import com.example.pc.shacus.Activity.MainActivity;
import com.example.pc.shacus.Activity.YuePaiDetailActivity;
import com.example.pc.shacus.Data.Cache.ACache;
import com.example.pc.shacus.Data.Model.LoginDataModel;
import com.example.pc.shacus.Data.Model.PhotographerModel;
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
 * licl 2016.9.4
 */
public class YuePaiAdapter extends BaseAdapter{

    private List<PhotographerModel> rankList;
    private MainActivity activity;
    private LayoutInflater layoutInflater;
    private ViewHolder viewHolder;
    //private Handler handler;

    public YuePaiAdapter(Activity activity, List<PhotographerModel> list){
        this.rankList = list;
        this.activity = (MainActivity)activity;
        //handler=handle;
    }

    public void add(List<PhotographerModel> persons){
        this.rankList.addAll(persons);
    }
    public void refresh(List<PhotographerModel> persons) {
        this.rankList = persons;
    }

    @Override
    public int getCount() {
        return rankList.size();
    }

    @Override
    public PhotographerModel getItem(int i) {
        return rankList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if(view ==null){
            layoutInflater = LayoutInflater.from(activity);
            view = layoutInflater.inflate(R.layout.item_rank_layout, viewGroup, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }
        else
            viewHolder = (ViewHolder) view.getTag();

        PhotographerModel item = getItem(i);
        viewHolder.setValues(item);



        return view;
    }


    private class ViewHolder {
        ImageView userIamgeSrc;
        TextView APTitle;
        TextView APstartT;
        ImageView mainPicture;
        ImageButton APlike;
        ImageButton APjoin;
        TextView praiseNum;
        TextView joinNum;

        public ViewHolder(View view){
            //name = (TextView) view.findViewById(R.id.text_view_name);
            userIamgeSrc=(ImageView)view.findViewById(R.id.user_image);
            APTitle =(TextView)view.findViewById(R.id.APtitle);
            APstartT =(TextView)view.findViewById(R.id.APstartT);
            mainPicture=(ImageView)view.findViewById(R.id.APimgurl);
            APlike =(ImageButton)view.findViewById(R.id.APlikeBtn);
            APjoin =(ImageButton)view.findViewById(R.id.APjoinBtn);
            praiseNum=(TextView)view.findViewById(R.id.APlikeN);
            joinNum =(TextView)view.findViewById(R.id.APregistN);
        }

        public void setValues(final PhotographerModel item){
            //name.setText(item.getName());
            Resources res=activity.getResources();
            //Drawable usrimg=res.getDrawable(R.drawable.user_image);
            //userIamgeSrc.setImageDrawable(usrimg);
            String userimg=item.getUserimg();
            Glide.with(activity)
                    .load(userimg)
                    .placeholder(R.drawable.user_image)
                    .error(R.drawable.loading_error)
                    .into(userIamgeSrc);
            APTitle.setText(item.getAPtitle());
            APstartT.setText(item.getAPstartT());
            String mainimg=item.getAPimgurl();
            Glide.with(activity)
                    .load(mainimg)
                    .centerCrop()
                    .placeholder(R.drawable.holder)
                    .error(R.drawable.p1)
                    .into(mainPicture);
            //mainPicture.setImageDrawable(mainimg);
            praiseNum.setText(String.valueOf(item.getAPlikeN()));
            joinNum.setText(String.valueOf(item.getAPregistN()));

            final Handler handler=new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);

                    if (msg.what== StatusCode.PRAISE_YUEPAI_SUCCESS){
                        APlike.setSelected(true);
                        praiseNum.setText(item.getAPlikeN());
                    }
                    if (msg.what == StatusCode.CANCEL_PRAISE_YUEPAI_SUCCESS){

                        APlike.setSelected(false);
                        praiseNum.setText(item.getAPlikeN());
                    }
                }
            };

            APlike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //发起一个点赞请求：点赞或取消点赞
                    NetRequest netRequest = new NetRequest(new NetworkCallbackInterface.NetRequestIterface() {
                        @Override
                        public void requestFinish(String result, String requestUrl) throws JSONException {
                            if (requestUrl.equals(CommonUrl.praiseAppointment)) {
                                JSONObject object = new JSONObject(result);
                                int code = Integer.valueOf(object.getString("code"));
                                if (code == StatusCode.PRAISE_YUEPAI_SUCCESS) {
                                    item.setUserliked(1);
                                    Message msg=handler.obtainMessage();
                                    msg.what= StatusCode.PRAISE_YUEPAI_SUCCESS;
                                    item.setAPlikeN(Integer.valueOf(praiseNum.getText().toString()) + 1 + "");

                                    handler.sendMessage(msg);
                                    return;
                                }
                                if (code == StatusCode.CANCEL_PRAISE_YUEPAI_SUCCESS){
                                    item.setUserliked(0);
                                    Message msg=handler.obtainMessage();
                                    msg.what= StatusCode.CANCEL_PRAISE_YUEPAI_SUCCESS;
                                    item.setAPlikeN(Integer.valueOf(praiseNum.getText().toString()) - 1 + "");
                                    handler.sendMessage(msg);
                                    return;
                                }
                            }
                        }

                        @Override
                        public void exception(IOException e, String requestUrl) {

                        }
                    }, activity);

                    Map map = new HashMap();
                    if (item.getUserliked()==1)
                    map.put("type", StatusCode.CANCEL_YUEPAI_PRAISE);
                    else
                    map.put("type", StatusCode.PRAISE_YUEPAI);
                    map.put("typeid", item.getAPid());
                    ACache cache = ACache.get(activity);
                    LoginDataModel model = (LoginDataModel) cache.getAsObject("loginModel");
                    map.put("uid", model.getUserModel().getId());
                    map.put("authkey", model.getUserModel().getAuth_key());
                    netRequest.httpRequest(map, CommonUrl.praiseAppointment);
                }
            });
            APjoin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity, YuePaiDetailActivity.class);
                    intent.putExtra("detail",item.getAPid());
                    intent.putExtra("type","yuepai");
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    activity.startActivity(intent);
                }

            });
            if (item.getUserliked()==0){
                APlike.setSelected(false);
            }else {
                APlike.setSelected(true);
            }
        }
    }

}
