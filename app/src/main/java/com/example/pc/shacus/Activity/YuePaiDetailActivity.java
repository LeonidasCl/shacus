package com.example.pc.shacus.Activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.pc.shacus.APP;
import com.example.pc.shacus.Adapter.JoinUserGridAdapter;
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

public class YuePaiDetailActivity extends AppCompatActivity implements NetworkCallbackInterface.NetRequestIterface{

    HorizontalScrollView horizontalScrollView;
    GridView gridView;
    int dwidth;
    private int num_per_page = 5; // 每行显示个数
    FilterMenuLayout filterMenu;
    private int width;//每列宽度
    //private int total = 10;//列数
    private BGABanner mSideZoomBanner;
    private NetRequest request;
    //private ImageView loadinganim;
    //private AnimationDrawable animator;

    FilterMenu.OnMenuChangeListener listener = new FilterMenu.OnMenuChangeListener() {
        @Override
        public void onMenuItemClick(View view, int position) {
            //Toast.makeText(getApplicationContext(), "Touched position " + position, Toast.LENGTH_SHORT).show();
            if (typo.equals("yuepai")){//约拍的操作：{他人发布的：报名与取消报名，自己发布的：取消约拍与}
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


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        request=new NetRequest(this,this);
        String apid=getIntent().getStringExtra("detail");
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
        map.put("authkey",authKey);
        if (typo.equals("yuepai"))
        map.put("uid",uid);
        else
        map.put("Uid",uid);
        map.put("type", type);
        if (typo.equals("yuepai"))
        map.put("apid", apid);
        else
        map.put("ACid", 7);

        setContentView(R.layout.activity_yue_pai_detail);

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
                    //finish();
                    return;
                }

                if (msg.what== StatusCode.REQUEST_JOIN_YUEPAI_SUCCESS||msg.what== StatusCode.REQUEST_JOIN_HUODONG_SUCCESS){
                    CommonUtils.getUtilInstance().showToast(APP.context, "报名成功");
                    return;
                }

                if (msg.what== StatusCode.CANCEL_JOIN_SUCCESS||msg.what==StatusCode.CANCEL_HUODONG_SUCCESS){
                    CommonUtils.getUtilInstance().showToast(APP.context, "取消报名成功");
                    return;
                }

                if (msg.what==StatusCode.REQUEST_DETAIL_SUCCESS){

                    data=(YuePaiDataModel)msg.obj;
                    isSponsor=data.getAP_issponsor();
                    selectJoinUser=(Button)findViewById(R.id.btn_select_join);
                    if (isSponsor==0){
                        selectJoinUser.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //
                            }
                        });
                    }
                    if (isSponsor==1){
                        selectJoinUser.setVisibility(View.GONE);
                    }

                    horizontalScrollView = (HorizontalScrollView) findViewById(R.id.join_user_scroll);
                    gridView = (GridView) findViewById(R.id.grid_join_user_scroll);
                    horizontalScrollView.setHorizontalScrollBarEnabled(false);// 隐藏滚动条
                    dwidth =(int)getScreenDen();width=100;num_per_page = dwidth / width;
                    setUserValue(data.getAPregisters());
                    mSideZoomBanner = (BGABanner) findViewById(R.id.banner_detail_zoom);
                    mSideZoomBanner.setViews(getPics(data.getAPimgurl().size(), data.getAPimgurl()));
                    filterMenu= (FilterMenuLayout)findViewById(R.id.detail_filter_menu);
                    filterMenu.setVisibility(View.VISIBLE);
                    attachMenu(filterMenu);
                    praiseNum=(TextView)findViewById(R.id.tv_praise_num);
                    praiseNum.setText(data.getAPlikeN()+"");
                    btn_praise=(ImageButton)findViewById(R.id.detail_praise);
                    btn_praise.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //点赞
                            praiseNum.setText(Integer.valueOf(praiseNum.getText().toString()) + 1 + "");
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
                }

                if (msg.what== StatusCode.REQUEST_HUODONG_DETAIL_SUCCESS){

                    data=(YuePaiDataModel)msg.obj;
                    isSponsor=data.getAC_issponsor();
                    selectJoinUser=(Button)findViewById(R.id.btn_select_join);
                    if (isSponsor==0){
                        selectJoinUser.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //
                            }
                        });
                    }
                    if (isSponsor==1){
                        selectJoinUser.setVisibility(View.GONE);
                    }

                    horizontalScrollView = (HorizontalScrollView) findViewById(R.id.join_user_scroll);
                    gridView = (GridView) findViewById(R.id.grid_join_user_scroll);
                    horizontalScrollView.setHorizontalScrollBarEnabled(false);// 隐藏滚动条
                    dwidth =(int)getScreenDen();width=100;num_per_page = dwidth / width;
                    setUserValue(data.getACregister());
                    mSideZoomBanner = (BGABanner) findViewById(R.id.banner_detail_zoom);
                    mSideZoomBanner.setViews(getPics(data.getACimageurl().size(), data.getACimageurl()));
                    filterMenu= (FilterMenuLayout)findViewById(R.id.detail_filter_menu);
                    filterMenu.setVisibility(View.VISIBLE);
                    attachMenu(filterMenu);
                    praiseNum=(TextView)findViewById(R.id.tv_praise_num);
                    praiseNum.setText(data.getAClikenumber()+"");
                    btn_praise=(ImageButton)findViewById(R.id.detail_praise);
                    btn_praise.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //点赞
                            praiseNum.setText(Integer.valueOf(praiseNum.getText().toString()) + 1 + "");
                        }
                    });
                    joinNum =(TextView)findViewById(R.id.tv_join_num);
                    joinNum.setText(data.getACregister().size()+"");
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
                }

            }
        };


    }

    private FilterMenu attachMenu(FilterMenuLayout filterMenu) {
        return new FilterMenu.Builder(this)
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

    public List<? extends View> getPics(int count,List<String> imgs) {

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
                 }
            }else {
                //
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

            if (code==StatusCode.CANCEL_JOIN_SUCCESS){
                Message msg=handler.obtainMessage();
                msg.what= StatusCode.CANCEL_JOIN_SUCCESS;
                handler.sendMessage(msg);
                return;
            }

                Message msg=handler.obtainMessage();
                msg.what= StatusCode.REQUEST_FAILURE;
                msg.obj=object.getString("contents");
                handler.sendMessage(msg);

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
                }
            }else {
                //
            }
        }

        if (requestUrl.equals(CommonUrl.joinHuodong)){
            JSONObject object = new JSONObject(result);
            int code = Integer.valueOf(object.getString("code"));
            if (code==StatusCode.REQUEST_JOIN_HUODONG_SUCCESS){
                Message msg=handler.obtainMessage();
                msg.what= StatusCode.REQUEST_JOIN_HUODONG_SUCCESS;
                //msg.obj=object.getString("contents");
                handler.sendMessage(msg);
                return;
            }

            if (code==StatusCode.CANCEL_HUODONG_SUCCESS){
                Message msg=handler.obtainMessage();
                msg.what= StatusCode.CANCEL_HUODONG_SUCCESS;
                handler.sendMessage(msg);
                return;
            }

            Message msg=handler.obtainMessage();
            msg.what= StatusCode.REQUEST_FAILURE;
            msg.obj=object.getString("contents");
            handler.sendMessage(msg);
        }
    }

    @Override
    public void exception(IOException e, String requestUrl) {
        Message msg=new Message();
        msg.what=StatusCode.REQUEST_FAILURE;
        msg.obj="网络请求失败";
        handler.sendMessage(msg);
    }
}
