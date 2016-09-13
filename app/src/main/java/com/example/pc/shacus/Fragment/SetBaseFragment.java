package com.example.pc.shacus.Fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import com.example.pc.shacus.Data.Cache.ACache;
import com.example.pc.shacus.Data.Model.SettingDataModel;
import com.example.pc.shacus.Network.NetRequest;
import com.example.pc.shacus.Network.NetworkCallbackInterface;
import com.example.pc.shacus.Network.StatusCode;
import com.example.pc.shacus.R;
import com.example.pc.shacus.Util.CommonUrl;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 */
//Author:李前
//Time:8.29
    //change1:继续完成
    //worker:LQ
    //time:8.29
public class SetBaseFragment extends Fragment implements View.OnClickListener,NetworkCallbackInterface.NetRequestIterface{

    private NetRequest netRequest;
    private SettingDataModel dataModel;

    private String password;
    private Handler  handler=new Handler(){
        @Override
        public void handleMessage(Message msg){
            if(msg.what==StatusCode.REQUEST_SETTING_CHANGE_PASSWORD_SUCCESS)
                changePasswordDialog(1);
            else if(msg.what==StatusCode.STATUS_ERROR)
                Toast.makeText(SetBaseFragment.this.getActivity(),"密码不能少于六位",Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(SetBaseFragment.this.getActivity(),"密码错误",Toast.LENGTH_SHORT).show();
            Log.d("LQQQQQQQQQQQ", "handleMessage: ");
        }
    };
    public SetBaseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_set_base, container, false);
        initData(view);

