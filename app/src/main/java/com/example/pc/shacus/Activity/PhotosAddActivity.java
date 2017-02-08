package com.example.pc.shacus.Activity;

/**
 * Created by licl on 2017/2/8.
 * 添加图片Activity
 * 可以给个人照片、作品集添加图片，可以发布新的作品集
 */

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.pc.shacus.APP;
import com.example.pc.shacus.Adapter.ImageAddGridViewAdapter;
import com.example.pc.shacus.Adapter.ImagePagerAdapter;
import com.example.pc.shacus.Adapter.PhotoViewAttacher;
import com.example.pc.shacus.Adapter.UploadViewPager;
import com.example.pc.shacus.Data.Cache.ACache;
import com.example.pc.shacus.Data.Model.LoginDataModel;
import com.example.pc.shacus.Data.Model.UserModel;
import com.example.pc.shacus.Fragment.ImageDisplayFragment;
import com.example.pc.shacus.Network.NetRequest;
import com.example.pc.shacus.Network.NetworkCallbackInterface;
import com.example.pc.shacus.Network.StatusCode;
import com.example.pc.shacus.R;
import com.example.pc.shacus.Util.CommonUrl;
import com.example.pc.shacus.Util.CommonUtils;
import com.example.pc.shacus.Util.UploadPhotoUtil;
import com.example.pc.shacus.View.Custom.ImgAddGridView;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class PhotosAddActivity extends AppCompatActivity implements View.OnClickListener, NetworkCallbackInterface.NetRequestIterface,
        NetworkCallbackInterface.OnSingleTapDismissBigPhotoListener{

    private final int UPLOAD_TAKE_PICTURE=5;
    private ProgressDialog progressDlg;
    private final int NONE=0,TAKE_PICTURE=1,LOCAL_PICTURE=2;
    private final int SAVE_THEME_IMAGE=8;
    private final int SHOW_TAKE_PICTURE=9;
    private final int SHOW_LOCAL_PICTURE=10;
    private FrameLayout edit_photo_fullscreen_layout;
    private RelativeLayout edit_photo_outer_layout,display_big_image_layout,show_upload_pic_layout;
    private Animation get_photo_layout_out_from_up,get_photo_layout_in_from_down;
    private TextView take_picture,select_local_picture,position_in_total,upload;
    private ImageView delete_image;
    private String takePictureUrl;
    private Intent intent;
    private NetRequest requestFragment;
    private ImgAddGridView add_image_gridview;
    private int picToAdd=-1;
    private int addPicCount=1,addTakePicCount=1,viewpagerPosition;
    private List<String> uploadImgUrlList=new ArrayList<>();
    private List<Drawable>addPictureList=new ArrayList<>();
    private List<String>pictureUrlList=new ArrayList<>();
    private ImageAddGridViewAdapter imageAddGridViewAdapter;
    private ImagePagerAdapter imagePagerAdapter;
    private UploadViewPager image_viewpager;
    private boolean isBigImageShow=false,isShowUploadPic=false,addPic=false,clearFormerUploadUrlList=true;
    private EditText theme_title_edit,theme_desc_edit;
    private UserModel user;
    private int apId;
    private ArrayList<String> imgList;
    private ArrayList<String> finalImgList;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos_add);
        requestFragment=new NetRequest(this,this);



        edit_photo_fullscreen_layout=(FrameLayout)findViewById(R.id.edit_photo_fullscreen_layout);
        edit_photo_fullscreen_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideBigPhotoLayout();
            }
        });
        edit_photo_outer_layout=(RelativeLayout)findViewById(R.id.edit_photo_outer_layout);
        TextView cancelEditPhoto=(TextView)edit_photo_outer_layout.findViewById(R.id.cancel_upload);
        cancelEditPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideBigPhotoLayout();
            }
        });
        display_big_image_layout=(RelativeLayout)findViewById(R.id.display_big_image_layout);
        show_upload_pic_layout=(RelativeLayout)findViewById(R.id.show_upload_pic_layout);
        take_picture=(TextView)findViewById(R.id.take_picture);

        position_in_total=(TextView)findViewById(R.id.position_in_total);
        select_local_picture=(TextView)findViewById(R.id.select_local_picture);
        upload=(TextView)findViewById(R.id.upload);
        upload.setVisibility(View.VISIBLE);
        theme_title_edit=(EditText)findViewById(R.id.theme_title_edit);
        theme_desc_edit=(EditText)findViewById(R.id.theme_desc_edit);
        delete_image=(ImageView)findViewById(R.id.delete_image);
        add_image_gridview=(ImgAddGridView)findViewById(R.id.add_image_gridview);
        add_image_gridview.setExpanded(true);
        image_viewpager=(UploadViewPager)findViewById(R.id.image_viewpager);
        upload.setOnClickListener(this);
        delete_image.setOnClickListener(this);
        ImageDisplayFragment.showNetImg=false;
        addPictureList.add(getResources().getDrawable(R.mipmap.theme_add_picture_icon));
        imageAddGridViewAdapter=new ImageAddGridViewAdapter(this, addPictureList);
        add_image_gridview.setAdapter(imageAddGridViewAdapter);
        //mProgress = (ProgressBar) root.findViewById(R.id.uploading_photo_progress);
        add_image_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                //这里是添加图片的按钮的回调
                if (position == 0) {
                    if (addPicCount == 10) {
                        CommonUtils.getUtilInstance().showToast(PhotosAddActivity.this, getString(R.string.no_more_than_9));
                    } else {
                        //点击添加图片
                        edit_photo_fullscreen_layout.setVisibility(View.VISIBLE);
                        get_photo_layout_in_from_down = AnimationUtils.loadAnimation(PhotosAddActivity.this, R.anim.search_layout_in_from_down);
                        edit_photo_outer_layout.startAnimation(get_photo_layout_in_from_down);
                    }
                } else {
                    //点击图片查看大图
                    showImageViewPager(position, pictureUrlList, uploadImgUrlList, "local", "upload");
                    viewpagerPosition = position - 1;
                }
            }
        });

        take_picture.setOnClickListener(this);
        select_local_picture.setOnClickListener(this);
    }

    //监听返回键，有弹出层时关闭弹出层，否则停止activity
    @Override
    public void onBackPressed() {
        boolean state;
                /*state=fraga.getEdit_big_photo_layout().getVisibility()==View.GONE&&fraga.getdisplay_big_img().getVisibility()==View.GONE;
                if (state)
                    finish();
                else
                    fraga.hideBigPhotoLayout();*/
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    private Handler handler=new Handler(){

        // 选完图片后就是在这里处理的
        @Override
        public void handleMessage(Message msg){
            switch(msg.what){

                case SAVE_THEME_IMAGE://响应第二次msg，将上传图片结果与真活动信息反馈给业务服务器
                    //Map<String, Object> map=(Map<String, Object>)msg.obj;
                    progressDlg.dismiss();
                    Map map=new HashMap<>();
                    map.put("auth_key",user.getAuth_key());
                    map.put("actitle",theme_title_edit.getText().toString());
                    map.put("type", StatusCode.REQUEST_SEND_HUODONG);
                    map.put("acid",apId);
                    map.put("content",theme_desc_edit.getText().toString());
                    map.put("images", finalImgList);
                    progressDlg=ProgressDialog.show(PhotosAddActivity.this, "发布活动", "正在创建活动", true, true, new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialogInterface) {
                            //上传完图片后取消了发布活动
                        }
                    });
                    // mProgress.setVisibility(View.GONE);
                    requestFragment.httpRequest(map, CommonUrl.createActivityInfo);//最后将图片在这里传出去
                    break;
                case UPLOAD_TAKE_PICTURE://响应第一次msg，发送第二次msg：在本地把图片封装保存，发送图片
                    Map<String, String> map2=(HashMap<String, String>)msg.obj;
                    picToAdd=uploadImgUrlList.size();
                    if(uploadImgUrlList.size()>0){
                        for(int i=0,j=picToAdd;j>0;j--,i++ ){
                            saveThemeImgNew(uploadImgUrlList.get(j-1),map2.get("auth_key"),i);//逐张保存要上传的图片并发消息到发送的handle
                        }
                    }
                    show_upload_pic_layout.setVisibility(View.VISIBLE);
                    isShowUploadPic=true;
                    break;
                case SHOW_TAKE_PICTURE://拍了照片后msg的处理
                    addPic=true;
                    if(clearFormerUploadUrlList){
                        if(uploadImgUrlList.size()>0){
                            uploadImgUrlList.clear();
                        }
                        clearFormerUploadUrlList=false;
                    }
                    //在这里处理，获取拍到的图
                    Bitmap bitmap= UploadPhotoUtil.getInstance()
                            .trasformToZoomBitmapAndLessMemory(takePictureUrl);
                    BitmapDrawable bd=new BitmapDrawable(getResources(),bitmap);
                    addPictureList.add(bd);
                    uploadImgUrlList.add(takePictureUrl);
                    imageAddGridViewAdapter.changeList(addPictureList);
                    imageAddGridViewAdapter.notifyDataSetChanged();
                    addPicCount++;
                    break;
                //在图库选中了本地的图
                case SHOW_LOCAL_PICTURE:
                    //获取到资源位置
                    Uri uri=intent.getData();
                    String photo_local_file_path=getPath_above19(PhotosAddActivity.this.getApplicationContext(), uri);
                    //程序员啊不要见得风就是雨，要有自己的判断，用户选出来的文件要判断它是不是真的图片
                    //如果不是，这个错的东西你再帮他传一遍，等于你也有责任吧
                    if (!(photo_local_file_path.toString().toLowerCase().endsWith("jpg")||photo_local_file_path.toString().toLowerCase().endsWith("png")
                            ||photo_local_file_path.toString().toLowerCase().endsWith("jpeg")||photo_local_file_path.toString().toLowerCase().endsWith("gif"))){
                        CommonUtils.getUtilInstance().showToast(PhotosAddActivity.this,"不支持此格式的上传");
                        break;
                    }
                    addPic=true;
                    if(clearFormerUploadUrlList){
                        if(uploadImgUrlList.size()>0){
                            uploadImgUrlList.clear();
                        }
                        clearFormerUploadUrlList=false;
                    }
                    Bitmap bitmap2=UploadPhotoUtil.getInstance().trasformToZoomBitmapAndLessMemory(photo_local_file_path);
                    addPictureList.add(new BitmapDrawable(getResources(), bitmap2));
                    uploadImgUrlList.add(photo_local_file_path);
                    imageAddGridViewAdapter.changeList(addPictureList);
                    imageAddGridViewAdapter.notifyDataSetChanged();
                    addPicCount++;

                    break;
            }
        }
    };


    //是否为外置存储器
    public static boolean isExternalStorageDocument(Uri uri){
        return"com.android.externalstorage.documents".equals(uri.getAuthority());
    }
    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri){
        return"com.android.providers.downloads.documents".equals(uri.getAuthority());
    }
    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri){
        return"com.android.providers.media.documents".equals(uri.getAuthority());
    }
    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.*/

    public static boolean isGooglePhotosUri(Uri uri){
        return"com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[]selectionArgs){
        Cursor cursor=null;
        final String column="_data";
        final String[]projection={column};
        try{
            cursor=context.getContentResolver().query(uri,projection,selection,selectionArgs, null);
            if(cursor!=null&&cursor.moveToFirst()){
                final int index=cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        }finally{
            if(cursor!=null)
                cursor.close();
        }
        return null;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPath_above19(final Context context,final Uri uri){
        final boolean isKitKat=Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if(isKitKat&& DocumentsContract.isDocumentUri(context, uri)){
            // ExternalStorageProvider
            if(isExternalStorageDocument(uri)){
                final String docId=DocumentsContract.getDocumentId(uri);
                final String[]split=docId.split(":");
                final String type=split[0];
                if("primary".equalsIgnoreCase(type)){
                    return Environment.getExternalStorageDirectory()+"/"+split[1];
                }

            }
            // DownloadsProvider
            else if(isDownloadsDocument(uri)){
                final String id=DocumentsContract.getDocumentId(uri);
                final Uri contentUri= ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context,contentUri,null,null);
            }
            // MediaProvider
            else if(isMediaDocument(uri)){
                final String docId=DocumentsContract.getDocumentId(uri);
                final String[]split=docId.split(":");
                final String type=split[0];
                Uri contentUri=null;
                if("image".equals(type)){
                    contentUri= MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                }else if("video".equals(type)){
                    contentUri=MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                }else if("audio".equals(type)){
                    contentUri=MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection="_id=?";
                final String[]selectionArgs=new String[]{
                        split[1]
                };
                return getDataColumn(context,contentUri,selection,selectionArgs);
            }
        }
        // MediaStore (and general)
        else if("content".equalsIgnoreCase(uri.getScheme())){
            // Return the remote address
            if(isGooglePhotosUri(uri))
                return uri.getLastPathSegment();
            return getDataColumn(context,uri,null,null);
        }
        // File
        else if("file".equalsIgnoreCase(uri.getScheme())){
            return uri.getPath();
        }
        return null;
    }

    //加监听，等到view完全显示了再去做调整
    /*public void onViewCreated(final View view, Bundle saved) {
        super.onViewCreated(view, saved);
        final ViewTreeObserver observer = view.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
                ViewGroup.MarginLayoutParams big_picLayoutParam = (ViewGroup.MarginLayoutParams) display_big_image_layout.getLayoutParams();
                big_picLayoutParam.bottomMargin = upload.getHeight();
                display_big_image_layout.setLayoutParams(big_picLayoutParam);
            }
        });
    }*/

    public void showImageViewPager(int position,
                                   final List<String>pictureUrlList,final List<String>localUrlList,
                                   final String flag,final String str){
        List<String>urlList;
        if(flag.equals("net")){
            urlList=pictureUrlList;
        }else{
            urlList=localUrlList;
        }
        display_big_image_layout.setVisibility(View.VISIBLE);
        imagePagerAdapter=new ImagePagerAdapter(PhotosAddActivity.this.getSupportFragmentManager(),urlList);
        image_viewpager.setAdapter(imagePagerAdapter);
        imagePagerAdapter.notifyDataSetChanged();
        image_viewpager.setOffscreenPageLimit(imagePagerAdapter.getCount());
        image_viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int state) {

            }

            @Override
            public void onPageSelected(int position) {

                viewpagerPosition = position;
                if (flag.equals("net")) {
                    position_in_total.setText((position + 1) + "/" + pictureUrlList.size());
                } else {
                    position_in_total.setText((position + 1) + "/" + localUrlList.size());
                }

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }
        });
        if(str.equals("display")){
            image_viewpager.setCurrentItem(position);
            delete_image.setVisibility(View.GONE);
            position_in_total.setText((position+1)+"/"+urlList.size());
            isBigImageShow=true;

        }else{
            image_viewpager.setCurrentItem(position-1);
            delete_image.setVisibility(View.VISIBLE);
            position_in_total.setText((position)+"/"+urlList.size());
            isBigImageShow=true;

        }
        PhotoViewAttacher.setOnSingleTapToPhotoViewListener(this);
    }

    // 下面函数是选中了要上传图片或者拍照打完钩后运行的
    public void onActivityResult(int requestCode,int resultCode,Intent intent){
        super.onActivityResult(requestCode, resultCode, intent);
        if(resultCode==NONE)
            return;
        //耗时操作传到handler里去处理
        if(requestCode==TAKE_PICTURE){
            handler.sendEmptyMessage(SHOW_TAKE_PICTURE);
            return;
        }
        if(resultCode== Activity.RESULT_OK){
            this.intent=intent;
            handler.sendEmptyMessage(SHOW_LOCAL_PICTURE);
        }
    }

    @Override
