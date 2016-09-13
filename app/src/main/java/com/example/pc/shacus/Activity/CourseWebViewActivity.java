package com.example.pc.shacus.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.pc.shacus.R;
/*
* 李前 2016/8/31
* */

public class CourseWebViewActivity extends AppCompatActivity implements View.OnClickListener{

    private WebView webView;
    private ImageButton btn_back;
    private TextView text_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_web_view);
        Intent intent=getIntent();
        String url=intent.getStringExtra("detail");

        webView= (WebView) findViewById(R.id.webView);
        btn_back= (ImageButton) findViewById(R.id.btn_back);
        text_title= (TextView) findViewById(R.id.text_title);
        webView.loadUrl(url);


        WebChromeClient wvcc = new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                Log.d("LQQ", "TITLE=" + title);
                text_title.setText(title);
            }};
//        webView.getSettings().setUseWideViewPort(true);
//        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.setWebChromeClient(wvcc);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        btn_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btn_back)
            finish();
    }
}
