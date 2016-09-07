package com.example.pc.shacus.Activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pc.shacus.APP;
import com.example.pc.shacus.Data.Cache.ACache;
import com.example.pc.shacus.Data.Model.LoginDataModel;
import com.example.pc.shacus.Data.Model.SettingDataModel;
import com.example.pc.shacus.Fragment.CourseFragment;
import com.example.pc.shacus.Data.Model.UserModel;
import com.example.pc.shacus.Fragment.HomeFragment;
import com.example.pc.shacus.Fragment.UserFragment;
import com.example.pc.shacus.Fragment.YuePaiFragment;
import com.example.pc.shacus.Network.NetRequest;
import com.example.pc.shacus.Network.NetworkCallbackInterface;
import com.example.pc.shacus.Network.StatusCode;
import com.example.pc.shacus.R;
import com.example.pc.shacus.Util.CommonUtils;
import com.example.pc.shacus.Util.SystemBarTintManager;
import com.example.pc.shacus.View.CircleImageView;

import org.json.JSONException;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,NavigationView.OnNavigationItemSelectedListener{


    private static final String TAG = "TAG";
    private boolean isLogin=false;
    private UserModel user;

    //管理Fragment
    private FragmentManager fragmentMgr = this.getSupportFragmentManager();
    private FragmentTransaction fragmentTrs;

    //四个功能项Fragment
    private HomeFragment mainFragmentNavigation;
    private YuePaiFragment yuePaiFragment;
    private CourseFragment courseFragment;
    private UserFragment userFragment;
    private Toolbar toolbar;
    //Fragment切换按钮
    private ImageButton btn_main;
    private ImageButton btn_course;
    private ImageButton btn_yuepai;
    private ImageButton btn_user;
    private android.support.v7.app.ActionBar actbar;
    private TextView toolbarTitle;
    private ACache cache;
    private CircleImageView btnAvartar;
    private TextView textName;

    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //获取toolbar
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.m_toolbar);
        //将这个toolbar设置为actionBar
        setSupportActionBar(toolbar);
        cache=ACache.get(this);

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
        //这个是父布局，也就是这个Activity的根布局
        mDrawerLayout = (DrawerLayout) findViewById(R.id.main_drawer_layout);
        //找到侧边栏
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        //为侧边栏设置监听，由于此Activity已实现OnNavigationItemSelectedListener接口，可以传this
        navigationView.setNavigationItemSelectedListener(this);
        //这是左上角一个普通的按钮，除了滑动还可以点击它来打开侧滑菜单
        btnAvartar= (CircleImageView) findViewById(R.id.toolbar_btn_avatar);
        btnAvartar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLogin) {
                    //如果用户已经登录，打开侧滑菜单
                    mDrawerLayout.openDrawer(navigationView);
                } else {
                    //如果没有登录，跳转到注册
                    Intent intent = new Intent("android.intent.action.LOGINACTIVITY");
                    intent.putExtra("method", StatusCode.STATUS_LOGIN);
                    startActivity(intent);
                }

            }
        });
        //获取登录状态添加到侧滑栏信息
        if (user!=null) {
            ACache acache=ACache.get(this);
            ImageView userImage = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.image_user);
            TextView userName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.text_UserName);
            ImageView userLevel = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.image_userLevel);
            TextView userSign = (TextView) navigationView.getHeaderView(0).findViewById(R.id.text_userSign);
            userName.setText(user.getNickName());
            userSign.setText(user.getSign());

            // 和设置缓存中
            SettingDataModel setModel = new SettingDataModel();
            setModel.setMessageInform(false);
            setModel.setPhoneVisible(false);
            setModel.setUserPhone(user.getPhone());
            setModel.setUserID(user.getId());
            acache.put("settingModel", setModel);
        }


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
        yuePaiFragment.getRankFrag().doRefresh();
    }

    @Override//不要忘记渲染APP动作条的overflow菜单
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_appbar, menu);
        return true;
    }

    private void initNetworkData(){
    }


    private boolean manageLogin() throws JSONException {
        ACache cache=ACache.get(MainActivity.this);
        LoginDataModel loginModel=(LoginDataModel)cache.getAsObject("loginModel");
        user=loginModel.getUserModel();
        if (user!=null)
        {
            textName.setText(user.getNickName());
            //cache.put("userModel", user);
            return true;
        }
        else return false;
    }


    private void initView(){

        btn_main=(ImageButton)findViewById(R.id.button_main);
        btn_course =(ImageButton)findViewById(R.id.button_find);
        btn_user=(ImageButton)findViewById(R.id.button_user);
        btn_yuepai=(ImageButton)findViewById(R.id.button_yuepai);

        btn_main.setOnClickListener(this);
        btn_course.setOnClickListener(this);
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
                btn_course.setSelected(true);
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
        if(courseFragment == null){
            courseFragment = new CourseFragment();
            fragmentTrs.add(R.id.fl_content, courseFragment);
        }else{
            btn_course.setSelected(true);
            fragmentTrs.show(courseFragment);
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
        btn_course.setSelected(false);
        btn_yuepai.setSelected(false);
        btn_user.setSelected(false);
        if(mainFragmentNavigation != null){
            fragmentTrs.hide(mainFragmentNavigation);
        }
        if(courseFragment != null){
            fragmentTrs.hide(courseFragment);
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

    //这里是导航侧边栏的回调
   @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();


        if (id == R.id.nav_personalInfo) {
            Intent intent=new Intent(getApplicationContext(),PersonalInfoActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_orderList) {
            Intent intent=new Intent(getApplicationContext(),OrdersActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_message) {

        } else if (id == R.id.nav_myConcern) {
            Intent intent=new Intent(getApplicationContext(),FollowActivity.class);
            intent.putExtra("user","myself");
            intent.putExtra("activity","following");
            startActivity(intent);
        } else if (id==R.id.nav_myFans){
            Intent intent=new Intent(getApplicationContext(),FollowActivity.class);
            intent.putExtra("user","myself");
            intent.putExtra("activity","follower");
            startActivity(intent);
        } else if (id == R.id.nav_myCollection) {
            Intent intent=new Intent(getApplicationContext(),FavoritemActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_Setting) {
            Intent intent=new Intent(getApplicationContext(),SettingsActivity.class);
            startActivity(intent);
        } else if(id==R.id.nav_Logout){
            //登出请求
            NetRequest netRequest=new NetRequest(new NetworkCallbackInterface.NetRequestIterface() {
                @Override
                public void requestFinish(String result, String requestUrl) throws JSONException {

                }

                @Override
                public void exception(IOException e, String requestUrl) {

                }
            }, this);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.main_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
