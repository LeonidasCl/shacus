package com.example.pc.shacus.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pc.shacus.Data.Cache.ACache;
import com.example.pc.shacus.Data.Model.LoginDataModel;
import com.example.pc.shacus.Data.Model.UserModel;
import com.example.pc.shacus.Network.NetRequest;
import com.example.pc.shacus.Network.NetworkCallbackInterface;
import com.example.pc.shacus.Network.StatusCode;
import com.example.pc.shacus.R;
import com.example.pc.shacus.Util.AutoHint;
import com.example.pc.shacus.Util.CommonUrl;
import com.example.pc.shacus.View.CircleImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

//Author:LQ
//Time:9.1
public class PersonalInfoEditActivity extends AppCompatActivity implements View.OnClickListener,NetworkCallbackInterface.NetRequestIterface{

    private ImageButton btn_back;
    private EditText userName,userAddress,userPhoneNumber,userEmail;
    private CircleImageView userImage;
    private TextView btn_finish;
    UserModel dataModel;
    private NetRequest netRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info_edit);

        //init
        btn_back= (ImageButton) findViewById(R.id.btn_back);
        btn_finish= (TextView) findViewById(R.id.btn_finish);
        userName = (EditText) findViewById(R.id.textData_UserName);
        userEmail= (EditText) findViewById(R.id.textData_UserEmail);
        userAddress= (EditText) findViewById(R.id.textData_UserAddress);
        userPhoneNumber= (EditText) findViewById(R.id.textData_UserPhoneNumber);
        userImage= (CircleImageView) findViewById(R.id.imageData_UserImage);

        netRequest=new NetRequest(this,this);

//        userEmail.setThreshold(1);
//        ArrayAdapter arrayAdapter=new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,AutoHint.EMAILS);
//        userEmail.setAdapter(arrayAdapter);

        //listener
        btn_back.setOnClickListener(this);
        btn_finish.setOnClickListener(this);

        //从缓存中取出数据
        ACache cache=ACache.get(PersonalInfoEditActivity.this);
        dataModel= ((LoginDataModel) cache.getAsObject("loginModel")).getUserModel();
        getUserName().setText(dataModel.getNickName());
        getUserPhoneNumber().setText(dataModel.getPhone());
        getUserEmail().setText(dataModel.getMailBox());
        getUserAddress().setText(dataModel.getLocation());

        Glide.with(this)
                .load(dataModel.getHeadImage()).centerCrop()
//                .placeholder(R.drawable.holder)
                .error(R.drawable.loading_error)
                .into(getUserImage());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_finish:
                //传入数据
                if(userName.getText().toString().equals("")||userEmail.getText().toString()=="") {
                    Toast.makeText(this, "昵称不能为空", Toast.LENGTH_SHORT).show();
                    break;
                }
                String result=checkChange();
                if(result.charAt(1)=='1'){
                    HashMap map=new HashMap<>();
                    map.put("Usernickname",getUserName().getText().toString());
                    map.put("Userid",dataModel.getId());
                    map.put("type", StatusCode.REQUEST_SETTING_CHANGE_NICKNAME);
                    netRequest.httpRequest(map, CommonUrl.settingChangeNetUrl);
                    Log.d("LQQQQQQQ", "user nickname");
                }
                if(result.charAt(2)=='1'){
                    HashMap map=new HashMap<>();
                    map.put("Userphone",getUserPhoneNumber().getText().toString());
                    map.put("Userid",dataModel.getId());
                    map.put("type", StatusCode.REQUEST_SETTING_CHANGE_PHONENUMBER);
                    netRequest.httpRequest(map, CommonUrl.settingChangeNetUrl);
                    Log.d("LQQQQQQQ", "Userphone");
                }
                if(result.charAt(3)=='1'){
                    HashMap map=new HashMap<>();
                    map.put("Userlocation",getUserAddress().getText().toString());
                    map.put("Userid",dataModel.getId());
                    map.put("type", StatusCode.REQUEST_SETTING_CHANGE_ADDRESS);
                    netRequest.httpRequest(map, CommonUrl.settingChangeNetUrl);
                    Log.d("LQQQQQQQ", "Userlocation");
                }
                if(result.charAt(4)=='1'){
                    HashMap map=new HashMap<>();
                    map.put("Usermail",getUserEmail().getText().toString());
                    map.put("Userid",dataModel.getId());
                    map.put("type", StatusCode.REQUEST_SETTING_CHANGE_EMAIL);
                    netRequest.httpRequest(map, CommonUrl.settingChangeNetUrl);
                    Log.d("LQQQQQQQ", "Usermail");
                }

                if(result.equals("00000")){finish();}
                break;
        }
    }

    //检测更改情况
    private String checkChange() {
        String result="";
        //头像更改
        if (false){result+="1";}else{result+="0";}
        //其他更改
        if(!dataModel.getNickName().equals(String.valueOf(userName.getText()))){result+="1";}else{result+="0";}
        if(!dataModel.getPhone().equals(String.valueOf(userPhoneNumber.getText()))){result+="1";}else{result+="0";}
        if(!dataModel.getLocation().equals(String.valueOf(userAddress.getText()))){result+="1";}else{result+="0";}
        if(!dataModel.getMailBox().equals(String.valueOf(userEmail.getText()))){result+="1";}else{result+="0";}

        //刷新缓存
        Log.d("LQQQQQQQQQQQ", result);
        return result;
    }

    public EditText getUserName() {
        return userName;
    }

    public EditText getUserEmail() {
        return userEmail;
    }

    public EditText getUserAddress() {
        return userAddress;
    }

    public EditText getUserPhoneNumber() {
        return userPhoneNumber;
    }

    @Override
    public void requestFinish(String result, String requestUrl) throws JSONException {
        if(requestUrl.equals(CommonUrl.settingChangeNetUrl)){
            JSONObject jsonObject=new JSONObject(result);
            String code=jsonObject.getString("code");
            Log.d("LQQQQQ", code);
            Log.d("LQQQQQ", jsonObject.getString("contents"));
            if(code.equals("10503")){
                dataModel.setNickName(getUserName().getText().toString());
                Log.d("LQQQQQQQ", "set name");}
            else if(code.equals("10504")){
                Log.d("LQQQQQQQ", "name fail");}
            else if(code.equals("10505")){
                dataModel.setPhone(getUserPhoneNumber().getText().toString());
                Log.d("LQQQQQQQ", "set num");}
            else if(code.equals("10506")){
                Log.d("LQQQQQQQ", "num fail");}
            else if(code.equals("10507")){
                dataModel.setLocation(getUserAddress().getText().toString());
                Log.d("LQQQQQQQ", "set address");}
            else if(code.equals("10508")){
                Log.d("LQQQQQQQ", "adress fail");}
            else if(code.equals("10509")){
                dataModel.setMailBox(getUserEmail().getText().toString());
                Log.d("LQQQQQQQ", "set email");}
            else if(code.equals("10510")){
                Log.d("LQQQQQQQ", "email fail");}
            ACache cache=ACache.get(PersonalInfoEditActivity.this);
            LoginDataModel model=(LoginDataModel)cache.getAsObject("loginModel");
            model.setUserModel(dataModel);
            cache.put("loginModel",model);
        }

        finish();
    }

    @Override
    public void exception(IOException e, String requestUrl) {

    }

    public CircleImageView getUserImage() {
        return userImage;
    }

}
