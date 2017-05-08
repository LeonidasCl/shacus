package com.example.pc.shacus.Activity;

/**
 * shacus项目组
 * 一元复始，赛艇风声又一年
 * Created by cuicui on 2017/1/24.
 */

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pc.shacus.APP;
import com.example.pc.shacus.Data.Cache.ACache;
import com.example.pc.shacus.Data.Model.LoginDataModel;
import com.example.pc.shacus.Data.Model.RecommandModel;
import com.example.pc.shacus.Data.Model.SettingDataModel;
import com.example.pc.shacus.Data.Model.UserModel;
import com.example.pc.shacus.Fragment.ConversationListStaticFragment;
import com.example.pc.shacus.Fragment.MyDisplayFragment;
import com.example.pc.shacus.Fragment.YuePaiFragment;
import com.example.pc.shacus.Network.NetRequest;
import com.example.pc.shacus.Network.NetworkCallbackInterface;
import com.example.pc.shacus.Network.StatusCode;
import com.example.pc.shacus.R;
import com.example.pc.shacus.Util.CommonUrl;
import com.example.pc.shacus.Util.CommonUtils;
import com.example.pc.shacus.Util.SystemBarTintManager;
import com.example.pc.shacus.View.CircleImageView;
import com.example.pc.shacus.swipecards.swipe.CardFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;


import android.os.Handler;
import android.view.LayoutInflater;

