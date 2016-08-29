package com.example.pc.shacus.Activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.pc.shacus.Fragment.SetBaseFragment;
import com.example.pc.shacus.R;
//Author:LQ
//Time:8.29
//设置页面（二级）
public class SettingsActivity extends AppCompatActivity {

    //使用Fragment调配设置界面
    private SetBaseFragment baseFm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        baseFm=new SetBaseFragment();
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction tx = fm.beginTransaction();
        tx.add(R.id.frameLayout, baseFm, "BaseFragment");
        tx.commit();
    }
}
