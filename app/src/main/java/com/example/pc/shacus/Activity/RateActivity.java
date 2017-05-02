package com.example.pc.shacus.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.pc.shacus.APP;
import com.example.pc.shacus.Data.Cache.ACache;
import com.example.pc.shacus.Data.Model.LoginDataModel;
import com.example.pc.shacus.Data.Model.UserModel;
import com.example.pc.shacus.Network.NetRequest;
import com.example.pc.shacus.Network.NetworkCallbackInterface;
import com.example.pc.shacus.R;
import com.example.pc.shacus.Util.CommonUrl;
import com.example.pc.shacus.Util.CommonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 李嘉文 on 2016/9/9.
 */
public class RateActivity extends Activity implements NetworkCallbackInterface.NetRequestIterface{

    private TextView ratingTv;
    private EditText comment;
    private Button btn_comment;
    private Button btn_cancel;
    private NetRequest request;
    private com.example.pc.shacus.View.RatingBar ratingBar;
    private float rate = (float) 4.5;
    private int id = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);
        setTitle("评分");
        Intent intent = getIntent();
        id = intent.getIntExtra("apid", -1);
        request=new NetRequest(this,this);
        ratingTv=(TextView)findViewById(R.id.starTextView);
        comment=(EditText)findViewById(R.id.comment_edit);
        btn_comment=(Button)findViewById(R.id.btn_rate);
        ratingBar = (com.example.pc.shacus.View.RatingBar) findViewById(R.id.rb);
        ratingBar.setStar(4.5f);
        ratingBar.setStepSize(com.example.pc.shacus.View.RatingBar.StepSize.Half);
        ratingBar.setOnRatingChangeListener(new com.example.pc.shacus.View.RatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(float ratingCount) {
                rate = ratingCount;
            }
        });
        btn_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (comment.getText().toString().equals("")){
                    CommonUtils.getUtilInstance().showLongToast(APP.context, "请填写评价内容");
                }else {
                    ACache aCache = ACache.get(RateActivity.this);
                    LoginDataModel loginDataModel = (LoginDataModel) aCache.getAsObject("loginModel");
                    UserModel content = loginDataModel.getUserModel();
                    String userId = content.getId();
                    String authkey = content.getAuth_key();
                    Map map = new HashMap();
                    map.put("authkey",authkey);
                    map.put("type",10908);
                    map.put("uid", userId);
                    map.put("apid", id);
                    map.put("comment", comment.getText().toString());
                    map.put("score", String.valueOf(rate));
                    request.httpRequest(map, CommonUrl.commentAp);
                }
            }
        });
        btn_cancel=(Button)findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonUtils.getUtilInstance().showLongToast(APP.context, "请到订单中进行评价");
                finish();
                Intent intent=new Intent(getApplicationContext(),OrdersActivity.class);
                intent.putExtra("page","3");
                startActivity(intent);
            }
        });

    }

    @Override
    public void requestFinish(String result, String requestUrl) throws JSONException {
        if (requestUrl.equals(CommonUrl.commentAp)){
            JSONObject object = new JSONObject(result);
            int code = Integer.valueOf(object.getString("code"));
            if (code == 10908){
                Message message = new Message();
                message.what = 10908;
                handler.sendMessage(message);
            }
        }
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 10908:{
                    CommonUtils.getUtilInstance().showLongToast(APP.context, "评分完成");
                    finish();
                    Intent intent = new Intent(getApplicationContext(), OrdersActivity.class);
                    intent.putExtra("page", "3");
                    startActivity(intent);
                    break;
                }

            }
        }
    };

    @Override
    public void exception(IOException e, String requestUrl) {

    }
}
