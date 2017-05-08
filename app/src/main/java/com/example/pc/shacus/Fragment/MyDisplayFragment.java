package com.example.pc.shacus.Fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.pc.shacus.APP;
import com.example.pc.shacus.Activity.FollowActivity;
import com.example.pc.shacus.Activity.OrdersActivity;
import com.example.pc.shacus.Activity.PersonalInfoActivity;
import com.example.pc.shacus.Activity.PersonalInfoEditActivity;
import com.example.pc.shacus.Activity.PhotoselfDetailActivity;
import com.example.pc.shacus.Activity.PhotosetOverviewActivity;
import com.example.pc.shacus.Activity.SettingsActivity;
import com.example.pc.shacus.Data.Cache.ACache;
import com.example.pc.shacus.Data.Model.LoginDataModel;
import com.example.pc.shacus.Network.NetRequest;
import com.example.pc.shacus.Network.NetworkCallbackInterface;
import com.example.pc.shacus.Network.StatusCode;
import com.example.pc.shacus.R;
import com.example.pc.shacus.Util.CommonUrl;
import com.example.pc.shacus.Util.CommonUtils;
import com.example.pc.shacus.Util.UploadPhotoUtil;
import com.example.pc.shacus.View.CircleImageView;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by cuicui on 2017/2/4.
 */
public class MyDisplayFragment extends Fragment implements View.OnClickListener,NetworkCallbackInterface.NetRequestIterface{


    private View view;
    private CircleImageView headimage;

    LinearLayout yuepaibtn;
    LinearLayout followingbtn;
    LinearLayout followerbtn;
//    ImageView bigimageview;
    ImageButton settingbtn;

    TextView username;
    TextView usersign;
    TextView userlocal;

    TextView myyuepainum;
    TextView followingnum;
    TextView followernum;
    TextView take_picture;
    TextView select_local_picture;
    TextView age;
//    Button editinfo;

    FrameLayout edit_photo_fullscreen_layout;
    RelativeLayout display_big_image_layout;
    RelativeLayout edit_photo_outer_layout;

    String takePictureUrl,upToken,imageFileName;
    private final int NONE=0,TAKE_PICTURE=1,LOCAL_PICTURE=2,UPLOAD_TAKE_PICTURE=4,SAVE_THEME_IMAGE=5;
    private boolean headImageChanged=false,imagefinish=true;
    private ProgressDialog progressDlg;


    NetRequest netRequest;

    String name = null;
    String sign = null;
    String local = null;
    String himage = null;
    String bimage = null;
    int following = 0;
    int follower = 0;
    int yuepai = 0;
    int GRZP = 100;
    int GRZP_NUM = 0;
    ArrayList<String> GRZP_URL= new ArrayList();
    ImageView grzp_i1;
    ImageView grzp_i2;
//    ImageView grzp_i3;
    ImageView grzp_i4;
    TextView grzp_im;
    TextView grzp_n;

    int ZPJ = 200;
    int ZPJ_NUM = 0;
    ArrayList<String> ZPJ_URL = new ArrayList<>();
    ImageView zpj_i1;
    ImageView zpj_i2;
//    ImageView zpj_i3;
    ImageView zpj_i4;
    TextView zpj_im;
    TextView zpj_n;



