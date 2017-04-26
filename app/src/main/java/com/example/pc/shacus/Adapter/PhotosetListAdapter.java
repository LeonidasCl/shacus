package com.example.pc.shacus.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.pc.shacus.APP;
import com.example.pc.shacus.Activity.OtherUserActivity;
import com.example.pc.shacus.Activity.OtherUserDisplayActivity;
import com.example.pc.shacus.Activity.PhotosetDetailActivity;
import com.example.pc.shacus.Activity.YuePaiDetailActivity;
import com.example.pc.shacus.Data.Cache.ACache;
import com.example.pc.shacus.Data.Model.ImageData;
import com.example.pc.shacus.Data.Model.LoginDataModel;
import com.example.pc.shacus.Data.Model.PhotographerModel;
import com.example.pc.shacus.Data.Model.PhotosetDetailModel;
import com.example.pc.shacus.Data.Model.PhotosetItemModel;
import com.example.pc.shacus.Data.Model.UserModel;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 李嘉文 2017/3/25.
 * 作品集列表的适配器
 */

public class PhotosetListAdapter extends BaseAdapter {

    private UserModel userModel;
    private List<PhotosetItemModel> rankList;
    private Activity activity;
    private LayoutInflater layoutInflater;
    private ViewHolder viewHolder;

    public PhotosetListAdapter(Activity activity, List<PhotosetItemModel> list){
        this.rankList = list;
        this.activity = activity;
        ACache cache=ACache.get(activity);
        LoginDataModel loginModel=(LoginDataModel)cache.getAsObject("loginModel");
        userModel=loginModel.getUserModel();
    }

    public void add(List<PhotosetItemModel> persons){
        this.rankList.addAll(persons);
    }
    public void refresh(List<PhotosetItemModel> persons) {
        this.rankList = persons;
    }

    @Override
    public int getCount() {
        return rankList.size();
    }

    @Override
    public PhotosetItemModel getItem(int i) {
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
            view = layoutInflater.inflate(R.layout.item_photoset_layout, viewGroup, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }
        else
            viewHolder = (ViewHolder) view.getTag();

        PhotosetItemModel item = getItem(i);
        viewHolder.setValues(item);

        return view;
    }


    private class ViewHolder {

        CircleImageView photoset_publish_user_avatar;
        TextView publish_user_name;
        ImageView photoset_publish_user_sex;
        TextView photoset_publish_user_age;
        Button btn_add_favor;
        TextView tv_photoset_title;
        TextView tv_photoset_content;
        LinearLayout ll_photoset_square_imgs;
        List<ImageView> photoset_img_list;
        ImageButton btn_photoset_addlike;
        HorizontalScrollView photoset_like_user_scroll;
        GridView photoset_grid_join_user_scroll;
        FrameLayout photoset_img_frame;
        TextView photoset_img_count;
        TextView btn_photoset_likecount;

        public ViewHolder(View view){
            photoset_grid_join_user_scroll=(GridView)view.findViewById(R.id.photoset_grid_join_user_scroll);
            photoset_like_user_scroll=(HorizontalScrollView)view.findViewById(R.id.photoset_like_user_scroll);
            photoset_like_user_scroll.setHorizontalScrollBarEnabled(false);// 隐藏滚动条
            btn_photoset_addlike=(ImageButton)view.findViewById(R.id.btn_photoset_addlike);
            photoset_img_list=new ArrayList<>();
            photoset_img_list.add((ImageView)view.findViewById(R.id.photoset_img_1));
            photoset_img_list.add((ImageView)view.findViewById(R.id.photoset_img_2));
            photoset_img_list.add((ImageView)view.findViewById(R.id.photoset_img_3));
            ll_photoset_square_imgs=(LinearLayout)view.findViewById(R.id.ll_photoset_square_imgs);
            tv_photoset_content=(TextView)view.findViewById(R.id.tv_photoset_content);
            tv_photoset_title=(TextView)view.findViewById(R.id.tv_photoset_title);
            btn_add_favor=(Button)view.findViewById(R.id.btn_add_favor);
            photoset_publish_user_age=(TextView)view.findViewById(R.id.photoset_publish_user_age);
            photoset_publish_user_sex=(ImageView)view.findViewById(R.id.photoset_publish_user_sex);
            publish_user_name=(TextView)view.findViewById(R.id.publish_user_name);
            photoset_publish_user_avatar=(CircleImageView)view.findViewById(R.id.photoset_publish_user_avatar);
            photoset_img_frame=(FrameLayout)view.findViewById(R.id.photoset_img_frame);
            photoset_img_count=(TextView)view.findViewById(R.id.photoset_img_count);
            btn_photoset_likecount=(TextView)view.findViewById(R.id.btn_photoset_likecount);
        }

