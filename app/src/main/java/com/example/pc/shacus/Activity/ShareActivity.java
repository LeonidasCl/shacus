package com.example.pc.shacus.Activity;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.pc.shacus.Network.NetworkCallbackInterface;
import com.example.pc.shacus.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.IOException;
import java.util.Hashtable;

//Author:LQ
//time:8.30
//分享界面（二级）
public class ShareActivity extends AppCompatActivity  implements  NetworkCallbackInterface.NetRequestIterface{

    private ImageView shareBarCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);


        shareBarCode= (ImageView) findViewById(R.id.shareBarCode);
        String url="http://bbs.ngacn.cc/thread.php?fid=188";
        create2DBarCodeBitmap(url);
    }

    //创建二维码
    private Bitmap create2DBarCodeBitmap(String url) {
        try {
            int QR_WIDTH=500,QR_HEIGHT=500;
            //判断URL合法性
            if (url == null || "".equals(url) || url.length() < 1)
            {
                return null;
            }
            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            //图像矩阵转换
            BitMatrix bitMatrix = new QRCodeWriter().encode(url, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
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
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void requestFinish(String result, String requestUrl) {

    }

    @Override
    public void exception(IOException e, String requestUrl) {

    }
}
