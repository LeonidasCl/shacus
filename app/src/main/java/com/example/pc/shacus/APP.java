package com.example.pc.shacus;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.example.pc.shacus.Activity.OtherUserActivity;
import com.example.pc.shacus.Data.Cache.ACache;
import com.example.pc.shacus.Data.Model.LoginDataModel;
import com.example.pc.shacus.Network.NetRequest;
import com.example.pc.shacus.Network.NetworkCallbackInterface;
import com.example.pc.shacus.Network.StatusCode;
import com.example.pc.shacus.Util.CommonUrl;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;
import io.rong.message.LocationMessage;
import io.rong.message.RichContentMessage;

/**
 *
 * Created by shacus on 2016/8/21.
 */
public class APP extends Application {

    public static Context context;


    @Override public void onCreate(){
        super.onCreate();
        context = this;
        applicationContext = this;
        getScreenDimension();
        initImageLoader(this);

        RongIM.init(this);
        RongIM.setConversationBehaviorListener(new RongIM.ConversationBehaviorListener() {

            @Override
            public boolean onUserPortraitClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
                Intent in = new Intent(context, OtherUserActivity.class);
                in.putExtra("id", userInfo.getUserId());
                context.startActivity(in);
                return true;
            }

            @Override
            public boolean onUserPortraitLongClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
                return false;
            }

            @Override
            public boolean onMessageClick(Context context, View view, Message message) {
                return false;
            }

            @Override
            public boolean onMessageLinkClick(Context context, String s) {
                return false;
            }

            @Override
            public boolean onMessageLongClick(Context context, View view, Message message) {
                return false;
            }
        });
        //提供用户信息
        RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
            @Override
            public UserInfo getUserInfo(String s) {
                //取出用户信息缓存
                Log.i("logrong","getInfo");
                ACache cache = ACache.get(getApplicationContext());
                LoginDataModel loginModel = (LoginDataModel) cache.getAsObject("loginModel");
                String uid = loginModel.getUserModel().getId();
                if (uid.equals(s)) {//如果是自己，直接取出提供给融云
                    String nickname = loginModel.getUserModel().getNickName();
                    String avatar = loginModel.getUserModel().getHeadImage();
                    return new UserInfo(uid, nickname, Uri.parse(avatar));
                }
                else{//if#1 不是自己，要向业务服务器请求

                    Map map = new HashMap();
                    //向业务服务器发请求
                    map.put("uid", uid);
                    map.put("seeid",s);
                    map.put("authkey", loginModel.getUserModel().getAuth_key());
                    map.put("type", StatusCode.GET_OTHER_USER_INFO);
                        Log.i("logrong", "getNetInfo");
                    new NetRequest(new NetworkCallbackInterface.NetRequestIterface() {
                        @Override
                        public void requestFinish(String result, String requestUrl) throws JSONException {
                            if (requestUrl.equals(CommonUrl.getOtherUser)){//if#2 收到业务服务器回复

                                JSONObject object = new JSONObject(result);
                                int code = Integer.valueOf(object.getString("code"));
                                if (code==StatusCode.REQUEST_OTHER_USER_SUCCESS){//if#3
                                    JSONObject content=object.getJSONObject("contents");
                                    String id=content.getString("uid");
                                    String nickname=content.getString("ualais");
                                    String avatar=content.getString("uheadimage");
                                    //把用户信息传回给融云
                                    RongIM.getInstance().refreshUserInfoCache(new UserInfo(id, nickname, Uri.parse(avatar)));

                                    return;
                                }else{//请求失败

                                }//end if#3
                            }//end if#2
                        }
                        @Override
                        public void exception(IOException e, String requestUrl) {}
                    }, APP.context).httpRequest(map, CommonUrl.getOtherUser);

                }//end if#1
                return null;
            }//end getUserInfo
        }, true);//end setUserInfoProvider

    }//end APP.OnCreate()

    public static String cache_image_path, photo_path;
    public static File cacheImageDir, photoDir;
    private static APP instance;
    public static int screenWidth, screenHeight;
    public static String loginShare = "";
    public static Context applicationContext;
    private String myName, myPhoto;

    public String getMyName() {
        return myName;
    }

    public void setMyName(String myName) {
        this.myName = myName;
    }

    public String getMyPhoto() {
        return myPhoto;
    }

    public void setMyPhoto(String myPhoto) {
        this.myPhoto = myPhoto;
    }

    public static synchronized APP getInstance() {
        if (instance == null) {
            instance = new APP();
        }
        return instance;
    }

    public void getScreenDimension() {
        WindowManager mWM = ((WindowManager) getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE));
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        mWM.getDefaultDisplay().getMetrics(mDisplayMetrics);
        screenWidth = mDisplayMetrics.widthPixels;
        screenHeight = mDisplayMetrics.heightPixels;
    }


    private File createCacheDir() {

        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            File sdcardDir = Environment.getExternalStorageDirectory();
            cache_image_path = sdcardDir.getPath() + "/shacus/cache/images/";
            cacheImageDir = new File(cache_image_path);
            photo_path = sdcardDir.getPath() + "/shacus/cache/photoes/";
            photoDir = new File(photo_path);
        } else {
            photo_path= "/storage/emulated/0"+"/shacus/cache/photoes/";
            cacheImageDir = new File("/storage/emulated/0"+"/shacus/cache/images");
            photoDir = new File(photo_path);
        }
        if (!cacheImageDir.exists()) {
            cacheImageDir.mkdirs();
        }
        if (!photoDir.exists()) {
            photoDir.mkdirs();
        }
        return cacheImageDir;
    }
    //先初始化UniversalImageLoader
    private void initImageLoader(Context context) {
        File cacheDir = createCacheDir();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context)
                .threadPriority(Thread.NORM_PRIORITY - 1)
                .threadPoolSize(3)
                .memoryCacheExtraOptions(480, 800)
                .diskCacheExtraOptions(480, 800, null)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .memoryCacheSize( 1024 * 1024)
                .diskCacheSize(50 * 1024 * 1024)
                        // .discCacheFileCount(200)
                        // .defaultDisplayImageOptions(options)
                .diskCache(new UnlimitedDiskCache(cacheDir)).build();

        // Initialize ImageLoader with configuration.
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);
    }
    @Override public void onTerminate() {
        super.onTerminate();
    }

}
