package com.example.pc.shacus.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.renderscript.ScriptGroup;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.pc.shacus.APP;
import com.example.pc.shacus.Data.Cache.ACache;
import com.example.pc.shacus.Data.Model.LoginDataModel;
import com.example.pc.shacus.Data.Model.PhotographerModel;
import com.example.pc.shacus.Data.Model.UserModel;
import com.example.pc.shacus.Network.NetworkCallbackInterface;
import com.example.pc.shacus.Network.NetRequest;
import com.example.pc.shacus.Network.StatusCode;
import com.example.pc.shacus.R;
import com.example.pc.shacus.Util.CommonUrl;
import com.example.pc.shacus.Util.CommonUtils;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * 李嘉文2016/8/24
 *
 */
public class LoginActivity extends AppCompatActivity implements NetworkCallbackInterface.NetRequestIterface{

    private TextView username;
    private TextView password;
    private TextView forgotpassword;
    private TextView signup;
    private TextView verifycode;
    private Button btn_login;
    //    private Button btn_verifycode;
    private TextView btn_verifycode;
    private NetRequest requestFragment;
    private Handler mHandler;
    private ProgressDialog loginProgressDlg;
    private CountDownTimer timeCount;
    private String phone;
    private int pflag=0;//是否在guide里点击了注册按钮的flag
    private int eventFlag=1;//1为登录 2为忘记密码 3为验证注册 4为验证注册通过 5为提交注册通过 6为修改密码验证
    TranslateAnimation animationHide=new TranslateAnimation(Animation.RELATIVE_TO_SELF,
            0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
            Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
            -1.0f);
    TranslateAnimation animationShow=new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
            Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
            -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);

    @Override
    public void onBackPressed(){
        finish();
    }

    View view1;
    View view2;
    View view111;
    String trueCODE;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        setContentView(R.layout.activity_login);
        setContentView(R.layout.activity_login_new);

        Intent intent=getIntent();
        int method=intent.getIntExtra("method", StatusCode.STATUS_ERROR);
        switch (method){
            case StatusCode.STATUS_LOGIN:
                eventFlag=1;
                break;
            case StatusCode.STATUS_REGISTER:
                eventFlag=3;
                pflag=1;
                break;
        }


        username=(TextView)findViewById(R.id.login_username);
        password=(TextView)findViewById(R.id.login_password);
        verifycode=(TextView)findViewById(R.id.register_verifycode);
        btn_login=(Button)findViewById(R.id.btn_login);

        view1 = findViewById(R.id.view1);
        view2 = findViewById(R.id.view2);
        view111 = findViewById(R.id.view111);

        btn_login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Map map = new HashMap();
                if (eventFlag==1) {
                    //检查输入格式，发弹窗请求到handler，并发网络请求
                    String usrnm = username.getText().toString();
                    String pwd = password.getText().toString();
                    if (!usrnm.equals("") && !pwd.equals("")&&judgeNameAndPwd(usrnm,pwd)){
                        map.put("phone", usrnm);
                        map.put("password", pwd);
                        map.put("askCode", StatusCode.REQUEST_LOGIN);
                        loginProgressDlg = ProgressDialog.show(LoginActivity.this, "shacus", "处理中", true, false);
                        requestFragment.httpRequest(map, CommonUrl.loginAccount);
                    }else {
                        CommonUtils.getUtilInstance().showToast(LoginActivity.this, "请规范用户名和密码：用户名为手机号，密码至少六位");
                        return;
                    }
                }
                //忘记密码
                if (eventFlag==2){
                    String name=username.getText().toString();
                    String code=verifycode.getText().toString();
                    trueCODE = code;
                    if (!code.equals("")&&!name.equals("")){
                        //验证验证码是否正确
                        map.put("phone",name);
                        phone=name;
                        map.put("code",code);
                        map.put("type", StatusCode.REQUEST_FORGOTPW_YZ);
                        requestFragment.httpRequest(map, CommonUrl.forgotpw);
                        loginProgressDlg = ProgressDialog.show(LoginActivity.this, "shacus", "处理中", true, false);
                        return;
                    }else {
                        CommonUtils.getUtilInstance().showToast(LoginActivity.this,"请输入用户名和验证码");
                        return;
                    }
                }

                if (eventFlag==3 ){
                    String name=username.getText().toString();
                    String code=verifycode.getText().toString();
                    if (!code.equals("")&&!name.equals("")){
                        eventFlag=4;
                        //验证验证码是否正确
                        map.put("phone",name);
                        phone=name;
                        map.put("code",code);
                        map.put("type", StatusCode.REQUEST_REGISTER_VERIFYB);
                        requestFragment.httpRequest(map, CommonUrl.registerAccount);
                        loginProgressDlg = ProgressDialog.show(LoginActivity.this, "shacus", "处理中", true, false);
                        return;
                    }else {
                        CommonUtils.getUtilInstance().showToast(LoginActivity.this,"请输入用户名和验证码");
                        return;
                    }
                }
                if (eventFlag==4){
                    String nickName=username.getText().toString();
                    String password=verifycode.getText().toString();
                    if (!password.equals("")&&!nickName.equals("")&&!phone.equals("")&&judgeNameAndPwd(phone,password)){
                        eventFlag=5;
                        //提交注册信息
                        map.put("phone",phone);
                        map.put("nickName",nickName);
                        map.put("password",password);
                        map.put("type", StatusCode.REQUEST_REGISTER);
                        requestFragment.httpRequest(map, CommonUrl.registerAccount);
                        loginProgressDlg = ProgressDialog.show(LoginActivity.this, "shacus", "处理中", true, false);
                        return;
                    }else {
                        CommonUtils.getUtilInstance().showToast(LoginActivity.this,"请完善信息");
                        return;
                    }
                }

                if(eventFlag==6){//修改密码
                    String password=verifycode.getText().toString();
                    if (!password.equals("")&&!phone.equals("")&&judgeNameAndPwd(phone,password)){
//                        eventFlag=5;
                        //提交注册信息
                        if(!trueCODE.equals("")){
                            map.put("phone",phone);
                            map.put("password",password);
                            map.put("type", StatusCode.REQUEST_FORGOTPW_SET);
                            map.put("code",trueCODE);
                            requestFragment.httpRequest(map, CommonUrl.forgotpw);
                            loginProgressDlg = ProgressDialog.show(LoginActivity.this, "shacus", "处理中", true, false);
                        }
                        return;
                    }else {
                        CommonUtils.getUtilInstance().showToast(LoginActivity.this,"请完善信息");
                        return;
                    }
                }
            }
        });
        btn_verifycode=(TextView)findViewById(R.id.btn_verify_code);
        btn_verifycode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map map=new HashMap();//忘记密码
                if (eventFlag==3 || eventFlag == 2){
                    view1.setVisibility(View.GONE);
                    view2.setVisibility(View.VISIBLE);
                    String usrnm=username.getText().toString();
                    if (!usrnm.equals("")&&eventFlag==3)
                    {
                        map.put("phone",usrnm);
                        map.put("type",StatusCode.REQUEST_REGISTER_VERIFYA);
                        requestFragment.httpRequest(map, CommonUrl.registerAccount);
                        loginProgressDlg = ProgressDialog.show(LoginActivity.this, "shacus", "处理中", true, false);
                        timeCount.start();
                    }else if(!usrnm.equals("")&&eventFlag==2){
                        map.put("phone",usrnm);
                        map.put("type",StatusCode.REQUEST_FORGOTPW);
                        requestFragment.httpRequest(map, CommonUrl.forgotpw);
                        loginProgressDlg = ProgressDialog.show(LoginActivity.this, "shacus", "处理中", true, false);
                        timeCount.start();
                    }
                    else {
                        CommonUtils.getUtilInstance().showToast(LoginActivity.this,"请输入用户名");
                    }
                }
            }
        });
        requestFragment=new NetRequest(this,this);
