package com.example.pc.shacus.Activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.pc.shacus.APP;
import com.example.pc.shacus.Data.Cache.ACache;
import com.example.pc.shacus.Data.Model.LoginDataModel;
import com.example.pc.shacus.Data.Model.UserModel;
import com.example.pc.shacus.Network.NetRequest;
import com.example.pc.shacus.Network.NetworkCallbackInterface;
import com.example.pc.shacus.Network.StatusCode;
import com.example.pc.shacus.R;
import com.example.pc.shacus.Util.CommonUrl;
import com.example.pc.shacus.Util.CommonUtils;
import com.example.pc.shacus.View.CircleImageView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import io.rong.imkit.RongIM;

/**
 * Created by cuicui on 2017/2/13.
 */
public class OtherUserDisplayActivity extends AppCompatActivity implements  NetworkCallbackInterface.NetRequestIterface{


    String type = null;
    String otherId = "1";
    private CircleImageView headimage;

    LinearLayout yuepaibtn;
    LinearLayout followingbtn;
    LinearLayout followerbtn;
    LinearLayout layout_otheruser_chat;
    RelativeLayout relativeLayout;
    Button btn_yuepai_chat;
    Button btn_common_chat;
    Button editinfo;
//    Button addfollow;
//    ImageView bigimageview;
    ImageButton settingbtn;
    ImageButton backbtn;

    TextView othername;
    TextView username;
    TextView usersign;
    TextView userlocal;

    TextView myyuepainum;
    TextView followingnum;
    TextView followernum;
    TextView age;

    RelativeLayout display_big_image_layout;

    String name = null;
    String sign = null;
    String local = null;
    String himage = null;
    String bimage = null;
    String sex = null;
    int following = 0;
    int follower = 0;
    int yuepai = 0;
    int GRZP = 1000;
    int GRZP_NUM = 0;
    ArrayList<String> GRZP_URL= new ArrayList();
    ImageView grzp_i1;
    ImageView grzp_i2;
//    ImageView grzp_i3;
    ImageView grzp_i4;
    TextView grzp_im;
    TextView grzp_n;

    int ZPJ = 2000;
    int ZPJ_NUM = 0;
    ArrayList<String> ZPJ_URL = new ArrayList<>();
    ImageView zpj_i1;
    ImageView zpj_i2;
//    ImageView zpj_i3;
    ImageView zpj_i4;
    TextView zpj_im;
    TextView zpj_n;

    ACache aCache;
    LoginDataModel loginModel;
    String myid = null;
    String otherid = null;
    String authkey =null;
    Map map = new HashMap<>();
    NetRequest requestOthers;

    Boolean followor;
    ImageView btnsex;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selfdisplay);

        Intent intent = getIntent();
        type = intent.getStringExtra("id");
        otherId = type;

//        bigimageview = (ImageView) findViewById(R.id.bigimageView);
        headimage = (CircleImageView) findViewById(R.id.imageData_UserImage);
        username = (TextView) findViewById(R.id.textData_UserName);
        usersign = (TextView) findViewById(R.id.textData_UserSign);
        userlocal = (TextView) findViewById(R.id.textData_UserLocal);
        yuepaibtn = (LinearLayout) findViewById(R.id.myYuepiaBtn);
        followingbtn = (LinearLayout) findViewById(R.id.followingBtn);
        followerbtn = (LinearLayout) findViewById(R.id.followerBtn);
        myyuepainum = (TextView) findViewById(R.id.myYuepaiNum);
        followingnum = (TextView) findViewById(R.id.followingNum);
        followernum = (TextView) findViewById(R.id.followerNum);

        age = (TextView) findViewById(R.id.textData_UserAge);

        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        relativeLayout.setBackgroundResource(R.color.zero_black);
        backbtn = (ImageButton) findViewById(R.id.backbtn);
        backbtn.setVisibility(View.VISIBLE);
        othername = (TextView) findViewById(R.id.othername);
        othername.setVisibility(View.VISIBLE);
        settingbtn = (ImageButton) findViewById(R.id.settingbtn);
        settingbtn.setVisibility(View.INVISIBLE);
        editinfo = (Button) findViewById(R.id.addfollow);