    int addTakePicCount=1;
    private Intent intent;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savadInstanceState){
        View view = inflater.inflate(R.layout.selfdisplay,container,false);
        this.view = view;
        return view;
    }

    @Override
    public void onResume(){

        super.onResume();
//        bigimageview = (ImageView) view.findViewById(R.id.bigimageView);
        headimage = (CircleImageView)view.findViewById(R.id.imageData_UserImage);
        username = (TextView) view.findViewById(R.id.textData_UserName);
        usersign = (TextView) view.findViewById(R.id.textData_UserSign);
        userlocal = (TextView) view.findViewById(R.id.textData_UserLocal);
        yuepaibtn = (LinearLayout) view.findViewById(R.id.myYuepiaBtn);
        followingbtn = (LinearLayout) view.findViewById(R.id.followingBtn);
        followerbtn = (LinearLayout) view.findViewById(R.id.followerBtn);
        myyuepainum = (TextView) view.findViewById(R.id.myYuepaiNum);
        followingnum = (TextView) view.findViewById(R.id.followingNum);
        followernum = (TextView) view.findViewById(R.id.followerNum);
        settingbtn = (ImageButton) view.findViewById(R.id.settingbtn);
        age = (TextView) view.findViewById(R.id.textData_UserAge);
//        editinfo = (Button) view.findViewById(R.id.edit_info);

        grzp_i1 = (ImageView) view.findViewById(R.id.grzp_i1);
        grzp_i2 = (ImageView) view.findViewById(R.id.grzp_i2);
//        grzp_i3 = (ImageView) view.findViewById(R.id.grzp_i3);
        grzp_i4 = (ImageView) view.findViewById(R.id.grzp_i4);
        grzp_im = (TextView) view.findViewById(R.id.grzp_im);
        grzp_n = (TextView) view.findViewById(R.id.grzp_n);

        zpj_i1 = (ImageView) view.findViewById(R.id.zpj_i1);
        zpj_i2 = (ImageView) view.findViewById(R.id.zpj_i2);
//        zpj_i3 = (ImageView) view.findViewById(R.id.zpj_i3);
        zpj_i4 = (ImageView) view.findViewById(R.id.zpj_i4);
        zpj_im = (TextView) view.findViewById(R.id.zpj_im);
        zpj_n = (TextView) view.findViewById(R.id.zpj_n);

        take_picture=(TextView)view.findViewById(R.id.take_picture);
        display_big_image_layout = (RelativeLayout) view.findViewById(R.id.display_big_image_layout);
        select_local_picture=(TextView) view.findViewById(R.id.select_local_picture);
        edit_photo_fullscreen_layout=(FrameLayout)view.findViewById(R.id.edit_photo_fullscreen_layout);
        edit_photo_outer_layout = (RelativeLayout) view.findViewById(R.id.edit_photo_outer_layout);
        edit_photo_fullscreen_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_photo_fullscreen_layout.setVisibility(View.GONE);
            }
        });
        TextView cancelEditPhoto=(TextView)edit_photo_outer_layout.findViewById(R.id.cancel_upload);
        cancelEditPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_photo_fullscreen_layout.setVisibility(View.GONE);
            }
        });

        RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.relativeLayout);
        relativeLayout.setBackgroundColor(00000000);
        ImageButton imageButton = (ImageButton) view.findViewById(R.id.backbtn);
        imageButton.setVisibility(View.INVISIBLE);
        TextView textView = (TextView) view.findViewById(R.id.othername);
        textView.setVisibility(View.INVISIBLE);
        LinearLayout layout_otheruser_chat = (LinearLayout) view.findViewById(R.id.layout_otheruser_chat);
        layout_otheruser_chat.setVisibility(View.GONE);

        netRequest = new NetRequest(MyDisplayFragment.this,MyDisplayFragment.this.getActivity());

        yuepaibtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //我的约拍
                Intent intent = new Intent();
                intent.putExtra("page", "0");
                intent.setClass(APP.context,OrdersActivity.class);
                startActivity(intent);
            }
        });

        followingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //我的关注
                Intent intent = new Intent();
                intent.putExtra("activity", "following");
                intent.putExtra("user", "myself");
                intent.setClass(APP.context, FollowActivity.class);
                startActivity(intent);
            }
        });

        followerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //我的粉丝
                Intent intent = new Intent();
                intent.putExtra("activity", "follower");
                intent.putExtra("user", "myself");
                intent.setClass(APP.context, FollowActivity.class);
                startActivity(intent);

            }
        });

        settingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //设置
                Intent intent = new Intent();
                intent.setClass(APP.context, SettingsActivity.class);
                startActivity(intent);
            }
        });

        /*editinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //编辑个人资料
                Intent intent = new Intent();
                intent.setClass(APP.context, PersonalInfoEditActivity.class);
                startActivity(intent);
            }
        });*/

        take_picture.setOnClickListener(this);
        select_local_picture.setOnClickListener(this);

        final TextView changetype = (TextView) view.findViewById(R.id.changetype);
        /*bigimageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changetype.setText("更换封面");
                edit_photo_fullscreen_layout.setVisibility(View.VISIBLE);
                Animation get_photo_layout_in_from_down = AnimationUtils.loadAnimation(getActivity(), R.anim.search_layout_in_from_down);
                edit_photo_outer_layout.startAnimation(get_photo_layout_in_from_down);
            }
        });*/

        headimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                display_big_image_layout.setVisibility(View.VISIBLE);
                ImageView image = (ImageView) view.findViewById(R.id.fulldisplay_headimage);
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

        LinearLayout grzp = (LinearLayout) view.findViewById(R.id.grzp);
        LinearLayout zpj = (LinearLayout) view.findViewById(R.id.zpj);



        ACache aCache = ACache.get(APP.context);
        final LoginDataModel model = (LoginDataModel) aCache.getAsObject("loginModel");
        grzp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),PhotoselfDetailActivity.class);
                intent.putExtra("uid",model.getUserModel().getId());
                startActivity(intent);
            }
        });


        zpj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),PhotosetOverviewActivity.class);
                intent.putExtra("uid",model.getUserModel().getId());
                startActivity(intent);
            }
        });


        initReq();
        initGRZP();
        initZPJ();
    }

    //请求基本信息
    private void initReq() {
        ACache aCache = ACache.get(APP.context);
        LoginDataModel model = (LoginDataModel) aCache.getAsObject("loginModel");
        HashMap map = new HashMap();
        map.put("uid", model.getUserModel().getId());
        map.put("authkey", model.getUserModel().getAuth_key());
        map.put("seeid", model.getUserModel().getId());
        map.put("type", StatusCode.REQUEST_OTHERUSER_INFO);
        netRequest.httpRequest(map, CommonUrl.otherUserInfo);
    }

    //请求个人照片
    private void initGRZP(){
        NetRequest netRequestG = new NetRequest(MyDisplayFragment.this,MyDisplayFragment.this.getActivity());
        ACache aCache = ACache.get(APP.context);
        LoginDataModel model = (LoginDataModel) aCache.getAsObject("loginModel");
        HashMap map = new HashMap();
        map.put("uid", model.getUserModel().getId());
        map.put("authkey", model.getUserModel().getAuth_key());
        map.put("type", StatusCode.REQUEST_USER_GRZP);
        netRequestG.httpRequest(map, CommonUrl.userImage);
    }

    //请求作品集
    private void initZPJ(){
        NetRequest netRequestZ = new NetRequest(MyDisplayFragment.this,MyDisplayFragment.this.getActivity());
        ACache aCache = ACache.get(APP.context);
        LoginDataModel model = (LoginDataModel) aCache.getAsObject("loginModel");
        HashMap map = new HashMap();
        map.put("uid", model.getUserModel().getId());
        map.put("authkey", model.getUserModel().getAuth_key());
        map.put("type", StatusCode.REQUEST_USER_ZPJ);
        netRequestZ.httpRequest(map, CommonUrl.userImage);
    }


    private void initView() {
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
    }

    private Handler myHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case StatusCode.RECIEVE_VISIT_SUCCESS:
                    initView();
                    break;
                case 100:
                    initGRZPView();
                    break;
                case 200:
                    initZPJView();
                    break;
                case SAVE_THEME_IMAGE://第一次接到请求
                    progressDlg.dismiss();
                    HashMap map=new HashMap();
                    ACache aCache = ACache.get(APP.context);
                    LoginDataModel model = (LoginDataModel) aCache.getAsObject("loginModel");
                    map.put("type",StatusCode.REQUEST_SAVE_CHANGED_HEAD_IMAGE);
                    map.put("authkey", model.getUserModel().getAuth_key());
                    ArrayList<String>temp=new ArrayList<>();
                    temp.add("\""+imageFileName+"\"");
                    map.put("image",temp);
                    map.put("uid", model.getUserModel().getId());
                    netRequest.httpRequest(map, CommonUrl.settingChangeNetUrl);
                    progressDlg=ProgressDialog.show(getActivity(), "上传头像", "正在保存头像", true, true, new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialogInterface) {
                            //上传完图片后取消了保存
                        }
                    });
                    break;
                case UPLOAD_TAKE_PICTURE://第一次发送请求
                    UploadManager uploadmgr=new UploadManager();
                    File data=new File(takePictureUrl);
                    String key=imageFileName;
                    String token=upToken;
                    //mProgress.setVisibility(View.VISIBLE);
                    progressDlg= ProgressDialog.show(getActivity(), "上传头像", "正在上传图片", true, true, new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialogInterface) {
                            //取消了上传
                        }
                    });
                    progressDlg.setMax(101);
                    uploadmgr.put(data, key, token, new UpCompletionHandler(){
                        @Override
                        public void complete(String key, ResponseInfo info, JSONObject response) {
                            //完成，发信息给业务服务器
                            new Thread(){
                                public void run(){
                                    Map<String, Object> map=new HashMap<>();
                        /* map.put("themeId",themeId);
                        map.put("imgBody",UploadPhotoUtil.getInstance().getUploadBitmapZoomString(picUrl));
                        map.put("imgType",UploadPhotoUtil.getInstance().getFileType(picUrl));
                        map.put("type",1);*/
                                    Message msg=myHandler.obtainMessage();
                                    msg.obj=map;
                                    msg.what=SAVE_THEME_IMAGE;
                                    myHandler.sendMessage(msg);//要上传的图片包装在msg后变成了消息发到handler
                                }
                            }.start();
                        }
                    },new UploadOptions(null, null, false,
                            new UpProgressHandler(){
                                public void progress(String key, double percent){
                                    //mProgress.setProgress((int)percent*100);
                                    progressDlg.setProgress((int)percent*100);
                                }
                            },null));

                    break;
                case TAKE_PICTURE:
                    //在这里处理，获取拍到的图
                    Bitmap bitmap= UploadPhotoUtil.getInstance()
                            .trasformToZoomPhotoAndLessMemory(takePictureUrl);
                    BitmapDrawable bd=new BitmapDrawable(getResources(),bitmap);
                    Drawable addPicture = bd;
