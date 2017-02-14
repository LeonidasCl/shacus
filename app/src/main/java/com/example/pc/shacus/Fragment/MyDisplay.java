package com.example.pc.shacus.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.pc.shacus.APP;
import com.example.pc.shacus.Activity.FollowActivity;
import com.example.pc.shacus.Activity.OtherUserActivity;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import io.rong.imkit.RongIM;

/**
 * Created by cuicui on 2017/1/23.
 */
public class MyDisplay extends Fragment implements NetworkCallbackInterface.NetRequestIterface{
    private View view;
    private SimpleAdapter adapter;
    private CircleImageView image3;//用户头像


    private NetRequest requestOthers;
    private NetRequest request1;
    private NetRequest request2;
    LinearLayout button1;
    LinearLayout button2;
    LinearLayout button3;
    LinearLayout button4;
    ImageView button5;
    ImageView button6;
    TextView text1;//地点
    TextView text2;//签名
    ImageButton retrunButton;
    ImageButton menuButton;
    TextView likeButton;
    TextView fansButton;
    TextView workButton;
    TextView projectBuuton;

    private Handler myHandler1;
    String otherName;
    String sign;
    int following ;
    int follow ;
    int photo;
    String location;
    int course;
    String murl;
    Boolean followor ;
    String newName;
    //单击大图变量
    Bitmap bitmap = null;
    float scaleWidth;
    float scaleHeight;
    ContextMenu menu;

    private Activity actvt;
    ACache aCache;
    LoginDataModel loginModel;
    private String type = null;
    Map map = new HashMap<>();
    NetRequest netRequest;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.mydisplay, container, false);
        this.view=view;
        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        button1 = (LinearLayout) view.findViewById(R.id.button1);
        button2 = (LinearLayout)view.findViewById(R.id.button2);
        button3 = (LinearLayout)view.findViewById(R.id.button3);
        button4 = (LinearLayout)view.findViewById(R.id.button4);
        button5 = (ImageView) view.findViewById(R.id.button5);
        button6 = (ImageView)view.findViewById(R.id.button6);
        text1 = (TextView) view.findViewById(R.id.text1);
        text2 = (TextView) view.findViewById(R.id.text2);
        retrunButton=(ImageButton)view.findViewById(R.id.returnbutton);
        menuButton=(ImageButton)view.findViewById(R.id.menuButton);
        image3 = (CircleImageView) view.findViewById(R.id.image3);
        likeButton=(TextView)view.findViewById(R.id.likeButton);
        workButton=(TextView)view.findViewById(R.id.workButton);
        projectBuuton=(TextView)view.findViewById(R.id.projectButton);
        fansButton=(TextView)view.findViewById(R.id.fansButton);


        netRequest=new NetRequest(MyDisplay.this,MyDisplay.this.getActivity());

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //他喜欢的
                Intent intent1 = new Intent();
                intent1.putExtra("activity", "following");
                intent1.putExtra("user","myself");
                intent1.setClass(APP.context, FollowActivity.class);
                startActivity(intent1);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //他的粉丝
                Intent intent2 = new Intent();
                intent2.putExtra("activity", "follower");
                intent2.putExtra("user","myself");
                intent2.setClass(APP.context, FollowActivity.class);
                startActivity(intent2);
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //他的作品

            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            //他的项目
            @Override
            public void onClick(View v) {
                //他的项目

                Intent intent = new Intent();
                intent.setClass(APP.context,MyDisplayFragment.class);
                startActivity(intent);
            }
        });

        image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //大图
            }
        });