        void setValues(final PhotosetItemModel item){

            final Handler handler=new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);

                    if (msg.what== StatusCode.PRAISE_YUEPAI_SUCCESS){
                        //APlike.setSelected(true);
                        btn_photoset_likecount.setText(item.getUserlikeNum());
                    }
                    if (msg.what == StatusCode.CANCEL_PRAISE_YUEPAI_SUCCESS){
                        //APlike.setSelected(false);
                        btn_photoset_likecount.setText(item.getUserlikeNum());
                    }
                    if (msg.what == StatusCode.REQUEST_FOLLOW_SUCCESS){
                        btn_add_favor.setVisibility(View.GONE);
                        CommonUtils.getUtilInstance().showToast(APP.context,"关注成功");
                    }
                }
            };

            //用户头像
            String user_avatar=item.getUserHeadimg().getHeadImage();
            Glide.with(activity)
                    .load(user_avatar)
                    .placeholder(R.drawable.holder)
                    .error(R.drawable.loading_error)
                    .into(photoset_publish_user_avatar);
            photoset_publish_user_avatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, OtherUserDisplayActivity.class);
                    intent.putExtra("id",item.getUserHeadimg().getId());
                    activity.startActivity(intent);
                }
            });
            //用户昵称
            String username=item.getUserHeadimg().getNickName();
            if (!username.equals(""))
                publish_user_name.setText(username);
            else
                publish_user_name.setText("加载中");
            //用户性别
            String sexual=item.getUserHeadimg().getSex();
            if (sexual.equals("1"))
                photoset_publish_user_sex.setImageResource(R.drawable.male);
            else
                photoset_publish_user_sex.setImageResource(R.drawable.female);
            String age=item.getUserHeadimg().getAge();
            age+="岁";
            photoset_publish_user_age.setText(age);
            //是否显示关注
            if (item.getUserIsFriend().equals("1")){
                btn_add_favor.setVisibility(View.VISIBLE);
                btn_add_favor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //发起关注请求
                        NetRequest netRequest = new NetRequest(new NetworkCallbackInterface.NetRequestIterface() {
                            @Override
                            public void requestFinish(String result, String requestUrl) throws JSONException {
                                if (requestUrl.equals(CommonUrl.getFollowInfo)) {
                                    JSONObject object = new JSONObject(result);
                                    int code = Integer.valueOf(object.getString("code"));
                                    if (code == StatusCode.REQUEST_FOLLOW_USER) {
                                        item.setUserIsFriend("0");
                                        Message msg=handler.obtainMessage();
                                        msg.what= StatusCode.REQUEST_FOLLOW_SUCCESS;
                                        handler.sendMessage(msg);
                                        return;
                                    }
                                }
                            }

                            @Override
                            public void exception(IOException e, String requestUrl) {}
                        }, activity);

                        Map map = new HashMap();
                        ACache cache = ACache.get(activity);
                        LoginDataModel model = (LoginDataModel) cache.getAsObject("loginModel");
                        map.put("uid", model.getUserModel().getId());
                        map.put("authkey", model.getUserModel().getAuth_key());
                        map.put("followerid", item.getUserHeadimg().getId());
                        map.put("type", StatusCode.REQUEST_FOLLOW_USER);
                        netRequest.httpRequest(map, CommonUrl.getFollowInfo);

                    }
                });
            }else
                btn_add_favor.setVisibility(View.GONE);
            //标题
            String title=item.getUCtitle();
            if (!title.equals(""))
                tv_photoset_title.setText(title);
            else
                tv_photoset_title.setText("加载中");
            //内容描述
            String content=item.getUCtitle();
            if (!content.equals(""))
                tv_photoset_content.setText(content);
            else
                tv_photoset_content.setText("加载中");
            //点击整个略缩图布局都是进入作品集详情
            ll_photoset_square_imgs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, PhotosetDetailActivity.class);
                    intent.putExtra("ucid",item.getUCid());
                    intent.putExtra("uid",userModel.getId());
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    activity.startActivity(intent);
                }
            });
            //处理略缩图的显示逻辑
            List<ImageData> imgs=item.getUCsimpleimg();
            int imgsSize=imgs.size();
            if (imgsSize==3){//图片大于三张，正常显示
                for (int i=0;i<imgsSize;i++) {
                    Glide.with(activity)
                        .load(imgs.get(i).getImageUrl())
                        .placeholder(R.drawable.holder)
                        .error(R.drawable.loading_error)
                        .into(photoset_img_list.get(i));
                }
                photoset_img_count.setText(String.valueOf(imgsSize));
            }else if (imgsSize==2){
                photoset_img_list.get(2).setVisibility(View.INVISIBLE);
                photoset_img_frame.setVisibility(View.INVISIBLE);
                photoset_img_count.setVisibility(View.INVISIBLE);
            }else if (imgsSize==1){
                photoset_img_list.get(2).setVisibility(View.INVISIBLE);
                photoset_img_list.get(1).setVisibility(View.INVISIBLE);
                photoset_img_frame.setVisibility(View.INVISIBLE);
                photoset_img_count.setVisibility(View.INVISIBLE);
            }

            btn_photoset_addlike.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    //发起点赞请求
                    NetRequest netRequest = new NetRequest(new NetworkCallbackInterface.NetRequestIterface() {
                        @Override
                        public void requestFinish(String result, String requestUrl) throws JSONException {
                            if (requestUrl.equals(CommonUrl.praiseAppointment)) {
                                JSONObject object = new JSONObject(result);
                                int code = Integer.valueOf(object.getString("code"));
                                if (code == StatusCode.PRAISE_PHOTOSET) {
                                    item.setUserIsLiked("1");
                                    Message msg=handler.obtainMessage();
                                    msg.what= StatusCode.PRAISE_YUEPAI_SUCCESS;
                                    item.setUserlikeNum(Integer.valueOf(btn_photoset_likecount.getText().toString()) + 1 + "");

                                    handler.sendMessage(msg);
                                    return;
                                }
                                if (code == StatusCode.CANCEL_PRAISE_PHOTOSET){
                                    item.setUserIsLiked("0");
                                    Message msg=handler.obtainMessage();
                                    msg.what= StatusCode.CANCEL_PRAISE_YUEPAI_SUCCESS;
                                    item.setUserlikeNum(Integer.valueOf(btn_photoset_likecount.getText().toString()) - 1 + "");
                                    handler.sendMessage(msg);
                                    return;
                                }
                            }
                        }

                        @Override
                        public void exception(IOException e, String requestUrl) {}
                    }, activity);

                    Map map = new HashMap();
                    if (item.getUserIsLiked().equals("1"))
                        map.put("type", StatusCode.CANCEL_PRAISE_PHOTOSET);
                    else
                        map.put("type", StatusCode.PRAISE_PHOTOSET);
                    ACache cache = ACache.get(activity);
                    LoginDataModel model = (LoginDataModel) cache.getAsObject("loginModel");
                    map.put("ucid", item.getUCid());
                    map.put("authkey", model.getUserModel().getAuth_key());
                    netRequest.httpRequest(map, CommonUrl.praiseAppointment);

                }
            });
            //处理点赞人水平滚动条
            List<UserModel> userlike=item.getUserlikeList();
            JoinUserGridAdapter adapter = new JoinUserGridAdapter(activity, userlike,true);
            photoset_grid_join_user_scroll.setAdapter(adapter);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(adapter.getCount() * 100, LinearLayout.LayoutParams.WRAP_CONTENT);
            photoset_grid_join_user_scroll.setLayoutParams(params);
            photoset_grid_join_user_scroll.setColumnWidth(100);
            photoset_grid_join_user_scroll.setStretchMode(GridView.NO_STRETCH);
            int itemCount = adapter.getCount();
            photoset_grid_join_user_scroll.setNumColumns(itemCount);
            //处理点赞人数量
            int likeCount=item.getUserlikeList().size();
            String likeStr="+"+String.valueOf(likeCount);
            btn_photoset_likecount.setText(likeStr);

