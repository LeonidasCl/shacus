package com.example.pc.shacus.Activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
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

public class YuePaiDetailActivity extends AppCompatActivity implements NetworkCallbackInterface.NetRequestIterface,NetworkCallbackInterface.OnSingleTapDismissBigPhotoListener{

    HorizontalScrollView horizontalScrollView;
    GridView gridView;
    int dwidth;
    private int num_per_page = 5; // 每行显示个数
    FilterMenuLayout filterMenu;
    private int width;//每列宽度
    private BGABanner mSideZoomBanner;
    private NetRequest request;
    private ImagePagerAdapter imagePagerAdapter;
    private UploadViewPager image_viewpager;


    FilterMenu.OnMenuChangeListener listener = new FilterMenu.OnMenuChangeListener() {
        @Override
        public void onMenuItemClick(View view, int position) {
            //Toast.makeText(getApplicationContext(), "Touched position " + position, Toast.LENGTH_SHORT).show();
            if (typo.equals("yuepai")){//约拍的操作：{他人发布的：报名与取消报名，自己发布的：取消约拍与}

                if (position==2){
                    //收藏约拍
                    Map map=new HashMap();
                    map.put("authkey",userModel.getAuth_key());
                    map.put("uid",userModel.getId());
                    map.put("type",StatusCode.REQUEST_FAVOURITE_YUEPAI);
                    map.put("typeid",data.getAPid());
                    request.httpRequest(map,CommonUrl.favouriteYuepai);
                }

                if (isSponsor==0){//是自己发布的
                    if (position==0){
                    //取消约拍
                }if (position==1){

                }
                }
                if (isSponsor==1){//是别人发布的
                    if (position==0){
                        //报名
                        Map map=new HashMap();
                        map.put("authkey",userModel.getAuth_key());
                        map.put("uid",userModel.getId());
                        map.put("type",StatusCode.REQUEST_JOIN_YUEPAI);
                        map.put("apid",data.getAPid());
                        request.httpRequest(map,CommonUrl.joinYuepai);
                    }if (position==1){
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

                if (position==2){
                    //收藏活动
                    Map map=new HashMap();
                    map.put("authkey",userModel.getAuth_key());
                    map.put("uid",userModel.getId());
                    map.put("type",StatusCode.REQUEST_FAVOURITE_HUODONG);
                    map.put("typeid",data.getACid());
                    request.httpRequest(map,CommonUrl.favouriteYuepai);
                }

                if (isSponsor==0){//是自己发布的
                    if (position==0){
                        //取消约拍
                    }if (position==1){

                    }
                }
                if (isSponsor==1){//是别人发布的
                    if (position==0){
                        //报名
                        Map map=new HashMap();
                        map.put("authkey",userModel.getAuth_key());
                        map.put("registerid",userModel.getId());
                        map.put("type",StatusCode.REQUEST_JOIN_HUODONG);
                        map.put("acid",data.getACid());
                        request.httpRequest(map,CommonUrl.joinHuodong);
                    }if (position==1){
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

        @Override
        public void onMenuCollapse() {

        }

        @Override
        public void onMenuExpand() {

        }
    };
    private RelativeLayout display_big_image_layout;
    private TextView textName;
    private TextView textTitle;
    private Handler handler;
    private FrameLayout loading;
    private YuePaiDataModel data;
    private Button selectJoinUser;
    private ImageButton btn_praise;//点赞按钮
    private TextView btn_join;//报名人数
    private int type;//种类（约拍或活动）
    private TextView praiseNum;//点赞数
    private TextView joinNum;//报名数
    private String typo;
    private int isSponsor;
    private UserModel userModel;
    private int viewpagerPosition;
    private TextView position_in_total;
    private boolean isBigImageShow=false;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        request=new NetRequest(this,this);
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

        Map map=new HashMap();
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

        setContentView(R.layout.activity_yue_pai_detail);
        mSideZoomBanner = (BGABanner) findViewById(R.id.banner_detail_zoom);
        display_big_image_layout=(RelativeLayout)findViewById(R.id.display_detail_image);
        position_in_total=(TextView)findViewById(R.id.position_total);
        image_viewpager=(UploadViewPager)findViewById(R.id.image_detail_viewpager);
        praiseNum=(TextView)findViewById(R.id.tv_praise_num);
        btn_praise=(ImageButton)findViewById(R.id.detail_praise);

        if (typo.equals("yuepai"))
        request.httpRequest(map, CommonUrl.getYuePaiInfo);
        else
            request.httpRequest(map, CommonUrl.getHuodongList);

        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                if (msg.what==StatusCode.REQUEST_FAILURE){
                    CommonUtils.getUtilInstance().showToast(APP.context, (String) msg.obj);
                    finish();
                    return;
                }

                if (msg.what == StatusCode.REQUEST_FAVOURITE_YUEPAI_SUCCESS){
                    CommonUtils.getUtilInstance().showToast(APP.context, "收藏成功");
                    return;
                }

                if (msg.what== StatusCode.PRAISE_YUEPAI_SUCCESS){
                    btn_praise.setSelected(true);
                    //praiseNum=(TextView)findViewById(R.id.tv_praise_num);
                    praiseNum.setText(data.getAPlikeN() + "");
                }

                if (msg.what== StatusCode.PRAISE_HUODONG_SUCCESS){
                    btn_praise.setSelected(true);
                    //praiseNum=(TextView)findViewById(R.id.tv_praise_num);
                    praiseNum.setText(data.getAClikenumber()+"");
                }

                if (msg.what == StatusCode.CANCEL_PRAISE_YUEPAI_SUCCESS){
                    btn_praise.setSelected(false);
                    //praiseNum=(TextView)findViewById(R.id.tv_praise_num);
                    praiseNum.setText(data.getAPlikeN()+"");
                }

                if (msg.what== StatusCode.PRAISE_HUODONG_CANCEL_SUCCESS){
                    btn_praise.setSelected(false);
                    //praiseNum=(TextView)findViewById(R.id.tv_praise_num);
                    praiseNum.setText(data.getAClikenumber()+"");
                }

                if (msg.what== StatusCode.REQUEST_JOIN_HUODONG_SUCCESS){
                    CommonUtils.getUtilInstance().showToast(APP.context, "报名成功");
                    joinNum.setText(data.getACregister().size()+"");
                    return;
                }

                if (msg.what== StatusCode.REQUEST_JOIN_YUEPAI_SUCCESS){
                    CommonUtils.getUtilInstance().showToast(APP.context, "报名成功");
                    joinNum.setText(data.getAPregistN()+"");
                    return;
                }

                if (msg.what== StatusCode.CANCEL_JOIN_SUCCESS){//取消约拍报名
                    CommonUtils.getUtilInstance().showToast(APP.context, "取消报名成功");
                    joinNum.setText(data.getAPregistN()+"");
                    return;
                }

                if (msg.what==StatusCode.CANCEL_JOIN_HUODONG_SUCCESS){
                    CommonUtils.getUtilInstance().showToast(APP.context, "取消报名成功");
                    joinNum.setText(data.getACregister().size() + "");
                    return;
                }


                if (msg.what==StatusCode.REQUEST_DETAIL_SUCCESS){

                    data=(YuePaiDataModel)msg.obj;
                    isSponsor=data.getAP_issponsor();
                    selectJoinUser=(Button)findViewById(R.id.btn_select_join);
                    if (isSponsor==1){
                        selectJoinUser.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent=new Intent(getApplicationContext(),SelectUserActivity.class);
                                if (typo.equals("yuepai"))
                                {
                                    intent.putExtra("apid",data.getAPid());
                                    intent.putExtra("type","yuepai");
                                }
                                else {
                                    intent.putExtra("acid",data.getACid());
                                    intent.putExtra("type","huodong");
                                }
                                startActivity(intent);
                            }
                        });
                    }
                    if (isSponsor==0){
                        selectJoinUser.setVisibility(View.GONE);
                    }


                    horizontalScrollView = (HorizontalScrollView) findViewById(R.id.join_user_scroll);
                    gridView = (GridView) findViewById(R.id.grid_join_user_scroll);
                    horizontalScrollView.setHorizontalScrollBarEnabled(false);// 隐藏滚动条
                    dwidth =(int)getScreenDen();width=100;num_per_page = dwidth / width;
                    setUserValue(data.getAPregisters());

                    mSideZoomBanner.setViews(getPics(data.getAPimgurl().size(), data.getAPimgurl()));
                    filterMenu= (FilterMenuLayout)findViewById(R.id.detail_filter_menu);
                    filterMenu.setVisibility(View.VISIBLE);
                    attachMenu(filterMenu);

                    praiseNum.setText(data.getAPlikeN() + "");

                    if (data.getUserliked()==0)
                        btn_praise.setSelected(false);
                    else
                        btn_praise.setSelected(true);
                    btn_praise.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //点赞
                            //praiseNum.setText(Integer.valueOf(praiseNum.getText().toString()) + 1 + "");
                            Map map = new HashMap();
                            if (data.getUserliked()==1)
                                map.put("type", StatusCode.CANCEL_YUEPAI_PRAISE);
                            else
                                map.put("type", StatusCode.PRAISE_YUEPAI);
                            map.put("typeid", data.getAPid());
                            //ACache cache = ACache.get(APP.context);
                            map.put("uid", userModel.getId());
                            map.put("authkey",userModel.getAuth_key());
                            request.httpRequest(map, CommonUrl.praiseAppointment);
                        }
                    });
                    joinNum =(TextView)findViewById(R.id.tv_join_num);
                    joinNum.setText(data.getAPregistN()+"");
                    btn_join=(TextView)findViewById(R.id.detail_join_num);
                    btn_join.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View view) {
                            //
                        }
                    });
                    textTitle=(TextView)findViewById(R.id.detail_toolbar_back);
                    textTitle.setText("＜返回");
                    textTitle.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            finish();
                        }
                    });
                    textName=(TextView)findViewById(R.id.detail_toolbar_title);
                    textName.setText(data.getAPtitle()+"");
                    loading=(FrameLayout)findViewById(R.id.loading_layout);
                    loading.setVisibility(View.GONE);
                    if (data.getUserliked()==0)
                        btn_praise.setSelected(false);
                    else
                        btn_praise.setSelected(true);
                }

                if (msg.what== StatusCode.REQUEST_HUODONG_DETAIL_SUCCESS){

                    data=(YuePaiDataModel)msg.obj;
                    isSponsor=data.getAC_issponsor();
                    selectJoinUser=(Button)findViewById(R.id.btn_select_join);
                    if (isSponsor==1){
                        selectJoinUser.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent=new Intent(getApplicationContext(),SelectUserActivity.class);
                                if (typo.equals("yuepai"))
                                {
                                    intent.putExtra("apid",data.getAPid());
                                    intent.putExtra("type","yuepai");
                                }
                                else {
                                    intent.putExtra("acid",data.getACid());
                                    intent.putExtra("type","huodong");
                                }
                                startActivity(intent);
                            }
                        });
                    }
                    if (isSponsor==0){
                        selectJoinUser.setVisibility(View.GONE);
                    }

                    horizontalScrollView = (HorizontalScrollView) findViewById(R.id.join_user_scroll);
                    gridView = (GridView) findViewById(R.id.grid_join_user_scroll);
                    horizontalScrollView.setHorizontalScrollBarEnabled(false);// 隐藏滚动条
                    dwidth =(int)getScreenDen();width=100;num_per_page = dwidth / width;
                    setUserValue(data.getACregister());
                    //mSideZoomBanner = (BGABanner) findViewById(R.id.banner_detail_zoom);
                    mSideZoomBanner.setViews(getPics(data.getACimageurl().size(), data.getACimageurl()));
                    filterMenu= (FilterMenuLayout)findViewById(R.id.detail_filter_menu);
                    filterMenu.setVisibility(View.VISIBLE);
                    attachMenu(filterMenu);
                    praiseNum.setText(data.getAClikenumber()+"");

                    if (data.getUserliked()==0)
                        btn_praise.setSelected(false);
                    else
                        btn_praise.setSelected(true);
                    btn_praise.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //点赞
                            //praiseNum.setText(Integer.valueOf(praiseNum.getText().toString()) + 1 + "");
                            Map map = new HashMap();
                            if (data.getUserliked()==1)
                                map.put("type", StatusCode.CANCEL_HUODONH_PRAISE);
                            else
                                map.put("type", StatusCode.PRAISE_HUODONG);
                            map.put("aclacid", data.getACid());
                            //ACache cache = ACache.get(APP.context);
                            map.put("uid", userModel.getId());
                            map.put("authkey",userModel.getAuth_key());
                            request.httpRequest(map, CommonUrl.praiseActivity);
                        }
                    });
                    joinNum =(TextView)findViewById(R.id.tv_join_num);
                    joinNum.setText(data.getACregister().size()+"");
                    btn_join=(TextView)findViewById(R.id.detail_join_num);
                    btn_join.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View view){
                            //
                        }
                    });
                    textTitle=(TextView)findViewById(R.id.detail_toolbar_back);
                    textTitle.setText("＜返回");
                    textTitle.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View view) {
                            finish();
                        }
                    });
                    textName=(TextView)findViewById(R.id.detail_toolbar_title);
                    textName.setText(data.getACtitle()+"");
                    loading=(FrameLayout)findViewById(R.id.loading_layout);
                    loading.setVisibility(View.GONE);

                    if (data.getUserliked()==0)
                        btn_praise.setSelected(false);
                    else
                        btn_praise.setSelected(true);
                }

            }
        };

        mSideZoomBanner.setDelegate(new BGABanner.Delegate() {
        @Override
        public void onClickBannerItem(int position) {
                    //Toast.makeText(APP.context, "点击了第" + (position + 1) + "页", Toast.LENGTH_SHORT).show();
                    showImageViewPager(position, data.getACimageurl(), data.getACimageurl(), "local", "upload");
                    viewpagerPosition = position - 1;
                }
            }
        );

    }

    private FilterMenu attachMenu(FilterMenuLayout filterMenu) {
        return new FilterMenu.Builder(this)
                .addItem(R.drawable.ic_action_info)
                .addItem(R.drawable.ic_action_info)
                .addItem(R.drawable.ic_action_info)
                .attach(filterMenu)
                .withListener(listener)
                .build();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
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
        LayoutParams params = new LayoutParams(adapter.getCount() * width, LayoutParams.WRAP_CONTENT);
        gridView.setLayoutParams(params);
        gridView.setColumnWidth(100);
        gridView.setStretchMode(GridView.NO_STRETCH);
        int count = adapter.getCount();
        gridView.setNumColumns(count);
    }

    private List<UserModel> init() {
        List<UserModel> list=new ArrayList<>();
        for (int i=0;i<10;i++)
        {
            UserModel user=new UserModel();
            user.setPhone("phone"+String.valueOf(i));
            user.setId("usrid" + i);
            list.add(user);
        }
        return  list;
    }

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
            int code = Integer.valueOf(object.getString("code"));
             if (code==StatusCode.REQUEST_DETAIL_SUCCESS){
                JSONObject content=object.getJSONObject("contents");
                Gson hson=new Gson();
                 if (type==StatusCode.REQUEST_YUEPAI_DETAIL)
                 {
                    YuePaiDataModel data=hson.fromJson(content.toString(), YuePaiDataModel.class);
                    Message msg=handler.obtainMessage();
                    msg.what= StatusCode.REQUEST_DETAIL_SUCCESS;
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

        if (requestUrl.equals(CommonUrl.praiseActivity)){
            JSONObject object = new JSONObject(result);
            int code = Integer.valueOf(object.getString("code"));
            if (code == StatusCode.PRAISE_HUODONG_SUCCESS){
                data.setUserliked(1);
                Message msg=handler.obtainMessage();
                msg.what= StatusCode.PRAISE_HUODONG_SUCCESS;
                data.setAClikenumber(Integer.valueOf(praiseNum.getText().toString()) + 1);
                handler.sendMessage(msg);
                return;
            }
            if (code == StatusCode.PRAISE_HUODONG_CANCEL_SUCCESS) {
                data.setUserliked(0);
                Message msg=handler.obtainMessage();
                msg.what= StatusCode.PRAISE_HUODONG_CANCEL_SUCCESS;
                data.setAClikenumber(Integer.valueOf(praiseNum.getText().toString()) - 1);
                handler.sendMessage(msg);
                return;
            }

            Message msg=handler.obtainMessage();
            msg.what= StatusCode.REQUEST_FAILURE;
            msg.obj=object.getString("contents");
            handler.sendMessage(msg);
            return;
        }

        if (requestUrl.equals(CommonUrl.praiseAppointment)){
            JSONObject object = new JSONObject(result);
            int code = Integer.valueOf(object.getString("code"));
            if (code == StatusCode.PRAISE_YUEPAI_SUCCESS) {
                data.setUserliked(1);
                Message msg=handler.obtainMessage();
                msg.what= StatusCode.PRAISE_YUEPAI_SUCCESS;
                data.setAPlikeN(Integer.valueOf(praiseNum.getText().toString()) + 1);
                handler.sendMessage(msg);
                return;
            }
            if (code == StatusCode.CANCEL_PRAISE_YUEPAI_SUCCESS) {
                data.setUserliked(0);
                Message msg=handler.obtainMessage();
                msg.what= StatusCode.CANCEL_PRAISE_YUEPAI_SUCCESS;
                data.setAPlikeN(Integer.valueOf(praiseNum.getText().toString()) - 1);
                handler.sendMessage(msg);
                return;
            }

            Message msg=handler.obtainMessage();
            msg.what= StatusCode.REQUEST_FAILURE;
            msg.obj=object.getString("contents");
            handler.sendMessage(msg);
            return;
        }

        if (requestUrl.equals(CommonUrl.joinYuepai)){
            JSONObject object = new JSONObject(result);
            int code = Integer.valueOf(object.getString("code"));
            if (code==StatusCode.REQUEST_JOIN_YUEPAI_SUCCESS){
                Message msg=handler.obtainMessage();
                msg.what= StatusCode.REQUEST_JOIN_YUEPAI_SUCCESS;
                //msg.obj=object.getString("contents");
                data.setAPregistN(Integer.valueOf(joinNum.getText().toString()) +1);
                handler.sendMessage(msg);
                return;
            }

            if (code==StatusCode.CANCEL_JOIN_SUCCESS){
                Message msg=handler.obtainMessage();
                msg.what= StatusCode.CANCEL_JOIN_SUCCESS;
                data.setAPregistN(Integer.valueOf(joinNum.getText().toString()) -1);
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
            }else {
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
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }
        });
        if(str.equals("display")){
            image_viewpager.setCurrentItem(position);

            position_in_total.setText((position+1)+"/"+urlList.size());
            isBigImageShow=true;

        }else{
            image_viewpager.setCurrentItem(position - 1);

            position_in_total.setText((position)+"/"+urlList.size());
            isBigImageShow=true;

        }
        PhotoViewAttacher.setOnSingleTapToPhotoViewListener(this);
    }

    @Override
    public void onDismissBigPhoto() {
        display_big_image_layout.setVisibility(View.GONE);
        isBigImageShow=false;
    }
}
