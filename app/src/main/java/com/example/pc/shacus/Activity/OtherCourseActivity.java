package com.example.pc.shacus.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pc.shacus.APP;
import com.example.pc.shacus.Adapter.CourseListAdapter;
import com.example.pc.shacus.Data.Cache.ACache;
import com.example.pc.shacus.Data.Model.CoursesModel;
import com.example.pc.shacus.Data.Model.LoginDataModel;
import com.example.pc.shacus.Data.Model.UserModel;
import com.example.pc.shacus.Network.NetRequest;
import com.example.pc.shacus.Network.NetworkCallbackInterface;
import com.example.pc.shacus.Network.StatusCode;
import com.example.pc.shacus.R;
import com.example.pc.shacus.Util.CommonUrl;
import com.example.pc.shacus.Util.CommonUtils;
import com.example.pc.shacus.View.Custom.CourseDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 孙启凡 on 2016/9/5.
 */
public class OtherCourseActivity  extends AppCompatActivity implements  NetworkCallbackInterface.NetRequestIterface,View.OnClickListener{


    private ImageButton returnButton;
    private ImageButton imageButton1;
    private TextView title;

    private RecyclerView recyclerView1;
    private CourseListAdapter courseListAdapter1;
    List<CoursesModel> courseItemList1;
    RecyclerView.LayoutManager layoutManager1;
    String tid;

    private ACache aCache ;
    private NetRequest  netRequest;
    private int itemid;
    String userId = null;
    String authkey = null;

    UserModel user = null;
    String url=null;
    private FrameLayout loading4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_othercourse);

        loading4 = (FrameLayout)findViewById(R.id.wait_loading_layout);
        loading4.setVisibility(View.VISIBLE);
        Intent intent = getIntent();
        String type = intent.getStringExtra("tid");
        tid=type;
        returnButton=(ImageButton)findViewById(R.id.returnbutton2);
        title=(TextView)findViewById(R.id.ownerName2);
        imageButton1=(ImageButton)findViewById(R.id.imagebutton);
        aCache = ACache.get(OtherCourseActivity.this);
        LoginDataModel loginModel = (LoginDataModel)aCache.getAsObject("loginModel");
        netRequest = new NetRequest(OtherCourseActivity.this,OtherCourseActivity.this);
        courseItemList1 = new ArrayList<>();
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        imageButton1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

        switch (tid){
            case "1":
                title.setText("风景拍摄");
                break;
            case "2":
                title.setText("人文拍摄");
                break;
            case "3":
                title.setText("后期制作");
                break;
            case "4":
                title.setText("器材选择");
                break;
            case "5":
                title.setText("技巧介绍");
                break;
            case "6":
                title.setText("人像描绘");
                break;
            case "7":
                title.setText("更多课程");
                break;

        }
        recyclerView1 = (RecyclerView)findViewById(R.id.otherrecyclerView);


        Log.d("wwwww", loginModel.toString());


        user = loginModel.getUserModel();
        userId = user.getId();
        authkey = user.getAuth_key();

        if(tid.equals("7")){
            Map map=new HashMap();
            map.put("uid",userId);
            map.put("authkey",authkey);
            map.put("type",StatusCode.REQUEST_COURSE_MORE_COURSES);
            netRequest.httpRequest(map,CommonUrl.courseHomePage);

        }else {
            Map map = new HashMap();
            map.put("uid", userId);
            map.put("authkey", authkey);
            map.put("type", StatusCode.REQUEST_KIND_COURSE);
            map.put("tid", tid);
            netRequest.httpRequest(map, CommonUrl.courseInfo);
        }

    }

