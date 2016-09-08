package com.example.pc.shacus.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.pc.shacus.APP;
import com.example.pc.shacus.Adapter.FirstPageAdapter;
import com.example.pc.shacus.Data.Cache.ACache;
import com.example.pc.shacus.Data.Model.ItemModel;
import com.example.pc.shacus.Data.Model.LoginDataModel;
import com.example.pc.shacus.Data.Model.UserModel;
import com.example.pc.shacus.Network.NetRequest;
import com.example.pc.shacus.Network.NetworkCallbackInterface;
import com.example.pc.shacus.Network.StatusCode;
import com.example.pc.shacus.R;
import com.example.pc.shacus.Util.CommonUrl;
import com.example.pc.shacus.Util.CommonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
* 崔颖华
* 发现
* */

public class HomeFragment extends Fragment implements NetworkCallbackInterface.NetRequestIterface{
    private List<ItemModel> itemModelList = new ArrayList<>();
    private ListView listView;
    private NetRequest netRequest;
    private ACache aCache;
    private FirstPageAdapter firstPageAdapter;
    Activity activity;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        activity = this.getActivity();
        listView = (ListView) view.findViewById(R.id.first_listview);
        netRequest = new NetRequest(this,getActivity());
        aCache = ACache.get(getActivity());
        for(int i = 0; i < 10; i++){
            ItemModel itemModel = new ItemModel();
            itemModel.setLikeNum(11);
            itemModel.setCommentNum(12);
            itemModel.setTitle("花海");
            itemModel.setDetial("与君初相识，犹如故人归");
            itemModel.setStartTime("2016-9-8");
            itemModelList.add(itemModel);
        }
        firstPageAdapter = new FirstPageAdapter(itemModelList,activity);

        listView.setAdapter(firstPageAdapter);
        //initInfo();
        return view;
    }

    private void initInfo(){
        Map map = new HashMap<>();
        LoginDataModel loginDataModel = (LoginDataModel) aCache.getAsObject("loginModel");
        UserModel content = loginDataModel.getUserModel();
        String userId = content.getId();
        String authkey = content.getAuth_key();
        map.put("uid", userId);
        map.put("authkey", authkey);
        map.put("type", StatusCode.REQUEST_ALLDONGTAI);
        netRequest.httpRequest(map, CommonUrl.allDongtai);
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case StatusCode.REQUEST_ALLDONGTAI_SUCCESS://成功返回所有动态
                {
                    firstPageAdapter = new FirstPageAdapter(itemModelList,getActivity());
                    listView.setAdapter(firstPageAdapter);
                    break;
                }
                case StatusCode.REQUESTT_ALLDONGTAI_ERROR: //用户身份认证失败
                {
                    CommonUtils.getUtilInstance().showToast(APP.context, "请重新登陆");
                    break;
                }
            }
        }
    };

    @Override
    public void requestFinish(String result, String requestUrl) throws JSONException {
        if (requestUrl.equals(CommonUrl.allDongtai)){
            JSONObject object = new JSONObject(result);
            int code  = Integer.valueOf(object.getString("code"));
            Log.d("aaaaaaaaaaaa",object.toString());
            Message message = new Message();
            switch (code){
                case StatusCode.REQUEST_ALLDONGTAI_SUCCESS: //
                {
                    JSONArray jsonArray = object.getJSONArray("contents");
                    for(int i = 0; i < jsonArray.length(); i++){
                        JSONObject dongTai = jsonArray.getJSONObject(i);
                        ItemModel itemModel = new ItemModel();
                        itemModel.setImage(dongTai.getString("TIimgurl"));
                        itemModel.setLikeNum(dongTai.getInt("TlikeN"));
                        itemModel.setCommentNum(dongTai.getInt("TcommentN"));
                        itemModel.setTitle(dongTai.getString("Ttitle"));
                        itemModel.setDetial(dongTai.getString("Tcontent"));
                        itemModel.setStartTime(dongTai.getString("TsponsT"));
                        itemModel.setUserImage(dongTai.getString("Tsponsorimg"));
                        itemModelList.add(itemModel);
                    }
                    message.what = StatusCode.REQUEST_ALLDONGTAI_SUCCESS;
                    handler.sendMessage(message);
                    break;
                }
                case  StatusCode.REQUESTT_ALLDONGTAI_ERROR:
                {
                    message.what = StatusCode.REQUESTT_ALLDONGTAI_ERROR;
                    handler.sendMessage(message);
                    break;
                }
            }
        }

    }

    @Override
    public void exception(IOException e, String requestUrl) {

    }
}