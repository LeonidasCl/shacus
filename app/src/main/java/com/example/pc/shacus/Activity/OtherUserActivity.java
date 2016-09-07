package com.example.pc.shacus.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 启凡 on 2016/9/3.
 */
public class OtherUserActivity extends AppCompatActivity implements  NetworkCallbackInterface.NetRequestIterface{


    private ListView listView;
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
    TextView ownerName;//用户姓名
    TextView likeButton;
    TextView fansButton;
    TextView workButton;
    TextView projectBuuton;
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

    ACache aCache;
    LoginDataModel loginModel;
    private String type = null;
    String otherId="1";




    //String[] data = {"我的学习", "订单", "我的地图", "优惠券", "分享给好友得积分", "设置"};
    //  int[] image1 = {R.drawable.a, R.drawable.a, R.drawable.a, R.drawable.a, R.drawable.a, R.drawable.a,};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_otheruser);
        //接受上一个界面传参
        Intent intent = getIntent();
        type = intent.getStringExtra("id");
        otherId=type;
       // listView = (ListView)findViewById(R.id.listView);

        //初始化
        button1 = (LinearLayout) findViewById(R.id.button1);
        button2 = (LinearLayout)findViewById(R.id.button2);
        button3 = (LinearLayout)findViewById(R.id.button3);
        button4 = (LinearLayout)findViewById(R.id.button4);
        button5 = (ImageView) findViewById(R.id.button5);
        button6 = (ImageView)findViewById(R.id.button6);
        text1 = (TextView) findViewById(R.id.text1);
        text2 = (TextView) findViewById(R.id.text2);
        retrunButton=(ImageButton)findViewById(R.id.returnbutton);
        menuButton=(ImageButton)findViewById(R.id.menuButton);
        ownerName=(TextView)findViewById(R.id.ownerName);
        image3 = (CircleImageView) findViewById(R.id.image3);
        likeButton=(TextView)findViewById(R.id.likeButton);
        workButton=(TextView)findViewById(R.id.workButton);
        projectBuuton=(TextView)findViewById(R.id.projectButton);
        fansButton=(TextView)findViewById(R.id.fansButton);

        aCache=ACache.get(OtherUserActivity.this);
        Log.d("achach",aCache.toString());
        requestOthers=new NetRequest(OtherUserActivity.this,OtherUserActivity.this);
        loginModel = (LoginDataModel)aCache.getAsObject("loginModel");
        Log.d("wwwww", loginModel.toString());

        UserModel content=null;

         Log.d("wwwwww","hkl");
          content = loginModel.getUserModel();
            if(loginModel ==null){
          Log.d("sssssssss", content.toString());}
        String userId = content.getId();
        String authkey = content.getAuth_key();

        //发送网络请求
        Map map=new HashMap();
        map.put("uid", userId);
        map.put("authkey", authkey);
        map.put("seeid", otherId);
        map.put("type",StatusCode.REQUEST_OTHERUSER_INFO);
        requestOthers.httpRequest(map, CommonUrl.otherUserInfo);




//        SimpleAdapter adapter1 = new SimpleAdapter(this, init(), R.layout.item_user_listview_layout, new String[]{"item1",
//                "item2", "item3"}, new int[]{R.id.image1, R.id.content, R.id.image2});
//        listView.setAdapter(adapter1);



    }
    private Handler myHandler=new Handler(){

            @Override
            public void handleMessage(Message msg){
                if (msg.what==222){
                    //成功返回信息
                    initView();
                }
                if(msg.what==100){
                   //发送关注请求
                    button5.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            button5.setImageDrawable(getResources().getDrawable(R.drawable.praise_after));
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
                            initView();

                        }
                    });



                }
                if(msg.what==101){
                    //发送取消关注请求
                    button5.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            button5.setImageDrawable(getResources().getDrawable(R.drawable.praise));
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
                            initView();
                        }
                    });


                }
                if (msg.what==223){
                    //访问请求拒绝
                    CommonUtils.getUtilInstance().showToast(APP.context, "获取信息失败");
                }
                if (msg.what==404){
                    //网络错误
                    CommonUtils.getUtilInstance().showToast(APP.context, "网络请求超时，请重试");
                }
            }
        };

