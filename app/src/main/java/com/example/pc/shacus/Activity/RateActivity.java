package com.example.pc.shacus.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.pc.shacus.APP;
import com.example.pc.shacus.Network.NetRequest;
import com.example.pc.shacus.Network.NetworkCallbackInterface;
import com.example.pc.shacus.R;
import com.example.pc.shacus.Util.CommonUtils;

import org.json.JSONException;

import java.io.IOException;

/**
 * Created by pc on 2016/9/9.
 */
public class RateActivity extends Activity implements RatingBar.OnRatingBarChangeListener, NetworkCallbackInterface.NetRequestIterface{

    private RatingBar ratingBar;
    private TextView ratingTv;
    private EditText comment;
    private Button btn_comment;
    private Button btn_cancel;
    private NetRequest request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);
        setTitle("评分");
        request=new NetRequest(this,this);
        ratingBar=(RatingBar)findViewById(R.id.ratingBar);
        ratingTv=(TextView)findViewById(R.id.starTextView);
        ratingBar.setOnRatingBarChangeListener(this);
        comment=(EditText)findViewById(R.id.comment_edit);
        btn_comment=(Button)findViewById(R.id.btn_rate);
        btn_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonUtils.getUtilInstance().showLongToast(APP.context, "评分完成");
                finish();
                Intent intent=new Intent(getApplicationContext(),OrdersActivity.class);
                intent.putExtra("page","3");
                startActivity(intent);
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
    public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
        ratingTv.setText("评分"+ratingBar.getProgress());
    }

    @Override
    public void requestFinish(String result, String requestUrl) throws JSONException {

    }

    @Override
    public void exception(IOException e, String requestUrl) {

    }
}
