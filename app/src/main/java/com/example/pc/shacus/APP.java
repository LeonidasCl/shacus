package com.example.pc.shacus;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.io.File;

import io.rong.imkit.RongIM;

/**
 *
 * Created by pc on 2016/4/21.
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
    }

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