//        private List<Map<String, Object>> init() {
//        List<Map<String, Object>> lst = new ArrayList<>();
//        for (int i = 0; i < 5; i++) {
//            Map<String, Object> item = new HashMap<>();
//          //  item.put("item1", image1[i]);
//          //  item.put("item2", data[i]);
//            item.put("item3", R.drawable.huodong_loading);
//            lst.add(item);
//        }
//        return lst;
//    }
    private void showPopupMenu(View view) {
        // View当前PopupMenu显示的相对View的位置
        PopupMenu popupMenu = new PopupMenu(OtherUserActivity.this, view);
        // menu布局
        popupMenu.getMenuInflater().inflate(R.menu.owner_menu, popupMenu.getMenu());
        // menu的item点击事件
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(OtherUserActivity.this, item.getTitle(), Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        // PopupMenu关闭事件
        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
                Toast.makeText(OtherUserActivity.this, "关闭PopupMenu", Toast.LENGTH_SHORT).show();
            }
        });
        popupMenu.show();
    }

    private void initView(){
                    //接收数据后初始化
                    ownerName.setText(otherName);
                    text1.setText(location);
                    text2.setText(sign);
                    likeButton.setText(Integer.toString(follow));
                    fansButton.setText(Integer.toString(following));
                    workButton.setText(Integer.toString(photo));
                    projectBuuton.setText(Integer.toString(course));
        if (murl!=null){
                Glide.with(this)
                .load(murl)
                .placeholder(R.drawable.holder)
                .error(R.drawable.holder)
                .into(image3);
        }
                    // image3.setImageURI(http://img5.imgtn.bdimg.com/it/u=1268523085,477716560&fm=21&gp=0.jpg);

                    //是否关注
                    if (followor == false) {
                        button5.setImageDrawable(getResources().getDrawable(R.drawable.praise));
                        Message msg = new Message();
                        msg.what = 100;
                        myHandler.sendMessage(msg);

                    } else {
                        button5.setImageDrawable(getResources().getDrawable(R.drawable.praise_after));
                        Message msg = new Message();
                        msg.what = 101;
                        myHandler.sendMessage(msg);

                    }


        retrunButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //返回上一级
                finish();
            }
        });
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //出现菜单
                showPopupMenu(menuButton);

            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //他喜欢的
                Intent intent1 = new Intent();
                intent1.putExtra("activity", "following");
                intent1.putExtra("user", "other");
                intent1.putExtra("user", otherId);
                intent1.setClass(OtherUserActivity.this, FollowActivity.class);
                startActivity(intent1);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //他的粉丝
                Intent intent2 = new Intent();
                intent2.putExtra("activity", "follower");
                intent2.putExtra("user", "other");
                intent2.putExtra("uuid", otherId);
                intent2.setClass(OtherUserActivity.this, FollowActivity.class);
                startActivity(intent2);
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //他的作品
                Intent intent3 = new Intent(OtherUserActivity.this, FavoritemActivity.class);
                startActivity(intent3);
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            //他的项目
            @Override
            public void onClick(View v) {
                //他的项目
                Intent intent4 = new Intent(OtherUserActivity.this, SignInActivity.class);
                startActivity(intent4);

            }
        });


//        button5.setOnTouchListener(new View.OnTouchListener() {
//
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                    //重新设置图片
//                    ((ImageButton) v).setImageDrawable(getResources().getDrawable(R.drawable.a));
//                } else if (event.getAction() == MotionEvent.ACTION_UP) {
//                    //再修改为抬正常图片
//                    ((ImageButton) v).setImageDrawable(getResources().getDrawable(R.drawable.bg_circle_pressed1));
//                }
//                return false;
//            }
//        });

        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //私信
                Intent intent6 = new Intent(OtherUserActivity.this, ChatActivity.class);
                startActivity(intent6);
            }
        });
        image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //大图
            }
        });

    }



    @Override
    public void requestFinish(String result, String requestUrl) throws JSONException {
        //在这里接收所有网络请求
        if(requestUrl.equals(CommonUrl.otherUserInfo)) {
            JSONObject object = new JSONObject(result);
            int code = Integer.valueOf(object.getString("code"));
            Message msg = new Message();
            //JSONArray content=object.getJSONArray("contents");
            Log.d("ccccc", "Cdo");
            if (code == StatusCode.RECIEVE_VISIT_SUCCESS) {

                // String otherInfoJSON=content.getJSONObject(1).toString();
                //String othername=otherInfoJSON.

                JSONObject object1 = object.getJSONObject("contents");
                Log.d("sssssssssss", object.toString());
                Log.d("sssssssssss", object1.toString());
                JSONObject object2 = object1.getJSONObject("user_info");
                Log.d("sssssssssss", object2.toString());
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
                msg.what=223;
            }

        }
        }



    @Override
    public void exception(IOException e, String requestUrl) {
        Message msg = new Message();
        msg.what=404;
        myHandler.sendMessage(msg);
    }

}
