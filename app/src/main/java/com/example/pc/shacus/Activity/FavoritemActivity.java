package com.example.pc.shacus.Activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabWidget;

import com.example.pc.shacus.Fragment.DiscoverCategoryFragment;
import com.example.pc.shacus.Fragment.DiscoverRecommendFragment;
import com.example.pc.shacus.Network.NetworkCallbackInterface;
import com.example.pc.shacus.R;
import com.example.pc.shacus.View.MyScrollView.CommonPagerAdapter;
import com.example.pc.shacus.View.MyScrollView.MasonryAdapter;
import com.example.pc.shacus.View.MyScrollView.Product;
import com.example.pc.shacus.View.MyScrollView.RecycleItemClickListener;
import com.example.pc.shacus.View.MyScrollView.SpacesItemDecoration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
//孙启凡
//收藏界面（二级）

public class FavoritemActivity extends AppCompatActivity implements  NetworkCallbackInterface.NetRequestIterface,TabLayout.OnTabSelectedListener{

    private ViewPager mPager;
    DiscoverCategoryFragment discoverCategoryFragment;
    DiscoverRecommendFragment discoverRecommendFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置Tablayout
        setContentView(R.layout.activity_favoritem);
        TabLayout tabLayout = (TabLayout)findViewById(R.id.discover_tab_bar);
        //!!!使用TanLayout+ViewPager的时候
        mPager = (ViewPager)findViewById(R.id.discover_viewPager);
        ArrayList<Fragment> fragments = new ArrayList<>();
        discoverCategoryFragment=new DiscoverCategoryFragment();
        discoverRecommendFragment=new DiscoverRecommendFragment();
        fragments.add(discoverCategoryFragment);
        fragments.add(discoverRecommendFragment);
        CommonPagerAdapter adapter = new CommonPagerAdapter(getSupportFragmentManager(), fragments);
        mPager.setAdapter(adapter);
        //添加Tab，需要使用TabLayout的new Tab（）方法来创建
        TabLayout.Tab tab = tabLayout.newTab();
        tab.setText("最新");
        tabLayout.addTab(tab);
        tab = tabLayout.newTab();
        tab.setText("喜欢");
        tabLayout.addTab(tab);
        mPager.addOnPageChangeListener(
                new TabLayout.TabLayoutOnPageChangeListener(tabLayout)
        );
        tabLayout.setOnTabSelectedListener(this);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        mPager.setCurrentItem(tab.getPosition(), false);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void requestFinish(String result, String requestUrl) {

    }

    @Override
    public void exception(IOException e, String requestUrl) {

    }


    }
