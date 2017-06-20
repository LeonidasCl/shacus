package com.example.pc.shacus.Activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.example.pc.shacus.Util.CToast;
import com.example.pc.shacus.Util.CommonUrl;
import com.example.pc.shacus.Util.CommonUtils;
import com.example.pc.shacus.Util.UploadPhotoUtil;
import com.example.pc.shacus.View.CircleImageView;
import com.example.pc.shacus.View.Custom.IconEditView;
import com.example.pc.shacus.View.DateTimePicker.CustomDatePicker;
import com.example.pc.shacus.View.DateTimePicker.SlideDateTimeListener;
import com.example.pc.shacus.View.DateTimePicker.SlideDateTimePicker;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import okhttp3.internal.Util;

/**
 * Created by cuicui on 2017/3/28.
 */
public class SignupActivity extends AppCompatActivity implements NetworkCallbackInterface.NetRequestIterface,View.OnClickListener {

    TextView sign;
    TextView sign_usersign;
    TextView sign_userlocal;
    CircleImageView userimage;
    TextView imageupload;
    CheckBox checkBox;
    TextView sign_userbirth;

    String sex = "1";
    String birthday = "1970-01-01";

    //拍照/选择图片
    private FrameLayout edit_photo_fullscreen_layout;
    private RelativeLayout edit_photo_outer_layout, display_big_image_layout;
    private TextView take_picture, select_local_picture, cancelEditPhoto;

    private String takePictureUrl, upToken, imageFileName;
    private final int NONE = 0, TAKE_PICTURE = 1, LOCAL_PICTURE = 2, UPLOAD_TAKE_PICTURE = 4, SAVE_THEME_IMAGE = 5;

    private Intent intent;
    private ProgressDialog progressDlg;
    UserModel dataModel;
    private NetRequest netRequest;
    private int addTakePicCount = 1;
    private boolean headImageChanged = false, imagefinish = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_second);

        //从缓存中取出数据
        ACache cache = ACache.get(this);
        dataModel = ((LoginDataModel) cache.getAsObject("loginModel")).getUserModel();
        RadioGroup radgroup = (RadioGroup) findViewById(R.id.radioGroup);
        netRequest = new NetRequest(this, this);
        //第一种获得单选按钮值的方法
        //为radioGroup设置一个监听器:setOnCheckedChanged()
        radgroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radbtn = (RadioButton) findViewById(checkedId);
                if (checkedId == R.id.btnWoman) {
                    sex = "0";//女是0
                    Log.d("ccccccccccccc", sex);
                } else if (checkedId == R.id.btnMan) {
                    sex = "1";
                    Log.d("ccccccccccccc", sex);
                }


