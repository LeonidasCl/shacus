package com.example.pc.shacus.Activity;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.TabWidget;

import com.example.pc.shacus.Data.Cache.ACache;
import com.example.pc.shacus.Data.Model.CoursesModel;
import com.example.pc.shacus.Network.NetRequest;
import com.example.pc.shacus.Network.NetworkCallbackInterface;
import com.example.pc.shacus.Network.StatusCode;
import com.example.pc.shacus.R;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 孙启凡
 */
//课程界面（二级）

public class CoursesActivity extends AppCompatActivity implements  NetworkCallbackInterface.NetRequestIterface{

    private ImageButton returnButton;
    private ImageButton imageButton1;
    private ViewPager viewPager = null;
    private List<View> viewContainter = new ArrayList<>();   //存放容器
    private ViewPagerAdapter viewPagerAdapter = null;   //声明适配器

    private TabHost mTabHost = null;
    private TabWidget mTabWidget = null;

    private RecyclerView recyclerView1;
    private CourseListAdapter courseListAdapter1;
    List<CoursesModel> courseItemList1;
    RecyclerView.LayoutManager layoutManager1;

    private ACache aCache;
    private NetRequest netRequest;
    public int index = StatusCode.REQUEST_FAVOR_YUEPAI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);
        returnButton=(ImageButton)findViewById(R.id.return1);
        imageButton1=(ImageButton)findViewById(R.id.imagebutton);


        netRequest = new NetRequest(CoursesActivity.this,CoursesActivity.this);
        aCache = ACache.get(CoursesActivity.this);

        //初始化TabHost
        initTabHost();
        //绑定组件
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        //初始化viewPager
        initViewPagerContainter();
        viewPagerAdapter = new ViewPagerAdapter();
        //设置adapter适配器
        viewPager.setAdapter(viewPagerAdapter);

        //设置viewpager的监听器
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            //当滑动切换时
            @Override
            public void onPageSelected(int position) {
                mTabWidget.setCurrentTab(position);

                //else index =
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        //TabHost的监听事件
        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if (tabId.equals("course")){
                    viewPager.setCurrentItem(0);

                }else if (tabId.equals("finished")){
                    viewPager.setCurrentItem(1);
                    //index =
                }
            }
        });
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //解决开始时初始化不显示viewPager
//        mTabHost.setCurrentTab(1);
//        mTabHost.setCurrentTab(0);

    }

    //初始化viewPager
    private void initViewPagerContainter() {
        //建立三个view，并找到对应的
        View view_1 = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_recyclerview_container,null);
        View view_2 = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_recyclerview_container,null);

        //加入ViewPager的容器
        viewContainter.add(view_1);
        viewContainter.add(view_2);
//        viewContainter.add(view3);

        recyclerView1 = (RecyclerView) view_1.findViewById(R.id.recyclerView);

        initInfo();

    }

    //初始化TabHost
    private void initTabHost() {
        //绑定id
        mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup();
        mTabWidget = mTabHost.getTabWidget();
        /**
         * newTabSpec（）   就是给每个Tab设置一个ID
         * setIndicator()   每个Tab的标题
         * setCount()       每个Tab的标签页布局
         */
        mTabHost.addTab(mTabHost.newTabSpec("course").setContent(R.id.course_tab1).setIndicator("收藏课程"));
        mTabHost.addTab(mTabHost.newTabSpec("finished").setContent(R.id.finished_tab2).setIndicator("完成列表"));
    }

    //获得收藏信息
    private void initInfo() {
        courseItemList1 = new ArrayList<>();


        layoutManager1 = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView1.setLayoutManager(layoutManager1);

        courseListAdapter1 = new CourseListAdapter(courseItemList1, CoursesActivity.this);
        recyclerView1.setAdapter(courseListAdapter1);

    }
        //内部类实现viewpager的适配器
        private class ViewPagerAdapter extends PagerAdapter {

            //该方法决定并返回viewPager中组件的数量
            @Override
            public int getCount() {
                return viewContainter.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }
            //滑动切换时，消除当前组件


            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(viewContainter.get(position));
            }

            //每次滑动时生成的组件
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(viewContainter.get(position));
                return viewContainter.get(position);
            }
        }

        @Override
        public void requestFinish (String result, String requestUrl)throws JSONException {
//        if(requestUrl.equals(CommonUrl.getFavorInfo)){//返回收藏信息
//            JSONObject object = new JSONObject(result);
//            int code = Integer.valueOf(object.getString("code"));
//            Message msg = new Message();
//
//            switch (code){
//                case StatusCode.REQUEST_FAVORYUEPAI_SUCCESS://请求收藏的约拍成功
//                {
//                    JSONArray content = object.getJSONArray("contents");
//                    Log.d("eeeeeeeeeeeeeeeeee",content.toString());
//                    for(int i = 0;i < content.length();i++){
//                        JSONObject favor = content.getJSONObject(i);
//                        ItemModel itemModel = new ItemModel();
//                        itemModel.setTitle(favor.getString("APtitle"));
//                        itemModel.setId(favor.getInt("APid"));
//                        itemModel.setUserImage(favor.getString("Userimg"));
//                        itemModel.setStartTime(favor.getString("APstartT"));
//                        itemModel.setLikeNum(favor.getInt("APlikeN"));
//                        itemModel.setImage(favor.getString("APimgurl"));
//                        itemModel.setRegistNum(favor.getInt("APregistN"));
//                        favorItemList1.add(itemModel);
//                    }
//                    msg.what = StatusCode.REQUEST_FAVORYUEPAI_SUCCESS;
//                    handler.sendMessage(msg);
//                    break;
//                }
//            }
//
//        }

        }

        @Override
        public void exception (IOException e, String requestUrl){

        }



}
