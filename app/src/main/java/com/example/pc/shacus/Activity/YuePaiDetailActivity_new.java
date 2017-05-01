package com.example.pc.shacus.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.pc.shacus.Adapter.ImagePagerAdapter;
import com.example.pc.shacus.Adapter.JoinUserGridAdapter;
import com.example.pc.shacus.Adapter.PhotoViewAttacher;
import com.example.pc.shacus.Adapter.UploadViewPager;
import com.example.pc.shacus.Data.Cache.ACache;
import com.example.pc.shacus.Data.Model.LoginDataModel;
import com.example.pc.shacus.Data.Model.UserModel;
import com.example.pc.shacus.Data.Model.YuePaiDataModel;
import com.example.pc.shacus.Network.NetRequest;
import com.example.pc.shacus.Network.NetworkCallbackInterface;
import com.example.pc.shacus.Network.StatusCode;
import com.example.pc.shacus.R;
import com.example.pc.shacus.Util.CommonUrl;
import com.example.pc.shacus.Util.CommonUtils;
import com.example.pc.shacus.View.FloatMenu.FilterMenu;
import com.example.pc.shacus.View.FloatMenu.FilterMenuLayout;
import com.example.pc.shacus.View.RatingBar;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bingoogolapple.bgabanner.BGABanner;
import io.rong.imkit.RongIM;

/*
* 约拍详情页修改
* 去掉点赞、收藏
* */

public class YuePaiDetailActivity_new extends AppCompatActivity implements NetworkCallbackInterface.NetRequestIterface,NetworkCallbackInterface.OnSingleTapDismissBigPhotoListener{

    HorizontalScrollView horizontalScrollView;
    GridView gridView;
    int dwidth;
    private int num_per_page = 5; // 每行显示个数
    private int width;//每列宽度
    private BGABanner mSideZoomBanner;
    private NetRequest request;
    private ImagePagerAdapter imagePagerAdapter;
    private UploadViewPager image_viewpager;
    private Activity actvt;

    class baoming implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            if (typo.equals("yuepai")){//约拍的操作：{他人发布的：报名与取消报名，自己发布的：取消约拍与}

                /*if (isSponsor==1){//是自己发布的
                    if (isSponsor==1){
                        CommonUtils.getUtilInstance().showToast(getApplicationContext(),"不能报名自己发布的约拍");
                    }
                    if (1==1){
                        //取消约拍
                        if (data.getACstatus()==0){
                            Map map=new HashMap();
                            map.put("type",StatusCode.CANCEL_YUEPAI);
                            map.put("apid",data.getAPid());
                            map.put("userid",userModel.getId());
                            map.put("authkey",userModel.getAuth_key());
                            request.httpRequest(map,CommonUrl.createYuePaiInfo);
                        }
                    }
                }*/
                if (isSponsor==0){//是别人发布的
                    if (Useregistd==0){
                        //报名
                        Map map=new HashMap();
                        map.put("authkey",userModel.getAuth_key());
                        map.put("uid",userModel.getId());
                        map.put("type",StatusCode.REQUEST_JOIN_YUEPAI);
                        map.put("apid",data.getAPid());
                        map.put("message","aaaa");
                        request.httpRequest(map,CommonUrl.joinYuepai);

                        if(data.getUserliked() == 0){
                            map = new HashMap();
                            map.put("type", StatusCode.PRAISE_YUEPAI);
                            map.put("typeid", data.getAPid());
                            //ACache cache = ACache.get(APP.context);
                            map.put("uid", userModel.getId());
                            map.put("authkey",userModel.getAuth_key());
                            request.httpRequest(map, CommonUrl.praiseAppointment);
                        }
                    }if (Useregistd==1){
                        //取消报名
                        Map map=new HashMap();
                        map.put("authkey",userModel.getAuth_key());
                        map.put("uid",userModel.getId());
                        map.put("type",StatusCode.REQUEST_CANCEL_YUEPAI);
                        map.put("apid",data.getAPid());
                        request.httpRequest(map,CommonUrl.joinYuepai);
                    }
                }
            }else {//活动的操作：{他人发布的：报名与取消报名，自己发布的：取消活动与}


                if (isSponsor==1){//是自己发布的

                    CommonUtils.getUtilInstance().showToast(getApplicationContext(),"不能报名自己发布的活动");

                }
                if (isSponsor==0){//是别人发布的
                    if (0==0){
                        //报名
                        Map map=new HashMap();
                        map.put("authkey",userModel.getAuth_key());
                        map.put("registerid",userModel.getId());
                        map.put("type",StatusCode.REQUEST_JOIN_HUODONG);
                        map.put("acid",data.getACid());
                        request.httpRequest(map,CommonUrl.joinHuodong);
                    }if (1==1){
                        //取消报名
                        Map map=new HashMap();
                        map.put("authkey",userModel.getAuth_key());
                        map.put("registerid",userModel.getId());
                        map.put("type",StatusCode.REQUEST_CANCEL_HUODONG);
                        map.put("acid",data.getACid());
                        request.httpRequest(map,CommonUrl.joinHuodong);
                    }
                }
            }
        }
    }

    private RelativeLayout display_big_image_layout;
    private TextView title;
    private ImageButton backbtn;

    private LinearLayout baominguser;

    private Handler handler;
    private FrameLayout loading;
    private YuePaiDataModel data;
    private Button selectJoinUser;