//                Toast.makeText(getApplicationContext(), "按钮组值发生改变,你选了" + radbtn.getText(), Toast.LENGTH_LONG).show();
            }
        });
        checkBox = (CheckBox) findViewById(R.id.checkbox);
        sign_userbirth = (TextView) findViewById(R.id.sign_birth);
        sign_userbirth.setOnClickListener(this);
        sign_usersign = (TextView) findViewById(R.id.sign_usersign);
        sign_userlocal = (TextView) findViewById(R.id.sign_userlocal);
        imageupload = (TextView) findViewById(R.id.imageupload);
        imageupload.setOnClickListener(this);
        userimage = (CircleImageView) findViewById(R.id.sign_userImage);
        userimage.setOnClickListener(this);
        sign = (TextView) findViewById(R.id.btnsign);
        sign.setOnClickListener(this);
        display_big_image_layout = (RelativeLayout) findViewById(R.id.display_big_image_layout);
        display_big_image_layout.setOnClickListener(this);
        edit_photo_fullscreen_layout = (FrameLayout) findViewById(R.id.edit_photo_fullscreen_layout);
        edit_photo_fullscreen_layout.setOnClickListener(this);
        edit_photo_outer_layout = (RelativeLayout) findViewById(R.id.edit_photo_outer_layout);

        select_local_picture = (TextView) findViewById(R.id.select_local_picture);
        select_local_picture.setOnClickListener(this);
        take_picture = (TextView) findViewById(R.id.take_picture);
        take_picture.setOnClickListener(this);
        cancelEditPhoto = (TextView) findViewById(R.id.cancel_upload);
        cancelEditPhoto.setOnClickListener(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED||
                ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                ||ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                ||ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                ||ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},0);
        }

    }

    //验证注册信息
    private String verify() {
        //除头像外的其它基本信息
        //昵称、性别、位置、年龄
        String result = "";
        result += "0"; //头像更改，此处保留
        if (!dataModel.getNickName().equals(String.valueOf(sign_usersign.getText())))
            result += "1";
        else
            result += "0";
        if (!dataModel.getSex().equals(sex))
            result += "1";
        else
            result += "0";

        if (!dataModel.getLocation().equals(String.valueOf(sign_userlocal.getText())))
            result += "1";
        else
            result += "0";

        result += "1";

        Log.d("CCCCCCCCCCCCCCC", result);
        return result;
    }

    public void hideBigPhotoLayout() {
        display_big_image_layout.setVisibility(View.GONE);
        edit_photo_fullscreen_layout.setVisibility(View.GONE);
    }

    @Override
    public void requestFinish(String result, String requestUrl) throws JSONException {
        if (requestUrl.equals(CommonUrl.settingChangeNetUrl)) {
            JSONObject jsonObject = new JSONObject(result);
            int code = Integer.valueOf(jsonObject.getString("code"));

            Log.d("CCCCCCCCCCCCCCC", jsonObject.toString());
            switch (code) {
                case StatusCode.REQUEST_CHANGE_HEAD_IMAGE_SUCCESS:
                    imagefinish = false;
                    JSONArray token = jsonObject.getJSONArray("contents");
                    upToken = token.getString(0);//成功获取口令
                    handler.sendEmptyMessage(UPLOAD_TAKE_PICTURE);
                    break;
                case StatusCode.REQUEST_SETTING_CHANGE_NICKNAME:
                    dataModel.setNickName(sign_usersign.getText().toString());
                    break;
                case StatusCode.REQUEST_SETTING_CHANGE_BIRTH:
                    dataModel.setBirthday(birthday);
                    break;
                case StatusCode.REQUEST_SETTING_CHANGE_SEX:
                    dataModel.setSex(sex);
                    break;
                case StatusCode.REQUEST_SETTING_CHANGE_ADDRESS:
                    dataModel.setLocation(sign_userlocal.getText().toString());
                    break;
                case 10518:
                    dataModel.setSign(sign_usersign.getText().toString());
                    break;
                case 66666:
                    Message message = new Message();
                    message.what = 66666;
                    handler.sendMessage(message);
                    String url = jsonObject.getString("contents");
                    dataModel.setHeadImage(url);
                    imagefinish = true;
                    break;
            }
            ACache aCache = ACache.get(SignupActivity.this);
            LoginDataModel model = (LoginDataModel) aCache.getAsObject("loginModel");
            model.setUserModel(dataModel);
            aCache.put("loginModel", model);
        }
        if (imagefinish)
        {
            Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
            intent.putExtra("result", 1);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
            finish();
        }
        else
            return;
    }

    @Override
    public void exception(IOException e, String requestUrl) {

    }

    @Override
    public void onClick(final View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.imageupload:
                edit_photo_fullscreen_layout.setVisibility(View.VISIBLE);
                Animation get_photo_layout_in_from_down = AnimationUtils.loadAnimation(this, R.anim.search_layout_in_from_down);
                edit_photo_outer_layout.startAnimation(get_photo_layout_in_from_down);
                break;
            case R.id.user_image:
                display_big_image_layout.setVisibility(View.VISIBLE);
                ImageView image = (ImageView) findViewById(R.id.fulldisplay_headimage);
                Glide.with(APP.context)
                        .load(userimage)
                        .into(image);
                break;
            case R.id.sign_birth:
                final DatePickerDialog datePickerDialog;

                Calendar calendar = Calendar.getInstance();

                datePickerDialog = new DatePickerDialog(SignupActivity.this, AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        //这里获取到的月份需要加上1哦~
                        String m = null;
                        String d = null;
                        if(month+1 < 10) {
                            m = "0" + (month + 1);
                        }else
                            m = String.valueOf(month + 1);

                        if (dayOfMonth < 10){
                            d = "0" + dayOfMonth;
                        }else
                            d = String.valueOf(dayOfMonth);
                        String result = String.valueOf(year) + "-" + m + "-" + d;
                        Log.d("CCCCCCCCCCCCCCCCC",result);
                        birthday = result;
                        sign_userbirth.setText(result);
//                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                    }
                }
                        , calendar.get(Calendar.YEAR)
                        , calendar.get(Calendar.MONTH)
                        , calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.setTitle("选择出生日期");
//                ((ViewGroup) ((ViewGroup) datePickerDialog.getDatePicker().getChildAt(0)).getChildAt(0)).getChildAt(2).setVisibility(View.GONE);
                datePickerDialog.getDatePicker().setMaxDate((new Date()).getTime());
                datePickerDialog.show();
                break;
            case R.id.btnsign:
                //传入数据
                if (sign_usersign.getText().toString().equals("")) {
                    CToast.makeText(this, "请输入个人简介", Toast.LENGTH_LONG).show();
                    break;
                }else if(sign_usersign.getText().length()>15){
                    sign_usersign.setText(sign_usersign.getText().toString().substring(0,14));
                    CommonUtils.getUtilInstance().showToast(SignupActivity.this, "签名长度不能超过15");
                    break;
                }
                if (sign_userlocal.getText().toString().equals("")) {
                    CToast.makeText(this, "请输入位置", Toast.LENGTH_LONG).show();
                    break;
                }
                if (birthday.equals("")) {
                    CToast.makeText(this, "请输入选择出生年月", Toast.LENGTH_LONG).show();
                    break;
                }
                if (!checkBox.isChecked()) {
                    CToast.makeText(this, "请先同意《人人拍用户协议》", Toast.LENGTH_LONG).show();
                    break;
                }

                String result = verify();//简介、性别、位置、年龄
                if (result.charAt(1) == '1') {
                    HashMap map=new HashMap();
                    map.put("uid",dataModel.getId());
                    map.put("authkey",dataModel.getAuth_key());
                    map.put("sign",sign_usersign.getText().toString());
                    map.put("type", "10517");
                    netRequest.httpRequest(map, CommonUrl.settingChangeNetUrl);
                    Log.d("CCCCCCCCCCCCCCC", "user nickname" + sign_usersign.getText().toString());
                }

                if (result.charAt(2) == '1') {
                    //性别
                    HashMap map = new HashMap<>();
                    map.put("uid", dataModel.getId());
                    map.put("gender", sex);
                    map.put("authkey", dataModel.getAuth_key());
                    map.put("type", StatusCode.REQUEST_SETTING_CHANGE_SEX);
                    netRequest.httpRequest(map, CommonUrl.settingChangeNetUrl);
                    Log.d("CCCCCCCCCCCCCCC", "user sex" + sign_usersign.getText().toString());

                }

                if (result.charAt(3) == '1') {
                    HashMap map = new HashMap<>();
                    map.put("Userlocation", sign_userlocal.getText().toString());
                    map.put("Userid", dataModel.getId());
                    map.put("type", StatusCode.REQUEST_SETTING_CHANGE_ADDRESS);
                    netRequest.httpRequest(map, CommonUrl.settingChangeNetUrl);
                    Log.d("CCCCCCCCCCCCC", "UserSex" + sex.toString());
                }

                if (result.charAt(4) == '1') {
                    //年龄
                    HashMap map = new HashMap<>();
                    map.put("authkey", dataModel.getAuth_key());
                    map.put("uid", dataModel.getId());
                    map.put("type", StatusCode.REQUEST_SETTING_CHANGE_BIRTH);
                    /////
                    map.put("birthday", birthday);
                    netRequest.httpRequest(map, CommonUrl.settingChangeNetUrl);
                    Log.d("CCCCCCCCCCCCC", "Userbirth" + birthday.toString());
                }

                //头像更改
                if (headImageChanged) {
                    HashMap map = new HashMap();
                    map.put("uid", dataModel.getId());
                    map.put("authkey", dataModel.getAuth_key());
                    String[] ext = takePictureUrl.split("\\.");
                    imageFileName = dataModel.getPhone() + "/" + takePictureUrl.hashCode() + new Random(System.nanoTime()).toString() + ext[ext.length - 1];
                    ArrayList<String> temp = new ArrayList<>();
                    temp.add("\"" + imageFileName + "\"");
                    map.put("image", temp);
                    map.put("type", StatusCode.REQUEST_CHANGE_HEAD_IMAGE);
                    netRequest.httpRequest(map, CommonUrl.settingChangeNetUrl);
                    return;
                }
                /*if (result.equals("00000")) {
                    //什么也没怎么做
                    finish();
                }*/
                break;
            case R.id.edit_photo_fullscreen_layout:
                hideBigPhotoLayout();
                break;
            case R.id.display_big_image_layout:
                hideBigPhotoLayout();
                break;
            case R.id.take_picture:
                edit_photo_fullscreen_layout.setVisibility(View.GONE);
                takePictureUrl = APP.photo_path + "picture_take_0" + ".jpg" + addTakePicCount + ".jpg";
                ;
                File file = new File(takePictureUrl);
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                startActivityForResult(intent, TAKE_PICTURE);
                addTakePicCount++;
                break;
            case R.id.select_local_picture:
                edit_photo_fullscreen_layout.setVisibility(View.GONE);
                intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, LOCAL_PICTURE);
                break;
            case R.id.cancel_upload:
                edit_photo_fullscreen_layout.setVisibility(View.GONE);
                break;

        }
    }

    // 下面函数是选中了要上传图片或者拍照打完钩后运行的
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == NONE)
            return;
        //耗时操作传到handler里去处理
        if (requestCode == TAKE_PICTURE) {
            handler.sendEmptyMessage(TAKE_PICTURE);
            return;
        }
        if (resultCode == Activity.RESULT_OK) {
            this.intent = intent;
            handler.sendEmptyMessage(LOCAL_PICTURE);
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SAVE_THEME_IMAGE://第一次接到请求
                    progressDlg.dismiss();
                    HashMap map = new HashMap();
                    map.put("type", StatusCode.REQUEST_SAVE_CHANGED_HEAD_IMAGE);
                    map.put("authkey", dataModel.getAuth_key());
                    ArrayList<String> temp = new ArrayList<>();
                    temp.add("\"" + imageFileName + "\"");
                    map.put("image", temp);
                    map.put("uid", dataModel.getId());
                    netRequest.httpRequest(map, CommonUrl.settingChangeNetUrl);
                    progressDlg = ProgressDialog.show(SignupActivity.this, "上传头像", "正在保存头像", true, true, new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialogInterface) {
                            //上传完图片后取消了保存
                        }
                    });
                    break;
                case TAKE_PICTURE://拍了照片后msg的处理
                    //在这里处理，获取拍到的图
                    Bitmap bitmap = UploadPhotoUtil.getInstance()
                            .trasformToZoomBitmapAndLessMemory(takePictureUrl);
                    BitmapDrawable bd = new BitmapDrawable(getResources(), bitmap);
                    Drawable addPicture = bd;
                    userimage.setImageDrawable(addPicture);
                    headImageChanged = true;
                    break;
                //在图库选中了本地的图
                case LOCAL_PICTURE:
                    Uri uri = intent.getData();
                    String photo_local_file_path = getPath_above19(SignupActivity.this, uri);
                    if (!(photo_local_file_path.toString().toLowerCase().endsWith("jpg") || photo_local_file_path.toString().toLowerCase().endsWith("png")
                            || photo_local_file_path.toString().toLowerCase().endsWith("jpeg") || photo_local_file_path.toString().toLowerCase().endsWith("gif"))) {
                        CommonUtils.getUtilInstance().showToast(SignupActivity.this, "不支持此格式的上传");
                        break;
                    }
                    Bitmap bitmap2 = UploadPhotoUtil.getInstance().trasformToZoomPhotoAndLessMemory(photo_local_file_path);
                    addPicture = new BitmapDrawable(getResources(), bitmap2);
                    takePictureUrl = photo_local_file_path;
                    userimage.setImageDrawable(addPicture);
                    headImageChanged = true;
                    break;
                case UPLOAD_TAKE_PICTURE://第一次发送请求
                    UploadManager uploadmgr = new UploadManager();
                    File data = new File(takePictureUrl);
                    String key = imageFileName;
                    String token = upToken;
                    //mProgress.setVisibility(View.VISIBLE);
                    progressDlg = ProgressDialog.show(SignupActivity.this, "上传头像", "正在上传图片", true, true, new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialogInterface) {
                            //取消了上传
                        }
                    });
                    progressDlg.setMax(101);
                    uploadmgr.put(data, key, token, new UpCompletionHandler() {
                        @Override
                        public void complete(String key, ResponseInfo info, JSONObject response) {
                            //完成，发信息给业务服务器
                            new Thread() {
                                public void run() {
                                    Map<String, Object> map = new HashMap<>();
                        /* map.put("themeId",themeId);
                        map.put("imgBody",UploadPhotoUtil.getInstance().getUploadBitmapZoomString(picUrl));
                        map.put("imgType",UploadPhotoUtil.getInstance().getFileType(picUrl));
                        map.put("type",1);*/
                                    Message msg = handler.obtainMessage();
                                    msg.obj = map;
                                    msg.what = SAVE_THEME_IMAGE;
                                    handler.sendMessage(msg);//要上传的图片包装在msg后变成了消息发到handler
                                }
                            }.start();
                        }
                    }, new UploadOptions(null, null, false,
                            new UpProgressHandler() {
                                public void progress(String key, double percent) {
                                    //mProgress.setProgress((int)percent*100);
                                    progressDlg.setProgress((int) percent * 100);
                                }
                            }, null));

                    break;
                case 66666:{
                    progressDlg.dismiss();
                    break;
                }
            }
        }
    };

    //是否为外置存储器
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */

    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
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
                                       String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPath_above19(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

}