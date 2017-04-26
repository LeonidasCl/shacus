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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.pc.shacus.APP;
import com.example.pc.shacus.Activity.OtherUserActivity;
import com.example.pc.shacus.Activity.OtherUserDisplayActivity;
import com.example.pc.shacus.Activity.YuePaiDetailActivity;
import com.example.pc.shacus.Activity.YuePaiDetailActivity_new;
import com.example.pc.shacus.Data.Cache.ACache;
import com.example.pc.shacus.Data.Model.LoginDataModel;
import com.example.pc.shacus.Data.Model.PhotographerModel;
import com.example.pc.shacus.Network.NetRequest;
import com.example.pc.shacus.Network.NetworkCallbackInterface;
import com.example.pc.shacus.Network.StatusCode;
import com.example.pc.shacus.R;
import com.example.pc.shacus.Util.CommonUrl;
import com.example.pc.shacus.View.CircleImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cuicui on 2017/4/12.
 */
public class YuePaiAdapter_new  extends BaseAdapter {
    private List<PhotographerModel> rankList;
    private Activity activity;
    private LayoutInflater layoutInflater;
    private ViewHolder viewHolder;
    //private Handler handler;

    public YuePaiAdapter_new(Activity activity, List<PhotographerModel> list){
        this.rankList = list;
        this.activity = activity;
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
            view = layoutInflater.inflate(R.layout.item_rank_layout_new, viewGroup, false);
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
        CircleImageView user_image;
        TextView user_name;
        TextView user_local;
        TextView interestcount;
        TextView yuepai_content;
        TextView user_age;
        TextView yuepai_fabutime;
        TextView yuepai_time;
        TextView yuepai_jiage;
        FrameLayout photoset_img_frame;
        TextView photoset_img_count;
        List<ImageView> photoset_img_list;
        LinearLayout ll_photoset_square_imgs;
        ImageView user_sex;
        TextView none_jp;
        LinearLayout yuepai;

        public ViewHolder(View view){
            yuepai = (LinearLayout) view.findViewById(R.id.yuepai);
            user_image = (CircleImageView) view.findViewById(R.id.user_image);
            user_name = (TextView) view.findViewById(R.id.user_name);
            user_local = (TextView) view.findViewById(R.id.user_local);
            user_sex = (ImageView) view.findViewById(R.id.user_sex);
            interestcount = (TextView) view.findViewById(R.id.interestcount);
            yuepai_content = (TextView) view.findViewById(R.id.yuepai_content);
            user_age = (TextView) view.findViewById(R.id.user_age);
            yuepai_fabutime = (TextView) view.findViewById(R.id.yuepai_fabutime);
            yuepai_time = (TextView) view.findViewById(R.id.yuepai_time);
            yuepai_jiage = (TextView) view.findViewById(R.id.yuepai_jiage);
            photoset_img_frame = (FrameLayout) view.findViewById(R.id.photoset_img_frame);
            photoset_img_count = (TextView) view.findViewById(R.id.photoset_img_count);
            ll_photoset_square_imgs = (LinearLayout) view.findViewById(R.id.ll_photoset_square_imgs);
            photoset_img_list=new ArrayList<>();
            photoset_img_list.add((ImageView)view.findViewById(R.id.photoset_img_1));
            photoset_img_list.add((ImageView)view.findViewById(R.id.photoset_img_2));
            photoset_img_list.add((ImageView)view.findViewById(R.id.photoset_img_3));
            none_jp = (TextView) view.findViewById(R.id.none_jp);
        }

        public void setValues(final PhotographerModel item){

            yuepai.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, YuePaiDetailActivity_new.class);
                    intent.putExtra("detail",String.valueOf(item.getAPid()));
                    intent.putExtra("type", "yuepai");
                    intent.putExtra("group", String.valueOf(item.getAPgroup()));
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    activity.startActivity(intent);
                }
            });

            //用户头像
            String userheadimg = item.getUserModel().getHeadImage();
            Glide.with(activity)
                    .load(userheadimg)
                    .into(user_image);
            user_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent in = new Intent(activity, OtherUserDisplayActivity.class);
                    in.putExtra("id", item.getUserModel().getId());
                    activity.startActivity(in);
                }
            });
            //用户昵称
            String name = item.getUserModel().getNickName();
            if(!name.equals(""))
                user_name.setText(name);
            else
                user_name.setText("加载中");

            //用户性别
            String sexual = item.getUserModel().getSex();
            if (sexual.equals("男"))
                user_sex.setBackgroundResource(R.drawable.male);
            else if(sexual.equals("女"))
                user_sex.setBackgroundResource(R.drawable.female);

            //用户地点
            String local = item.getUserModel().getLocation();
            if (!local.equals(""))
                user_local.setText(local);
            else
                user_local.setText("江苏");

            //用户年龄
            String age = item.getUserModel().getAge();
            if (!age.equals(""))
                user_age.setText(age);
            else
                user_age.setText("");

            //用户相册
            List<String> imgs = item.getAPimgurl();
            int imgsSize = imgs.size();
            if (imgsSize>=3){//图片大于三张，正常显示
                none_jp.setVisibility(View.GONE);
                for(int i = 0; i < 3;i++){
                    Glide.with(activity)
                            .load((imgs.get(i)))
                            .placeholder(R.drawable.holder)
                            .error(R.drawable.loading_error)
                            .into(photoset_img_list.get(i));
                }

                if (imgsSize > 3)
                {
                    photoset_img_count.setVisibility(View.VISIBLE);
                    photoset_img_count.setText(String.valueOf(imgsSize - 3));
                }
            }else if(imgsSize==2){
                none_jp.setVisibility(View.GONE);
                photoset_img_list.get(2).setVisibility(View.INVISIBLE);
                photoset_img_frame.setVisibility(View.INVISIBLE);
                photoset_img_count.setVisibility(View.INVISIBLE);
                Glide.with(activity)
                        .load((imgs.get(0)))
                        .placeholder(R.drawable.holder)
                        .error(R.drawable.loading_error)
                        .into(photoset_img_list.get(0));
                Glide.with(activity)
                        .load((imgs.get(1)))
                        .placeholder(R.drawable.holder)
                        .error(R.drawable.loading_error)
                        .into(photoset_img_list.get(1));
            }else if(imgsSize==1){
                none_jp.setVisibility(View.GONE);
                photoset_img_list.get(2).setVisibility(View.INVISIBLE);
                photoset_img_list.get(1).setVisibility(View.INVISIBLE);
                photoset_img_frame.setVisibility(View.INVISIBLE);
                photoset_img_count.setVisibility(View.INVISIBLE);
                Glide.with(activity)
                        .load((imgs.get(0)))
                        .placeholder(R.drawable.holder)
                        .error(R.drawable.loading_error)
                        .into(photoset_img_list.get(0));
            }else if (imgsSize == 0){
                none_jp.setVisibility(View.VISIBLE);
                photoset_img_list.get(2).setVisibility(View.GONE);
                photoset_img_list.get(1).setVisibility(View.GONE);
                photoset_img_list.get(0).setVisibility(View.GONE);
                photoset_img_frame.setVisibility(View.GONE);
                photoset_img_count.setVisibility(View.GONE);
            }

            //约拍时间
            String time = item.getAPtime();
            if(!time.equals(""))
                yuepai_time.setText(time);
            else
                yuepai_time.setText("暂无");
            //约拍发布时间
            String createtime = item.getAPstartT();
            if (!createtime.equals("")){
                yuepai_fabutime.setVisibility(View.VISIBLE);
                yuepai_fabutime.setText(createtime);
            }
            else
                yuepai_fabutime.setVisibility(View.INVISIBLE);

            //约拍内容
            String content = item.getAPcontent();
            if (!content.equals(""))
                yuepai_content.setText(content);

            else
                yuepai_content.setText("暂无描述");

            List<String> pricetype = new ArrayList<>();
            pricetype.add("希望收费");
            pricetype.add("最多付费");
            pricetype.add("希望互勉");
            pricetype.add("价格商议");
            //价格描述
            int pricet = item.getAPpricetype();
            if (pricet==0&&pricet==1){
                yuepai_jiage.setText(pricetype.get(pricet) + item.getAPprice());
            }
            else{
                yuepai_jiage.setText(pricetype.get(pricet));
            }

            //感兴趣
//            interestcount.setText(item.getAPlikeN());
        }
    }
}
