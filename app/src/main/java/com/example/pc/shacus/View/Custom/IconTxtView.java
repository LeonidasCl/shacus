package com.example.pc.shacus.View.Custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

public class IconTxtView extends TextView {

    private final String nameSpace="http://com.licl.icontext";

    //保存图像资源ID的变量
    private int resourceId=0;

    private Bitmap bitmap;

    public IconTxtView(Context context, AttributeSet attrs) {
        super(context, attrs);  
        resourceId=attrs.getAttributeResourceValue(nameSpace, "iconSrc", 0);//获取图像资源的值  
        if(resourceId!=0)  
            bitmap=BitmapFactory.decodeResource(getResources(), resourceId);  
          
    }
      
    @Override  
    protected void onDraw(Canvas canvas) {  
        if(bitmap!=null){  
            Rect src=new Rect();//原图区域  
            Rect target=new Rect();//目标区域  
              
            src.left=0;  
            src.top=0;  
            src.right=bitmap.getWidth();
            src.bottom=bitmap.getHeight();  
              
            int textHeight=(int) (getTextSize()*3);
            target.left=0;  
              
            //计算图像复制区域的纵坐标
            target.top=(int) (((getMeasuredHeight()-getTextSize())/2)+1)-25;
              
            target.bottom=target.top+textHeight;
            target.right=(int) (textHeight*((float)bitmap.getWidth()/bitmap.getHeight()));
              
            //绘制  
            canvas.drawBitmap(bitmap, src, target, getPaint());  
            //向右移动TextView的的距离  
            canvas.translate(target.right+20,0);
              
        }  
        super.onDraw(canvas);  
    }
}