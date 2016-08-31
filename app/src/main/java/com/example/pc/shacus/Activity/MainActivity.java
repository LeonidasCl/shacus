package com.example.pc.shacus.Activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.pc.shacus.APP;
import com.example.pc.shacus.Data.Cache.ACache;
import com.example.pc.shacus.Data.Model.LoginDataModel;
import com.example.pc.shacus.Fragment.FindFragment;
import com.example.pc.shacus.Data.Model.UserModel;
import com.example.pc.shacus.Fragment.HomeFragment;
import com.example.pc.shacus.Fragment.UserFragment;
import com.example.pc.shacus.Fragment.YuePaiFragment;
import com.example.pc.shacus.Network.StatusCode;
import com.example.pc.shacus.R;
import com.example.pc.shacus.Util.CommonUtils;
import com.example.pc.shacus.Util.SystemBarTintManager;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private static final String TAG = "TAG";
    private boolean isLogin=false;
    private UserModel user;

    //管理Fragment
    private FragmentManager fragmentMgr = this.getSupportFragmentManager();
    private FragmentTransaction fragmentTrs;

    //四个功能项Fragment
    private HomeFragment mainFragmentNavigation;
    private YuePaiFragment yuePaiFragment;
    private FindFragment findFragment;
    private UserFragment userFragment;
    private Toolbar toolbar;
    //Fragment切换按钮
    private ImageButton btn_main;
    private ImageButton btn_find;
    private ImageButton btn_yuepai;
    private ImageButton btn_user;
    private android.support.v7.app.ActionBar actbar;
    private TextView toolbarTitle;
    private ACache cache;
    private ImageButton btnAvartar;
    private TextView textName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //获取toolbar
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.m_toolbar);
        //将这个toolbar设置为actionBar
        setSupportActionBar(toolbar);
        cache=ACache.get(this);
  /*      //尝试获取toolbar的标题TextView
        CharSequence actionbarTitle =  toolbar.getTitle();
        for(int i= 0; i < toolbar.getChildCount(); i++){
            View v = toolbar.getChildAt(i);
            if(v != null && v instanceof TextView){
                TextView t = (TextView) v;
                CharSequence title = t.getText();
                if(!TextUtils.isEmpty(title) && actionbarTitle.equals(title) && t.getId() == View.NO_ID){
                    //Toolbar does not assign id to views with layout params SYSTEM, hence getId() == View.NO_ID
                    //in same manner subtitle TextView can be obtained.
                    toolbarTitle=t;
                }
            }
        }*/

        actbar=getSupportActionBar();
        if (actbar!=null)
        actbar.setDisplayShowTitleEnabled(false);


       textName=(TextView)findViewById(R.id.m_toolbar_title);
        textName.setText("未登录");

        Window window = getWindow();
        //4.4版本及以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            //创建状态栏的管理实例
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            //激活状态栏设置
            tintManager.setStatusBarTintEnabled(true);
            //设置状态栏颜色
            tintManager.setTintResource(R.color.gold);
            //激活导航栏设置
            tintManager.setNavigationBarTintEnabled(true);
            //设置导航栏颜色
            tintManager.setNavigationBarTintResource(R.color.gold);
        }
        //5.0版本及以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏颜色
            window.setStatusBarColor(getResources().getColor(R.color.gold));
            //设置导航栏颜色
            window.setNavigationBarColor(getResources().getColor(R.color.gold));
        }
        //initLocalData();

        initNetworkData();
        try {
            isLogin=manageLogin();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        initView();
        btnAvartar=(ImageButton)findViewById(R.id.toolbar_btn_avatar);
        btnAvartar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isLogin){

                    }else {
                        Intent intent = new Intent("android.intent.action.LOGINACTIVITY");
                        intent.putExtra("method", StatusCode.STATUS_LOGIN);/////////////
                        startActivity(intent);
                    }

                }
            });

        fragmentTrs=fragmentMgr.beginTransaction();
        btn_yuepai.setSelected(true);
        toYuePai();
        fragmentTrs.commit();

    }

    //登录完成后在这里处理UI的更新
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        try {
            isLogin=manageLogin();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String hint=intent.getStringExtra("result");
        CommonUtils.getUtilInstance().showToast(APP.context, hint);
    }

    @Override//不要忘记渲染APP动作条的overflow菜单
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_appbar, menu);
        return true;
    }

    private void initNetworkData() {
    }


    private boolean manageLogin() throws JSONException {
        Gson gson=new Gson();
        JSONObject userStr=cache.getAsJSONObject("loginModel");
        if (userStr!=null)
        {
            LoginDataModel model=gson.fromJson(userStr.toString(), LoginDataModel.class);
            user=model.getUserModel();
            textName.setText(user.getNickName());
            return true;
        }
        else return false;
    }

    private void initView() {

        btn_main=(ImageButton)findViewById(R.id.button_main);
        btn_find=(ImageButton)findViewById(R.id.button_find);
        btn_user=(ImageButton)findViewById(R.id.button_user);
        btn_yuepai=(ImageButton)findViewById(R.id.button_yuepai);

        btn_main.setOnClickListener(this);
        btn_find.setOnClickListener(this);
        btn_user.setOnClickListener(this);
        btn_yuepai.setOnClickListener(this);

    }

    @Override
    public void onClick(View v){

        fragmentTrs=fragmentMgr.beginTransaction();
        setSelected();
        switch (v.getId()) {
            case R.id.button_main:
                btn_main.setSelected(true);
                toMain();
                break;
            case R.id.button_find:
                btn_find.setSelected(true);
                toFind();
                break;
            case R.id.button_yuepai:
                btn_yuepai.setSelected(true);
                toYuePai();
                break;
            case R.id.button_user:
                btn_user.setSelected(true);
                toUser();
                break;
        }
        fragmentTrs.commit();

    }

    private void toMain(){
     if(mainFragmentNavigation == null){
            mainFragmentNavigation = new HomeFragment();
            fragmentTrs.add(R.id.fl_content, mainFragmentNavigation);
        }else{
            fragmentTrs.show(mainFragmentNavigation);
        }
    }

    private void toFind(){
        if(findFragment == null){
            findFragment = new FindFragment();
            fragmentTrs.add(R.id.fl_content, findFragment);
        }else{
            btn_find.setSelected(true);
            fragmentTrs.show(findFragment);
        }
    }

    private void toYuePai(){
        if(yuePaiFragment == null){
            yuePaiFragment = new YuePaiFragment();
            fragmentTrs.add(R.id.fl_content, yuePaiFragment);
        }else{
            btn_yuepai.setSelected(true);
            fragmentTrs.show(yuePaiFragment);
        }
    }

    private void toUser(){
        if(userFragment == null){
            userFragment = new UserFragment();
            fragmentTrs.add(R.id.fl_content, userFragment);
        }else{
            fragmentTrs.show(userFragment);
        }
    }

    private void setSelected(){
        btn_main.setSelected(false);
        btn_find.setSelected(false);
        btn_yuepai.setSelected(false);
        btn_user.setSelected(false);
        if(mainFragmentNavigation != null){
            fragmentTrs.hide(mainFragmentNavigation);
        }
        if(findFragment != null){
            fragmentTrs.hide(findFragment);
        }
        if(userFragment != null){
            fragmentTrs.hide(userFragment);
        }
        if(yuePaiFragment != null){
            fragmentTrs.hide(yuePaiFragment);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //重载这个函数以识别requestCode并进行相应操作
        if(requestCode == 1) {
            finish();//结束父activity自身
            //重新创建来刷新UI
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            startActivity(intent);


        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:

                return true;

            case R.id.action_settings:

                return true;

            default:

                return super.onOptionsItemSelected(item);

        }
    }

}
