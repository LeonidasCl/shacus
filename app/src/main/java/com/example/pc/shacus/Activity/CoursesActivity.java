package com.example.pc.shacus.Activity;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
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

import com.example.pc.shacus.Adapter.CommonPagerAdapter;
import com.example.pc.shacus.Adapter.CourseListAdapter;
import com.example.pc.shacus.Data.Cache.ACache;
import com.example.pc.shacus.Data.Model.CoursesModel;
import com.example.pc.shacus.Fragment.FinishedCourseFragment;
import com.example.pc.shacus.Fragment.UndoCourseFragment;
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

public class CoursesActivity extends AppCompatActivity implements  NetworkCallbackInterface.NetRequestIterface,TabLayout.OnTabSelectedListener{

    private ImageButton returnButton;
    private ImageButton imageButton1;
    private ViewPager mPager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);
        returnButton=(ImageButton)findViewById(R.id.return1);
        imageButton1=(ImageButton)findViewById(R.id.imagebutton);

        returnButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
          finish();
      }
  });
        imageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        TabLayout tabLayout = (TabLayout)findViewById(R.id.discover_tab_bar);
        mPager = (ViewPager) findViewById(R.id.discover_viewPager);
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new UndoCourseFragment());
        fragments.add(new FinishedCourseFragment());
        CommonPagerAdapter adapter = new CommonPagerAdapter(getSupportFragmentManager(), fragments);
        mPager.setAdapter(adapter);

        TabLayout.Tab tab = tabLayout.newTab();
        tab.setText("课程表");
        tabLayout.addTab(tab);
        tab = tabLayout.newTab();
        tab.setText("完成课程");
        tabLayout.addTab(tab);
        tabLayout.setTabTextColors(Color.BLACK, Color.WHITE);
        tabLayout.setSelectedTabIndicatorColor(Color.TRANSPARENT);




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
        public void requestFinish (String result, String requestUrl)throws JSONException {
//
        }

        @Override
        public void exception (IOException e, String requestUrl){

        }



}
