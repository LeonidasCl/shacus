package com.example.pc.shacus.Activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.pc.shacus.Fragment.FragmentCreateYuePaiA_new;
import com.example.pc.shacus.Fragment.FragmentCreateYuePaiB;
import com.example.pc.shacus.R;
import com.example.pc.shacus.Util.CommonUtils;

import static android.support.design.widget.TabLayout.MODE_SCROLLABLE;

/**
 * 创建约拍和活动的界面
 * Created by pc on 2016/8/28.
 */

//隐藏掉发活动

public class CreateYuePaiActivity extends AppCompatActivity {
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    //private Fragment fragment;
    private int position=-1;
    //private static final String SELECTED_ITEM = "selected_item";
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_yuepai);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_createyuepai);
        toolbar.setTitleTextColor(getResources().getColor(R.color.zero_black));
        setSupportActionBar(toolbar);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container_createyuepai);
        mViewPager.setAdapter(mSectionsPagerAdapter);



        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs_createyuepai);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                switch (position){
                    /*case 0:
                        yuepeifrag1 = new FragmentCreateYuePaiA_new();
                        yuepeifrag1.setYUEPAI_TYPE(1);
                        break;
                    case 1:
                        fragment=new FragmentCreateYuePaiA_new();
                        FragmentCreateYuePaiA_new yuepeifrag2=(FragmentCreateYuePaiA_new)fragment;
                        yuepeifrag2.setYUEPAI_TYPE(0);
                        break;*/
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        tabLayout.setupWithViewPager(mViewPager);
        //tabLayout.setTabMode(MODE_SCROLLABLE);

        final ImageButton finish = (ImageButton) findViewById(R.id.backbtn_createyuepai);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState)
    {}
    //监听返回键，有弹出层时关闭弹出层，否则停止activity
    @Override
    public void onBackPressed() {
        boolean state;
        switch (position){
            case 0:
                //FragmentCreateYuePaiA_new fraga= (FragmentCreateYuePaiA_new) fragment;
                FragmentCreateYuePaiA_new fraga=(FragmentCreateYuePaiA_new)mSectionsPagerAdapter.getItem(0);
                state=fraga.getEdit_big_photo_layout().getVisibility()==View.GONE&&fraga.getdisplay_big_img().getVisibility()==View.GONE;
                if (state)
                    finish();
                else
                    fraga.hideBigPhotoLayout();
                break;
            case 1:
                //FragmentCreateYuePaiA_new frag= (FragmentCreateYuePaiA_new) fragment;
                FragmentCreateYuePaiA_new frag=(FragmentCreateYuePaiA_new)mSectionsPagerAdapter.getItem(1);
                state=frag.getEdit_big_photo_layout().getVisibility()==View.GONE&&frag.getdisplay_big_img().getVisibility()==View.GONE;
                if (state)
                    finish();
                else
                    frag.hideBigPhotoLayout();
                break;
        }

    }

    /*@Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String type=intent.getStringExtra("type");
        if (type.equals("tagAdd1")){
            String tag=intent.getStringExtra("tag");
        }
        if (type.equals("tagAdd2")){
            String tag=intent.getStringExtra("tag");
            FragmentCreateYuePaiB frag=(FragmentCreateYuePaiB)fragment;
            frag.addTag(tag);
        }
    }*/

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            //WantBePhotographActivity.PlaceholderFragment frag= WantBePhotographActivity.PlaceholderFragment.newInstance(position);
            FragmentCreateYuePaiA_new frag=new FragmentCreateYuePaiA_new();
            if (position==0)
                frag.setYUEPAI_TYPE(1);
            else
                frag.setYUEPAI_TYPE(0);
            return frag;
        }

        @Override
        public int getCount(){
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position==0)
                return "约模特";
            else
                return "约摄影师";
        }
    }

}
