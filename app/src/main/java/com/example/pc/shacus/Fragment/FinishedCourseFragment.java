package com.example.pc.shacus.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import org.json.JSONException;

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
        LoginDataModel loginModel = (LoginDataModel)aCache.getAsObject("loginModel");
        UserModel user = null;
        Map map = new HashMap<>();
        String userId = null;
        String authkey = null;

        user = loginModel.getUserModel();
        userId = user.getId();
        authkey = user.getAuth_key();
        initInfo();

        return view;
    }

    private void initInfo() {
        courseItemList1 = new ArrayList<>();


        layoutManager1 = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView1.setLayoutManager(layoutManager1);

        courseListAdapter1 = new CourseListAdapter(courseItemList1, getActivity());
        recyclerView1.setAdapter(courseListAdapter1);

    }

    @Override
    public void requestFinish(String result, String requestUrl) throws JSONException {
      //  if(requestUrl.equals(CommonUrl.getFavorInfo)){//返回收藏信息
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
//                        courseItemList1.add(itemModel);
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
    public void exception(IOException e, String requestUrl) {

    }
}
