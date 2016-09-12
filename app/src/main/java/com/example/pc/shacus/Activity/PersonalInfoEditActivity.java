package com.example.pc.shacus.Activity;

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
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pc.shacus.APP;
import com.example.pc.shacus.Data.Cache.ACache;
import com.example.pc.shacus.Data.Model.LoginDataModel;
import com.example.pc.shacus.Data.Model.UserModel;
import com.example.pc.shacus.Network.NetRequest;
import com.example.pc.shacus.Network.NetworkCallbackInterface;
import com.example.pc.shacus.Network.StatusCode;
import com.example.pc.shacus.R;
import com.example.pc.shacus.Util.AutoHint;
import com.example.pc.shacus.Util.CommonUrl;
import com.example.pc.shacus.Util.CommonUtils;
import com.example.pc.shacus.Util.UploadPhotoUtil;
import com.example.pc.shacus.View.CircleImageView;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

//Author:LQ
//Time:9.1
public class PersonalInfoEditActivity extends AppCompatActivity implements View.OnClickListener,NetworkCallbackInterface.NetRequestIterface{

    private ImageButton btn_back;
    private EditText userName,userAddress,userPhoneNumber,userEmail;
    private CircleImageView userImage;
    private TextView btn_finish;
    private TextView take_picture,select_local_picture;
    private FrameLayout edit_photo_fullscreen_layout;
    UserModel dataModel;
    private NetRequest netRequest;
    private String takePictureUrl,upToken,imageFileName;
    private int addTakePicCount=1;
    private final int NONE=0,TAKE_PICTURE=1,LOCAL_PICTURE=2,UPLOAD_TAKE_PICTURE=4,SAVE_THEME_IMAGE=5;
    private RelativeLayout edit_photo_outer_layout;
    private Intent intent;
    private Drawable addPicture;
    private boolean headImageChanged=false;
    private ProgressDialog progressDlg;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case SAVE_THEME_IMAGE://第一次接到请求
                    progressDlg.dismiss();
                    HashMap map=new HashMap();
                    map.put("type",StatusCode.REQUEST_SAVE_CHANGED_HEAD_IMAGE);
                    map.put("authkey",dataModel.getAuth_key());
                    ArrayList<String>temp=new ArrayList<>();
                    temp.add("\""+imageFileName+"\"");
                    map.put("image",temp);
                    map.put("uid",dataModel.getId());
                    netRequest.httpRequest(map, CommonUrl.settingChangeNetUrl);
                    progressDlg=ProgressDialog.show(PersonalInfoEditActivity.this, "上传头像", "正在保存头像", true, true, new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialogInterface) {
                            //上传完图片后取消了保存
                        }
                    });
                    break;
                case UPLOAD_TAKE_PICTURE://第一次发送请求
                    UploadManager uploadmgr=new UploadManager();
                    File data=new File(takePictureUrl);
                    String key=imageFileName;
                    String token=upToken;
                    //mProgress.setVisibility(View.VISIBLE);
                    progressDlg= ProgressDialog.show(PersonalInfoEditActivity.this, "上传头像", "正在上传图片", true, true, new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialogInterface) {
                            //取消了上传
                        }
                    });
                    progressDlg.setMax(101);
                    uploadmgr.put(data, key, token, new UpCompletionHandler(){
                        @Override
                        public void complete(String key, ResponseInfo info, JSONObject response) {
                            //完成，发信息给业务服务器
                            new Thread(){
                                public void run(){
                                    Map<String, Object> map=new HashMap<>();
                        /* map.put("themeId",themeId);
                        map.put("imgBody",UploadPhotoUtil.getInstance().getUploadBitmapZoomString(picUrl));
                        map.put("imgType",UploadPhotoUtil.getInstance().getFileType(picUrl));
                        map.put("type",1);*/
                                        Message msg=handler.obtainMessage();
                                        msg.obj=map;
                                        msg.what=SAVE_THEME_IMAGE;
                                        handler.sendMessage(msg);//要上传的图片包装在msg后变成了消息发到handler
                                }
                            }.start();
                        }
                    },new UploadOptions(null, null, false,
                            new UpProgressHandler(){
                                public void progress(String key, double percent){
                                    //mProgress.setProgress((int)percent*100);
                                    progressDlg.setProgress((int)percent*100);
                                }
                            },null));

                    break;
                case TAKE_PICTURE:
                    //在这里处理，获取拍到的图
                    Bitmap bitmap= UploadPhotoUtil.getInstance()
                            .trasformToZoomBitmapAndLessMemory(takePictureUrl);
                    BitmapDrawable bd=new BitmapDrawable(getResources(),bitmap);
                    addPicture=bd;
                    userImage.setImageDrawable(addPicture);
                    headImageChanged=true;
                    break;
                case LOCAL_PICTURE:
                    Uri uri=intent.getData();
                    String photo_local_file_path=getPath_above19(PersonalInfoEditActivity.this, uri);
                    if (!(photo_local_file_path.toString().toLowerCase().endsWith("jpg")||photo_local_file_path.toString().toLowerCase().endsWith("png")
                            ||photo_local_file_path.toString().toLowerCase().endsWith("jpeg")||photo_local_file_path.toString().toLowerCase().endsWith("gif"))){
                        CommonUtils.getUtilInstance().showToast(PersonalInfoEditActivity.this,"不支持此格式的上传");
                        break;
                    }
                    Bitmap bitmap2=UploadPhotoUtil.getInstance().trasformToZoomBitmapAndLessMemory(photo_local_file_path);
                    addPicture=new BitmapDrawable(getResources(), bitmap2);
                    takePictureUrl=photo_local_file_path;
                    userImage.setImageDrawable(addPicture);
                    headImageChanged=true;
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info_edit);

        //init
        btn_back= (ImageButton) findViewById(R.id.btn_back);
        btn_finish= (TextView) findViewById(R.id.btn_finish);
        userName = (EditText) findViewById(R.id.textData_UserName);
        userEmail= (EditText) findViewById(R.id.textData_UserEmail);
        userAddress= (EditText) findViewById(R.id.textData_UserAddress);
        userPhoneNumber= (EditText) findViewById(R.id.textData_UserPhoneNumber);
        userImage= (CircleImageView) findViewById(R.id.imageData_UserImage);

        take_picture= (TextView) findViewById(R.id.take_picture);
        select_local_picture= (TextView) findViewById(R.id.select_local_picture);
        netRequest=new NetRequest(this,this);

        userPhoneNumber.setEnabled(false);
