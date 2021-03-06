package com.example.pc.shacus.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.pc.shacus.Data.Cache.ACache;
import com.example.pc.shacus.Data.Model.LoginDataModel;
import com.example.pc.shacus.Data.Model.UserModel;
import com.example.pc.shacus.Network.NetworkCallbackInterface;
import com.example.pc.shacus.R;

import org.json.JSONException;

import java.io.IOException;

//Author:LQ
//Time:9.1
public class PersonalInfoActivity extends AppCompatActivity implements NetworkCallbackInterface.NetRequestIterface,View.OnClickListener{

    private TextView userName;
    private TextView phoneNumber;
    private TextView Email;
    private TextView Address;

    private TextView sign;
    private ImageView userImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        //初始化
        Button btn_e = (Button) findViewById(R.id.textData_Edit);
        TextView btn_edit = (TextView) findViewById(R.id.btn_edit);
        ImageButton btn_back = (ImageButton) findViewById(R.id.btn_back);
        userImage= (ImageView) findViewById(R.id.imageData_UserImage);
        userName= (TextView) findViewById(R.id.textData_UserName);
        phoneNumber= (TextView) findViewById(R.id.textData_UserPhoneNumber);
        Email= (TextView) findViewById(R.id.textData_UserEmail);
        Address= (TextView) findViewById(R.id.textData_UserAddress);
        sign = (TextView) findViewById(R.id.textData_UserSign);

        btn_e.setOnClickListener(this);
        btn_edit.setOnClickListener(this);
        btn_back.setOnClickListener(this);


//        String username="15051867518";
//        String password="951214";
//        HashMap map=new HashMap();
//        map.put("phone",username);
//        map.put("password",password);
//        map.put("askCode", StatusCode.REQUEST_LOGIN);
//        NetRequest request=new NetRequest(this,this);
//        request.httpRequest(map, CommonUrl.loginAccount);
    }

    @Override
    public void requestFinish(String result, String requestUrl) throws JSONException {
//        if(requestUrl.equals(CommonUrl.loginAccount)){
//            JSONObject loginModel=new JSONObject(result);
//            int code= Integer.valueOf(loginModel.getString("code"));
//            Log.d("code", String.valueOf(code));
//            if(code==StatusCode.REQUEST_LOGIN_SUCCESS) {
//                JSONArray jsonArray=loginModel.getJSONArray("contents");
//                ACache cache=ACache.get(this);
//                cache.put("loginModel",jsonArray.getJSONObject(0),ACache.TIME_WEEK);
//            }
//        }

    }

    @Override
    public void exception(IOException e, String requestUrl) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_edit:
                Intent intent=new Intent();
                intent.setClass(this, PersonalInfoEditActivity.class);
                startActivity(intent);
                break;
            case R.id.textData_Edit:
                Intent intent2=new Intent();
                intent2.setClass(this, PersonalInfoEditActivity.class);
                startActivity(intent2);
                break;
        }
    }

    @Override
    public  void  onResume(){
        super.onResume();
        ACache cache=ACache.get(PersonalInfoActivity.this);
        UserModel dataModel = ((LoginDataModel) cache.getAsObject("loginModel")).getUserModel();
        getUserName().setText(dataModel.getNickName());
        getPhoneNumber().setText(dataModel.getPhone());
        getEmail().setText(dataModel.getMailBox());
        getSign().setText(dataModel.getSign());
        getAddress().setText(dataModel.getLocation());
        Glide.with(this)
                .load(dataModel.getHeadImage()).centerCrop()
//                .placeholder(R.drawable.holder)
                .error(R.drawable.loading_error)
                .into(getUserImage());
    }
    public TextView getUserName() {
        return userName;
    }

    public TextView getPhoneNumber() {
        return phoneNumber;
    }

    public TextView getEmail() {
        return Email;
    }

    public TextView getAddress() {
        return Address;
    }

    public ImageView getUserImage() {
        return userImage;
    }

    public TextView getSign() {
        return sign;
    }

}