//选择本地图片 拍照 取消 三个按钮 发表按钮
    public void onClick(View view){
        Intent intent;
        switch(view.getId()){
            case R.id.take_picture:
                edit_photo_fullscreen_layout.setVisibility(View.GONE);
                takePictureUrl= APP.photo_path+"picture_take_0"
                        +addTakePicCount+".jpg";
                File file=new File(takePictureUrl);
                intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(file));
                startActivityForResult(intent,TAKE_PICTURE);
                addTakePicCount++;
                break;
            case R.id.select_local_picture:
                edit_photo_fullscreen_layout.setVisibility(View.GONE);
                intent=new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        "image/*");
                startActivityForResult(intent,LOCAL_PICTURE);
                break;
            /*case R.id.cancel_upload:
                edit_photo_fullscreen_layout.setVisibility(View.GONE);*/
            case R.id.upload://第一次握手：按发表键后
                //检查用户是否登录

                ACache cache= ACache.get(PhotosAddActivity.this);
                LoginDataModel userStr=(LoginDataModel)cache.getAsObject("loginModel");
                user=userStr.getUserModel();
                String usename=user.getPhone();
                String authKey=user.getAuth_key();
                if(!(authKey!=null&&!authKey.equals(""))){
                    CommonUtils.getUtilInstance().showToast(PhotosAddActivity.this,getString(R.string.publish_theme_after_login));
                    return;
                }
                try {
                    if (!checkInput())//检查用户输入
                        return;
                } catch (ParseException e) {
                    e.printStackTrace();
                    return;
                }
                String title=theme_title_edit.getText().toString();

                if(!addPic){
                    if(clearFormerUploadUrlList){
                        if(uploadImgUrlList.size()>0){
                            uploadImgUrlList.clear();
                            clearFormerUploadUrlList=false;
                        }
                    }
                }
                saveThemeInfo(usename,authKey,title);//发第一次请求


                if(!clearFormerUploadUrlList){
                    clearFormerUploadUrlList=true;
                }
                break;
            case R.id.delete_image:
                if(uploadImgUrlList.size()==0){
                    return;
                }
                uploadImgUrlList.remove(viewpagerPosition);
                imagePagerAdapter.changeList(uploadImgUrlList);
                imagePagerAdapter.notifyDataSetChanged();
                addPictureList.remove(viewpagerPosition+1);
                imageAddGridViewAdapter.changeList(addPictureList);
                imageAddGridViewAdapter.notifyDataSetChanged();
                position_in_total.setText((viewpagerPosition+1)+"/"+uploadImgUrlList.size());
                if(uploadImgUrlList.size()==0){
                    display_big_image_layout.setVisibility(View.GONE);
                    isBigImageShow=false;
                }
                addPicCount--;
                break;
        }
    }

    private boolean checkInput() throws ParseException {


        if (theme_title_edit.getText().toString().equals("")||theme_title_edit.getText().length()>20){
            CommonUtils.getUtilInstance().showLongToast(PhotosAddActivity.this,"请正确输入标题（20字以内）");
            return false;
        }

        if (theme_desc_edit.getText().length()<15){
            CommonUtils.getUtilInstance().showLongToast(PhotosAddActivity.this,"请输入15字以上的详细描述");
            return false;
        }
        if (uploadImgUrlList.size()<1){
            CommonUtils.getUtilInstance().showLongToast(PhotosAddActivity.this,"请至少配上一张图片");
            return false;
        }
        return true;
    }

    //上传图片(每一张调用一次这个函数)
    public void saveThemeImgNew(final String picUrl,String tk,int num){
        UploadManager uploadmgr=new UploadManager();
        File data=new File(picUrl);
        String key=imgList.get(num);
        String token=tk;
        //mProgress.setVisibility(View.VISIBLE);
        progressDlg=ProgressDialog.show(PhotosAddActivity.this, "发布活动", "正在上传图片", true, true, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                //取消了上传
            }
        });
        progressDlg.setMax(101);
        uploadmgr.put(data, key, token, new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo info, JSONObject response) {
                //完成，发信息给OSS服务器
                new Thread() {
                    public void run() {
                        Map<String, Object> map = new HashMap<>();
                        /* map.put("themeId",themeId);
                        map.put("imgBody",UploadPhotoUtil.getInstance().getUploadBitmapZoomString(picUrl));
                        map.put("imgType",UploadPhotoUtil.getInstance().getFileType(picUrl));
                        map.put("type",1);*/
                        picToAdd-=1;
                        if (picToAdd == 0) {
                            Message msg = handler.obtainMessage();
                            msg.obj = map;
                            msg.what = SAVE_THEME_IMAGE;
                            handler.sendMessage(msg);
                        }//要上传的图片包装在msg后变成了消息发到handler
                    }
                }.start();
            }
        }, new UploadOptions(null, null, false,
                new UpProgressHandler() {
                    public void progress(String key, double percent) {
                        //mProgress.setProgress((int)percent*100);
                        progressDlg.setProgress((int) percent * 100);
                    }
                },null));
    }