//        userEmail.setThreshold(1);
//        ArrayAdapter arrayAdapter=new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,AutoHint.EMAILS);
//        userEmail.setAdapter(arrayAdapter);

        edit_photo_fullscreen_layout=(FrameLayout)findViewById(R.id.edit_photo_fullscreen_layout);
        edit_photo_fullscreen_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_photo_fullscreen_layout.setVisibility(View.GONE);
            }
        });
        edit_photo_outer_layout=(RelativeLayout)findViewById(R.id.edit_photo_outer_layout);
        TextView cancelEditPhoto=(TextView)edit_photo_outer_layout.findViewById(R.id.cancel_upload);
        cancelEditPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {edit_photo_fullscreen_layout.setVisibility(View.GONE);
            }
        });

        //listener
        btn_back.setOnClickListener(this);
        btn_finish.setOnClickListener(this);
        userImage.setOnClickListener(this);
        take_picture.setOnClickListener(this);
        select_local_picture.setOnClickListener(this);

        //从缓存中取出数据
        ACache cache=ACache.get(PersonalInfoEditActivity.this);
        dataModel= ((LoginDataModel) cache.getAsObject("loginModel")).getUserModel();
        getUserName().setText(dataModel.getNickName());
        getUserPhoneNumber().setText(dataModel.getPhone());
        getUserEmail().setText(dataModel.getMailBox());
        getUserAddress().setText(dataModel.getLocation());

        Glide.with(this)
                .load(dataModel.getHeadImage()).centerCrop()