private Handler handler=new Handler(){

    @Override
    public void handleMessage(Message msg) {
        if(msg.what==StatusCode.REQUEST_KIND_SECCESS){

            initInfo();
            try {
                Thread.sleep(2 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
      //      sendMessageDelayed(msg,10000);
            loading4.setVisibility(View.GONE);
        }
        if(msg.what==StatusCode.REQUEST_COURSE_MORE_COURSES_SUCCESS){
            initInfo();
            try {
                Thread.sleep(2 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            loading4.setVisibility(View.GONE);
        }
        if (msg.what==StatusCode.REQUEST_KIND_FAIL){
            CommonUtils.getUtilInstance().showToast(APP.context, "请求失败");
        }
        if (msg.what==StatusCode.REQUEST_KIND_NOMORE){

            CommonUtils.getUtilInstance().showToast(APP.context, "没有更多了！");
        }
        if (msg.what==StatusCode.REQUEST_DETAIL_COURSE){
            aCache = ACache.get(OtherCourseActivity.this);
            LoginDataModel loginModel = (LoginDataModel)aCache.getAsObject("loginModel");
            netRequest = new NetRequest(OtherCourseActivity.this,OtherCourseActivity.this);
            user = loginModel.getUserModel();
            userId = user.getId();
            authkey = user.getAuth_key();
            Map map1=new HashMap();
            map1.put("uid",userId);
            map1.put("authkey",authkey);
            map1.put("type",StatusCode.REQUEST_DETAIL_COURSE);
            map1.put("cid", itemid);
            netRequest.httpRequest(map1, CommonUrl.courseInfo);
            Log.d("sssssssssssssss","lllllllllll");
        }
        if (msg.what==StatusCode.REQUEST_DETAIL_SECCESS){
            Intent intent = new Intent(OtherCourseActivity.this,CourseWebViewActivity.class);
            intent.putExtra("detail", url);
            startActivity(intent);

        }
        if (msg.what==StatusCode.REQUSET_DETAIL_INVALID){

            CommonUtils.getUtilInstance().showToast(APP.context, "教程不存在！");
        }
        if (msg.what==StatusCode.REQUEST_DISCOLLECT_COURSE){
            aCache = ACache.get(OtherCourseActivity.this);
            LoginDataModel loginModel = (LoginDataModel)aCache.getAsObject("loginModel");
            netRequest = new NetRequest(OtherCourseActivity.this,OtherCourseActivity.this);
            user = loginModel.getUserModel();
            userId = user.getId();
            authkey = user.getAuth_key();
            Map map1=new HashMap();
            map1.put("uid",userId);
            map1.put("authkey",authkey);
            map1.put("cid", itemid);
            map1.put("type", StatusCode.REQUEST_DISCOLLECT_COURSE);
            netRequest.httpRequest(map1, CommonUrl.courseFav);
            finish();
            Intent intent=new Intent(OtherCourseActivity.this,OtherCourseActivity.class);
            intent.putExtra("tid",tid);
            startActivity(intent);
        }
        if (msg.what==StatusCode.REQUEST_DISCOLLECT_SUCCESS){
            CommonUtils.getUtilInstance().showToast(APP.context, "取消收藏成功");
            finish();
            Intent intent=new Intent(OtherCourseActivity.this,OtherCourseActivity.class);
            startActivity(intent);
        }
        if (msg.what==StatusCode.REQUEST_DISCOLLECT_ALREADY){
            CommonUtils.getUtilInstance().showToast(APP.context, "已取消过收藏");
        }
        if (msg.what==StatusCode.REQUEST_DISCOLLECT_EVER){

           // CommonUtils.getUtilInstance().showToast(APP.context, "你没有收藏过次课程");
        }
        if(msg.what==StatusCode.REQUEST_SEVER_FAIL){

            CommonUtils.getUtilInstance().showToast(APP.context, "服务器错误");
        }
        if(msg.what==StatusCode.REQUEST_COURSE_INVALID){

            CommonUtils.getUtilInstance().showToast(APP.context, "该教程无效");
        }
        if(msg.what==StatusCode.REQUEST_COLLECT_AUTHKEYWRONG){

            CommonUtils.getUtilInstance().showToast(APP.context, "授权码不正确");
        }
        if(msg.what==StatusCode.REQUEST_COLLECT_COURSE){
            aCache = ACache.get(OtherCourseActivity.this);
            LoginDataModel loginModel = (LoginDataModel)aCache.getAsObject("loginModel");
            netRequest = new NetRequest(OtherCourseActivity.this,OtherCourseActivity.this);
            user = loginModel.getUserModel();
            userId = user.getId();
            authkey = user.getAuth_key();
            Map map1=new HashMap();
            map1.put("uid",userId);
            map1.put("authkey",authkey);
            map1.put("cid", itemid);
            map1.put("type", StatusCode.REQUEST_COLLECT_COURSE);
            netRequest.httpRequest(map1, CommonUrl.courseFav);
            finish();
            Intent intent=new Intent(OtherCourseActivity.this,OtherCourseActivity.class);
            intent.putExtra("tid",tid);
            startActivity(intent);
        }
        if(msg.what==StatusCode.REQUEST_COLLECT_SUCCESS){
            CommonUtils.getUtilInstance().showToast(APP.context, "收藏成功");
        }
        if(msg.what==StatusCode.REQUEST_COLLECT_ALREADY){
            CommonUtils.getUtilInstance().showToast(APP.context, "已收藏过此课程");
        }
        }

};
    //获得收藏信息
    private void initInfo() {

        layoutManager1 = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView1.setLayoutManager(layoutManager1);
      //  recyclerView1.addItemDecoration(new CourseDecoration(this, CourseDecoration.VERTICAL_LIST));
        courseListAdapter1 = new CourseListAdapter(courseItemList1, OtherCourseActivity.this);
        recyclerView1.setAdapter(courseListAdapter1);

    }

    @Override
    public void requestFinish (String result, String requestUrl)throws JSONException {
        if(requestUrl.equals(CommonUrl.courseInfo)){//返回收藏信息
            JSONObject object = new JSONObject(result);
            int code = Integer.valueOf(object.getString("code"));
            Message msg = new Message();

            switch (code){
                case StatusCode.REQUEST_KIND_SECCESS://请求收藏的约拍成功
                {
                    JSONArray content = object.getJSONArray("contents");
                    Log.d("eeeeeeeeeeeeeeeeee",content.toString());
                    for(int i=0;i<content.length();i++){
                        Log.d("wwwwwwwwwwwww","fffffff");
                        JSONObject course = content.getJSONObject(i);
                        CoursesModel coursesModel=new CoursesModel();
                        coursesModel.setSee(course.getInt("Csee"));
                        coursesModel.setTitle(course.getString("Ctitle"));
                        coursesModel.setReadNum(course.getInt("CwatchN"));
                        coursesModel.setImage(course.getString("CimageUrl"));
                        coursesModel.setItemid(course.getInt("Cid"));
                        coursesModel.setValid(course.getInt("Cvalid"));
                        coursesModel.setCollet(course.getInt("Cfav"));
                        coursesModel.setLikeNum(course.getInt("CfavN"));

                        coursesModel.setKind(2);
                        courseItemList1.add(coursesModel);
                    }
                    msg.what = StatusCode.REQUEST_KIND_SECCESS;
                    handler.sendMessage(msg);
                    break;
                }
                case StatusCode.REQUEST_KIND_FAIL://请求收藏的约拍成功
                {

                    msg.what = StatusCode.REQUEST_KIND_FAIL;
                    handler.sendMessage(msg);
                    break;
                }
                case StatusCode.REQUEST_KIND_NOMORE://请求收藏的约拍成功
                {

                    msg.what = StatusCode.REQUEST_KIND_NOMORE;
                    handler.sendMessage(msg);
                    break;
                }
                case StatusCode.REQUEST_DETAIL_SECCESS: {
                    JSONObject object1 = object.getJSONObject("contents");
                    JSONObject object2=object1.getJSONObject("course");
                    url = object2.getString("Curl");
                    msg.what=StatusCode.REQUEST_DETAIL_SECCESS;
                    handler.sendMessage(msg);
                    Log.d("oooooooooo","ppppppppp");
                    break;
                }
                case StatusCode.REQUSET_DETAIL_INVALID:
                {
                    msg.what=StatusCode.REQUSET_DETAIL_INVALID;
                    handler.sendMessage(msg);
                    break;
                }
                case StatusCode.REQUEST_DISCOLLECT_SUCCESS:
                {
                    msg.what=StatusCode.REQUEST_DISCOLLECT_SUCCESS;
                    handler.sendMessage(msg);
                    break;
                }
                case StatusCode.REQUEST_DISCOLLECT_ALREADY:
                {
                    msg.what=StatusCode.REQUEST_DISCOLLECT_ALREADY;
                    handler.sendMessage(msg);
                    break;
                }
                case StatusCode.REQUEST_DISCOLLECT_EVER:
                {
                    msg.what=StatusCode.REQUEST_DISCOLLECT_EVER;
                    handler.sendMessage(msg);
                    break;
                }
                case StatusCode.REQUEST_SEVER_FAIL:
                {
                    msg.what=StatusCode.REQUEST_SEVER_FAIL;
                    handler.sendMessage(msg);
                    break;
                }
                case StatusCode.REQUEST_COURSE_INVALID:
                {
                    msg.what=StatusCode.REQUEST_COURSE_INVALID;
                    handler.sendMessage(msg);
                    break;
                }
                case StatusCode.REQUEST_COLLECT_AUTHKEYWRONG:
                {
                    msg.what=StatusCode.REQUEST_COLLECT_AUTHKEYWRONG;
                    handler.sendMessage(msg);
                    break;
                }
                case StatusCode.REQUEST_COLLECT_SUCCESS:
                {
                    msg.what=StatusCode.REQUEST_COLLECT_SUCCESS;
                    handler.sendMessage(msg);
                    break;
                }
                case StatusCode.REQUEST_COLLECT_ALREADY:
                {
                    msg.what=StatusCode.REQUEST_COLLECT_ALREADY;
                    handler.sendMessage(msg);
                    break;
                }
            }

        }
        else if(requestUrl==CommonUrl.courseHomePage){
            JSONObject object = new JSONObject(result);
            int code = Integer.valueOf(object.getString("code"));
            Message msg = new Message();
            if(code==StatusCode.REQUEST_COURSE_MORE_COURSES_SUCCESS){
                JSONArray array=object.getJSONArray("contents");
                for(int i=0;i<array.length();i++){
                    Log.d("wwwwwwwwwwwww","fffffff");
                    JSONObject course = array.getJSONObject(i);
                    CoursesModel coursesModel=new CoursesModel();
                    coursesModel.setSee(course.getInt("Csee"));
                    coursesModel.setTitle(course.getString("Ctitle"));
                    coursesModel.setReadNum(course.getInt("CwatchN"));
                    coursesModel.setImage(course.getString("CimageUrl"));
                    coursesModel.setItemid(course.getInt("Cid"));
                    coursesModel.setValid(course.getInt("Cvalid"));
                    coursesModel.setCollet(course.getInt("Cfav"));
                    coursesModel.setLikeNum(course.getInt("CfavN"));

                    coursesModel.setKind(2);
                    courseItemList1.add(coursesModel);
                }
                msg.what = StatusCode.REQUEST_COURSE_MORE_COURSES_SUCCESS;
                handler.sendMessage(msg);
            }
        }

    }

    @Override
    public void exception (IOException e, String requestUrl){

    }

private int itemCollect;
    ImageView collect;
    ImageView cancel;

    @Override
    public void onClick(View v) {
        List list = new ArrayList();
        list = (List) v.getTag();
        int i = (int) list.get(0);
        if( i == 2){
            int position = (int) list.get(1);
            itemid=courseItemList1.get(position).getItemid();
            Message msg = new Message();
            msg.what = StatusCode.REQUEST_DETAIL_COURSE;
            handler.sendMessage(msg);
        }

        if(i == 1) {
            int position = (int) list.get(1);
            itemid=(int)list.get(2);
            itemCollect=(int)list.get(3);
            collect=(ImageView)list.get(4);
            cancel=(ImageView)list.get(5);
            if (itemCollect == 1) {

                //    collect.setImageResource(R.drawable.button_pop_down);
                Message msg = new Message();
                msg.what = StatusCode.REQUEST_DISCOLLECT_COURSE;
                handler.sendMessage(msg);
            }
            if(itemCollect == 0){

                //  collect.setImageResource(R.drawable.button_model_up);
                Message msg = new Message();
                msg.what = StatusCode.REQUEST_COLLECT_COURSE;
                handler.sendMessage(msg);
            }
        }

    }
}
