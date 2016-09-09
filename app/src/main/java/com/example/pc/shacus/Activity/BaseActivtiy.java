package com.example.pc.shacus.Activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import com.example.pc.shacus.R;
import com.example.pc.shacus.Util.SystemBarTintManager;


/**
 * Created by Bob_ge on 15/7/26.
 */
public class BaseActivtiy extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSystemBar();
        getSupportActionBar().setLogo(R.drawable.de_bar_logo);//actionbar 添加logo
    }

    private void initSystemBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            // Holo light action bar color is #DDDDDD
            int actionBarColor = Color.parseColor("#0195ff");
            tintManager.setStatusBarTintColor(actionBarColor);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}