import static com.example.pc.shacus.APP.context;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {


    private static final String TAG = "TAG";
    private boolean isLogin = false;
    private UserModel user;
    //管理Fragment
    private FragmentManager fragmentMgr = this.getSupportFragmentManager();
    private FragmentTransaction fragmentTrs;
    //四个功能项Fragment
    private CardFragment cardListFragment;
    private YuePaiFragment yuePaiFragment;
    private MyDisplayFragment mydisplay;
    private ConversationListStaticFragment conversationListStaticFragment;
    private Toolbar toolbar;
    //Fragment切换按钮
    private ImageButton btn_main;
    private ImageButton btn_course;
    private ImageButton btn_yuepai;
    private ImageButton btn_user;
    private ImageButton btn_upload;
    private android.support.v7.app.ActionBar actbar;
    private ACache cache;
    private CircleImageView btnAvartar;
    private TextView textName;
    private Button btnSelect;
    private EditText userSign;
    private String cacheSign = "";
    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;
    private NetRequest netRequest;
    private ProgressDialog progressDlg;
    private boolean signFlag = false;
    private boolean exitFlag = false;

    //底部栏
    private View navibar;

    //筛选菜单
    private CheckBox checkbox_sex_all;
    private CheckBox checkbox_sex_man;
    private CheckBox checkbox_sex_woman;
    private CheckBox checkbox_people_all;
    private CheckBox checkbox_people_photogragher;
    private CheckBox checkbox_people_model;
    private boolean[] sex_selector = {true, false, false};
    private boolean[] people_selector = {true, false, false};
    private boolean[] selector = {true, false, false, true, false, false};

    RelativeLayout display_big_image_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //获取toolbar
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.m_toolbar);
        //将这个toolbar设置为actionBar
        setSupportActionBar(toolbar);
        cache = ACache.get(this);
        actbar = getSupportActionBar();
        if (actbar != null)
            actbar.setDisplayShowTitleEnabled(false);


        textName = (TextView) findViewById(R.id.m_toolbar_title);
        textName.setText("未登录");

        btnSelect = (Button) findViewById(R.id.m_toolbar_selector);
        //为btnSelect设置监听事件
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog builder = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialog).create();
                // 通过LayoutInflater来加载一个xml的布局文件作为一个View对象
                view = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_selector_layout, null);
                // 设置我们自己定义的布局文件作为弹出框的Content
                builder.setView(view);
                //要调用顶部的view，这样才会从顶部的view开始搜索
                checkbox_sex_all = (CheckBox) view.findViewById(R.id.sex_all);
                checkbox_sex_man = (CheckBox) view.findViewById(R.id.sex_man);
                checkbox_sex_woman = (CheckBox) view.findViewById(R.id.sex_woman);
                checkbox_people_all = (CheckBox) view.findViewById(R.id.people_all);
                checkbox_people_photogragher = (CheckBox) view.findViewById(R.id.people_photogragher);
                checkbox_people_model = (CheckBox) view.findViewById(R.id.people_model);
                //保存上次所选择的记录
                if(selector[0]) checkbox_sex_all.setChecked(true);
                if(selector[1]) checkbox_sex_man.setChecked(true);
                if(selector[2]) checkbox_sex_woman.setChecked(true);
                if(selector[3]) checkbox_people_all.setChecked(true);
                if(selector[4]) checkbox_people_photogragher.setChecked(true);
                if(selector[5]) checkbox_people_model.setChecked(true);
                //六个实现每个列表单选的监听方法
                checkbox_people_all.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (checkbox_people_model.isChecked())
                            checkbox_people_model.setChecked(false);
                        if (checkbox_people_photogragher.isChecked()) checkbox_people_photogragher
                                .setChecked(false);
                    }

                });
                checkbox_people_photogragher.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (checkbox_people_all.isChecked()) checkbox_people_all.setChecked(false);
                        if (checkbox_people_model.isChecked()) checkbox_people_model
                                .setChecked(false);
                    }

                });

                checkbox_people_model.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (checkbox_people_all.isChecked()) checkbox_people_all.setChecked(false);
                        if (checkbox_people_photogragher.isChecked()) checkbox_people_photogragher
                                .setChecked(false);
                    }

                });
                checkbox_sex_woman.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (checkbox_sex_all.isChecked()) checkbox_sex_all.setChecked(false);
                        if (checkbox_sex_man.isChecked()) checkbox_sex_man
                                .setChecked(false);
                    }

                });
                checkbox_sex_man.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (checkbox_sex_all.isChecked()) checkbox_sex_all.setChecked(false);
                        if (checkbox_sex_woman.isChecked()) checkbox_sex_woman
                                .setChecked(false);
                    }

                });
                checkbox_sex_all.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (checkbox_sex_woman.isChecked()) checkbox_sex_woman.setChecked(false);
                        if (checkbox_sex_man.isChecked()) checkbox_sex_man
                                .setChecked(false);
                    }

                });
                //确定时保存当前选择状态
                builder.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (checkbox_sex_all.isChecked()) {
                            sex_selector[0] = true;
                            sex_selector[1] = false;
                            sex_selector[2] = false;
                        };
                        if (checkbox_sex_man.isChecked()) {
                            sex_selector[0] = false;
                            sex_selector[1] = true;
                            sex_selector[2] = false;
                        }
                        if (checkbox_sex_woman.isChecked()) {
                            sex_selector[0] = false;
                            sex_selector[1] = false;
                            sex_selector[2] = true;
                        };

                        if (checkbox_people_all.isChecked()) {
                            people_selector[0] = true;
                            people_selector[1] = false;
                            people_selector[2] = false;
                        }
                        if (checkbox_people_photogragher.isChecked()) {
                            people_selector[0] = false;
                            people_selector[1] = true;
                            people_selector[2] = false;
                        };
                        if (checkbox_people_model.isChecked()) {
                            people_selector[0] = false;
                            people_selector[1] = false;
                            people_selector[2] = true;
                        };
                        //转移到一个数组中
                        selector = new boolean[]{sex_selector[0], sex_selector[1], sex_selector[2],
                                people_selector[0], people_selector[1], people_selector[2]};

                        ArrayList<RecommandModel> recommandModel = cardListFragment.getmOurList();
                        cardListFragment.updateOurSelectListView(cardListFragment.selectMethod(recommandModel, selector));
                    }
                });

                builder.show();
                Button btnPositive =
                        builder.getButton(android.app.AlertDialog.BUTTON_POSITIVE);
                btnPositive.setTextColor(getResources().getColor(R.color.ff_white));
                btnPositive.setTextSize(15);

            }
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED||
           ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
         ||ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
         ||ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
         ||ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION},0);
        }



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
            tintManager.setTintResource(R.color.toolbar_white);
            //激活导航栏设置
            tintManager.setNavigationBarTintEnabled(true);
            //设置导航栏颜色
            tintManager.setNavigationBarTintResource(R.color.toolbar_white);
        }
        //5.0版本及以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏颜色
            window.setStatusBarColor(getResources().getColor(R.color.toolbar_white));
            //设置导航栏颜色
            window.setNavigationBarColor(getResources().getColor(R.color.toolbar_white));
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        initNetworkData();

        isLogin = manageLogin();

        initView();
        //这个是父布局，也就是这个Activity的根布局
        mDrawerLayout = (DrawerLayout) findViewById(R.id.main_drawer_layout);
        //找到侧边栏
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        //为侧边栏设置监听，由于此Activity已实现OnNavigationItemSelectedListener接口，可以传this
        navigationView.setNavigationItemSelectedListener(this);
        //这是左上角一个普通的按钮，除了滑动还可以点击它来打开侧滑菜单
        btnAvartar = (CircleImageView) findViewById(R.id.toolbar_btn_avatar);
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

        RongIM.connect(user.getChattoken(), new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {
                Log.e(TAG, "-----onTokenIncorrect-----");
            }

            @Override
            public void onSuccess(String s) {
                Toast.makeText(MainActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "-----onSuccess-----" + s);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Log.e(TAG, "-----onError-----" + errorCode);
                Toast.makeText(MainActivity.this, "登录失败，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });

        fragmentTrs = fragmentMgr.beginTransaction();
        btn_yuepai.setSelected(true);
        toYuePai();
        fragmentTrs.commit();


    }

    @Override
    protected void onResume() {
        super.onResume();

        //获取登录状态添加到侧滑栏信息
        if (user != null) {
            final ACache acache = ACache.get(this);
            LoginDataModel model = (LoginDataModel) acache.getAsObject("loginModel");
            user = model.getUserModel();
            ImageView userImage = (CircleImageView) navigationView.getHeaderView(0).findViewById(R.id.image_user);
            TextView userName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.text_UserName);
            ImageView userLevel = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.image_userLevel);
            userSign = (EditText) navigationView.getHeaderView(0).findViewById(R.id.text_userSign);
            userName.setText(user.getNickName());
            userSign.setText(user.getSign());
            textName.setText(user.getNickName());

            Glide.with(getApplicationContext())
                    .load(user.getHeadImage()).centerCrop()
//                    .placeholder(R.drawable.holder)
                    .error(R.drawable.loading_error)
                    .into(userImage);

            Glide.with(this)
                    .load(user.getHeadImage()).centerCrop()
//                    .placeholder(R.drawable.holder)
                    .error(R.drawable.loading_error)
                    .into(btnAvartar);
            //签名
            netRequest = new NetRequest(new NetworkCallbackInterface.NetRequestIterface() {
                @Override
                public void requestFinish(String result, String requestUrl) throws JSONException {
                    if (requestUrl.equals(CommonUrl.settingChangeNetUrl)) {
                        JSONObject js = new JSONObject(result);
                        String code = js.getString("code");
                        switch (code) {
                            case "10518":
                                progressDlg.dismiss();
                                Log.d("LQQQQQ", "修改成功 ");
                                user.setSign(userSign.getText().toString());
                                LoginDataModel data = (LoginDataModel) acache.getAsObject("loginModel");
                                data.setUserModel(user);
                                acache.put("loginModel", data);
                                cacheSign = userSign.getText().toString();
                                break;
                            case "10514":
                                userSign.setText(cacheSign);
                                cacheSign = "";
                                Log.d("LQQQQQ", "网络异常，修改失败 ");
                                progressDlg.dismiss();
                                signFlag = false;
                                break;
                            default:
                                userSign.setText(cacheSign);
                                cacheSign = "";
                                progressDlg.dismiss();
                                Log.d("LQQQQQ", "网络异常，修改失败 ");
                                signFlag = false;
                                break;
                        }
                    }
                }

                @Override
                public void exception(IOException e, String requestUrl) {

                }
            }, MainActivity.this);
            //设置签名监听事件
            userSign.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!signFlag) {
                        cacheSign = userSign.getText().toString();
                        userSign.setText("");
                        userSign.setText(cacheSign);
                        signFlag = true;
                    } else if (signFlag) {
                        userSign.setText(cacheSign);
                        cacheSign = "";
                        signFlag = false;
                    }
                    Log.d("LQQQQQQ", "on FocusChange: ");
                }
            });
            userSign.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (userSign.getText().length() > 30) {
                        userSign.setText(cacheSign);
                        cacheSign = "";
                        signFlag = true;
                        CommonUtils.getUtilInstance().showToast(MainActivity.this, "签名长度不能超过30");
                    } else if (userSign.getText().length() < 1) {
                        userSign.setText(cacheSign);
                        cacheSign = "";
                        signFlag = true;
                        CommonUtils.getUtilInstance().showToast(MainActivity.this, "签名长度不能为空");
                    } else {
                        HashMap map = new HashMap();
                        map.put("uid", user.getId());
                        map.put("authkey", user.getAuth_key());
                        map.put("sign", userSign.getText().toString());
                        map.put("type", "10517");
                        netRequest.httpRequest(map, CommonUrl.settingChangeNetUrl);
                        progressDlg = ProgressDialog.show(MainActivity.this, "上传签名", "正在上传签名", true, true, new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                userSign.setText(cacheSign);
                                CommonUtils.getUtilInstance().showToast(MainActivity.this, "取消更改");
                            }
                        });
                        signFlag = true;
                    }
                    Log.d("LQQQQQQ", "onEditorAction: ");
                    return false;
                }
            });


            // 和设置缓存中
            SettingDataModel setModel = new SettingDataModel();
            setModel.setMessageInform(false);
            setModel.setPhoneVisible(false);
            setModel.setUserPhone(user.getPhone());
            setModel.setUserID(user.getId());
            acache.put("settingModel", setModel);
        }

    }

    //登录完成后在这里处理UI的更新
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        isLogin = manageLogin();

        String hint = intent.getStringExtra("result");
        CommonUtils.getUtilInstance().showToast(APP.context, hint);
        yuePaiFragment.getRankFrag().doRefresh();
    }