//发第一次请求，仅请求活动立项

    public void saveThemeInfo(String usrname,String auth_key,String title){
        Map<String, Object>map=new HashMap<String, Object>();
        if (finalImgList!=null)
            finalImgList.clear();
        finalImgList=new ArrayList<>();
        imgList=new ArrayList<>();
        for (int i=0;i<uploadImgUrlList.size();i++){
            String[] ext=uploadImgUrlList.get(i).split("\\.");
            String extention="."+ext[ext.length-1];
            String filename=user.getPhone()+"/"+uploadImgUrlList.get(i).hashCode()+new Random(System.nanoTime()).toString()+extention;
            imgList.add(filename);
            finalImgList.add(String.valueOf("\"" + filename + "\""));
        }
        map.put("username",usrname);
        map.put("auth_key",auth_key);
        map.put("title",title);
        map.put("type",StatusCode.REQUEST_CREATE_HUODONG);
        map.put("images", finalImgList);
        requestFragment.httpRequest(map, CommonUrl.createActivityInfo);
    }

    //
    @Override
    public void requestFinish(final String result,String requestUrl) throws JSONException {

        if(requestUrl.equals(CommonUrl.createActivityInfo)){//活动立项（第一次请求）完成的回调
            JSONObject object = new JSONObject(result);
            int code = Integer.valueOf(object.getString("code"));
            if (code == StatusCode.REQUEST_HUODONG_SUCCESS) {
                final JSONObject content=object.getJSONObject("contents");
                try {
                    apId = content.getInt("acID");
                    JSONArray auth_key_arr = content.getJSONArray("image_token");
                    for (int i = 0; i < auth_key_arr.length(); i++) {
                        Message msg = handler.obtainMessage();
                        Map<String, String> map = new HashMap<>();
                        map.put("auth_key", auth_key_arr.getString(i));
                        msg.obj = map;
                        msg.what = UPLOAD_TAKE_PICTURE;
                        handler.sendMessageDelayed(msg, 100);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Looper.prepare();CommonUtils.getUtilInstance().showToast(PhotosAddActivity.this, getString(R.string.publish_huodong_sucess));Looper.loop();
                return;
            }
            if (code== StatusCode.REQUEST_HUODONG_SUCCEED){
                if (progressDlg!=null)
                    progressDlg.dismiss();
                //Looper.prepare();CommonUtils.getUtilInstance().showToast(getActivity(), getString(R.string.publish_huodong_succeed));Looper.loop();
                Intent intent = new Intent(PhotosAddActivity.this, MainActivity.class);
                intent.putExtra("result","发布成功");
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                PhotosAddActivity.this.finish();
                return;
            }else {
                if (progressDlg!=null)
                    progressDlg.dismiss();
                Looper.prepare();
                CommonUtils.getUtilInstance().showToast(PhotosAddActivity.this,object.getString("contents"));Looper.loop();
            }
        }

    }

    @Override
    public void onDismissBigPhoto(){
        hideDisplayBigImageLayout();
    }

    private void hideDisplayBigImageLayout(){
        display_big_image_layout.setVisibility(View.GONE);
        isBigImageShow=false;
    }

    @Override
    public void exception(IOException e, String requestUrl) {
        Log.d("logout", "--------------------------" + e.getMessage());
    }

    public void hideBigPhotoLayout(){
        display_big_image_layout.setVisibility(View.GONE);
        edit_photo_fullscreen_layout.setVisibility(View.GONE);
    }

}
