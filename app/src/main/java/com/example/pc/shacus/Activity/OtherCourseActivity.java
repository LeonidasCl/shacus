package com.example.pc.shacus.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.pc.shacus.Adapter.CourseListAdapter;
import com.example.pc.shacus.Data.Cache.ACache;
import com.example.pc.shacus.Data.Model.CoursesModel;
import com.example.pc.shacus.Network.NetRequest;
import com.example.pc.shacus.Network.NetworkCallbackInterface;
import com.example.pc.shacus.R;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 启凡 on 2016/9/5.
 */
public class OtherCourseActivity  extends AppCompatActivity implements  NetworkCallbackInterface.NetRequestIterface{


    private ImageButton returnButton;
    private ImageButton imageButton1;
    private TextView title;

    private RecyclerView recyclerView1;
    private CourseListAdapter courseListAdapter1;
    List<CoursesModel> courseItemList1;
    RecyclerView.LayoutManager layoutManager1;

    private ACache aCache;
    private NetRequest netRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_othercourse);
        returnButton=(ImageButton)findViewById(R.id.returnbutton2);
        title=(TextView)findViewById(R.id.ownerName2);
        imageButton1=(ImageButton)findViewById(R.id.imagebutton);


        netRequest = new NetRequest(OtherCourseActivity.this,OtherCourseActivity.this);
        aCache = ACache.get(OtherCourseActivity.this);


        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView1 = (RecyclerView)findViewById(R.id.recyclerView);

        initInfo();

    }


    //获得收藏信息
    private void initInfo() {
        courseItemList1 = new ArrayList<>();

        layoutManager1 = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView1.setLayoutManager(layoutManager1);

        courseListAdapter1 = new CourseListAdapter(courseItemList1, OtherCourseActivity.this);
        recyclerView1.setAdapter(courseListAdapter1);

    }

    @Override
    public void requestFinish (String result, String requestUrl)throws JSONException {
//        if(requestUrl.equals(CommonUrl.getFavorInfo)){//返回收藏信息
//            JSONObject object = new JSONObject(result);
//            int code = Integer.valueOf(object.getString("code"));
//            Message msg = new Message();
//
//            switch (code){
//                case StatusCode.REQUEST_FAVORYUEPAI_SUCCESS://请求收藏的约拍成功
//                {
//                    JSONArray content = object.getJSONArray("contents");
//                    Log.d("eeeeeeeeeeeeeeeeee",content.toString());
//                    for(int i = 0;i < content.length();i++){
//                        JSONObject favor = content.getJSONObject(i);
//                        ItemModel itemModel = new ItemModel();
//                        itemModel.setTitle(favor.getString("APtitle"));
//                        itemModel.setId(favor.getInt("APid"));
//                        itemModel.setUserImage(favor.getString("Userimg"));
//                        itemModel.setStartTime(favor.getString("APstartT"));
//                        itemModel.setLikeNum(favor.getInt("APlikeN"));
//                        itemModel.setImage(favor.getString("APimgurl"));
//                        itemModel.setRegistNum(favor.getInt("APregistN"));
//                        favorItemList1.add(itemModel);
//                    }
//                    msg.what = StatusCode.REQUEST_FAVORYUEPAI_SUCCESS;
//                    handler.sendMessage(msg);
//                    break;
//                }
//            }
//
//        }

    }

    @Override
    public void exception (IOException e, String requestUrl){

    }



}
