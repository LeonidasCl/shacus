package com.example.pc.shacus.Activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.pc.shacus.Fragment.FragmentCreateYuePaiA;
import com.example.pc.shacus.Fragment.FragmentCreateYuePaiB;
import com.example.pc.shacus.R;

/**
 * 创建约拍和活动的界面
 * Created by pc on 2016/8/28.
 */
public class CreateYuePaiActivity extends AppCompatActivity implements
        ActionBar.TabListener {
    private Fragment fragment;
    private int position=-1;
    private static final String SELECTED_ITEM = "selected_item";
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_yuepai);
        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        // 设置ActionBar的导航方式：Tab导航
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        // 依次添加三个Tab页，并为三个Tab标签添加事件监听器
        actionBar.addTab(actionBar.newTab().setText("约模特").setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText("约摄影师").setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText("发起活动").setTabListener(this));
        actionBar.setTitle("发起约拍/创建活动");
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState)
    {
        if (savedInstanceState.containsKey(SELECTED_ITEM))
        {
            // 选中前面保存的索引对应的Fragment页
            getActionBar().setSelectedNavigationItem(
                    savedInstanceState.getInt(SELECTED_ITEM));
        }
    }
    //监听返回键，有弹出层时关闭弹出层，否则停止activity
    @Override
    public void onBackPressed() {
        boolean state;
        switch (position){
            case 0:
                FragmentCreateYuePaiA fraga=(FragmentCreateYuePaiA)fragment;
                state=fraga.getEdit_big_photo_layout().getVisibility()==View.GONE&&fraga.getdisplay_big_img().getVisibility()==View.GONE;
                if (state)
                    finish();
                else
                    fraga.hideBigPhotoLayout();
                break;
            case 1:
                FragmentCreateYuePaiA frag=(FragmentCreateYuePaiA)fragment;
                state=frag.getEdit_big_photo_layout().getVisibility()==View.GONE&&frag.getdisplay_big_img().getVisibility()==View.GONE;
                if (state)
                    finish();
                else
                    frag.hideBigPhotoLayout();
                break;
            case 2:
                FragmentCreateYuePaiB fragc=(FragmentCreateYuePaiB)fragment;
                state=fragc.getEdit_big_photo_layout().getVisibility()==View.GONE&&fragc.getdisplay_big_img().getVisibility()==View.GONE;
                if (state)
                    finish();
                else
                    fragc.hideBigPhotoLayout();
                break;
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        // 将当前选中的Fragment页的索引保存到Bundle中
        outState.putInt(SELECTED_ITEM, getSupportActionBar().getSelectedNavigationIndex());
    }

    // 当指定Tab被选中时激发该方法
    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        fragment=null;
        position=tab.getPosition();
        switch (position){
            case 0:
                fragment = new FragmentCreateYuePaiA();
                FragmentCreateYuePaiA yuepeifrag1=(FragmentCreateYuePaiA)fragment;
                yuepeifrag1.setYUEPAI_TYPE(1);
                break;
            case 1:
                fragment=new FragmentCreateYuePaiA();
                FragmentCreateYuePaiA yuepeifrag2=(FragmentCreateYuePaiA)fragment;
                yuepeifrag2.setYUEPAI_TYPE(2);
                break;
            case 2:
                fragment=new FragmentCreateYuePaiB();
                break;
        }
        // 创建一个Bundle对象，用于向Fragment传入参数
        //Bundle args = new Bundle();
        // args.putInt(FragmentCreateYuePaiA.ARG_SECTION_NUMBER, tab.getPosition() + 1);
        // 向fragment传入参数
        //fragment.setArguments(args);
        // 获取FragmentTransaction对象
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        // 使用fragment代替该Activity中的container组件
        ft.replace(R.id.container, fragment);
        // 提交事务
        ft.commit();
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }
    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String type=intent.getStringExtra("type");
        if (type.equals("tagAdd1")){
            String tag=intent.getStringExtra("tag");
            FragmentCreateYuePaiA frag=(FragmentCreateYuePaiA)fragment;
            frag.addTag(tag);
        }
        if (type.equals("tagAdd2")){
            String tag=intent.getStringExtra("tag");
            FragmentCreateYuePaiB frag=(FragmentCreateYuePaiB)fragment;
            //frag.addTag(tag);
        }
    }
}