//                .placeholder(R.drawable.holder)
                .error(R.drawable.loading_error)
                .into(getUserImage());



    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_finish:
                //传入数据
                if(userName.getText().toString().equals("")||userEmail.getText().toString()=="") {
                    Toast.makeText(this, "昵称不能为空", Toast.LENGTH_SHORT).show();
                    break;
                }
                String result=checkChange();
                if(result.charAt(1)=='1'){
                    HashMap map=new HashMap<>();
                    map.put("Usernickname",getUserName().getText().toString());
                    map.put("Userid",dataModel.getId());
                    map.put("type", StatusCode.REQUEST_SETTING_CHANGE_NICKNAME);
                    netRequest.httpRequest(map, CommonUrl.settingChangeNetUrl);
                    Log.d("LQQQQQQQ", "user nickname");
                }
                if(result.charAt(2)=='1'){
                    HashMap map=new HashMap<>();
                    map.put("Userphone",getUserPhoneNumber().getText().toString());
                    map.put("Userid",dataModel.getId());
                    map.put("type", StatusCode.REQUEST_SETTING_CHANGE_PHONENUMBER);
                    netRequest.httpRequest(map, CommonUrl.settingChangeNetUrl);
                    Log.d("LQQQQQQQ", "Userphone");
                }
                if(result.charAt(3)=='1'){
                    HashMap map=new HashMap<>();
                    map.put("Userlocation",getUserAddress().getText().toString());
                    map.put("Userid",dataModel.getId());
                    map.put("type", StatusCode.REQUEST_SETTING_CHANGE_ADDRESS);
                    netRequest.httpRequest(map, CommonUrl.settingChangeNetUrl);
                    Log.d("LQQQQQQQ", "Userlocation");
                }
                if(result.charAt(4)=='1'){
                    HashMap map=new HashMap<>();
                    map.put("Usermail",getUserEmail().getText().toString());
                    map.put("Userid",dataModel.getId());
                    map.put("type", StatusCode.REQUEST_SETTING_CHANGE_EMAIL);
                    netRequest.httpRequest(map, CommonUrl.settingChangeNetUrl);
                    Log.d("LQQQQQQQ", "Usermail");
                }
                //////////////////
                //头像更改
                /////////////////
                if(headImageChanged){
                    HashMap map=new HashMap();
                    map.put("uid",dataModel.getId());
                    map.put("authkey",dataModel.getAuth_key());
                    String []ext=takePictureUrl.split("\\.");
                    imageFileName=dataModel.getPhone()+"/"+takePictureUrl.hashCode()+new Random(System.nanoTime()).toString()+ext[ext.length-1];
                    ArrayList<String>temp=new ArrayList<>();
                    temp.add("\""+imageFileName+"\"");
                    map.put("image",temp);
                    map.put("type", StatusCode.REQUEST_CHANGE_HEAD_IMAGE);
                    netRequest.httpRequest(map,CommonUrl.settingChangeNetUrl);
                    return;
                }


                if(result.equals("00000")){finish();}
                break;
            case R.id.imageData_UserImage:
                edit_photo_fullscreen_layout.setVisibility(View.VISIBLE);
                Animation get_photo_layout_in_from_down = AnimationUtils.loadAnimation(this, R.anim.search_layout_in_from_down);
                edit_photo_outer_layout.startAnimation(get_photo_layout_in_from_down);

                break;
            case R.id.take_picture:
                edit_photo_fullscreen_layout.setVisibility(View.GONE);
                takePictureUrl= APP.photo_path+"picture_take_0"
                        +addTakePicCount+".jpg";
                File file=new File(takePictureUrl);
                intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
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
        }
    }

    //检测更改情况
    private String checkChange() {
        String result="";
        //头像更改
        if (false){result+="1";}else{result+="0";}
        //其他更改
        if(!dataModel.getNickName().equals(String.valueOf(userName.getText()))){result+="1";}else{result+="0";}
        if(!dataModel.getPhone().equals(String.valueOf(userPhoneNumber.getText()))){result+="1";}else{result+="0";}
        if(!dataModel.getLocation().equals(String.valueOf(userAddress.getText()))){result+="1";}else{result+="0";}
        if(!dataModel.getMailBox().equals(String.valueOf(userEmail.getText()))){result+="1";}else{result+="0";}

        //刷新缓存
        Log.d("LQQQQQQQQQQQ", result);
        return result;
    }

    public EditText getUserName() {
        return userName;
    }

    public EditText getUserEmail() {
        return userEmail;
    }

    public EditText getUserAddress() {
        return userAddress;
    }

    public EditText getUserPhoneNumber() {
        return userPhoneNumber;
    }

    @Override
    public void requestFinish(String result, String requestUrl) throws JSONException {
        if(requestUrl.equals(CommonUrl.settingChangeNetUrl)){
            JSONObject jsonObject=new JSONObject(result);
            String code=jsonObject.getString("code");
            Log.d("LQQQQQ", code);
            Log.d("LQQQQQ", jsonObject.getString("contents"));
            if(code.equals("10503")){
                dataModel.setNickName(getUserName().getText().toString());}
            else if(code.equals("10504")){
                Log.d("LQQQQQQQ", "name fail");}
            else if(code.equals("10505")){
                dataModel.setPhone(getUserPhoneNumber().getText().toString());}
            else if(code.equals("10506")){
                Log.d("LQQQQQQQ", "num fail");}
            else if(code.equals("10507")){
                dataModel.setLocation(getUserAddress().getText().toString());}
            else if(code.equals("10508")){
                Log.d("LQQQQQQQ", "adress fail");}
            else if(code.equals("10509")){
                dataModel.setMailBox(getUserEmail().getText().toString());}
            else if(code.equals("10510")){
                Log.d("LQQQQQQQ", "email fail");}

            if (code.equals("10514")){
                CommonUtils.getUtilInstance().showToast(PersonalInfoEditActivity.this,"获取上传认证失败");
                return;
            }else if(code.equals("10515")){
                JSONArray token=jsonObject.getJSONArray("contents");
                upToken=token.getString(0);//成功获取口令
                handler.sendEmptyMessage(UPLOAD_TAKE_PICTURE);
                return;
            }else if(code.equals("66666")){//66666
                progressDlg.dismiss();
                String url=jsonObject.getString("contents");
                dataModel.setHeadImage(url);
            }
            ACache cache=ACache.get(PersonalInfoEditActivity.this);
            LoginDataModel model=(LoginDataModel)cache.getAsObject("loginModel");
            model.setUserModel(dataModel);
            cache.put("loginModel", model);
        }
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==NONE)
            return;
        //耗时操作传到handler里去处理
        if(requestCode==TAKE_PICTURE){
            handler.sendEmptyMessage(TAKE_PICTURE);
            return;
        }
        if(resultCode== Activity.RESULT_OK){
            this.intent=data;
            handler.sendEmptyMessage(LOCAL_PICTURE);
        }
    }

    /*************
     * @param uri
     * @return
     */
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
    public static String getDataColumn(Context context,Uri uri,String selection,
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

    @Override
    public void exception(IOException e, String requestUrl) {

    }

    public CircleImageView getUserImage() {
        return userImage;
    }

}