//
//            final Handler handler=new Handler() {
//                @Override
//                public void handleMessage(Message msg) {
//                    super.handleMessage(msg);
//
//                    if (msg.what== StatusCode.PRAISE_YUEPAI_SUCCESS){
//                        APlike.setSelected(true);
//                        praiseNum.setText(item.getAPlikeN());
//                    }
//                    if (msg.what == StatusCode.CANCEL_PRAISE_YUEPAI_SUCCESS){
//
//                        APlike.setSelected(false);
//                        praiseNum.setText(item.getAPlikeN());
//                    }
//                }
//            };
//
//            APlike.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    //发起一个点赞请求：点赞或取消点赞
//                    NetRequest netRequest = new NetRequest(new NetworkCallbackInterface.NetRequestIterface() {
//                        @Override
//                        public void requestFinish(String result, String requestUrl) throws JSONException {
//                            if (requestUrl.equals(CommonUrl.praiseAppointment)) {
//                                JSONObject object = new JSONObject(result);
//                                int code = Integer.valueOf(object.getString("code"));
//                                if (code == StatusCode.PRAISE_YUEPAI_SUCCESS) {
//                                    item.setUserliked(1);
//                                    Message msg=handler.obtainMessage();
//                                    msg.what= StatusCode.PRAISE_YUEPAI_SUCCESS;
//                                    item.setAPlikeN(Integer.valueOf(praiseNum.getText().toString()) + 1 + "");
//
//                                    handler.sendMessage(msg);
//                                    return;
//                                }
//                                if (code == StatusCode.CANCEL_PRAISE_YUEPAI_SUCCESS){
//                                    item.setUserliked(0);
//                                    Message msg=handler.obtainMessage();
//                                    msg.what= StatusCode.CANCEL_PRAISE_YUEPAI_SUCCESS;
//                                    item.setAPlikeN(Integer.valueOf(praiseNum.getText().toString()) - 1 + "");
//                                    handler.sendMessage(msg);
//                                    return;
//                                }
//                            }
//                        }
//
//                        @Override
//                        public void exception(IOException e, String requestUrl) {
//
//                        }
//                    }, activity);
//
//                    Map map = new HashMap();
//                    if (item.getUserliked()==1)
//                        map.put("type", StatusCode.CANCEL_YUEPAI_PRAISE);
//                    else
//                        map.put("type", StatusCode.PRAISE_YUEPAI);
//                    map.put("typeid", item.getAPid());
//                    ACache cache = ACache.get(activity);
//                    LoginDataModel model = (LoginDataModel) cache.getAsObject("loginModel");
//                    map.put("uid", model.getUserModel().getId());
//                    map.put("authkey", model.getUserModel().getAuth_key());
//                    netRequest.httpRequest(map, CommonUrl.praiseAppointment);
//                }
//            });
//            APjoin.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(activity, YuePaiDetailActivity.class);
//                    intent.putExtra("detail",item.getAPid());
//                    intent.putExtra("type", "yuepai");
//                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//                    activity.startActivity(intent);
//                }
//
//            });
//            mainPicture.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(activity, YuePaiDetailActivity.class);
//                    intent.putExtra("detail", item.getAPid());
//                    intent.putExtra("type", "yuepai");
//                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//                    activity.startActivity(intent);
//                }
//
//            });
//            if (item.getUserliked()==0){
//                APlike.setSelected(false);
//            }else {
//                APlike.setSelected(true);
//            }


        }
    }

}