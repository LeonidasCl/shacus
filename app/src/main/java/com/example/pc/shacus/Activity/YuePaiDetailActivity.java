package com.example.pc.shacus.Activity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import com.example.pc.shacus.Adapter.JoinUserGridAdapter;
import com.example.pc.shacus.Data.Cache.ACache;
import com.example.pc.shacus.Data.Model.LoginDataModel;
import com.example.pc.shacus.Data.Model.UserModel;
import com.example.pc.shacus.Data.Model.YuePaiDataModel;
import com.example.pc.shacus.Network.NetRequest;
import com.example.pc.shacus.Network.NetworkCallbackInterface;
import com.example.pc.shacus.Network.StatusCode;
import com.example.pc.shacus.R;
import com.example.pc.shacus.Util.CommonUrl;
import com.example.pc.shacus.Util.CommonUtils;
import com.example.pc.shacus.View.FloatMenu.FilterMenu;
import com.example.pc.shacus.View.FloatMenu.FilterMenuLayout;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.LogRecord;

import cn.bingoogolapple.bgabanner.BGABanner;
import cn.bingoogolapple.bgabanner.BGABannerUtil;

public class YuePaiDetailActivity extends AppCompatActivity implements NetworkCallbackInterface.NetRequestIterface{

    HorizontalScrollView horizontalScrollView;
    GridView gridView;
    int dwidth;
    private int num_per_page = 5; // 每行显示个数
    FilterMenuLayout filterMenu;
    private int width;//每列宽度
    //private int total = 10;//列数
    private BGABanner mSideZoomBanner;
    private NetRequest request;
    //private ImageView loadinganim;
    //private AnimationDrawable animator;

    FilterMenu.OnMenuChangeListener listener = new FilterMenu.OnMenuChangeListener() {
        @Override
        public void onMenuItemClick(View view, int position) {
            Toast.makeText(getApplicationContext(), "Touched position " + position, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onMenuCollapse() {

        }

        @Override
        public void onMenuExpand() {

        }
    };

    private TextView textName;
    private TextView textTitle;
    private Handler handler;
    private FrameLayout loading;
    //private List<Drawable> userAvatarList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        request=new NetRequest(this,this);
        String apid=getIntent().getStringExtra("detail");
        ACache cache=ACache.get(this);
        LoginDataModel loginModel=(LoginDataModel)cache.getAsObject("loginModel");
        UserModel userModel=loginModel.getUserModel();
        String authKey=userModel.getAuth_key();
        String uid=userModel.getId();
        int type= StatusCode.REQUEST_YUEPAI_DETAIL;
        Map map=new HashMap();
        map.put("authkey",authKey);
        map.put("uid",uid);
        map.put("type",type);
        map.put("apid", apid);


        setContentView(R.layout.activity_yue_pai_detail);
        request.httpRequest(map, CommonUrl.getYuePaiInfo);


        handler=new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what==StatusCode.REQUEST_DETAIL_SUCCESS){

                    YuePaiDataModel data=(YuePaiDataModel)msg.obj;

                    horizontalScrollView = (HorizontalScrollView) findViewById(R.id.join_user_scroll);
                    gridView = (GridView) findViewById(R.id.grid_join_user_scroll);
                    horizontalScrollView.setHorizontalScrollBarEnabled(false);// 隐藏滚动条
                    dwidth =(int)getScreenDen();
                    width=100;
                    num_per_page = dwidth / width;
                    setUserValue();
                    mSideZoomBanner = (BGABanner) findViewById(R.id.banner_detail_zoom);
                    mSideZoomBanner.setViews(getPics());
                    filterMenu= (FilterMenuLayout)findViewById(R.id.detail_filter_menu);
                    filterMenu.setVisibility(View.VISIBLE);
                    attachMenu(filterMenu);
                    textTitle=(TextView)findViewById(R.id.detail_toolbar_back);
                    textTitle.setText("＜返回");
                    textTitle.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            finish();
                        }
                    });
                    textName=(TextView)findViewById(R.id.detail_toolbar_title);
                    textName.setText(data.getAPtitle());
                    loading=(FrameLayout)findViewById(R.id.loading_layout);
                    loading.setVisibility(View.GONE);
                }
            }
        };


    }

    private FilterMenu attachMenu(FilterMenuLayout filterMenu) {
        return new FilterMenu.Builder(this)
                .addItem(R.drawable.ic_action_info)
                .addItem(R.drawable.ic_action_info)
                .addItem(R.drawable.ic_action_info)
                .attach(filterMenu)
                .withListener(listener)
                .build();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    public float getScreenDen() {
        //dwidth = new DisplayMetrics();
        //getWindowManager().getDefaultDisplay().getMetrics(dwidth);
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics ();
        display.getMetrics(outMetrics);

        float density  = getResources().getDisplayMetrics().density;

        return outMetrics.widthPixels / density;
    }

    private void setUserValue() {
        JoinUserGridAdapter adapter = new JoinUserGridAdapter(this, init(),true);

        gridView.setAdapter(adapter);
        LayoutParams params = new LayoutParams(adapter.getCount() * width, LayoutParams.WRAP_CONTENT);
        gridView.setLayoutParams(params);
        gridView.setColumnWidth(100);
        gridView.setStretchMode(GridView.NO_STRETCH);
        int count = adapter.getCount();
        gridView.setNumColumns(count);
    }

    private List<UserModel> init() {
        List<UserModel> list=new ArrayList<>();
        for (int i=0;i<10;i++)
        {
            UserModel user=new UserModel();
            user.setPhone("phone"+String.valueOf(i));
            user.setId("usrid" + i);
            list.add(user);
        }
        return  list;
    }

    public List<? extends View> getPics() {
        List<ImageView> views = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            views.add(BGABannerUtil.getItemImageView(this, R.drawable.holder));
        }
        return views;
    }

    @Override
    public void requestFinish(String result, String requestUrl) throws JSONException {
        if (requestUrl.equals(CommonUrl.getYuePaiInfo)){
          JSONObject object = new JSONObject(result);
            int code = Integer.valueOf(object.getString("code"));
             if (code==StatusCode.REQUEST_DETAIL_SUCCESS){
                JSONObject content=object.getJSONObject("contents");
                Gson hson=new Gson();
                YuePaiDataModel data=hson.fromJson(content.toString(), YuePaiDataModel.class);
                Message msg=handler.obtainMessage();
                msg.what= StatusCode.REQUEST_DETAIL_SUCCESS;
                msg.obj=data;
                handler.sendMessage(msg);
            }else {
                //
            }
        }
    }

    @Override
    public void exception(IOException e, String requestUrl) {

    }
}