//                    bigimageview.setImageDrawable(addPicture);
                    headImageChanged=true;
                    break;
                case LOCAL_PICTURE:
                    Uri uri=intent.getData();
                    String photo_local_file_path=getPath_above19(getActivity(), uri);
                    if (!(photo_local_file_path.toString().toLowerCase().endsWith("jpg")||photo_local_file_path.toString().toLowerCase().endsWith("png")
                            ||photo_local_file_path.toString().toLowerCase().endsWith("jpeg")||photo_local_file_path.toString().toLowerCase().endsWith("gif"))){
                        CommonUtils.getUtilInstance().showToast(getActivity(),"不支持此格式的上传");
                        break;
                    }
                    Bitmap bitmap2=UploadPhotoUtil.getInstance().trasformToZoomPhotoAndLessMemory(photo_local_file_path);
                    addPicture =new BitmapDrawable(getResources(), bitmap2);
                    takePictureUrl=photo_local_file_path;
//                    bigimageview.setImageDrawable(addPicture);
                    headImageChanged=true;
                    break;

            }
        }
    };

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
                        zpj_i4.setVisibility(View.INVISIBLE);
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
                        zpj_i2.setVisibility(View.INVISIBLE);
//                        zpj_i3.setVisibility(View.INVISIBLE);
                        zpj_i4.setVisibility(View.INVISIBLE);
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
                    /*case 4:
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
                        grzp_i4.setVisibility(View.INVISIBLE);
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
                        grzp_i2.setVisibility(View.INVISIBLE);
//                        grzp_i3.setVisibility(View.INVISIBLE);
                        grzp_i4.setVisibility(View.INVISIBLE);
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

    //更换背景图
    public void changeHeadImage(){
        if(headImageChanged){
            ACache aCache = ACache.get(APP.context);
            LoginDataModel model = (LoginDataModel) aCache.getAsObject("loginModel");
            HashMap map = new HashMap();
            map.put("uid",model.getUserModel().getId());
            map.put("authkey",model.getUserModel().getAuth_key());
            map.put("type", StatusCode.REQUEST_CHANGE_HEAD_IMAGE);
            String []ext=takePictureUrl.split("\\.");
            imageFileName=model.getUserModel().getPhone()+"/"+takePictureUrl.hashCode()+new Random(System.nanoTime()).toString()+ext[ext.length-1];
            ArrayList<String>temp=new ArrayList<>();
            temp.add("\"" + imageFileName + "\"");
            map.put("image",temp);
            netRequest.httpRequest(map, CommonUrl.settingChangeNetUrl);
        }
    }



    @Override
    public void requestFinish(String result, String requestUrl) throws JSONException {

        Message message = new Message();
        if (requestUrl.equals(CommonUrl.otherUserInfo)) {
            JSONObject object = new JSONObject(result);
            Log.d("AAAAAAAAAAAAAAA",object.toString());
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

                message.what = StatusCode.RECIEVE_VISIT_SUCCESS;
                myHandler.sendMessage(message);
            }
        } /*else if (requestUrl.equals(CommonUrl.settingChangeNetUrl)) {
            JSONObject jsonObject = new JSONObject(result);
            String code = jsonObject.getString("code");
            switch (code) {
                case "10514":
                    CommonUtils.getUtilInstance().showToast(getActivity(), "获取上传认证失败");
                    return;
                case "10515":
                    imagefinish = false;
                    JSONArray token = jsonObject.getJSONArray("contents");
                    upToken = token.getString(0);//成功获取口令
                    myHandler.sendEmptyMessage(UPLOAD_TAKE_PICTURE);
                    return;
                case "66666": //66666
                    progressDlg.dismiss();
                    String url = jsonObject.getString("contents");
                    ACache aCache = ACache.get(APP.context);
                    LoginDataModel model = (LoginDataModel) aCache.getAsObject("loginModel");
                    model.getUserModel().setHeadImage(url);
                    imagefinish = true;
                    break;
            }
        }*/
        else if(requestUrl.equals(CommonUrl.userImage)){
            JSONObject jsonObject = new JSONObject(result);
            int code = Integer.valueOf(jsonObject.getString("code"));
            if(code == StatusCode.REQUEST_USER_GRZP){
                //个人照片
                JSONArray jsonArray = jsonObject.getJSONArray("contents");
                GRZP_NUM = jsonArray.length();
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
                ZPJ_NUM = jsonArray.length();
                if(ZPJ_NUM != 0){
                    for(int i = 0; i < ZPJ_NUM; i++){
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        JSONArray jsonArray1 = jsonObject1.getJSONArray("UCimg");
                        ZPJ_URL.add(jsonArray1.get(0).toString());
                    }
                }
                message.what = ZPJ;
                myHandler.sendMessage(message);
            }
        }
    }

    @Override
    public void exception(IOException e, String requestUrl) {

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.take_picture:
                edit_photo_fullscreen_layout.setVisibility(View.GONE);
                takePictureUrl= APP.photo_path+"picture_take_0" +addTakePicCount+".jpg";
                File file=new File(takePictureUrl);
                intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                startActivityForResult(intent, TAKE_PICTURE);
                addTakePicCount++;
                Log.d("AAAAAAAAAAAAAAAAAAAAA","1");
                break;
            case R.id.select_local_picture:
                edit_photo_fullscreen_layout.setVisibility(View.GONE);
                intent=new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, LOCAL_PICTURE);
                Log.d("1111111111111111111","1");
                break;

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("yyyyyyyyyyyy","a");
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==NONE)
            return;
        //耗时操作传到handler里去处理
        if(requestCode==TAKE_PICTURE){
            myHandler.sendEmptyMessage(TAKE_PICTURE);
            return;
        }
        if(resultCode== Activity.RESULT_OK){
            this.intent=data;
            myHandler.sendEmptyMessage(LOCAL_PICTURE);
        }
    }

    /*************
     */
    //是否为外置存储器
    public static boolean isExternalStorageDocument(Uri uri){
        return"com.android.externalstorage.documents".equals(uri.getAuthority());
    }
    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri){
        return"com.android.providers.downloads.documents".equals(uri.getAuthority());
    }
    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri){
        return"com.android.providers.media.documents".equals(uri.getAuthority());
    }
    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.*/

    public static boolean isGooglePhotosUri(Uri uri){
        return"com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context,Uri uri,String selection,
                                       String[]selectionArgs){
        Cursor cursor=null;
        final String column="_data";
        final String[]projection={column};
        try{
            cursor=context.getContentResolver().query(uri,projection,selection,selectionArgs, null);
            if(cursor!=null&&cursor.moveToFirst()){
                final int index=cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        }finally{
            if(cursor!=null)
                cursor.close();
        }
        return null;
    }
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPath_above19(final Context context,final Uri uri){
        final boolean isKitKat=Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if(isKitKat&& DocumentsContract.isDocumentUri(context, uri)){
            // ExternalStorageProvider
            if(isExternalStorageDocument(uri)){
                final String docId=DocumentsContract.getDocumentId(uri);
                final String[]split=docId.split(":");
                final String type=split[0];
                if("primary".equalsIgnoreCase(type)){
                    return Environment.getExternalStorageDirectory()+"/"+split[1];
                }

            }
            // DownloadsProvider
            else if(isDownloadsDocument(uri)){
                final String id=DocumentsContract.getDocumentId(uri);
                final Uri contentUri= ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context,contentUri,null,null);
            }
            // MediaProvider
            else if(isMediaDocument(uri)){
                final String docId=DocumentsContract.getDocumentId(uri);
                final String[]split=docId.split(":");
                final String type=split[0];
                Uri contentUri=null;
                if("image".equals(type)){
                    contentUri= MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                }else if("video".equals(type)){
                    contentUri=MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                }else if("audio".equals(type)){
                    contentUri=MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection="_id=?";
                final String[]selectionArgs=new String[]{
                        split[1]
                };
                return getDataColumn(context,contentUri,selection,selectionArgs);
            }
        }
        // MediaStore (and general)
        else if("content".equalsIgnoreCase(uri.getScheme())){
            // Return the remote address
            if(isGooglePhotosUri(uri))
                return uri.getLastPathSegment();
            return getDataColumn(context,uri,null,null);
        }
        // File
        else if("file".equalsIgnoreCase(uri.getScheme())){
            return uri.getPath();
        }
        return null;
    }

}
