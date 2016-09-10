package com.example.pc.shacus.Activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.FloatEvaluator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.pc.shacus.Util.DisplayUtil;

/**
 *
 * Created by pc on 2016/3/10.
 */
public abstract class BaseSplashActivity extends AppCompatActivity {

    public static final int LOGIN_CHECK = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        //播放动画
        ImageView imageView = new ImageView(this);
        ImageView imageView2 = new ImageView(this);
        RelativeLayout relativeLayout = new RelativeLayout(this);
        relativeLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        RelativeLayout.LayoutParams picLayout = new RelativeLayout.LayoutParams(DisplayUtil.dip2px(this, 200), DisplayUtil.dip2px(this, 200));
        RelativeLayout.LayoutParams picLayout2 = new RelativeLayout.LayoutParams(DisplayUtil.dip2px(this, 160), DisplayUtil.dip2px(this, 160));
        picLayout.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        picLayout2.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        imageView.setLayoutParams(picLayout);
        imageView2.setLayoutParams(picLayout2);
        relativeLayout.addView(imageView);
        relativeLayout.addView(imageView2);
        setContentView(relativeLayout);

        imageView.setImageResource(getLogoImageResource());
        imageView2.setImageResource(getFrontImageResource());

        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView2.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        ObjectAnimator objectAnimator = ObjectAnimator.ofObject(imageView, "alpha", new FloatEvaluator(), 0f, 1f);
        objectAnimator.setDuration(3000);
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                Intent intent = new Intent(BaseSplashActivity.this, getNextActivityClass());
                startActivity(intent);
                finish();
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }
        });
        objectAnimator.start();
        ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(imageView2, "rotation", 0f, 2160f);
        objectAnimator2.setDuration(2500);
        objectAnimator2.start();
        initNetworkData();
        initCheck();
    }


    abstract public void initNetworkData();

    abstract public void initCheck();

    //获取闪屏界面的主图片
    abstract public int getLogoImageResource();

    abstract public int getFrontImageResource();

    //获取闪屏界面结束后的activity
    abstract public Class getNextActivityClass();

}