//        editinfo = (Button) findViewById(R.id.edit_info);
        editinfo.setText("关注");
        btnsex = (ImageView) findViewById(R.id.sex);

        LinearLayout grzp = (LinearLayout) findViewById(R.id.grzp);
        LinearLayout zpj = (LinearLayout) findViewById(R.id.zpj);

        grzp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(OtherUserDisplayActivity.this,PhotoselfDetailActivity.class);
                intent.putExtra("uid",otherId);
                startActivity(intent);
            }
        });


        zpj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(OtherUserDisplayActivity.this,PhotosetOverviewActivity.class);
                intent.putExtra("uid",otherId);
                startActivity(intent);
            }
        });

        grzp_i1 = (ImageView) findViewById(R.id.grzp_i1);
        grzp_i2 = (ImageView) findViewById(R.id.grzp_i2);
//        grzp_i3 = (ImageView) findViewById(R.id.grzp_i3);
        grzp_i4 = (ImageView) findViewById(R.id.grzp_i4);
        grzp_im = (TextView) findViewById(R.id.grzp_im);
        grzp_n = (TextView) findViewById(R.id.grzp_n);

        zpj_i1 = (ImageView) findViewById(R.id.zpj_i1);
        zpj_i2 = (ImageView) findViewById(R.id.zpj_i2);
