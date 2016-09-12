package com.example.pc.shacus.Util;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

import com.example.pc.shacus.APP;
import com.example.pc.shacus.Activity.OtherUserActivity;
import com.example.pc.shacus.Activity.SOSOLocationActivity;

import io.rong.imkit.RongIM;
import io.rong.imkit.widget.AlterDialogFragment;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.location.RealTimeLocationConstant;
import io.rong.imlib.location.message.RealTimeLocationStartMessage;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;
import io.rong.message.ImageMessage;
import io.rong.message.LocationMessage;
import io.rong.message.PublicServiceMultiRichContentMessage;
import io.rong.message.PublicServiceRichContentMessage;
import io.rong.message.RichContentMessage;


/**
 * Created by zhjchen on 1/29/15.
 */

/**
 * 融云SDK事件监听处理。
 */
public final class RongCloudEvent implements  RongIM.LocationProvider{

    private static final String TAG = RongCloudEvent.class.getSimpleName();

    private static RongCloudEvent mRongCloudInstance;
    private Context mContext;


    /**
     * 初始化 RongCloud.
     *
     * @param context 上下文。
     */
    public static void init(Context context) {

        if (mRongCloudInstance == null) {

            synchronized (RongCloudEvent.class) {

                if (mRongCloudInstance == null) {
                    mRongCloudInstance = new RongCloudEvent(context);
                }
            }
        }

    }

    /**
     * 构造方法。
     *
     * @param context 上下文。
     */
    private RongCloudEvent(Context context) {
        mContext = context;
        initDefaultListener();
    }

    /**
     * 获取RongCloud 实例。
     *
     * @return RongCloud。
     */
    public static RongCloudEvent getInstance() {
        return mRongCloudInstance;
    }

    /**
     * RongIM.init(this) 后直接可注册的Listener。
     */
    private void initDefaultListener() {
        RongIM.setLocationProvider(this);//设置地理位置提供者
    }


    /**
     * 位置信息提供者:LocationProvider 的回调方法，打开第三方地图页面。
     *
     * @param context  上下文
     * @param callback 回调
     */
    @Override
    public void onStartLocation(Context context, LocationCallback callback) {

        APP.setLastLocationCallback(callback);

        Intent intent = new Intent(context, SOSOLocationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


//    @Override
//    public boolean onMessageClick(final Context context, final View view, final Message message) {
//
//
//        if (message.getContent() instanceof LocationMessage) {
//            Intent intent = new Intent(context, SOSOLocationActivity.class);
//            intent.putExtra("location", message.getContent());
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(intent);
//        }
//        Log.d("Begavior", message.getObjectName() + ":" + message.getMessageId());
//
//        return false;
//    }


}
