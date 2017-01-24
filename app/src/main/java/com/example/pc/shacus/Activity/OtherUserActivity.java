package com.example.pc.shacus.Activity;

import android.app.Activity;
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

import io.rong.imkit.RongIM;

/**
 * Created by 启凡 on 2016/9/3.
 */
public class OtherUserActivity extends AppCompatActivity implements  NetworkCallbackInterface.NetRequestIterface{


   // private ListView listView;
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
    String otherId="1";
    String myid = null;
    String otherid = null;
    String authkey =null;
    Map map = new HashMap<>();
    NetRequest netRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_otheruser);

        Intent intent = getIntent();
        type = intent.getStringExtra("id");
        otherId=type;
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
        UserModel content = loginModel.getUserModel();
        myid = content.getId();
        authkey = content.getAuth_key();

       /* Log.d("wwwww", loginModel.toString());

         Log.d("wwwwww","hkl");
          content = loginModel.getUserModel();
            if(loginModel ==null){
          Log.d("sssssssss", content.toString());}
        String userId = content.getId();
        String authkey = content.getAuth_key();*/
        actvt=this;

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
                intent1.putExtra("user","other");
                intent1.putExtra("id",otherId);
//                intent1.putExtra("authkey",otherauthkey);
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
                intent2.putExtra("user","other");
                intent2.putExtra("id",otherId);
//                intent2.putExtra("authkey",otherauthkey);
                intent2.setClass(OtherUserActivity.this, FollowActivity.class);
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

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                button5.setImageResource(R.drawable.praise_after);
                request1=new NetRequest(OtherUserActivity.this,OtherUserActivity.this);
                Map map1=new HashMap();
                map1.put("uid", myid);
                map1.put("authkey", authkey);
                map1.put("followerid", otherId);

                if(followor == false){
                    //未关注
                    map1.put("type",StatusCode.REQUEST_FOLLOW_USER);
                }else{
                    //已关注
                    map1.put("type",StatusCode.REQUEST_CANCEL_FOLLOWING);
                }
                request1.httpRequest(map1, CommonUrl.getFollowInfo);
//                CommonUtils.getUtilInstance().showToast(APP.context, "已关注该用户");
            }
        });

        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //私信
               // Intent intent6 = new Intent(OtherUserActivity.this, ConversationActivity.class);
                //intent6.putExtra("uid",otherId);
                //startActivity(intent6);
                //RongIM.getInstance().startPrivateChat(OtherUserActivity.this, otherId, "title");
                RongIM.getInstance().startPrivateChat(actvt, otherId, "title");
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

    private void initInfo(){
        map.put("uid", myid);
        map.put("authkey", authkey);
        map.put("seeid", otherId);
        map.put("type",StatusCode.REQUEST_OTHERUSER_INFO);
        requestOthers.httpRequest(map, CommonUrl.otherUserInfo);
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

                    ownerName.setText(otherName);
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
        //在这里接收所有网络请求
        Message msg = new Message();
        if(requestUrl.equals(CommonUrl.otherUserInfo)) {
            JSONObject object = new JSONObject(result);
            int code = Integer.valueOf(object.getString("code"));
            //JSONArray content=object.getJSONArray("contents");
            Log.d("ccccc", "Cdo");
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
