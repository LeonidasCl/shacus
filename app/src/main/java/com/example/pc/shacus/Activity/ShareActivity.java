package com.example.pc.shacus.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.pc.shacus.Network.NetworkCallbackInterface;
import com.example.pc.shacus.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.umeng.socialize.Config;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.utils.Log;
import com.umeng.socialize.utils.SocializeUtils;

import java.io.IOException;
import java.util.Hashtable;

//Author:李前
//time:8.30
//分享界面（二级）
public class ShareActivity extends AppCompatActivity  implements  NetworkCallbackInterface.NetRequestIterface,View.OnClickListener{

    private ImageView shareBarCode;
    private ProgressDialog dialog;
    private Bitmap image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        ImageButton btn_back = (ImageButton) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);

        shareBarCode= (ImageView) findViewById(R.id.shareBarCode);
        String url="http://shacus.cn";
        create2DBarCodeBitmap(url);

        Config.DEBUG = true;
        UMShareAPI.get(this);
        dialog = new ProgressDialog(this);
        ImageButton share = (ImageButton) findViewById(R.id.btn_share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                UMImage image = new UMImage(ShareActivity.this, shareBarCode);//资源文件
                UMImage shareImage = new UMImage(ShareActivity.this,image);
                ShareAction mShareAction = new ShareAction(ShareActivity.this).withMedia(shareImage)
                        .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.QQ, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QZONE)
                        .setCallback(umShareListener);
                mShareAction.open();
            }
        });
    }

    //创建二维码
    private Bitmap create2DBarCodeBitmap(String url) {
        try {
            int QR_WIDTH=600,QR_HEIGHT=600;
            //判断URL合法性
            if (url == null || "".equals(url) || url.length() < 1)
            {
                return null;
            }
            Hashtable<EncodeHintType, String> hints = new Hashtable<>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            //图像矩阵转换
            BitMatrix bitMatrix = new QRCodeWriter().encode(url, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
            bitMatrix=deleteWhite(bitMatrix);
            QR_HEIGHT=bitMatrix.getHeight();
            QR_WIDTH=bitMatrix.getWidth();
            int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
            //下面这里按照二维码的算法，生成二维码的图片
            for (int y = 0; y < QR_HEIGHT; y++)
            {
                for (int x = 0; x < QR_WIDTH; x++)
                {
                    if (bitMatrix.get(x, y))
                    {
                        pixels[y * QR_WIDTH + x] = 0xff000000;
                    }
                    else
                    {
                        pixels[y * QR_WIDTH + x] = 0xffffffff;
                    }
                }
            }
            //生成二维码图片的格式，使用ARGB_8888
            Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
            //设置二维码图片显示
            shareBarCode.setImageBitmap(bitmap);
            image = bitmap;
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    private BitMatrix deleteWhite(BitMatrix bitMatrix) {
        int[] rec = bitMatrix.getEnclosingRectangle();
        int resWidth = rec[2] + 1;
        int resHeight = rec[3] + 1;

        BitMatrix resMatrix = new BitMatrix(resWidth, resHeight);
        resMatrix.clear();
        for (int i = 0; i < resWidth; i++) {
            for (int j = 0; j < resHeight; j++) {
                if (bitMatrix.get(i + rec[0], j + rec[1]))
                    resMatrix.set(i, j);
            }
        }
        return resMatrix;
    }

    @Override
    public void requestFinish(String result, String requestUrl) {

    }

    @Override
    public void exception(IOException e, String requestUrl) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            //分享开始的回调
        }
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Log.d("plat", "platform" + platform);

            Toast.makeText(ShareActivity.this, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
            SocializeUtils.safeCloseDialog(dialog);

        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(ShareActivity.this,platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
            if(t!=null){
                Log.d("throw","throw:"+t.getMessage());
            }
            SocializeUtils.safeCloseDialog(dialog);
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(ShareActivity.this,platform + " 分享取消了", Toast.LENGTH_SHORT).show();
            SocializeUtils.safeCloseDialog(dialog);
        }
    };
}