        return view;
    }

    private void initData(View view) {
        //初始化各部件
        Switch phoneVisible = (Switch) view.findViewById(R.id.btn_phoneVisible);
        Switch messageInform = (Switch) view.findViewById(R.id.btn_messageInform);

        ImageButton btn_back = (ImageButton) view.findViewById(R.id.btn_back);
        View changePassword = view.findViewById(R.id.layout_changePassword);
        View versionUpdate = view.findViewById(R.id.layout_versionUpdate);
        View functionIntroduction = view.findViewById(R.id.layout_functionIntroduce);
        View advice = view.findViewById(R.id.layout_advice);
        View cleanCache = view.findViewById(R.id.layout_clearCache);

        netRequest=new NetRequest(this,this.getActivity());

        ACache aCache=ACache.get(this.getActivity());
        dataModel = (SettingDataModel) aCache.getAsObject("settingModel");

        //设置点击事件
        btn_back.setOnClickListener(SetBaseFragment.this);
        changePassword.setOnClickListener(SetBaseFragment.this);
        versionUpdate.setOnClickListener(SetBaseFragment.this);
        functionIntroduction.setOnClickListener(SetBaseFragment.this);
        advice.setOnClickListener(SetBaseFragment.this);
        cleanCache.setOnClickListener(SetBaseFragment.this);

        //获取已缓存的配置
        ACache a=ACache.get(this.getActivity());
        SettingDataModel setModel= (SettingDataModel) a.getAsObject("settingModel");
        phoneVisible.setChecked(setModel.isPhoneVisible());
        messageInform.setChecked(setModel.isMessageInform());
    }


    @Override
    public void onClick(View v) {
        //跳转至各个Fragment
        switch (v.getId()){
            case R.id.btn_back:
                Log.d("LQQQQQQQ", "toast");
                this.getActivity().finish();
                break;
            case R.id.layout_changePassword:
                changePasswordDialog(0);
                break;
            case R.id.layout_functionIntroduce:
//                SetGeneralFragment generalFragment=new SetGeneralFragment();
//                FragmentManager fm3=getFragmentManager();
//                FragmentTransaction tx3=fm3.beginTransaction();
//                tx3.replace(R.id.frameLayout,generalFragment,"UserManage");
//                tx3.addToBackStack(null);
//                tx3.commit();
                break;
            case R.id.layout_versionUpdate:
                SetAboutFragment aboutFragment =new SetAboutFragment();
                FragmentManager fm4=getFragmentManager();
                FragmentTransaction tx4=fm4.beginTransaction();
                tx4.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                tx4.replace(R.id.frameLayout,aboutFragment,"UserManage");
                tx4.addToBackStack(null);
                tx4.commit();
                break;
            case R.id.layout_advice:
                SetAdviceFragment adviceFragment=new SetAdviceFragment();
                FragmentManager fm5=getFragmentManager();
                FragmentTransaction tx5=fm5.beginTransaction();
                tx5.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                tx5.replace(R.id.frameLayout,adviceFragment,"Advice");
                tx5.addToBackStack(null);
                tx5.commit();
                break;
            case R.id.layout_clearCache:
                break;
        }
    }

    private void changePasswordDialog(int i) {
        final EditText UN=new EditText(this.getActivity());
        UN.setMaxLines(1);
        UN.setText("");
        UN.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        UN.setFocusable(true);
        UN.setFocusableInTouchMode(true);
        UN.requestFocus();
        //自动弹出键盘
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                InputMethodManager inputManager = (InputMethodManager) UN.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(UN, 0);
            }
        }, 500);


        switch (i) {
            case 0:
                new AlertDialog.Builder(this.getActivity())
                        .setTitle("输入旧密码")
                        .setIcon(R.drawable.ic_launcher)
                        .setView(UN)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {//监听
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //修改旧密码
                                if(UN.getText().toString().equals("")||UN.getText().toString().length()<6)
                                {
                                    Message msg=handler.obtainMessage();
                                    msg.what=StatusCode.STATUS_ERROR;
                                    handler.sendMessage(msg);
                                }
                                else {
                                    HashMap map = new HashMap();
                                    map.put("oldpassword", UN.getText().toString());
                                    map.put("type", 10501);
                                    map.put("Userid", dataModel.getUserID());
                                    netRequest.httpRequest(map, CommonUrl.settingChangeNetUrl);
                                    Log.d("LQQQQQQQQ", "put old password");
                                }
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
                break;
            case 1:
                new AlertDialog.Builder(this.getActivity())
                        .setTitle("请输入新密码")
                        .setIcon(R.drawable.ic_launcher)
                        .setView(UN)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {//监听
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //输入新密码
                                if(UN.getText().toString().equals("")||UN.getText().toString().length()<6)
                                {
                                    Message msg=handler.obtainMessage();
                                    msg.what=StatusCode.STATUS_ERROR;
                                    handler.sendMessage(msg);
                                }
                                else {
                                    password = UN.getText().toString();
                                    changePasswordDialog(2);
                                }
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
                break;
            case 2:
                new AlertDialog.Builder(this.getActivity())
                        .setTitle("请再次输入新密码")
                        .setIcon(R.drawable.ic_launcher)
                        .setView(UN)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {//监听
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //确认新密码
                                if (UN.getText().toString().equals("") || UN.getText().toString().length() < 6) {
                                    Message msg = handler.obtainMessage();
                                    msg.what = StatusCode.STATUS_ERROR;
                                    handler.sendMessage(msg);
                                } else {
                                    String str = UN.getText().toString();
                                    if (str.equals(password)) {
                                        HashMap map = new HashMap();
                                        map.put("newpassword", UN.getText().toString());
                                        map.put("type", 10511);
                                        map.put("Userid", dataModel.getUserID());
                                        netRequest.httpRequest(map, CommonUrl.settingChangeNetUrl);
                                        Log.d("LQQQQQQQQ", "put new password");
                                        Toast.makeText(SetBaseFragment.this.getActivity(), "修改成功", Toast.LENGTH_SHORT).show();
                                    } else
                                        Toast.makeText(SetBaseFragment.this.getActivity(), "两次输入不一致", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
                break;
        }
    }

    @Override
    public void requestFinish(String result, String requestUrl) throws JSONException {
        if(requestUrl.equals(CommonUrl.settingChangeNetUrl)){
            JSONObject jsonObject=new JSONObject(result);
            String code=jsonObject.getString("code");
            Log.d("LQQQQQ", code);
            Log.d("LQQQQQ", jsonObject.getString("contents"));
            if (code.equals("10501")){
                Message msg=handler.obtainMessage();
                msg.what= StatusCode.REQUEST_SETTING_CHANGE_PASSWORD_SUCCESS;
                handler.sendMessage(msg);
            }else{
                Message msg=handler.obtainMessage();
                msg.what= StatusCode.REQUEST_SETTING_CHANGE_PASSWORD_FAILED;
                handler.sendMessage(msg);}
        }
    }

    @Override
    public void exception(IOException e, String requestUrl) {

    }
}