//        zpj_i3 = (ImageView) findViewById(R.id.zpj_i3);
        zpj_i4 = (ImageView) findViewById(R.id.zpj_i4);
        zpj_im = (TextView) findViewById(R.id.zpj_im);
        zpj_n = (TextView) findViewById(R.id.zpj_n);
        display_big_image_layout = (RelativeLayout) findViewById(R.id.display_big_image_layout);

        layout_otheruser_chat = (LinearLayout) findViewById(R.id.layout_otheruser_chat);
        btn_yuepai_chat = (Button) findViewById(R.id.btn_yuepai_chat);
        btn_common_chat = (Button) findViewById(R.id.btn_common_chat);

        requestOthers = new NetRequest(OtherUserDisplayActivity.this,OtherUserDisplayActivity.this);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //返回上一级
                finish();
            }
        });

        //关注与否
       editinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetRequest request1 = new NetRequest(OtherUserDisplayActivity.this,OtherUserDisplayActivity.this);
                Map map1=new HashMap();
                map1.put("uid", myid);
                map1.put("authkey", authkey);
                map1.put("followerid", otherId);

                if(followor == false){
                    //未关注
                    map1.put("type", StatusCode.REQUEST_FOLLOW_USER);
                }else {
                    //已关注
                    map1.put("type",StatusCode.REQUEST_CANCEL_FOLLOWING);
                }
                request1.httpRequest(map1, CommonUrl.getFollowInfo);
            }
        });


        btn_common_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RongIM.getInstance().startPrivateChat(OtherUserDisplayActivity.this, otherId, "title");
            }
        });

        followingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ta的关注
                Intent intent = new Intent();
                intent.putExtra("activity", "following");
                intent.putExtra("user", "other");
                intent.putExtra("id",otherId);
                intent.setClass(APP.context, FollowActivity.class);
                startActivity(intent);
            }
        });

        followerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ta的粉丝
                Intent intent = new Intent();
                intent.putExtra("activity", "follower");
                intent.putExtra("user", "other");
                intent.putExtra("id",otherId);
                intent.setClass(APP.context, FollowActivity.class);
                startActivity(intent);

            }
        });

        headimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                display_big_image_layout.setVisibility(View.VISIBLE);
                ImageView image = (ImageView) findViewById(R.id.fulldisplay_headimage);
                Glide.with(APP.context)
                        .load(himage)
                        .into(image);
            }
        });

        display_big_image_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                display_big_image_layout.setVisibility(View.GONE);
            }
        });

        aCache=ACache.get(OtherUserDisplayActivity.this);
        loginModel = (LoginDataModel)aCache.getAsObject("loginModel");
        UserModel content = loginModel.getUserModel();
        myid = content.getId();
        authkey = content.getAuth_key();

        initReq();
        initGRZP();
        initZPJ();

    }

    private void initReq() {
        map = new HashMap();
        map.put("uid", myid);
        map.put("authkey", authkey);
        map.put("seeid", otherId);
        map.put("type",StatusCode.REQUEST_OTHERUSER_INFO);
        requestOthers.httpRequest(map, CommonUrl.otherUserInfo);
    }

    private void initGRZP() {
        NetRequest netRequestG = new NetRequest(OtherUserDisplayActivity.this,OtherUserDisplayActivity.this);
        map = new HashMap();
        map.put("uid", otherId);
        map.put("authkey", authkey);
        map.put("type", StatusCode.REQUEST_USER_GRZP);
        netRequestG.httpRequest(map, CommonUrl.userImage);
    }


    private void initZPJ() {
        NetRequest netRequestZ = new NetRequest(OtherUserDisplayActivity.this,OtherUserDisplayActivity.this);
        map = new HashMap();
        map.put("uid", otherId);
        map.put("authkey", authkey);
        map.put("type", StatusCode.REQUEST_USER_ZPJ);
        netRequestZ.httpRequest(map, CommonUrl.userImage);
    }


    private void initView() {
        othername.setText(name);
        username.setText(name);
        if(sign.equals("")){
            sign = "简介--暂无";
        }else {
            String temp = sign;
            sign = "简介--" + temp;
        }
        usersign.setText(sign);

        if(local.equals("")){
            local = "常住--暂无";
        }else {
            String temp = local;
            local = "常住--" + temp;
        }
        userlocal.setText(local);

        myyuepainum.setText(Integer.toString(yuepai));
        followingnum.setText(Integer.toString(following));
        followernum.setText(Integer.toString(follower));

        if(himage != null){
            Glide.with(APP.context)
                    .load(himage)
                    .into(headimage);
        }

        if (followor == false){
            editinfo.setText("关注");
        }else {
            editinfo.setText("已关注");
        }

        if(sex.equals("false")){
            btnsex.setImageResource(R.drawable.female);
        } else if(sex.equals("true")){
            btnsex.setImageResource(R.drawable.male);
        }
    }

    private void initGRZPView() {
        if(GRZP_NUM == 0){
            grzp_i1.setVisibility(View.GONE);
            grzp_i2.setVisibility(View.GONE);
//            grzp_i3.setVisibility(View.GONE);
            grzp_i4.setVisibility(View.GONE);
            grzp_im.setVisibility(View.GONE);
            grzp_n.setVisibility(View.VISIBLE);
        }else{
            if(GRZP_NUM <= 3){
                switch (GRZP_NUM){
                   /* case 4:
                        grzp_i1.setVisibility(View.VISIBLE);
                        grzp_i2.setVisibility(View.VISIBLE);
                        grzp_i3.setVisibility(View.VISIBLE);
                        grzp_i4.setVisibility(View.VISIBLE);
                        grzp_im.setVisibility(View.INVISIBLE);
                        grzp_n.setVisibility(View.INVISIBLE);
                        Glide.with(APP.context)
                                .load(GRZP_URL.get(0))
                                .into(grzp_i1);
                        Glide.with(APP.context)
                                .load(GRZP_URL.get(1))
                                .into(grzp_i2);
                        Glide.with(APP.context)
                                .load(GRZP_URL.get(2))
                                .into(grzp_i3);
                        Glide.with(APP.context)
                                .load(GRZP_URL.get(3))
                                .into(grzp_i4);
                        break;*/
                    case 3:
                        grzp_i1.setVisibility(View.VISIBLE);
                        grzp_i2.setVisibility(View.VISIBLE);
//                        grzp_i3.setVisibility(View.VISIBLE);
                        grzp_i4.setVisibility(View.VISIBLE);
                        grzp_im.setVisibility(View.INVISIBLE);
                        grzp_n.setVisibility(View.INVISIBLE);
                        Glide.with(APP.context)
                                .load(GRZP_URL.get(0))
                                .into(grzp_i1);
                        Glide.with(APP.context)
                                .load(GRZP_URL.get(1))
                                .into(grzp_i2);
                        Glide.with(APP.context)
                                .load(GRZP_URL.get(2))
                                .into(grzp_i4);
                        break;
                    case 2:
                        grzp_i1.setVisibility(View.VISIBLE);
                        grzp_i2.setVisibility(View.VISIBLE);
//                        grzp_i3.setVisibility(View.INVISIBLE);
                        grzp_i4.setVisibility(View.GONE);
                        grzp_im.setVisibility(View.INVISIBLE);
                        grzp_n.setVisibility(View.INVISIBLE);
                        Glide.with(APP.context)
                                .load(GRZP_URL.get(0))
                                .into(grzp_i1);
                        Glide.with(APP.context)
                                .load(GRZP_URL.get(1))
                                .into(grzp_i2);
                        break;
                    case 1:
                        grzp_i1.setVisibility(View.VISIBLE);
                        grzp_i2.setVisibility(View.GONE);
//                        grzp_i3.setVisibility(View.INVISIBLE);
                        grzp_i4.setVisibility(View.GONE);
                        grzp_im.setVisibility(View.INVISIBLE);
                        grzp_n.setVisibility(View.INVISIBLE);
                        Glide.with(APP.context)
                                .load(GRZP_URL.get(0))
                                .into(grzp_i1);
                        break;
                }
            }else {
                grzp_i1.setVisibility(View.VISIBLE);
                grzp_i2.setVisibility(View.VISIBLE);
//                grzp_i3.setVisibility(View.VISIBLE);
                grzp_i4.setVisibility(View.VISIBLE);
                grzp_im.setVisibility(View.VISIBLE);
                grzp_n.setVisibility(View.INVISIBLE);
                Glide.with(APP.context)
                        .load(GRZP_URL.get(0))
                        .into(grzp_i1);
                Glide.with(APP.context)
                        .load(GRZP_URL.get(1))
                        .into(grzp_i2);
                /*Glide.with(APP.context)
                        .load(GRZP_URL.get(2))
                        .into(grzp_i3);*/
                Glide.with(APP.context)
                        .load(GRZP_URL.get(2))
                        .into(grzp_i4);
                int temp = GRZP_NUM - 3;
                grzp_im.setText("+" + String.valueOf(temp));
            }
        }
    }

    private void initZPJView() {
        if (ZPJ_NUM == 0){
            zpj_i1.setVisibility(View.GONE);
            zpj_i2.setVisibility(View.GONE);
//            zpj_i3.setVisibility(View.GONE);
            zpj_i4.setVisibility(View.GONE);
            zpj_im.setVisibility(View.GONE);
            zpj_n.setVisibility(View.VISIBLE);
        }else {
            if(ZPJ_NUM <= 3){
                switch (ZPJ_NUM){
                    /*case 4:
                        zpj_i1.setVisibility(View.VISIBLE);
                        zpj_i2.setVisibility(View.VISIBLE);
                        zpj_i3.setVisibility(View.VISIBLE);
                        zpj_i4.setVisibility(View.VISIBLE);
                        zpj_im.setVisibility(View.INVISIBLE);
                        zpj_n.setVisibility(View.INVISIBLE);
                        Glide.with(APP.context)
                                .load(ZPJ_URL.get(0))
                                .into(zpj_i1);
                        Glide.with(APP.context)
                                .load(ZPJ_URL.get(1))
                                .into(zpj_i2);
                        Glide.with(APP.context)
                                .load(ZPJ_URL.get(2))
                                .into(zpj_i3);
                        Glide.with(APP.context)
                                .load(ZPJ_URL.get(3))
                                .into(zpj_i4);
                        break;*/
                    case 3:
                        zpj_i1.setVisibility(View.VISIBLE);
                        zpj_i2.setVisibility(View.VISIBLE);
//                        zpj_i3.setVisibility(View.VISIBLE);
                        zpj_i4.setVisibility(View.VISIBLE);
                        zpj_im.setVisibility(View.INVISIBLE);
                        zpj_n.setVisibility(View.INVISIBLE);
                        Glide.with(APP.context)
                                .load(ZPJ_URL.get(0))
                                .into(zpj_i1);
                        Glide.with(APP.context)
                                .load(ZPJ_URL.get(1))
                                .into(zpj_i2);
                        Glide.with(APP.context)
                                .load(ZPJ_URL.get(2))
                                .into(zpj_i4);
                        break;
                    case 2:
                        zpj_i1.setVisibility(View.VISIBLE);
                        zpj_i2.setVisibility(View.VISIBLE);
//                        zpj_i3.setVisibility(View.INVISIBLE);
                        zpj_i4.setVisibility(View.GONE);
                        zpj_im.setVisibility(View.INVISIBLE);
                        zpj_n.setVisibility(View.INVISIBLE);
                        Glide.with(APP.context)
                                .load(ZPJ_URL.get(0))
                                .into(zpj_i1);
                        Glide.with(APP.context)
                                .load(ZPJ_URL.get(1))
                                .into(zpj_i2);
                        break;
                    case 1:
                        zpj_i1.setVisibility(View.VISIBLE);
                        zpj_i2.setVisibility(View.GONE);
//                        zpj_i3.setVisibility(View.INVISIBLE);
                        zpj_i4.setVisibility(View.GONE);
                        zpj_im.setVisibility(View.INVISIBLE);
                        zpj_n.setVisibility(View.INVISIBLE);
                        Glide.with(APP.context)
                                .load(ZPJ_URL.get(0))
                                .into(zpj_i1);
                        break;
                }
            }else{
                zpj_i1.setVisibility(View.VISIBLE);
                zpj_i2.setVisibility(View.VISIBLE);
//                zpj_i3.setVisibility(View.VISIBLE);
                zpj_i4.setVisibility(View.VISIBLE);
                zpj_im.setVisibility(View.VISIBLE);
                zpj_n.setVisibility(View.INVISIBLE);
                Glide.with(APP.context)
                        .load(ZPJ_URL.get(0))
                        .into(zpj_i1);
                Glide.with(APP.context)
                        .load(ZPJ_URL.get(1))
                        .into(zpj_i2);
                /*Glide.with(APP.context)
                        .load(ZPJ_URL.get(2))
                        .into(zpj_i3);*/
                Glide.with(APP.context)
                        .load(ZPJ_URL.get(2))
                        .into(zpj_i4);
                int temp = ZPJ_NUM- 3;
                zpj_im.setText("+" + String.valueOf(temp));
            }
        }
    }

    @Override
    public void requestFinish(String result, String requestUrl) throws JSONException {
        Message message = new Message();
        if (requestUrl.equals(CommonUrl.otherUserInfo)){
            JSONObject object = new JSONObject(result);
            Log.d("PPPPPPPPPPPPPPPPP",object.toString());
            int code = Integer.valueOf(object.getString("code"));
            if (code == StatusCode.RECIEVE_VISIT_SUCCESS) {
                JSONObject object1 = object.getJSONObject("contents");
                JSONObject object2 = object1.getJSONObject("user_info");
                name = object2.getString("ualais");
                sign = object2.getString("usign");
                local = object2.getString("ulocation");
                himage = object2.getString("uimage");
                following = object2.getInt("ulikeN");
                follower = object2.getInt("ulikedN");
                yuepai = object2.getInt("uapN");
                followor = object1.getBoolean("follow");
                sex = String.valueOf(object2.get("usex"));

                message.what = StatusCode.RECIEVE_VISIT_SUCCESS;
                myHandler.sendMessage(message);
            }else if(code == StatusCode.RECIEVE_VISIT_REJECT){
                message.what = StatusCode.RECIEVE_VISIT_REJECT;
                myHandler.sendMessage(message);
            }
        }else if(requestUrl.equals(CommonUrl.getFollowInfo)){
            JSONObject object = new JSONObject(result);
            int code = Integer.valueOf(object.getString("code"));
            if (code == StatusCode.REQUEST_FOLLOW_SUCCESS){
                message.what = StatusCode.REQUEST_FOLLOW_SUCCESS;
                myHandler.sendMessage(message);
            }
            if(code == StatusCode.REQUEST_CANCEL_SUCCESS){
                message.what = StatusCode.REQUEST_CANCEL_SUCCESS;
                myHandler.sendMessage(message);
            }
        }else if(requestUrl.equals(CommonUrl.userImage)){
            JSONObject jsonObject = new JSONObject(result);
            int code = Integer.valueOf(jsonObject.getString("code"));
            if(code == StatusCode.REQUEST_USER_GRZP){
                //个人照片
                JSONArray jsonArray = jsonObject.getJSONArray("contents");
                Log.d("AAAAAAAAAAAAAAAAA", jsonArray.toString());
                GRZP_NUM = jsonArray.length();
                Log.d("AAAAAAAAAAAAAAAAA",String.valueOf(GRZP_NUM));
                if(GRZP_NUM != 0){
                    for(int i=0; i < GRZP_NUM; i++){
                        GRZP_URL.add(jsonArray.getString(i));
                    }
                }
                message.what = GRZP;
                myHandler.sendMessage(message);
            }else if (code == StatusCode.REQUEST_USER_ZPJ){
                //作品集
                JSONArray jsonArray = jsonObject.getJSONArray("contents");
                Log.d("BBBBBBBBBBBBBBBBB",jsonArray.toString());
                ZPJ_NUM = jsonArray.length();
                Log.d("BBBBBBBBBBBBBBB", String.valueOf(ZPJ_NUM));
                if(ZPJ_NUM != 0){
                    for(int i = 0; i < ZPJ_NUM; i++){
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        JSONArray jsonArray1 = jsonObject1.getJSONArray("UCimg");
                        Log.d("BBBBBBBBBBBB",jsonArray1.get(0).toString());
                        ZPJ_URL.add(jsonArray1.get(0).toString());
                    }
                }
                message.what = ZPJ;
                myHandler.sendMessage(message);
            }
        }
    }

    private Handler myHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case StatusCode.RECIEVE_VISIT_SUCCESS:
                    initView();
                    break;
                case StatusCode.RECIEVE_VISIT_REJECT:
                    CommonUtils.getUtilInstance().showToast(APP.context, "加载失败");
                    break;
                case 1000:
                    initGRZPView();
                    break;
                case 2000:
                    initZPJView();
                    break;
                case StatusCode.REQUEST_FOLLOW_SUCCESS:
                    editinfo.setText("已关注");
                    CommonUtils.getUtilInstance().showToast(APP.context, "已关注");
                    initReq();
                    break;
                case StatusCode.REQUEST_CANCEL_SUCCESS:
                    editinfo.setText("关注");
                    CommonUtils.getUtilInstance().showToast(APP.context, "已取消关注");
                    initReq();
                    break;
            }
        }
    };

    @Override
    public void exception(IOException e, String requestUrl) {

    }
}
