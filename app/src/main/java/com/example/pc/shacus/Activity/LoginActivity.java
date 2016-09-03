package com.example.pc.shacus.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
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
 *
 */
public class LoginActivity extends AppCompatActivity implements NetworkCallbackInterface.NetRequestIterface{

    private TextView username;
    private TextView password;
    private TextView forgotpassword;
    private TextView signup;
    private TextView verifycode;
    private Button btn_login;
    private Button btn_verifycode;
    private NetRequest requestFragment;
    private Handler mHandler;
    private ProgressDialog loginProgressDlg;
    private int loginReturn;
    private ImageView loginbtnimg;
    private CountDownTimer timeCount;
    private String phone;
    private int eventFlag=1;//1为登录 2为忘记密码 3为验证注册 4为验证注册通过 5为提交注册通过
    TranslateAnimation animationHide=new TranslateAnimation(Animation.RELATIVE_TO_SELF,
            0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
            Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
            -1.0f);
    TranslateAnimation animationShow=new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
            Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
            -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);


  /*  private DialogInterface.OnCancelListener cancelListener = new DialogInterface.OnCancelListener() {
        @Override
        public void onCancel(DialogInterface dialog) {
            if(loginReturn==0){
                CommonUtils.getUtilInstance().showToast(APP.context, getString(R.string.login_success));
                finish();
            }else{
                CommonUtils.getUtilInstance().showToast(APP.context, getString(R.string.login_fail));
            }
        }
    };*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        Intent intent=getIntent();
        int method=intent.getIntExtra("method", StatusCode.STATUS_ERROR);
        switch (method){
            case StatusCode.STATUS_LOGIN:
                eventFlag=1;
                break;
            case StatusCode.STATUS_REGISTER:
                eventFlag=3;
                break;
        }

        username=(TextView)findViewById(R.id.login_username);
        password=(TextView)findViewById(R.id.login_password);
        verifycode=(TextView)findViewById(R.id.register_verifycode);
        btn_login=(Button)findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map map = new HashMap();
                if (eventFlag==1){
                //检查输入格式，发弹窗请求到handler，并发网络请求
                String usrnm=username.getText().toString();
                String pwd=password.getText().toString();
                map.put("phone",usrnm);
                map.put("password",pwd);
                map.put("askCode",StatusCode.REQUEST_LOGIN);
                loginProgressDlg = ProgressDialog.show(LoginActivity.this, "shacus", "处理中", true, false);
                requestFragment.httpRequest(map, CommonUrl.loginAccount);
                }
                if (eventFlag==2){
                    return;
                }
                if (eventFlag==3){
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
                    if (!password.equals("")&&!nickName.equals("")&&!phone.equals("")){
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
                        CommonUtils.getUtilInstance().showToast(LoginActivity.this,"请完善注册信息");
                        return;
                    }
                }
            }
        });
        btn_verifycode=(Button)findViewById(R.id.btn_verify_code);
        btn_verifycode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map map=new HashMap();
                if (eventFlag==3){
                    String usrnm=username.getText().toString();
                    if (!usrnm.equals(""))
                    {
                        map.put("phone",usrnm);
                        map.put("type",StatusCode.REQUEST_REGISTER_VERIFYA);
                        requestFragment.httpRequest(map, CommonUrl.registerAccount);
                        loginProgressDlg = ProgressDialog.show(LoginActivity.this, "shacus", "处理中", true, false);
                        timeCount.start();
                    }else {
                        CommonUtils.getUtilInstance().showToast(LoginActivity.this,"请输入用户名");
                    }
                }
            }
        });
        requestFragment=new NetRequest(this,this);
        loginbtnimg=(ImageView)findViewById(R.id.loginimgview);
        loginbtnimg.bringToFront();
        forgotpassword=(TextView)findViewById(R.id.btn_forgot);
        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(eventFlag==2){
                    signup.setVisibility(View.VISIBLE);
                    eventFlag=1;
                    forgotpassword.setText("忘记密码了");
                    animationHide.setDuration(500);
                    verifycode.startAnimation(animationHide);
                    verifycode.setVisibility(View.GONE);
                    animationShow.setDuration(500);
                    password.startAnimation(animationShow);
                    btn_verifycode.setVisibility(View.GONE);
                    password.setVisibility(View.VISIBLE);
                    return;
                }
                if(eventFlag!=2){
                signup.setVisibility(View.INVISIBLE);
                eventFlag=2;
                animationHide.setDuration(500);
                password.startAnimation(animationHide);
                password.setVisibility(View.GONE);
                animationShow.setDuration(500);
                verifycode.startAnimation(animationShow);
                verifycode.setVisibility(View.VISIBLE);
                btn_verifycode.setVisibility(View.VISIBLE);
                forgotpassword.setText("找回了密码");}
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
                signup.setText("老用户登录");
                forgotpassword.setVisibility(View.INVISIBLE);
                btn_verifycode.setVisibility(View.VISIBLE);
                btn_login.setText("  注   册");
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
                    forgotpassword.setVisibility(View.VISIBLE);
                    btn_login.setText("  登   录");
                    return;
                }
            }
        });
        if (eventFlag==3)
            signup.performClick();
        timeCount=new CountDownTimer(60000,1000) {
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
                btn_verifycode.setBackground(getResources().getDrawable(R.drawable.shape_verifycode));
                btn_verifycode.setClickable(true);
            }
        };
        mHandler=new Handler(){
            @Override
            public void handleMessage(Message msg){
              if (msg.what==StatusCode.RECIEVE_REGISTER_SUCCESS){
                  btn_verifycode.setVisibility(View.GONE);
                  forgotpassword.setVisibility(View.INVISIBLE);
                  signup.setVisibility(View.INVISIBLE);
                  username.setText("");
                  verifycode.setText("");
                  username.setHint("请指定昵称");
                  verifycode.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                  verifycode.setHint("请设置密码");
                  btn_login.setText("  注   册");
              }
          }
        };
    }

    @Override
    public void requestFinish(String result, String requestUrl) throws JSONException {
        if (requestUrl.equals(CommonUrl.loginAccount)) {//返回登录请求
                JSONObject object = new JSONObject(result);
                int code = Integer.valueOf(object.getString("code"));
            if (code==StatusCode.REQUEST_LOGIN_SUCCESS){
                JSONArray content=object.getJSONArray("contents");
                //还有其它的JSONArray比如榜单、广告栏等初始化数据未接
                //String userJSON=content.getJSONObject(0).toString();
                // Gson gson=new Gson();
                //LoginDataModel loginDataModel=gson.fromJson(userJSON,LoginDataModel.class);
                //UserModel user = loginDataModel.getUserModel();

                //JSONArray photoList=content.getJSONObject(0).getJSONArray("photoList");
                //List<PhotographerModel>
                //for (int i=0;i<photoList.length();i++){

//                }

                ACache cache=ACache.get(LoginActivity.this);
                cache.put("loginModel",content.getJSONObject(0),ACache.TIME_WEEK*2);
            }else {
                //Looper.prepare();CommonUtils.getUtilInstance().showToast(APP.context, content.toString());Looper.loop();
                loginProgressDlg.cancel();//进度条取消
                String str=object.getString("contents");
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("result","登录失败:"+str);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                return;
            }

                loginProgressDlg.cancel();//进度条取消
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("result", "登录成功");
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);

        }
        if (requestUrl.equals(CommonUrl.registerAccount)) {//返回了注册请求
            try {
                JSONObject object = new JSONObject(result);
                int code = Integer.valueOf(object.getString("code"));
                JSONArray content=null;
                String contentstr=null;
                UserModel user=null;
                LoginDataModel loginModel;
                String userJSON=null;

                //还有其它的JSONArray比如榜单、广告栏等初始化数据未接
                Gson gson=new Gson();
                if (eventFlag==5)
                {
                    content = object.getJSONArray("contents");
                    userJSON=content.getJSONObject(0).toString();
                    loginModel = gson.fromJson(userJSON.toString(), LoginDataModel.class);
                    //user = loginModel.getUserModel();
                }else
                    contentstr=object.getString("contents");

                if (code==StatusCode.RECIEVE_REGISTER_SUCCESS)//注册的三次请求
                {
                    if (eventFlag==3){
                        loginProgressDlg.cancel();//进度条取消
                        Looper.prepare();
                        CommonUtils.getUtilInstance().showToast(APP.context, "验证码短信已发送");
                        Looper.loop();
                    //eventFlag=4;
                    return;
                    }

                    if (eventFlag==4) {
                        loginProgressDlg.cancel();//进度条取消
                        Message msg=new Message();
                        msg.what=StatusCode.RECIEVE_REGISTER_SUCCESS;
                        mHandler.sendMessage(msg);
                        Looper.prepare();
                        CommonUtils.getUtilInstance().showToast(APP.context, "验证成功!请设置昵称和密码!"

                        );
                        Looper.loop();
                        return;
                    }

                    if (eventFlag==5) {
                        loginProgressDlg.cancel();//进度条取消
                        //解析并缓存用户信息、登录首信息//
                        ACache cache=ACache.get(LoginActivity.this);
                        cache.put("loginModel", content.getJSONObject(0), ACache.TIME_WEEK * 2);

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("result","注册成功");
                        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent);
                        /*finish();

                        Looper.prepare();
                        CommonUtils.getUtilInstance().showToast(APP.context, "注册成功");
                        Looper.loop();*/
                        return;
                    }

                    loginProgressDlg.dismiss();//进度条取消
                }else{
                    if (eventFlag!=5)
                        eventFlag=3;
                    else
                        eventFlag=4;
                    loginProgressDlg.cancel();//进度条取消
                    Looper.prepare();CommonUtils.getUtilInstance().showToast(APP.context, "请求失败");Looper.loop();
                    return;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void exception(IOException e, String requestUrl) {

    }
}