//        SimpleAdapter adapter1 = new SimpleAdapter(this, init(), R.layout.item_user_listview_layout, new String[]{"item1",
//                "item2", "item3"}, new int[]{R.id.image1, R.id.content, R.id.image2});
//        listView.setAdapter(adapter1);

        initInfo();

    }

    private Handler myHandler=new Handler(){

        @Override
        public void handleMessage(Message msg){
            if (msg.what==222){
                initView();
            }
            if(msg.what==100){

                   /* button5.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            button5.setImageResource(R.drawable.praise_after);
                            UserModel content1 = loginModel.getUserModel();
                            String userId = content1.getId();
                            String authkey = content1.getAuth_key();
                            request1=new NetRequest(OtherUserActivity.this,OtherUserActivity.this);



                            Map map1=new HashMap();
                            map1.put("uid", userId);
                            map1.put("authkey", authkey);
                            map1.put("followerid", otherId);
                            map1.put("type",10401);
                            request1.httpRequest(map1, CommonUrl.getFollowInfo);
                            CommonUtils.getUtilInstance().showToast(APP.context, "已关注该用户");
                            initView();
                        }
                    });*/
                button5.setImageResource(R.drawable.praise_after);
                CommonUtils.getUtilInstance().showToast(APP.context, "已关注");
                initInfo();
            }
            if(msg.what==101){

                   /* button5.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            button5.setImageResource(R.drawable.praise);
                            UserModel content1 = loginModel.getUserModel();
                            String userId = content1.getId();
                            String authkey = content1.getAuth_key();
                            request2=new NetRequest(OtherUserActivity.this,OtherUserActivity.this);
                            Map map2=new HashMap();
                            map2.put("uid", userId);
                            map2.put("authkey", authkey);
                            map2.put("followerid", otherId);
                            map2.put("type",10402);
                            request2.httpRequest(map2, CommonUrl.getFollowInfo);
                            CommonUtils.getUtilInstance().showToast(APP.context, "已取消关注");

                            initView();
                        }
                    });*/
                button5.setImageResource(R.drawable.praise);
                CommonUtils.getUtilInstance().showToast(APP.context, "已取消关注");
                initInfo();

            }
            if(msg.what==223){
                CommonUtils.getUtilInstance().showToast(APP.context, "加载失败");
            }
        }
    };

    private void initInfo() {
        ACache aCache=ACache.get(APP.context);
        LoginDataModel model= (LoginDataModel) aCache.getAsObject("loginModel");
        HashMap map=new HashMap();
        map.put("uid",model.getUserModel().getId());
        map.put("authkey",model.getUserModel().getAuth_key());
        map.put("seeid", model.getUserModel().getId());
        map.put("type", StatusCode.REQUEST_OTHERUSER_INFO);
        Log.d("ssssssssssssssssssss", "Cdo");
        netRequest.httpRequest(map,  CommonUrl.otherUserInfo);
    }

    private void initView(){

        text1.setText(location);
        text2.setText(sign);
        likeButton.setText(Integer.toString(follow));
        fansButton.setText(Integer.toString(following));
        workButton.setText(Integer.toString(photo));
        projectBuuton.setText(Integer.toString(course));
        if (murl!=null){
            Glide.with(APP.context)
                    .load(murl)
                    .into(image3);

        }
        // image3.setImageURI(http://img5.imgtn.bdimg.com/it/u=1268523085,477716560&fm=21&gp=0.jpg);
        if (followor == false) {
            button5.setImageResource(R.drawable.heart);
            /*Message msg = new Message();
            msg.what = 100;
            myHandler.sendMessage(msg);*/

        } else {
            button5.setImageResource(R.drawable.likedafter);
            /*Message msg = new Message();
            msg.what = 101;
            myHandler.sendMessage(msg);*/

        }

    }


    @Override
    public void requestFinish(String result, String requestUrl) throws JSONException {
        Message msg = new Message();
        if(requestUrl.equals(CommonUrl.otherUserInfo)) {
            JSONObject object = new JSONObject(result);
            int code = Integer.valueOf(object.getString("code"));
            //JSONArray content=object.getJSONArray("contents");
            Log.d("sssssssssssssssssss", "Cdo");
            if (code == StatusCode.RECIEVE_VISIT_SUCCESS) {

                // String otherInfoJSON=content.getJSONObject(1).toString();
                //String othername=otherInfoJSON.

                JSONObject object1 = object.getJSONObject("contents");
                JSONObject object2 = object1.getJSONObject("user_info");
                otherName = object2.getString("ualais");
                sign = object2.getString("usign");
                following = object2.getInt("ulikedN");
                follow = object2.getInt("ulikeN");
                photo = object2.getInt("uphotoN");
                location = object2.getString("ulocation");
                course = object2.getInt("ucourseN");
                murl = object2.getString("uimage");
                followor = object1.getBoolean("follow");

                msg.what =222;
                myHandler.sendMessage(msg);

            }
            if (code == StatusCode.RECIEVE_VISIT_REJECT) {
                Log.d("aaaaa", "失败");
                msg.what =223;
                myHandler.sendMessage(msg);
            }

        }else if(requestUrl.equals(CommonUrl.getFollowInfo)){
            JSONObject object = new JSONObject(result);
            int code = Integer.valueOf(object.getString("code"));
            if (code == StatusCode.REQUEST_FOLLOW_SUCCESS){
                msg.what = 100;
                myHandler.sendMessage(msg);
            }
            if(code == StatusCode.REQUEST_CANCEL_SUCCESS){
                msg.what = 101;
                myHandler.sendMessage(msg);
            }
        }
    }

    @Override
    public void exception(IOException e, String requestUrl) {

    }
}