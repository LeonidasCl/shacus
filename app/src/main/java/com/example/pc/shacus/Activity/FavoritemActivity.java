package com.example.pc.shacus.Activity;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabWidget;

import com.example.pc.shacus.Network.NetworkCallbackInterface;
import com.example.pc.shacus.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
//崔颖华
//收藏界面（二级）

public class FavoritemActivity extends AppCompatActivity implements  NetworkCallbackInterface.NetRequestIterface{

    private ViewPager viewPager = null;
    private List<View> viewContainter = new ArrayList<>();   //存放容器
    private ViewPagerAdapter viewPagerAdapter = null;   //声明适配器

    private TabHost mTabHost = null;
    private TabWidget mTabWidget = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritem);

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
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        //TabHost的监听事件
        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if (tabId.equals("favoritem_yuepai")){
                    viewPager.setCurrentItem(0);
                }else if (tabId.equals("favoritem_huodong")){
                    viewPager.setCurrentItem(1);
                }else{
                    viewPager.setCurrentItem(2);
                }
            }
        });

        //解决开始时初始化不显示viewPager
//        mTabHost.setCurrentTab(1);
//        mTabHost.setCurrentTab(0);

    }

    //初始化viewPager
    private void initViewPagerContainter() {
        //建立三个view，并找到对应的
        View view1 = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_favoritem_list,null);
        View view2 = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_favoritem_list,null);
        View view3 = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_favoritem_list,null);
        //加入ViewPager的容器
        viewContainter.add(view1);
        viewContainter.add(view2);
        viewContainter.add(view3);

        /*
        * 在这里添加对应的listview
        * 直接先通过LayoutInflater对象来实例化listview所在的布局，然后通过这个view的findviewById方法来获取listview的实例
        * ListView listView = (ListView) view1.findViewById(R.id.favoritem_list);
        * */

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
        mTabHost.addTab(mTabHost.newTabSpec("yuepai").setContent(R.id.favoritem_yuepai).setIndicator("约拍"));
        mTabHost.addTab(mTabHost.newTabSpec("huodong").setContent(R.id.favoritem_huodong).setIndicator("活动"));
        mTabHost.addTab(mTabHost.newTabSpec("works").setContent(R.id.favoritem_works).setIndicator("作品"));
    }

    //内部类实现viewpager的适配器
    private class ViewPagerAdapter extends PagerAdapter{

        //该方法决定并返回viewPager中组件的数量
        @Override
        public int getCount() {
            return viewContainter.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
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
    public void requestFinish(String result, String requestUrl) {

    }

    @Override
    public void exception(IOException e, String requestUrl) {

    }

}