//    @Override//不要忘记渲染APP动作条的overflow菜单
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_appbar, menu);
//        return true;
//    }

    private void initNetworkData() {
    }


    private boolean manageLogin() {
        ACache cache = ACache.get(MainActivity.this);
        LoginDataModel loginModel = (LoginDataModel) cache.getAsObject("loginModel");
        user = loginModel.getUserModel();
        if (user != null) {
            textName.setText(user.getNickName());
            //cache.put("userModel", user);
            return true;
        } else return false;
    }


    private void initView() {

        btn_main = (ImageButton) findViewById(R.id.button_main);
        btn_course = (ImageButton) findViewById(R.id.button_find);
        btn_user = (ImageButton) findViewById(R.id.button_user);
        btn_yuepai = (ImageButton) findViewById(R.id.button_yuepai);
        btn_upload = (ImageButton) findViewById(R.id.button_upload);

        btn_main.setOnClickListener(this);
        btn_course.setOnClickListener(this);
        btn_user.setOnClickListener(this);
        btn_yuepai.setOnClickListener(this);
        btn_upload.setOnClickListener(this);

        navibar = (View)findViewById(R.id.fragment_list);

    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.button_upload) {
            toUpload();
            return;
        }

        fragmentTrs = fragmentMgr.beginTransaction();
        setSelected();
        switch (v.getId()) {
            case R.id.button_main:
                btn_main.setSelected(true);
                btnSelect.setVisibility(View.VISIBLE);
                toMain();
                break;
            case R.id.button_find:
                btn_course.setSelected(true);
                btnSelect.setVisibility(View.GONE);
                toMine();
                break;
            case R.id.button_yuepai:
                btn_yuepai.setSelected(true);
                btnSelect.setVisibility(View.GONE);
                toYuePai();
                break;
            case R.id.button_user:
                btn_user.setSelected(true);
                btnSelect.setVisibility(View.GONE);
                toUser();
                break;
        }
        fragmentTrs.commit();

    }

    private void toUpload() {
        startActivity(new Intent(this, CreateYuePaiActivity.class));
    }

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            exitFlag = false;
            handler.removeCallbacks(runnable);
        }
    };

    @Override
    public void onBackPressed() {
        display_big_image_layout = cardListFragment.getDisplay_big_image_layout();
        if(display_big_image_layout.getVisibility() == View.VISIBLE){
            display_big_image_layout.setVisibility(View.GONE);
            navibar.setVisibility(View.VISIBLE);
        }
        else{
            if (!exitFlag) {
                exitFlag = true;
                CommonUtils.getUtilInstance().showToast(this, "再点击一次返回键退出应用");
                handler.postDelayed(runnable, 3000);
            } else {
                finish();
            }
        }
    }

    private void toMain() {
        if (cardListFragment == null) {
            cardListFragment = new CardFragment();
            //fragmentTrs.commitAllowingStateLoss();
            fragmentTrs.add(R.id.fl_content, cardListFragment);
            cardListFragment.setNavibar(navibar);   //在initView()之后调用
        } else {
            fragmentTrs.show(cardListFragment);
        }
    }

    private void toMine() {
        //个人主页，1.23添加
        if (mydisplay == null) {
            mydisplay = new MyDisplayFragment();
            fragmentTrs.add(R.id.fl_content, mydisplay);
        } else {
            btn_course.setSelected(true);
            fragmentTrs.show(mydisplay);
        }
    }

    private void toYuePai() {
        if (yuePaiFragment == null) {
            yuePaiFragment = new YuePaiFragment();
            fragmentTrs.add(R.id.fl_content, yuePaiFragment);
        } else {
            btn_yuepai.setSelected(true);
            fragmentTrs.show(yuePaiFragment);
        }
    }

    private void toUser() {
        if (conversationListStaticFragment == null) {
            conversationListStaticFragment = new ConversationListStaticFragment();
            fragmentTrs.add(R.id.fl_content, conversationListStaticFragment);
        } else {
            fragmentTrs.show(conversationListStaticFragment);
        }
    }

    private void setSelected() {
        btn_main.setSelected(false);
        btn_course.setSelected(false);
        btn_yuepai.setSelected(false);
        btn_user.setSelected(false);
        if (cardListFragment != null) {
            fragmentTrs.hide(cardListFragment);
        }
        if (mydisplay != null) {
            fragmentTrs.hide(mydisplay);
        }
        if (conversationListStaticFragment != null) {
            fragmentTrs.hide(conversationListStaticFragment);
        }
        if (yuePaiFragment != null) {
            fragmentTrs.hide(yuePaiFragment);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //重载这个函数以识别requestCode并进行相应操作
        if (requestCode == 1) {
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
            Intent intent = new Intent(getApplicationContext(), PersonalInfoActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_orderList) {
            Intent intent = new Intent(getApplicationContext(), OrdersActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_myConcern) {
            Intent intent = new Intent(getApplicationContext(), FollowActivity.class);
            intent.putExtra("user", "myself");
            intent.putExtra("activity", "following");
            startActivity(intent);
        } else if (id == R.id.nav_myFans) {
            Intent intent = new Intent(getApplicationContext(), FollowActivity.class);
            intent.putExtra("user", "myself");
            intent.putExtra("activity", "follower");
            startActivity(intent);
        } else if (id == R.id.nav_myCollection) {
            Intent intent = new Intent(getApplicationContext(), FavoritemActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_Setting) {
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_sharing) {
            Intent intent = new Intent(getApplicationContext(), ShareActivity.class);
            startActivity(intent);
        } else if (id == R.id.debug_photoset_detail) {//临时作品集入口
            Intent intent = new Intent(getApplicationContext(), PhotosetOverviewActivity.class);
            intent.putExtra("uid", Integer.valueOf(user.getId()));
            startActivity(intent);
        } else if (id == R.id.debug_photos_detail) {//临时个人照片入口
            Intent intent = new Intent(getApplicationContext(), PhotoselfDetailActivity.class);
            intent.putExtra("uid", Integer.valueOf(user.getId()));
            startActivity(intent);
        } else if (id == R.id.nav_Logout) {
            //登出请求
            ACache cache = ACache.get(APP.context);
            cache.clear();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
            return true;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.main_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}