//    private Button selectNew;

    private TextView baoming;
    private TextView chat;
    private int type;//种类（约拍或活动）

    private String typo;
    private int isSponsor;
    private int Useregistd;
    private UserModel userModel;
    private int viewpagerPosition;
    private TextView position_in_total;
    private boolean isBigImageShow=false;
    private YuePaiDetailActivity_new self;
    private EditText tv_content;
    private TextView tv_location;
    private TextView tv_price;
    private TextView tv_time;

    private ImageView userImage;
    private TextView user_name;
    private TextView user_age;
    private ImageView user_sex;
    private TextView user_local;
    TextView interestcount;

    private LinearLayout pingjia;
    private ImageView oneimage;
    private TextView onename;
    private RatingBar onerb;
    private TextView onepj;
    private ImageView twoimage;
    private TextView twoname;
    private RatingBar tworb;
    private TextView twopj;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        actvt=this;

        request=new NetRequest(this,this);
        String group = getIntent().getStringExtra("group");
        String aid=getIntent().getStringExtra("detail");
        typo=getIntent().getStringExtra("type");
        type=10000;
        if (typo.equals("yuepai"))
            type= StatusCode.REQUEST_YUEPAI_DETAIL;
        else
            type=StatusCode.REQUEST_HUODONG_DETAIL;

        ACache cache=ACache.get(this);
        LoginDataModel loginModel=(LoginDataModel)cache.getAsObject("loginModel");
        userModel=loginModel.getUserModel();
        String authKey=userModel.getAuth_key();
        String uid=userModel.getId();

        self=this;

        Map map=new HashMap();
        map.put("group",group);
        map.put("authkey", authKey);
        if (typo.equals("yuepai"))
            map.put("uid",uid);
        else
            map.put("uid",uid);
        map.put("type", type);
        if (typo.equals("yuepai"))
            map.put("apid", aid);
        else
            map.put("acid", aid);

        setContentView(R.layout.activity_yue_pai_detail_new);
        mSideZoomBanner = (BGABanner) findViewById(R.id.banner_detail_zoom);
        display_big_image_layout=(RelativeLayout)findViewById(R.id.display_detail_image);
        position_in_total=(TextView)findViewById(R.id.position_total);
        image_viewpager=(UploadViewPager)findViewById(R.id.image_detail_viewpager);

        baominguser = (LinearLayout) findViewById(R.id.baominguser);
        interestcount = (TextView) findViewById(R.id.interestcount);
        backbtn = (ImageButton) findViewById(R.id.backbtn);
        title=(TextView)findViewById(R.id.title);
        title.setText("约拍详情");
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        pingjia = (LinearLayout) findViewById(R.id.pingjia);
        oneimage = (ImageView) findViewById(R.id.oneimage);
        onepj = (TextView) findViewById(R.id.onepj);
        onerb = (RatingBar) findViewById(R.id.onerb);
        onename = (TextView) findViewById(R.id.onename);
        twoimage = (ImageView) findViewById(R.id.twoimage);
        twopj = (TextView) findViewById(R.id.twopj);
        twoname = (TextView) findViewById(R.id.twoname);
        tworb = (RatingBar) findViewById(R.id.tworb);

        if (typo.equals("yuepai"))
            request.httpRequest(map, CommonUrl.getYuePaiInfo);
        else
            request.httpRequest(map, CommonUrl.getHuodongList);

        handler=new Handler(){
            @Override
            public void handleMessage(Message msg){
                super.handleMessage(msg);

                if (msg.what==StatusCode.REQUEST_FAILURE){
                    CommonUtils.getUtilInstance().showToast(APP.context, (String) msg.obj);
                    //finish();
                    return;
                }

                if (msg.what==StatusCode.REQUEST_FINISH_YUEPAI_SUCCESS){
                    CommonUtils.getUtilInstance().showLongToast(APP.context, "约拍完成！请给对方作出评价！");
                    finish();
                    Intent intent = new Intent(YuePaiDetailActivity_new.this, RateActivity.class);
                    intent.putExtra("apid",data.getAPid());
                    startActivity(intent);
                    return;
                }

                if (msg.what==StatusCode.REQUEST_FINISH_HUODONG_SUCCESS){
                    CommonUtils.getUtilInstance().showLongToast(APP.context, "活动成功结束！");
                    finish();
                    Intent intent=new Intent(getApplicationContext(),OrdersActivity.class);
                    intent.putExtra("page", "3");
                    startActivity(intent);
                    return;
                }

                if (msg.what==StatusCode.REQUEST_FINISH_JOIN_HUODONG_SUCCESS){
                    CommonUtils.getUtilInstance().showLongToast(APP.context, "已经终止报名！");
                    finish();
                    Intent intent=new Intent(getApplicationContext(),OrdersActivity.class);
                    intent.putExtra("page", "2");
                    startActivity(intent);
                    return;
                }

                if (msg.what==StatusCode.REQUEST_CANCEL_HUODONG_SUCCESS){
                    CommonUtils.getUtilInstance().showLongToast(APP.context, "取消成功！该活动已经无效！");
                    finish();
                    return;
                }

                if (msg.what==StatusCode.REQUEST_CANCEL_YUEPAI_SUCCESS){
                    CommonUtils.getUtilInstance().showLongToast(APP.context, "取消成功！该约拍已经无效！");
                    finish();
                    return;
                }

                if (msg.what == StatusCode.REQUEST_FAVOURITE_YUEPAI_SUCCESS){
                    CommonUtils.getUtilInstance().showToast(APP.context, "收藏成功");
                    return;
                }

                if (msg.what== StatusCode.CANCEL_JOIN_YUEPAI_SUCCESS){//取消约拍报名
                    CommonUtils.getUtilInstance().showToast(APP.context, "取消报名成功");
                    baoming.setText("报名");
                    Useregistd = 0;
                    return;
                }

                if(msg.what == StatusCode.PRAISE_YUEPAI_SUCCESS){//感兴趣
                    int i = Integer.valueOf(interestcount.getText().toString());
                    interestcount.setText(String.valueOf(i+1));
                    Log.d("AAAAAAAAAAAAAA", "感兴趣成功");
                }

                if (msg.what == StatusCode.REQUEST_JOIN_YUEPAI_SUCCESS){
                    Log.d("AAAAAAAAAAAAAAAAAA","baoming");
                    CommonUtils.getUtilInstance().showToast(APP.context, "报名成功");
                    baoming.setText("取消报名");
                    Useregistd = 1;
                    Map map=new HashMap();
                    map.put("authkey",userModel.getAuth_key());
                    map.put("userid", userModel.getId());
                    map.put("fromuserid", userModel.getId());
                    map.put("touserid",data.getAPsponsorid());
                    map.put("appid", data.getAPid());
                    map.put("type", 11524);
                    request.httpRequest(map,CommonUrl.sendInfo);
                    return;
                }


                if (msg.what==StatusCode.REQUEST_YUEPAI_DETAIL_SUCCESS){

                    data=(YuePaiDataModel)msg.obj;
                    interestcount = (TextView) findViewById(R.id.interestcount);
                    interestcount.setText(String.valueOf(data.getAPlikeN()));
                    user_age = (TextView) findViewById(R.id.user_age);
                    user_name = (TextView) findViewById(R.id.user_name);
                    user_sex = (ImageView) findViewById(R.id.user_sex);
                    user_local = (TextView) findViewById(R.id.user_local);
                    userImage = (ImageView) findViewById(R.id.user_image);
                    user_age.setText(data.getUserage() + "");
                    user_local.setText(data.getUserlocation());
                    user_name.setText(data.getUseralais());
                    Glide.with(YuePaiDetailActivity_new.this)
                            .load(data.getUserimg())
                            .into(userImage);

                    String sexual = data.getUsex();
                    if (sexual.equals("男"))
                        user_sex.setBackgroundResource(R.drawable.male);
                    else if(sexual.equals("女"))
                        user_sex.setBackgroundResource(R.drawable.female);
                    tv_price = (TextView) findViewById(R.id.detail_dd_price);
                    tv_time = (TextView) findViewById(R.id.detail_dd_time);
                    tv_content= (EditText) findViewById(R.id.detail_dd_content);
                    tv_location=(TextView)findViewById(R.id.detail_dd_location);
                    isSponsor=data.getAP_issponsor();
                    Useregistd = data.getUseregistd();

                    selectJoinUser=(Button)findViewById(R.id.btn_select_join);
                    tv_content.setText(data.getAPcontent()+"");
                    tv_location.setText(data.getAPlocation()+"");
                    tv_time.setText(data.getAPtime()+"");
                    tv_price.setText(price(data.getAPpricetag()) + data.getAPprice() + "");
                    if (isSponsor==1){
                        baominguser.setVisibility(View.VISIBLE);
                        if (data.getAPstatus()==0){//0:报名中
                        selectJoinUser.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View view){
                                Intent intent=new Intent(getApplicationContext(),SelectUserActivity.class);
                                if (typo.equals("yuepai"))
                                {
                                    intent.putExtra("apid",data.getAPid());
                                    intent.putExtra("type","yuepai");
                                    intent.putExtra("title",data.getUseralais()+"的约拍");
                                    intent.putExtra("group",String.valueOf(data.getAPgroup()));
                                }
                                else {
                                    intent.putExtra("acid",data.getACid());
                                    intent.putExtra("type","huodong");
                                    intent.putExtra("title",data.getACtitle());
                                }
                                startActivity(intent);
                            }
                        });
                        selectJoinUser.setText("选择报名用户");
                        }
                        if (data.getAPstatus()==1){//1:已选择报名人，可以完成
                            selectJoinUser.setOnClickListener(new View.OnClickListener(){
                                @Override
                                public void onClick(View view){
                                    new AlertDialog.Builder(self).setTitle("完成约拍")
                                            .setMessage("确定完成约拍吗？" +"\n此操作不可撤销！")
                                            .setPositiveButton("确定", new DialogInterface.OnClickListener(){
                                                @Override
                                                public void onClick(DialogInterface dialog, int which){//确定按钮响应事件
                                                    ACache aCache = ACache.get(self);
                                                    LoginDataModel loginDataModel = (LoginDataModel) aCache.getAsObject("loginModel");
                                                    UserModel content = loginDataModel.getUserModel();
                                                    String myid = content.getId();
                                                    String authkey = content.getAuth_key();
                                                    int apid = data.getAPid();
                                                    Map map=new HashMap();
                                                    map.put("apid",apid);
                                                    map.put("authkey",authkey);
                                                    map.put("uid", myid);
                                                    map.put("type", StatusCode.REQUEST_FINISH_YUEPAI);
                                                    dialog.dismiss();
                                                    request.httpRequest(map, CommonUrl.getOrdersInfo);
                                                }
                                            }).setNegativeButton("不", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    }).show();
                                }
                            });
                            selectJoinUser.setText("完成约拍");
                        }
                        if (data.getAPstatus()==2){//1:已正常结束,双方都没有评价
                            selectJoinUser.setText("约拍评价");
                            selectJoinUser.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
//                                    finish();
                                    Intent intent = new Intent(YuePaiDetailActivity_new.this, RateActivity.class);
                                    intent.putExtra("apid",data.getAPid());
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        }
                        if (data.getAPstatus() == 3){//3是结束了,一方给出了评价
                            if (data.getUsercommented() == 1)
                                selectJoinUser.setVisibility(View.GONE);
                            else{
                                baominguser.setVisibility(View.VISIBLE);
                                selectJoinUser.setVisibility(View.VISIBLE);
                                selectJoinUser.setText("约拍评价");
                                selectJoinUser.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(YuePaiDetailActivity_new.this, RateActivity.class);
                                        intent.putExtra("apid", data.getAPid());
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                            }

                        }
                        if (data.getAPstatus() == 4){//4是两方都评价了,显示评价
                            baominguser.setVisibility(View.GONE);
                            pingjia.setVisibility(View.VISIBLE);
                            Log.d("TTTTTTTTTTTTTTT1", data.getComment().get(0).getUheadimage().toString());
                            Glide.with(YuePaiDetailActivity_new.this)
                                    .load(data.getComment().get(0).getUheadimage().toString())
                                    .error(R.drawable.loading_error)
                                    .into(oneimage);
                            onename.setText(data.getComment().get(0).getUalias().toString());
                            onepj.setText(data.getComment().get(0).getUcomment().toString());
                            onerb.setStar((float) data.getComment().get(0).getUscore());
                            onerb.setClickable(false);
                            Log.d("TTTTTTTTTTTTTTT2", data.getComment().get(1).getUheadimage().toString());
                            Glide.with(YuePaiDetailActivity_new.this)
                                    .load(data.getComment().get(1).getUheadimage().toString())
                                    .error(R.drawable.loading_error)
                                    .into(twoimage);
                            twoname.setText(data.getComment().get(1).getUalias().toString());
                            twopj.setText(data.getComment().get(1).getUcomment().toString());
                            tworb.setStar((float) data.getComment().get(1).getUscore());
                            tworb.setClickable(false);
                        }
                    }
                    if (isSponsor==0){
                        baominguser.setVisibility(View.GONE);
                        baoming = (TextView) findViewById(R.id.btn_baoming);
                        if (data.getAPstatus() == 0){
                            if (data.getUseregistd()== 0){
                                baoming.setText("报名");
                            }else
                                baoming.setText("取消报名");
                            baoming.setOnClickListener(new baoming());
                            chat=(TextView)findViewById(R.id.btn_chat);
                            RelativeLayout chatlayout=(RelativeLayout)findViewById(R.id.layout_chat);
                            chatlayout.setVisibility(View.VISIBLE);
                            chat.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    RongIM.getInstance().startPrivateChat(actvt, String.valueOf(data.getAPsponsorid()), "约拍咨询");
                                    if(data.getUserliked() == 0){
                                        Map map = new HashMap();
                                        map.put("type", StatusCode.PRAISE_YUEPAI);
                                        map.put("typeid", data.getAPid());
                                        //ACache cache = ACache.get(APP.context);
                                        map.put("uid", userModel.getId());
                                        map.put("authkey",userModel.getAuth_key());
                                        request.httpRequest(map, CommonUrl.praiseAppointment);
                                    }
                                }
                            });
                            selectJoinUser.setVisibility(View.GONE);
                        }

                        if (data.getAPstatus() == 1){
                            baominguser.setVisibility(View.VISIBLE);
                            selectJoinUser.setVisibility(View.VISIBLE);
                            horizontalScrollView = (HorizontalScrollView) findViewById(R.id.join_user_scroll);
                            horizontalScrollView.setVisibility(View.GONE);
                            selectJoinUser.setText("完成约拍");
                            selectJoinUser.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    new AlertDialog.Builder(self).setTitle("完成约拍")
                                            .setMessage("确定完成约拍吗？" + "\n此操作不可撤销！")
                                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {//确定按钮响应事件
                                                    ACache aCache = ACache.get(self);
                                                    LoginDataModel loginDataModel = (LoginDataModel) aCache.getAsObject("loginModel");
                                                    UserModel content = loginDataModel.getUserModel();
                                                    String myid = content.getId();
                                                    String authkey = content.getAuth_key();
                                                    int apid = data.getAPid();
                                                    Map map = new HashMap();
                                                    map.put("apid", apid);
                                                    map.put("authkey", authkey);
                                                    map.put("uid", myid);
                                                    map.put("type", StatusCode.REQUEST_FINISH_YUEPAI);
                                                    dialog.dismiss();
                                                    request.httpRequest(map, CommonUrl.getOrdersInfo);
                                                }
                                            }).setNegativeButton("不", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    }).show();
                                }
                            });
                        }

                        if (data.getAPstatus()==2){//1:已正常结束,双方都没有评价
                            baominguser.setVisibility(View.VISIBLE);
                            selectJoinUser.setVisibility(View.VISIBLE);
                            horizontalScrollView = (HorizontalScrollView) findViewById(R.id.join_user_scroll);
                            horizontalScrollView.setVisibility(View.GONE);
                            selectJoinUser.setText("约拍评价");
                            selectJoinUser.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(YuePaiDetailActivity_new.this, RateActivity.class);
                                    intent.putExtra("apid", data.getAPid());
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        }

                        if (data.getAPstatus() == 3){//3是结束了一方给出了评价
                            if (data.getUsercommented() == 1)
                                selectJoinUser.setVisibility(View.GONE);
                            else{
                                baominguser.setVisibility(View.VISIBLE);
                                selectJoinUser.setVisibility(View.VISIBLE);
                                selectJoinUser.setText("约拍评价");
                                horizontalScrollView = (HorizontalScrollView) findViewById(R.id.join_user_scroll);
                                horizontalScrollView.setVisibility(View.GONE);
                                selectJoinUser.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(YuePaiDetailActivity_new.this, RateActivity.class);
                                        intent.putExtra("apid", data.getAPid());
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                            }

                        }

                        if (data.getAPstatus() == 4){//4是两方都评价了,显示评价
                            baominguser.setVisibility(View.GONE);
                            pingjia.setVisibility(View.VISIBLE);
                            Log.d("TTTTTTTTTTTTTTT1", data.getComment().get(0).getUheadimage().toString());
                            Glide.with(YuePaiDetailActivity_new.this)
                                    .load(data.getComment().get(0).getUheadimage().toString())
                                    .error(R.drawable.loading_error)
                                    .into(oneimage);
                            onename.setText(data.getComment().get(0).getUalias().toString());
                            onepj.setText(data.getComment().get(0).getUcomment().toString());
                            onerb.setStar((float) data.getComment().get(0).getUscore());
                            onerb.setClickable(false);
                            Glide.with(YuePaiDetailActivity_new.this)
                                    .load(data.getComment().get(1).getUheadimage().toString())
                                    .error(R.drawable.loading_error)
                                    .into(twoimage);
                            Log.d("TTTTTTTTTTTTTTT2",data.getComment().get(1).getUheadimage().toString());
                            twoname.setText(data.getComment().get(1).getUalias().toString());
                            twopj.setText(data.getComment().get(1).getUcomment().toString());
                            tworb.setStar((float) data.getComment().get(1).getUscore());
                            tworb.setClickable(false);
                        }
                    }


                    horizontalScrollView = (HorizontalScrollView) findViewById(R.id.join_user_scroll);
                    gridView = (GridView) findViewById(R.id.grid_join_user_scroll);
                    horizontalScrollView.setHorizontalScrollBarEnabled(false);// 隐藏滚动条
                    dwidth =(int)getScreenDen();width=100;num_per_page = dwidth / width;
                    setUserValue(data.getAPregisters());

                    if ( null == data.getAPimgurl() ||data.getAPimgurl().isEmpty()){
                        mSideZoomBanner.setVisibility(View.GONE);
                    }else{
                        mSideZoomBanner.setVisibility(View.VISIBLE);
                        mSideZoomBanner.setViews(getPics(data.getAPimgurl().size(), data.getAPimgurl()));
                    }


                    loading=(FrameLayout)findViewById(R.id.loading_layout);
                    loading.setVisibility(View.GONE);
                }

                if (msg.what== StatusCode.REQUEST_HUODONG_DETAIL_SUCCESS){

                    data=(YuePaiDataModel)msg.obj;
                    user_age = (TextView) findViewById(R.id.user_age);
                    user_name = (TextView) findViewById(R.id.user_name);
                    user_sex = (ImageView) findViewById(R.id.user_sex);
                    user_local = (TextView) findViewById(R.id.user_local);
                    userImage = (ImageView) findViewById(R.id.user_image);
                    user_age.setText(data.getUserage() + "");
                    user_local.setText(data.getUserlocation());
                    user_name.setText(data.getUseralais());
                    Glide.with(YuePaiDetailActivity_new.this)
                            .load(data.getUserimg())
                            .placeholder(R.drawable.holder)
                            .error(R.drawable.loading_error)
                            .into(userImage);
                    String sexual = data.getUsex();
                    if (sexual.equals("男"))
                        user_sex.setBackgroundResource(R.drawable.male);
                    else if(sexual.equals("女"))
                        user_sex.setBackgroundResource(R.drawable.female);
                    tv_price = (TextView) findViewById(R.id.detail_dd_price);
                    tv_time = (TextView) findViewById(R.id.detail_dd_time);
                    tv_content= (EditText) findViewById(R.id.detail_dd_content);
                    tv_location=(TextView)findViewById(R.id.detail_dd_location);
                    isSponsor=data.getAC_issponsor();
                    selectJoinUser=(Button)findViewById(R.id.btn_select_join);
                    tv_content.setText(data.getAPcontent()+"");
                    tv_location.setText(data.getAPlocation()+"");
                    tv_time.setText(data.getAPtime()+"");
                    tv_price.setText(price(data.getAPpricetag()) + data.getAPprice() + "");
                    if (isSponsor==1){
                        if (data.getACstatus()==0){
                        /*selectJoinUser.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent=new Intent(getApplicationContext(),SelectUserActivity.class);
                                if (typo.equals("yuepai"))
                                {
                                    intent.putExtra("apid",data.getAPid());
                                    intent.putExtra("type","yuepai");
                                    intent.putExtra("title",data.getAPtitle());
                                }
                                else {
                                    intent.putExtra("acid",data.getACid());
                                    intent.putExtra("type","huodong");
                                    intent.putExtra("title",data.getACtitle());
                                }
                                startActivity(intent);
                            }
                        });
                        selectJoinUser.setText("查看报名用户");*/
                            selectJoinUser.setOnClickListener(new View.OnClickListener(){
                                @Override
                                public void onClick(View view) {
                                    ACache aCache = ACache.get(self);
                                    LoginDataModel loginDataModel = (LoginDataModel) aCache.getAsObject("loginModel");
                                    UserModel content = loginDataModel.getUserModel();
                                    String myid = content.getId();
                                    String authkey = content.getAuth_key();
                                    int acid = data.getACid();
                                    Map map=new HashMap();
                                    map.put("acid",acid);
                                    map.put("authkey",authkey);
                                    map.put("uid", myid);
                                    map.put("type", StatusCode.REQUEST_FINISH_JOIN_HUODONG);
                                    request.httpRequest(map, CommonUrl.finishHuodong);
                                }
                            });
                            selectJoinUser.setText("结束报名");
                        }
                        if (data.getACstatus()==1){
                            selectJoinUser.setOnClickListener(new View.OnClickListener(){
                                @Override
                                public void onClick(View view){
                                    new AlertDialog.Builder(self).setTitle("完成活动")
                                            .setMessage("确定完成活动吗？" +"\n此操作不可撤销！")
                                            .setPositiveButton("确定", new DialogInterface.OnClickListener(){
                                                @Override
                                                public void onClick(DialogInterface dialog, int which){//确定按钮响应事件
                                                    ACache aCache = ACache.get(self);
                                                    LoginDataModel loginDataModel = (LoginDataModel) aCache.getAsObject("loginModel");
                                                    UserModel content = loginDataModel.getUserModel();
                                                    String myid = content.getId();
                                                    String authkey = content.getAuth_key();
                                                    int acid = data.getACid();
                                                    Map map=new HashMap();
                                                    map.put("acid",acid);
                                                    map.put("authkey",authkey);
                                                    map.put("uid", myid);
                                                    map.put("type", StatusCode.REQUEST_FINISH_HUODONG);
                                                    dialog.dismiss();
                                                    request.httpRequest(map, CommonUrl.finishHuodong);
                                                }
                                            }).setNegativeButton("不", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    }).show();
                                }
                            });
                            selectJoinUser.setText("完成活动");
                        }
                        if (data.getACstatus()==2){
                            selectJoinUser.setVisibility(View.GONE);
                        }
                    }
                    if (isSponsor==0){
                        selectJoinUser.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent=new Intent(getApplicationContext(),SelectUserActivity.class);
                                if (typo.equals("yuepai"))
                                {
                                    intent.putExtra("apid",data.getAPid());
                                    intent.putExtra("type","yuepai");
                                    intent.putExtra("group",String.valueOf(data.getAPgroup()));
                                    intent.putExtra("title",data.getUseralais()+"的约拍");
                                }
                                else {
                                    intent.putExtra("acid",data.getACid());
                                    intent.putExtra("type","huodong");
                                    intent.putExtra("title",data.getACtitle());
                                }
                                startActivity(intent);
                            }
                        });
                        selectJoinUser.setText("查看报名用户");
                    }

                    horizontalScrollView = (HorizontalScrollView) findViewById(R.id.join_user_scroll);
                    gridView = (GridView) findViewById(R.id.grid_join_user_scroll);
                    horizontalScrollView.setHorizontalScrollBarEnabled(false);// 隐藏滚动条
                    dwidth =(int)getScreenDen();width=100;num_per_page = dwidth / width;
                    setUserValue(data.getACregister());
                    //mSideZoomBanner = (BGABanner) findViewById(R.id.banner_detail_zoom);
                    mSideZoomBanner.setViews(getPics(data.getACimageurl().size(), data.getACimageurl()));


                    loading=(FrameLayout)findViewById(R.id.loading_layout);
                    loading.setVisibility(View.GONE);

                }

            }
        };

        mSideZoomBanner.setDelegate(new BGABanner.Delegate(){
        @Override
        public void onClickBannerItem(int position){
                    if(typo.equals("yuepai"))
                        showImageViewPager(position, data.getAPimgurl(), data.getAPimgurl(), "net", "upload");
                    else
                        showImageViewPager(position, data.getACimageurl(), data.getACimageurl(), "nte", "upload");
                    viewpagerPosition = position ;
                }
            }
        );

    }

    String[] pt = {"希望收费","最多付费","希望互勉","价格商议"};

    private String price(int APpricetag){
        return pt[APpricetag];
    }

    @Override
    protected void onNewIntent(Intent intent){
        super.onNewIntent(intent);

        String type=intent.getStringExtra("type");
        if (type.equals("selectuser")){
            String result=intent.getStringExtra("result");
            if (result.equals("success")){
                selectJoinUser.setText("完成约拍");
                selectJoinUser.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        if (typo.equals("yuepai")){
                            new AlertDialog.Builder(self).setTitle("完成约拍")
                                    .setMessage("确定完成约拍吗？" +"\n此操作不可撤销！")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener(){
                                        @Override
                                        public void onClick(DialogInterface dialog, int which){//确定按钮响应事件
                                            ACache aCache = ACache.get(self);
                                            LoginDataModel loginDataModel = (LoginDataModel) aCache.getAsObject("loginModel");
                                            UserModel content = loginDataModel.getUserModel();
                                            String myid = content.getId();
                                            String authkey = content.getAuth_key();
                                            int apid = data.getAPid();
                                            Map map=new HashMap();
                                            map.put("apid",apid);
                                            map.put("authkey",authkey);
                                            map.put("uid", myid);
                                            map.put("type", StatusCode.REQUEST_FINISH_YUEPAI);
                                            dialog.dismiss();
                                            request.httpRequest(map, CommonUrl.getOrdersInfo);
                                        }
                                    }).setNegativeButton("不", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
                        }else {
                            new AlertDialog.Builder(self).setTitle("完成活动")
                                    .setMessage("确定完活动拍吗？" +"\n此操作不可撤销！")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener(){
                                        @Override
                                        public void onClick(DialogInterface dialog, int which){//确定按钮响应事件
                                            ACache aCache = ACache.get(self);
                                            LoginDataModel loginDataModel = (LoginDataModel) aCache.getAsObject("loginModel");
                                            UserModel content = loginDataModel.getUserModel();
                                            String myid = content.getId();
                                            String authkey = content.getAuth_key();
                                            int acid = data.getACid();
                                            Map map=new HashMap();
                                            map.put("acid",acid);
                                            map.put("authkey",authkey);
                                            map.put("uid", myid);
                                            map.put("type", StatusCode.REQUEST_FINISH_HUODONG);
                                            dialog.dismiss();
                                            request.httpRequest(map, CommonUrl.finishHuodong);
                                        }
                                    }).setNegativeButton("不", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
                        }
                    }
                });
            }
        }

    }


    public float getScreenDen(){
        //dwidth = new DisplayMetrics();
        //getWindowManager().getDefaultDisplay().getMetrics(dwidth);
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics                                                                                                                                                       ();
        display.getMetrics(outMetrics);

        float density  = getResources().getDisplayMetrics().density;

        return outMetrics.widthPixels / density;
    }

    private void setUserValue(List<UserModel> list) {

        if(list==null){
            return;
        }

        JoinUserGridAdapter adapter = new JoinUserGridAdapter(this, list,true);

        gridView.setAdapter(adapter);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(adapter.getCount() * width, LinearLayout.LayoutParams.WRAP_CONTENT);
        gridView.setLayoutParams(params);
        gridView.setColumnWidth(100);
        gridView.setStretchMode(GridView.NO_STRETCH);
        int count = adapter.getCount();
        gridView.setNumColumns(count);
    }

   /* private List<UserModel> init() {
        List<UserModel> list=new ArrayList<>();
        for (int i=0;i<10;i++)
        {
            UserModel user=new UserModel();
            user.setPhone("phone"+String.valueOf(i));
            user.setId("usrid" + i);
            list.add(user);
        }
        return  list;
    }*/

    public List<? extends View> getPics(int count,List<String> imgs){

        if (count==0)
            return null;

        List<ImageView> views = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            //views.add(BGABannerUtil.getItemImageView(this, R.drawable.holder));
            ImageView imageView=new ImageView(this);
            Glide.with(this)
                    .load(imgs.get(i)).centerCrop()
                    .placeholder(R.drawable.huodong_loading)
                            //.error(R.drawable.loading_error)
                    .into(imageView);
            views.add(imageView);
        }
        return views;
    }

    @Override
    public void requestFinish(String result, String requestUrl) throws JSONException {
        if (requestUrl.equals(CommonUrl.getYuePaiInfo)){
            JSONObject object = new JSONObject(result);
            Log.d("EEEEEEEEEEEEEEEEEEEEE",object.toString());
            int code = Integer.valueOf(object.getString("code"));
            if (code==StatusCode.REQUEST_YUEPAI_DETAIL_SUCCESS){
                JSONObject content=object.getJSONObject("contents");
                Gson hson=new Gson();
                if (type==StatusCode.REQUEST_YUEPAI_DETAIL)
                {
                    YuePaiDataModel data=hson.fromJson(content.toString(), YuePaiDataModel.class);
                    Message msg=handler.obtainMessage();
                    msg.what= StatusCode.REQUEST_YUEPAI_DETAIL_SUCCESS;
                    msg.obj=data;
                    handler.sendMessage(msg);
                    return;
                }
            }else {
                Message msg=handler.obtainMessage();
                msg.what= StatusCode.REQUEST_FAILURE;
                msg.obj=object.getString("contents");
                handler.sendMessage(msg);
                return;
            }
        }

        if (requestUrl.equals(CommonUrl.favouriteYuepai)){
            JSONObject object = new JSONObject(result);
            int code = Integer.valueOf(object.getString("code"));
            if (code==StatusCode.REQUEST_FAVOURITE_YUEPAI_SUCCESS){
                //JSONObject content=object.getJSONObject("contents");
                Message msg=handler.obtainMessage();
                msg.what= StatusCode.REQUEST_FAVOURITE_YUEPAI_SUCCESS;
                msg.obj=data;
                handler.sendMessage(msg);
                return;
            }else {
                Message msg=handler.obtainMessage();
                msg.what= StatusCode.REQUEST_FAILURE;
                msg.obj=object.getString("contents");
                handler.sendMessage(msg);
                return;
            }
        }

        if(requestUrl.equals(CommonUrl.sendInfo)){
            JSONObject object = new JSONObject(result);
            int code = Integer.valueOf(object.getString("code"));
            if (code == 11524)
                Log.d("AAAAAAAAAAAAAA","发送消息成功");
        }

        if (requestUrl.equals(CommonUrl.praiseAppointment)){
            JSONObject object = new JSONObject(result);
            int code = Integer.valueOf(object.getString("code"));
            if (code == StatusCode.PRAISE_YUEPAI_SUCCESS){
                /*int i = Integer.valueOf(interestcount.getText().toString());
                interestcount.setText(String.valueOf(i+1));
                Log.d("AAAAAAAAAAAAAA", "感兴趣成功");*/
                Message message = handler.obtainMessage();
                message.what = StatusCode.PRAISE_YUEPAI_SUCCESS;
                handler.sendMessage(message);
            }
        }

        if (requestUrl.equals(CommonUrl.joinYuepai)){
            JSONObject object = new JSONObject(result);
            int code = Integer.valueOf(object.getString("code"));
            if (code==StatusCode.REQUEST_JOIN_YUEPAI_SUCCESS){
                Message msg=handler.obtainMessage();
                msg.what= StatusCode.REQUEST_JOIN_YUEPAI_SUCCESS;
                //msg.obj=object.getString("contents");
                handler.sendMessage(msg);
                return;
            }

            if (code==StatusCode.CANCEL_JOIN_YUEPAI_SUCCESS){
                Message msg=handler.obtainMessage();
                msg.what= StatusCode.CANCEL_JOIN_YUEPAI_SUCCESS;
                handler.sendMessage(msg);
                return;
            }

            Message msg=handler.obtainMessage();
            msg.what= StatusCode.REQUEST_FAILURE;
            msg.obj=object.getString("contents");
            handler.sendMessage(msg);
            return;
        }

        if (requestUrl.equals(CommonUrl.getHuodongList)){
            JSONObject object = new JSONObject(result);
            int code = Integer.valueOf(object.getString("code"));
            if (code==StatusCode.REQUEST_HUODONG_DETAIL_SUCCESS){
                JSONArray content=object.getJSONArray("contents");
                Gson hson=new Gson();
                if (type==StatusCode.REQUEST_HUODONG_DETAIL)
                {
                    JSONObject jsobj=content.getJSONObject(0);
                    YuePaiDataModel data=hson.fromJson(jsobj.toString(), YuePaiDataModel.class);
                    Message msg=handler.obtainMessage();
                    msg.what= StatusCode.REQUEST_HUODONG_DETAIL_SUCCESS;
                    msg.obj=data;
                    handler.sendMessage(msg);
                    return;
                }
            }else{
                Message msg=handler.obtainMessage();
                msg.what= StatusCode.REQUEST_FAILURE;
                msg.obj=object.getString("contents");
                handler.sendMessage(msg);
                return;
            }
        }

        if (requestUrl.equals(CommonUrl.joinHuodong)){
            JSONObject object = new JSONObject(result);
            int code = Integer.valueOf(object.getString("code"));
            if (code==StatusCode.REQUEST_JOIN_HUODONG_SUCCESS){
                Message msg=handler.obtainMessage();
                msg.what= StatusCode.REQUEST_JOIN_HUODONG_SUCCESS;
                //msg.obj=object.getString("contents");
                data.setAClikenumber(data.getACregister().size() + 1);
                handler.sendMessage(msg);
                return;
            }

            if (code==StatusCode.CANCEL_JOIN_HUODONG_SUCCESS){
                Message msg=handler.obtainMessage();
                msg.what= StatusCode.CANCEL_JOIN_HUODONG_SUCCESS;
                data.setAClikenumber(data.getACregister().size() - 1);
                handler.sendMessage(msg);
                return;
            }

            Message msg=handler.obtainMessage();
            msg.what= StatusCode.REQUEST_FAILURE;
            msg.obj=object.getString("contents");
            handler.sendMessage(msg);
            return;
        }

        if (requestUrl.equals(CommonUrl.createActivityInfo)){
            JSONObject object = new JSONObject(result);
            int code = Integer.valueOf(object.getString("code"));
            if (code==StatusCode.REQUEST_CANCEL_HUODONG_SUCCESS){
                Message msg=handler.obtainMessage();
                msg.what= StatusCode.REQUEST_CANCEL_HUODONG_SUCCESS;
                handler.sendMessage(msg);
                return;
            }

            Message msg=handler.obtainMessage();
            msg.what= StatusCode.REQUEST_FAILURE;
            msg.obj=object.getString("contents");
            handler.sendMessage(msg);
            return;
        }


        if (requestUrl.equals(CommonUrl.createYuePaiInfo)){
            JSONObject object = new JSONObject(result);
            int code = Integer.valueOf(object.getString("code"));
            if (code==StatusCode.REQUEST_CANCEL_YUEPAI_SUCCESS){
                Message msg=handler.obtainMessage();
                msg.what= StatusCode.REQUEST_CANCEL_YUEPAI_SUCCESS;
                handler.sendMessage(msg);
                return;
            }

            Message msg=handler.obtainMessage();
            msg.what= StatusCode.REQUEST_FAILURE;
            msg.obj=object.getString("contents");
            handler.sendMessage(msg);
            return;
        }

        if (requestUrl.equals(CommonUrl.getOrdersInfo)){
            JSONObject object = new JSONObject(result);
            int code = Integer.valueOf(object.getString("code"));
            if (code==StatusCode.REQUEST_FINISH_YUEPAI_SUCCESS){
                Message msg=handler.obtainMessage();
                msg.what= StatusCode.REQUEST_FINISH_YUEPAI_SUCCESS;
                handler.sendMessage(msg);
                return;
            }

            if (code==StatusCode.REQUEST_FINISH_JOIN_HUODONG_SUCCESS){
                Message msg=handler.obtainMessage();
                msg.what= StatusCode.REQUEST_FINISH_JOIN_HUODONG_SUCCESS;
                handler.sendMessage(msg);
                return;
            }

            if (code==StatusCode.REQUEST_FINISH_HUODONG_SUCCESS){
                Message msg=handler.obtainMessage();
                msg.what= StatusCode.REQUEST_FINISH_HUODONG_SUCCESS;
                handler.sendMessage(msg);
                return;
            }

            Message msg=handler.obtainMessage();
            msg.what= StatusCode.REQUEST_FAILURE;
            msg.obj=object.getString("contents");
            handler.sendMessage(msg);
            return;
        }

    }

    @Override
    public void exception(IOException e, String requestUrl) {
        Message msg=new Message();
        msg.what=StatusCode.REQUEST_FAILURE;
        msg.obj="网络请求失败";
        handler.sendMessage(msg);
    }

    public void showImageViewPager(int position,
                                   final List<String>pictureUrlList,final List<String>localUrlList,
                                   final String flag,final String str){
        List<String>urlList;
        if(flag.equals("net")){
            urlList=pictureUrlList;
        }else{
            urlList=localUrlList;
        }
        display_big_image_layout.setVisibility(View.VISIBLE);
        imagePagerAdapter=new ImagePagerAdapter(this.getSupportFragmentManager(),urlList);
        image_viewpager.setAdapter(imagePagerAdapter);
        imagePagerAdapter.notifyDataSetChanged();
        image_viewpager.setOffscreenPageLimit(imagePagerAdapter.getCount());
        image_viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int state) {

            }

            @Override
            public void onPageSelected(int position) {

                viewpagerPosition = position;
                if (flag.equals("net")) {
                    position_in_total.setText((position + 1) + "/" + pictureUrlList.size());
                } else {
                    position_in_total.setText((position + 1) + "/" + localUrlList.size());
                }

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2){

            }
        });
        if(str.equals("display")){
            image_viewpager.setCurrentItem(position);

            position_in_total.setText((position+1)+"/"+urlList.size());
            isBigImageShow=true;

        }else{
            image_viewpager.setCurrentItem(position - 1);

            position_in_total.setText((position+1)+"/"+urlList.size());
            isBigImageShow=true;

        }
        PhotoViewAttacher.setOnSingleTapToPhotoViewListener(this);
    }

    @Override
    public void onDismissBigPhoto(){
        display_big_image_layout.setVisibility(View.GONE);
        isBigImageShow=false;
    }
}
