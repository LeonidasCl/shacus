package com.example.pc.shacus.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pc.shacus.APP;
import com.example.pc.shacus.Activity.ChatActivity;
import com.example.pc.shacus.Activity.CoursesActivity;
import com.example.pc.shacus.Activity.FavoritemActivity;
import com.example.pc.shacus.Activity.FollowActivity;
import com.example.pc.shacus.Activity.MainActivity;
import com.example.pc.shacus.Activity.OrdersActivity;
import com.example.pc.shacus.Activity.OtherCourseActivity;
import com.example.pc.shacus.Activity.OtherUserActivity;
import com.example.pc.shacus.Activity.SettingsActivity;
import com.example.pc.shacus.Activity.ShareActivity;
import com.example.pc.shacus.Activity.SignInActivity;
import com.example.pc.shacus.Activity.WorksActiviy;
import com.example.pc.shacus.Data.Cache.ACache;
import com.example.pc.shacus.Data.Model.LoginDataModel;
import com.example.pc.shacus.Data.Model.UserModel;
import com.example.pc.shacus.Network.NetRequest;
import com.example.pc.shacus.Network.NetworkCallbackInterface;
import com.example.pc.shacus.Network.StatusCode;
import com.example.pc.shacus.R;
import com.example.pc.shacus.Util.CommonUrl;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//孙启凡 2016.08.29
//用户个人界面（一级）
public class UserFragment extends Fragment implements  NetworkCallbackInterface.NetRequestIterface {


private Button bo;
    private Button oo;
    private NetRequest requestFragment1;

    @Override
    public void exception(IOException e, String requestUrl) {
        //处理网络请求的异常信息，一般用不到
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_user, container, false);
        requestFragment1=new NetRequest(this,getContext());
        bo=(Button)view.findViewById(R.id.denglu);
        oo=(Button)view.findViewById(R.id.paga);
        bo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent ii=new Intent(getActivity(),CoursesActivity.class);
                startActivity(ii);
//
//                Map map=new HashMap();
//                map.put("phone","15951726659");
//                map.put("password","000000");
//                map.put("askCode",StatusCode.REQUEST_LOGIN);
//
//                requestFragment1.httpRequest(map, CommonUrl.loginAccount);
            }
        });
        oo.setOnClickListener(new View.OnClickListener() {
            @Override

                    public void onClick(View v) {
                        Intent intent6 = new Intent(getActivity(), OtherUserActivity.class);
                        startActivity(intent6);
                    }

        });
        return view;
    }

//    private List<Map<String, Object>> init() {
//        List<Map<String, Object>> lst = new ArrayList<>();
//        for (int i = 0; i < data.length; i++) {
//            Map<String, Object> item = new HashMap<>();
//            item.put("item1", image1[i]);
//            item.put("item2", data[i]);
//            item.put("item3", R.drawable.a);
//            lst.add(item);
//        }
//        return lst;
//    }



    @Override
    public void requestFinish(String result, String requestUrl) throws JSONException {
        //在这里接收所有网络请求
        JSONObject object = new JSONObject(result);
        int code = Integer.valueOf(object.getString("code"));
        Log.d("ssssssssss",object.toString());







    }

}