//        ImageView loginbtnimg = (ImageView) findViewById(R.id.loginimgview);
//        loginbtnimg.bringToFront();
        forgotpassword=(TextView)findViewById(R.id.btn_forgot);
        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(eventFlag==2){
                    signup.setVisibility(View.VISIBLE);
                    eventFlag=1;
                    forgotpassword.setText("忘记密码");
                    animationHide.setDuration(500);
                    verifycode.startAnimation(animationHide);
                    verifycode.setVisibility(View.GONE);
                    animationShow.setDuration(500);
                    password.startAnimation(animationShow);
                    username.setText("");
                    username.setVisibility(View.VISIBLE);
                    username.setHint("手机号");
                    view111.setVisibility(View.VISIBLE);
                    btn_verifycode.setVisibility(View.GONE);
                    view1.setVisibility(View.VISIBLE);
                    view2.setVisibility(View.GONE);
                    password.setVisibility(View.VISIBLE);
                    btn_login.setText("  登   录");
                    return;
                }
                if(eventFlag!=2){
//                signup.setVisibility(View.INVISIBLE);
                    eventFlag=2;
                    animationHide.setDuration(500);
                    password.startAnimation(animationHide);
                    password.setVisibility(View.GONE);
                    animationShow.setDuration(500);
                    verifycode.startAnimation(animationShow);
                    username.setText("");
                    username.setVisibility(View.VISIBLE);
                    username.setHint("手机号");
                    view111.setVisibility(View.VISIBLE);
                    verifycode.setVisibility(View.VISIBLE);
                    btn_verifycode.setVisibility(View.VISIBLE);
                    view1.setVisibility(View.GONE);
                    view2.setVisibility(View.VISIBLE);
                    forgotpassword.setText("去登陆");
                    btn_login.setText("验证账号");
                }
                return;
            }
        });
        signup=(TextView)findViewById(R.id.btn_newuser);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(eventFlag!=3){
                    eventFlag=3;
                    animationHide.setDuration(500);
                    password.startAnimation(animationHide);
                    password.setVisibility(View.GONE);
                    animationShow.setDuration(500);
                    verifycode.startAnimation(animationShow);
                    verifycode.setVisibility(View.VISIBLE);
                    username.setText("");
                    username.setVisibility(View.VISIBLE);
                    username.setHint("手机号");
                    view111.setVisibility(View.VISIBLE);
                    signup.setText("老用户登录");
//                forgotpassword.setVisibility(View.INVISIBLE);
                    btn_verifycode.setVisibility(View.VISIBLE);
                    view1.setVisibility(View.GONE);
                    view2.setVisibility(View.VISIBLE);
                    btn_login.setText("  注   册");

                    //下一步
                    return;
                }
                if(eventFlag==3){
                    eventFlag=1;
                    animationHide.setDuration(500);
                    verifycode.startAnimation(animationHide);
                    verifycode.setVisibility(View.GONE);
                    animationShow.setDuration(500);
                    password.startAnimation(animationShow);
                    password.setVisibility(View.VISIBLE);
                    signup.setText("新用户注册");
                    btn_verifycode.setVisibility(View.GONE);
                    view2.setVisibility(View.GONE);
                    view1.setVisibility(View.VISIBLE);
                    username.setText("");
                    username.setVisibility(View.VISIBLE);
                    username.setHint("手机号");
                    view111.setVisibility(View.VISIBLE);

//                    forgotpassword.setVisibility(View.VISIBLE);
                    btn_login.setText("  登   录");
                    return;
                }
            }
        });
        //if (eventFlag==1)
        //  signup.performClick();
        timeCount=new CountDownTimer(120000,1000){
            @Override
            public void onTick(long millisUntilFinished) {
                //btn_verifycode.setBackgroundColor(getResources().getColor(R.drawable.shape_verifycode));
                btn_verifycode.setBackground(getResources().getDrawable(R.drawable.shape_verifycode_clicked));
                btn_verifycode.setClickable(false);
                btn_verifycode.setText("重发 "+millisUntilFinished/1000+"s");
            }

            @Override
            public void onFinish() {
                btn_verifycode.setText(R.string.text_verify_code);
                //btn_verifycode.setBackgroundColor(getResources().getColor(R.color.gold));
//                btn_verifycode.setBackground(getResources().getDrawable(R.drawable.shape_verifycode));
                btn_verifycode.setBackground(getResources().getDrawable(R.drawable.loginbtn));
                btn_verifycode.setClickable(true);
            }
        };
        mHandler=new Handler(){
            @Override
            public void handleMessage(Message msg){
                if (msg.what==StatusCode.REQUEST_FAILURE){
                    loginProgressDlg.cancel();
                    CommonUtils.getUtilInstance().showToast(APP.context, (String)msg.obj);
                    return;
                }
                if (msg.what==StatusCode.RECIEVE_REGISTER_SUCCESS){
                    loginProgressDlg.cancel();//进度条取消
                    CommonUtils.getUtilInstance().showToast(APP.context, "验证成功!请设置昵称和密码!");
                    view1.setVisibility(View.VISIBLE);
                    view2.setVisibility(View.GONE);
                    btn_verifycode.setVisibility(View.GONE);
                    username.setText("");
                    verifycode.setText("");
                    username.setHint("请指定昵称");
                    verifycode.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    verifycode.setHint("请设置密码");
                    btn_login.setText("  注   册");
                }
                if (msg.what == 10008){
                    loginProgressDlg.cancel();
                    CommonUtils.getUtilInstance().showToast(APP.context, "该昵称已被使用");
                }
                if (msg.what == 20010){
                    loginProgressDlg.cancel();//进度条取消
                    Intent intent = new Intent(getApplicationContext(), SplashActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);;
                    intent.putExtra("result", 1);
                    //intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    finish();
                }
                if (msg.what == 20015){
                    loginProgressDlg.cancel();//进度条取消
                    Intent intent = new Intent(getApplicationContext(),SignupActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);;
                    startActivity(intent);
                    finish();
                }
                if (msg.what == 20013){
                    loginProgressDlg.cancel();//进度条取消
                    CommonUtils.getUtilInstance().showToast(APP.context, "验证码短信已发送");
                }
                if (msg.what == 20017){
                    loginProgressDlg.dismiss();//进度条取消
                }
                if (msg.what == StatusCode.REQUEST_FORGOTPW){
                    loginProgressDlg.cancel();//进度条取消
                    CommonUtils.getUtilInstance().showToast(APP.context, "验证码短信已发送");
                }
                if (msg.what == StatusCode.REQUEST_FORGOTPW_ERROR){
                    loginProgressDlg.cancel();//进度条取消
                    CommonUtils.getUtilInstance().showToast(APP.context, "服务器错误");
                }
                if (msg.what == StatusCode.REQUEST_FORGOTPW_NONE){
                    loginProgressDlg.cancel();//进度条取消
                    CommonUtils.getUtilInstance().showToast(APP.context, "该手机号尚未注册");
                }
                if (msg.what == StatusCode.REQUEST_FORGOTPW_YZ){
                    loginProgressDlg.cancel();//进度条取消
                    CommonUtils.getUtilInstance().showToast(APP.context, "验证成功!请重新设置密码!");
                    eventFlag = 6;
                    view1.setVisibility(View.VISIBLE);
                    view111.setVisibility(View.GONE);
                    view2.setVisibility(View.GONE);
                    btn_verifycode.setVisibility(View.GONE);
                    username.setText("");
                    verifycode.setText("");
                    username.setVisibility(View.GONE);
                    verifycode.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    verifycode.setHint("请设置密码");
                    btn_login.setText("  修改密码");
                }
                if (msg.what == StatusCode.REQUEST_FORGOTPW_YZ_ERROR){
                    loginProgressDlg.cancel();//进度条取消
                    CommonUtils.getUtilInstance().showToast(APP.context, "验证码验证失败");
                    trueCODE = "";
                }
                if (msg.what == StatusCode.REQUEST_FORGOTPW_SET){
                    loginProgressDlg.cancel();//进度条取消
                    CommonUtils.getUtilInstance().showToast(APP.context, "修改密码成功，去登陆吧~");
                    eventFlag = 1;
                    animationHide.setDuration(500);
                    verifycode.startAnimation(animationHide);
                    verifycode.setVisibility(View.GONE);
                    animationShow.setDuration(500);
                    password.startAnimation(animationShow);
                    password.setVisibility(View.VISIBLE);
                    password.setHint("密码");
                    password.setText("");
                    username.setText("");
                    username.setVisibility(View.VISIBLE);
                    username.setHint("手机号");
                    view111.setVisibility(View.VISIBLE);
                    view1.setVisibility(View.VISIBLE);
                    view2.setVisibility(View.GONE);
                    signup.setText("新用户注册");
                    forgotpassword.setText("忘记密码");
                    btn_login.setText("  登   录");
                }
                if (msg.what == StatusCode.REQUEST_FORGOTPW_SET_ERROR){
                    loginProgressDlg.cancel();//进度条取消
                    CommonUtils.getUtilInstance().showToast(APP.context, "修改密码失败，请重试");
                }
            }
        };

        if (pflag==1){
            eventFlag=666;
            signup.performClick();
        }
    }

    private boolean judgeNameAndPwd(String usrnm, String pwd) {
        for (int i = usrnm.length();--i>=0;){
            if (!Character.isDigit(usrnm.charAt(i))){
                return false;
            }
        }
        return !(usrnm.length() != 11 || pwd.length() < 6);
    }

    @Override
    public void requestFinish(String result, String requestUrl) throws JSONException {

        if (requestUrl.equals(CommonUrl.loginAccount)){//返回登录请求
            JSONObject object = new JSONObject(result);
            int code = Integer.valueOf(object.getString("code"));

            if (code == StatusCode.REQUEST_LOGIN_SUCCESS) {
                Gson gson=new Gson();
                Log.d("ssssssssssssssssss",object.getJSONArray("contents").getJSONObject(0).toString());
                LoginDataModel loginModel=gson.fromJson(object.getJSONArray("contents").getJSONObject(0).toString(),LoginDataModel.class);
                ACache cache=ACache.get(LoginActivity.this);
                cache.put("loginModel",loginModel,ACache.TIME_WEEK*2);
            }else {
                //Looper.prepare();CommonUtils.getUtilInstance().showToast(APP.context, "登录失败");Looper.loop();
                //loginProgressDlg.cancel();//进度条取消
                Message msg=mHandler.obtainMessage();
                msg.what= StatusCode.REQUEST_FAILURE;
                msg.obj=object.getString("contents");
                mHandler.sendMessage(msg);
                return;
            }

            Message msg=mHandler.obtainMessage();
            msg.what= 20010;
            mHandler.sendMessage(msg);

            return;
        }

        if (requestUrl.equals(CommonUrl.forgotpw)){
            try {
                JSONObject object = new JSONObject(result);
                int code = Integer.valueOf(object.getString("code"));
                if(code == StatusCode.REQUEST_FORGOTPW){//手机号验证成功，发送验证码
                    Message msg=mHandler.obtainMessage();
                    msg.what= StatusCode.REQUEST_FORGOTPW;
                    mHandler.sendMessage(msg);
                    return;
                }else if(code == StatusCode.REQUEST_FORGOTPW_ERROR){//服务器错误
                    Message msg=mHandler.obtainMessage();
                    msg.what= StatusCode.REQUEST_FORGOTPW_ERROR;
                    mHandler.sendMessage(msg);
                    return;
                }else if(code == StatusCode.REQUEST_FORGOTPW_NONE){//手机未注册
                    Message msg=mHandler.obtainMessage();
                    msg.what= StatusCode.REQUEST_FORGOTPW_NONE;
                    mHandler.sendMessage(msg);
                    return;
                }else if(code == StatusCode.REQUEST_FORGOTPW_YZ){
                    Message msg=mHandler.obtainMessage();
                    msg.what= StatusCode.REQUEST_FORGOTPW_YZ;
                    mHandler.sendMessage(msg);
                    return;
                }else if(code == StatusCode.REQUEST_FORGOTPW_YZ_ERROR){
                    Message msg=mHandler.obtainMessage();
                    msg.what= StatusCode.REQUEST_FORGOTPW_YZ_ERROR;
                    mHandler.sendMessage(msg);
                    return;
                }else if(code == StatusCode.REQUEST_FORGOTPW_SET){

                    Message msg=mHandler.obtainMessage();
                    msg.what= StatusCode.REQUEST_FORGOTPW_SET;
                    mHandler.sendMessage(msg);
                    return;
                }else if(code == StatusCode.REQUEST_FORGOTPW_SET_ERROR){
                    Message msg=mHandler.obtainMessage();
                    msg.what= StatusCode.REQUEST_FORGOTPW_SET_ERROR;
                    mHandler.sendMessage(msg);
                    return;
                }
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (requestUrl.equals(CommonUrl.registerAccount)){//返回了注册请求
            try {
                JSONObject object = new JSONObject(result);
                int code = Integer.valueOf(object.getString("code"));

                if (code==StatusCode.RECIEVE_REGISTER_SUCCESS)//注册的三次请求
                {
                    if (eventFlag==5)
                    {

                        Gson gson=new Gson();
                        LoginDataModel loginModel = gson.fromJson(object.getJSONArray("contents").getJSONObject(0).toString(),LoginDataModel.class);
                        ACache cache=ACache.get(LoginActivity.this);
                        cache.put("loginModel", loginModel, ACache.TIME_WEEK * 2);
                        Message msg=mHandler.obtainMessage();
                        msg.what= 20015;
                        mHandler.sendMessage(msg);
                        return;
                    }

                    if (eventFlag==3){
                        Message msg=mHandler.obtainMessage();
                        msg.what= 20013;
                        mHandler.sendMessage(msg);
                        return;
                    }

                    if (eventFlag==4) {

                        Message msg=new Message();
                        msg.what=StatusCode.RECIEVE_REGISTER_SUCCESS;
                        mHandler.sendMessage(msg);

                        return;
                    }

                    Message msg=new Message();
                    msg.what=20017;
                    mHandler.sendMessage(msg);

                }else if (code == 10008){
                    eventFlag=4;
                    Message msg=mHandler.obtainMessage();
                    msg.what= 10008;
                    mHandler.sendMessage(msg);
                    return;
                }
                else{
                    if (eventFlag!=5)
                        eventFlag=3;
                    else
                        eventFlag=4;
                    Message msg=mHandler.obtainMessage();
                    msg.what= StatusCode.REQUEST_FAILURE;
                    msg.obj=object.getString("contents");
                    mHandler.sendMessage(msg);
                    return;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void exception(IOException e, String requestUrl){
        Message msg=new Message();
        msg.what=StatusCode.REQUEST_FAILURE;
        msg.obj="网络请求失败";
        mHandler.sendMessage(msg);
    }
}
