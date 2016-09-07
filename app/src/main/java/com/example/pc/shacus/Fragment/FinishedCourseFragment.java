package com.example.pc.shacus.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pc.shacus.APP;
import com.example.pc.shacus.Activity.CoursesActivity;
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
 * Created by 启凡 on 2016/9/6.
 */
public class FinishedCourseFragment extends Fragment implements NetworkCallbackInterface.NetRequestIterface{
    private RecyclerView recyclerView1;
    private CourseListAdapter courseListAdapter1;
    List<CoursesModel> courseItemList1;
    RecyclerView.LayoutManager layoutManager1;

    private ACache aCache;
    private NetRequest netRequest;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_finishedcourse,container,false);
        recyclerView1= (RecyclerView) view.findViewById(R.id.finishrecyclerView);
        netRequest = new NetRequest(this,getActivity());
        aCache = ACache.get(getActivity());
        courseItemList1 = new ArrayList<>();
        LoginDataModel loginModel = (LoginDataModel)aCache.getAsObject("loginModel");
        UserModel user = null;
        Map map = new HashMap<>();
        String userId = null;
        String authkey = null;

        user = loginModel.getUserModel();
        userId = user.getId();
        authkey = user.getAuth_key();
        map.put("uid",userId);
        map.put("authkey",authkey);
        map.put("type",StatusCode.REQUEST_FINISHED_COURSE);
        netRequest.httpRequest(map,CommonUrl.courseInfo);

        initInfo();

        return view;
    }
private Handler handler=new Handler(){
    @Override
    public void handleMessage(Message msg) {
        if(msg.what==StatusCode.REQUSET_FINISHED_SUCCESS){
            initInfo();
        }
        if (msg.what==StatusCode.REQUSET_FINISHED_FAIL){
            CommonUtils.getUtilInstance().showToast(APP.context, "请求失败！");
        }
    }
};
    private void initInfo() {



        layoutManager1 = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView1.setLayoutManager(layoutManager1);
        recyclerView1.addItemDecoration(new CourseDecoration(getActivity(), CourseDecoration.VERTICAL_LIST));
        courseListAdapter1 = new CourseListAdapter(courseItemList1, getActivity());
        recyclerView1.setAdapter(courseListAdapter1);

    }

    @Override
    public void requestFinish(String result, String requestUrl) throws JSONException {
       if(requestUrl.equals(CommonUrl.courseInfo)){//返回收藏信息
            JSONObject object = new JSONObject(result);
            int code = Integer.valueOf(object.getString("code"));
            Message msg = new Message();

            switch (code){
                case StatusCode. REQUSET_FINISHED_SUCCESS://请求收藏的约拍成功
                {
                    JSONArray content = object.getJSONArray("contents");
                    Log.d("eeeeeeeeeeeeeeeeee", content.toString());
                    for(int i = 0;i < content.length();i++){
                        JSONObject course = content.getJSONObject(i);
                        CoursesModel coursesModel=new CoursesModel();
                        coursesModel.setSee(course.getInt("Csee"));
                        coursesModel.setTitle(course.getString("Ctitle"));
                        coursesModel.setReadNum(course.getInt("CwatchN"));
                        coursesModel.setImage(course.getString("CimageUrl"));
                        coursesModel.setItemid(course.getInt("Cid"));
                        coursesModel.setValid(course.getInt("Cvalid"));
                        coursesModel.setCollet(course.getInt("Cfav"));
                        coursesModel.setLikeNum(course.getInt("Cliked"));
                        coursesModel.setKind(1);
                        courseItemList1.add(coursesModel);
                    }
                    msg.what = StatusCode.REQUSET_FINISHED_SUCCESS;
                    handler.sendMessage(msg);
                    break;
                }
                case StatusCode.REQUSET_FINISHED_FAIL:
                {
                    msg.what=StatusCode.REQUSET_FINISHED_FAIL;
                    handler.sendMessage(msg);
                    break;
                }
            }

        }

        }

    @Override
    public void exception(IOException e, String requestUrl) {

    }
}
