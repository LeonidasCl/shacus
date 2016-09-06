package com.example.pc.shacus.Fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
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
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pc.shacus.APP;
import com.example.pc.shacus.Activity.CreateYuePaiActivity;
import com.example.pc.shacus.Activity.MainActivity;
import com.example.pc.shacus.Activity.TagAddActivity;
import com.example.pc.shacus.Adapter.ImageAddGridViewAdapter;
import com.example.pc.shacus.Adapter.ImagePagerAdapter;
import com.example.pc.shacus.Adapter.PhotoViewAttacher;
import com.example.pc.shacus.Adapter.UploadViewPager;
import com.example.pc.shacus.Data.Cache.ACache;
import com.example.pc.shacus.Data.Model.LoginDataModel;
import com.example.pc.shacus.Data.Model.UserModel;
import com.example.pc.shacus.Network.NetRequest;
import com.example.pc.shacus.Network.NetworkCallbackInterface;
import com.example.pc.shacus.Network.StatusCode;
import com.example.pc.shacus.R;
import com.example.pc.shacus.Util.CommonUrl;
import com.example.pc.shacus.Util.CommonUtils;
import com.example.pc.shacus.Util.UploadPhotoUtil;
import com.example.pc.shacus.View.Custom.ImgAddGridView;
import com.example.pc.shacus.View.DateTimePicker.SlideDateTimeListener;
import com.example.pc.shacus.View.DateTimePicker.SlideDateTimePicker;
import com.example.pc.shacus.View.TagView.TagContainerLayout;
import com.example.pc.shacus.View.TagView.TagView;
import com.google.gson.Gson;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 创建约拍界面
 * Created by pc on 2016/8/26.
 */
public class FragmentCreateYuePaiA extends Fragment implements View.OnClickListener, NetworkCallbackInterface.NetRequestIterface,
            NetworkCallbackInterface.OnSingleTapDismissBigPhotoListener{

    private final int UPLOAD_TAKE_PICTURE=5;
    private ProgressDialog progressDlg;
    private final int NONE=0,TAKE_PICTURE=1,LOCAL_PICTURE=2;
    private final int SAVE_THEME_IMAGE=8;
    private final int SHOW_TAKE_PICTURE=9;
    private final int SHOW_LOCAL_PICTURE=10;
    private FrameLayout edit_photo_fullscreen_layout;
    private RelativeLayout edit_photo_outer_layout,display_big_image_layout,show_upload_pic_layout;
    private Animation get_photo_layout_in_from_down;
    private TextView take_picture,select_local_picture,position_in_total,upload;
    private ImageView delete_image;
    private TextView startTime;
    private boolean timeFlag=false;
    private Date startdate=new Date();
    private Date enddate=new Date();
    private TextView endTime,joinEndTime;
    private String takePictureUrl,newThemeId;
    private Intent intent;
    private NetRequest requestFragment;
    private SimpleDateFormat mFormatter = new SimpleDateFormat("yyyy/MM/dd E HH:mm");
    private ImgAddGridView add_image_gridview;
    private CheckBox checkbox_free;
    private EditText price_edit;
    private int picToAdd=-1;
    private int addPicCount=1,addTakePicCount=1,viewpagerPosition;
    private List<String> uploadImgUrlList=new ArrayList<>();
    private List<Drawable>addPictureList=new ArrayList<>();
    private List<String>pictureUrlList=new ArrayList<>();
    private ImageAddGridViewAdapter imageAddGridViewAdapter;
    private ImagePagerAdapter imagePagerAdapter;
    private UploadViewPager image_viewpager;
    private boolean isBigImageShow=false,isShowUploadPic=false,addPic=false,clearFormerUploadUrlList=true;
    private EditText theme_title_edit,theme_desc_edit,location_edit;
    private ListView theme_listview;
    private TagContainerLayout mTagContainerLayout;
    private Date joinenddate;
    private int YUEPAI_TYPE=1;//约拍的种类，1为约模特，2为约摄影师
    private UserModel user;
    private int apId;
    private ArrayList<String> imgList;

    private SlideDateTimeListener startlistener = new SlideDateTimeListener() {
        @Override
        public void onDateTimeSet(Date date)
        {
            //Toast.makeText(getContext(), mFormatter.format(date), Toast.LENGTH_SHORT).show();
            startdate=date;
            timeFlag=true;
            startTime.setText(mFormatter.format(date));
        }
        @Override
        public void onDateTimeCancel() {}
    };
    private SlideDateTimeListener endlistener = new SlideDateTimeListener() {
        @Override
        public void onDateTimeSet(Date date)
        {
            enddate=date;
            endTime.setText(mFormatter.format(date));
        }
        @Override
        public void onDateTimeCancel() {}
    };
    private SlideDateTimeListener joinlistener = new SlideDateTimeListener() {
        @Override
        public void onDateTimeSet(Date date)
        {
            joinenddate=date;
            joinEndTime.setText(mFormatter.format(date));
        }
        @Override
        public void onDateTimeCancel() {}
    };

    private Handler handler=new Handler(){

        // 选完图片后就是在这里处理的
        @Override
        public void handleMessage(Message msg){
            switch(msg.what){

                case SAVE_THEME_IMAGE://响应第二次msg，将上传图片结果与真约拍信息反馈给业务服务器
                    //Map<String, Object> map=(Map<String, Object>)msg.obj;
                    progressDlg.dismiss();
                    Map map=new HashMap<>();
                    map.put("ap_type",YUEPAI_TYPE==1?"1":"2");
                    map.put("auth_key",user.getAuth_key());
                    map.put("title",theme_title_edit.getText().toString());
                    map.put("type", StatusCode.REQUEST_SEND_YUEPAI);
                    map.put("apid",apId);
                    List<String> list=mTagContainerLayout.getTags();
                    map.put("tags",list.toString());
                    String[] start=startTime.getText().toString().split(" ");
                    map.put("start_time",start[0].replaceAll("/","-")+" "+start[2]+":00");
                    String[] end=endTime.getText().toString().split(" ");
                    map.put("end_time",end[0].replaceAll("/","-")+" "+end[2]+":00");
                    String[] join=joinEndTime.getText().toString().split(" ");
                    map.put("join_time",join[0].replaceAll("/","-")+" "+join[2]+":00");
                    map.put("location",location_edit.getText().toString());
                    map.put("free",checkbox_free.isChecked()?1:0);
                    Editable price=price_edit.getText();
                    map.put("price",price.equals("")?"free":"none");
                    map.put("contents",theme_desc_edit.getText().toString());
                    map.put("ap_allowed", "0");
                    progressDlg=ProgressDialog.show(getActivity(), "发布约拍", "正在创建约拍", true, true, new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialogInterface) {
                            //上传完图片后取消了约拍
                        }
                    });
                   // mProgress.setVisibility(View.GONE);
                    requestFragment.httpRequest(map, CommonUrl.createYuePaiInfo);//最后将图片在这里传出去
                    break;
                case UPLOAD_TAKE_PICTURE://响应第一次msg，发送第二次msg：在本地把图片封装保存，发送图片
                    Map<String, String> map2=(HashMap<String, String>)msg.obj;
                    picToAdd=uploadImgUrlList.size();
                    if(uploadImgUrlList.size()>0){
                        for(int i=0;picToAdd>0;picToAdd--,i++){
                            saveThemeImgNew(newThemeId,uploadImgUrlList.get(picToAdd-1),map2.get("auth_key"),i);//逐张保存要上传的图片并发消息到发送的handle
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
                    String photo_local_file_path=getPath_above19(getActivity().getApplicationContext(), uri);
                    //程序员啊不要见得风就是雨，要有自己的判断，用户选出来的文件要判断它是不是真的图片
                    //如果不是，这个错的东西你再帮他传一遍，等于你也有责任吧
                    if (!(photo_local_file_path.toString().toLowerCase().endsWith("jpg")||photo_local_file_path.toString().toLowerCase().endsWith("png")
                            ||photo_local_file_path.toString().toLowerCase().endsWith("jpeg")||photo_local_file_path.toString().toLowerCase().endsWith("gif"))){
                        CommonUtils.getUtilInstance().showToast(getActivity(),"不支持此格式的上传");
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
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View root=inflater.inflate(R.layout.fragment_create_yuepai_a, container,false);
        requestFragment=new NetRequest(this,getActivity());
        mTagContainerLayout = (TagContainerLayout) root.findViewById(R.id.tag_layout_yuepai);
        List<String> list1 = new ArrayList<String>();
        list1.add("Java");list1.add("C++");
        list1.add("C#");list1.add("PHP");
        mTagContainerLayout.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(final int position, String text) {
                AlertDialog dialog = new AlertDialog.Builder(getActivity())
                        .setTitle("移除标签")
                        .setMessage("要移除这个标签吗")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mTagContainerLayout.removeTag(position);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create();
                dialog.show();
            }

            @Override
            public void onTagLongClick(int position, String text) {
            }
        });
        mTagContainerLayout.setTags(list1);

        Button btnAddTag = (Button) root.findViewById(R.id.btn_tag_add);
        btnAddTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TagAddActivity.class);
                intent.putExtra("type",1);
                startActivity(intent);
            }
        });

        edit_photo_fullscreen_layout=(FrameLayout)root.findViewById(R.id.edit_photo_fullscreen_layout);
        edit_photo_fullscreen_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideBigPhotoLayout();
            }
        });
        edit_photo_outer_layout=(RelativeLayout)root.findViewById(R.id.edit_photo_outer_layout);
        TextView cancelEditPhoto=(TextView)edit_photo_outer_layout.findViewById(R.id.cancel_upload);
        cancelEditPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideBigPhotoLayout();
            }
        });
        //uploading_photo_progress=(RelativeLayout)root.findViewById(R.id.uploading_photo_progress);
        display_big_image_layout=(RelativeLayout)root.findViewById(R.id.display_big_image_layout);
        show_upload_pic_layout=(RelativeLayout)root.findViewById(R.id.show_upload_pic_layout);
        take_picture=(TextView)root.findViewById(R.id.take_picture);
        price_edit=(EditText)root.findViewById(R.id.theme_price_edit);
        checkbox_free=(CheckBox)root.findViewById(R.id.checkbox_free);
        checkbox_free.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                     price_edit.setVisibility(View.GONE);
                }else {
                    price_edit.setVisibility(View.VISIBLE);
                    //price_edit.setEnabled(false);
                    //price_edit.setEditableFactory();
                }
            }
        });
        startTime=(TextView)root.findViewById(R.id.text_start_time);
        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timeFlag) {
                    new SlideDateTimePicker.Builder(getActivity().getSupportFragmentManager())
                            .setListener(startlistener)
                            .setInitialDate(new Date())
                                    //.setMinDate(new Date())
                                    //.setMaxDate(enddate)
                            .setIs24HourTime(true)
                            .setTheme(SlideDateTimePicker.HOLO_DARK)
                                    //.setIndicatorColor(Color.parseColor("E6BF66"))
                            .build()
                            .show();
                    return;
                }
                new SlideDateTimePicker.Builder(getActivity().getSupportFragmentManager())
                        .setListener(startlistener)
                        .setInitialDate(new Date())
                                //.setMinDate(new Date())
                        .setIs24HourTime(true)
                        .setTheme(SlideDateTimePicker.HOLO_DARK)
                                //.setIndicatorColor(Color.parseColor("E6BF66"))
                        .build()
                        .show();
            }
        });
        endTime=(TextView)root.findViewById(R.id.text_end_time);
        endTime.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (!timeFlag)
                {
                    Toast.makeText(getContext(), "请先选择开始时间", Toast.LENGTH_SHORT).show();
                    return;
                }
                new SlideDateTimePicker.Builder(getActivity().getSupportFragmentManager())
                        .setListener(endlistener)
                        .setInitialDate(startdate)
                        .setMinDate(startdate)
                        .build()
                        .show();
            }
        });
        joinEndTime=(TextView)root.findViewById(R.id.text_join_time);
        joinEndTime.setOnClickListener(new View.OnClickListener() {
          @Override
            public void onClick(View view) {
              if (!timeFlag)
              {
                  Toast.makeText(getContext(), "请先选择开始时间!", Toast.LENGTH_SHORT).show();
                  return;
              }

                new SlideDateTimePicker.Builder(getActivity().getSupportFragmentManager())
                        .setListener(joinlistener)
                        .setInitialDate(startdate)
                        .setMinDate(startdate)
                        .build()
                        .show();
            }
        });
        position_in_total=(TextView)root.findViewById(R.id.position_in_total);
        select_local_picture=(TextView)root.findViewById(R.id.select_local_picture);
        upload=(TextView)root.findViewById(R.id.upload);
        upload.setVisibility(View.VISIBLE);
        theme_title_edit=(EditText)root.findViewById(R.id.theme_title_edit);
        theme_desc_edit=(EditText)root.findViewById(R.id.theme_desc_edit);
        delete_image=(ImageView)root.findViewById(R.id.delete_image);
        add_image_gridview=(ImgAddGridView)root.findViewById(R.id.add_image_gridview);
        add_image_gridview.setExpanded(true);
        image_viewpager=(UploadViewPager)root.findViewById(R.id.image_viewpager);
        theme_listview=(ListView)root.findViewById(R.id.theme_listview);
        upload.setOnClickListener(this);
        delete_image.setOnClickListener(this);
        location_edit=(EditText)root.findViewById(R.id.theme_location_edit);
        ImageDisplayFragment.showNetImg=false;
        addPictureList.add(getResources().getDrawable(R.mipmap.theme_add_picture_icon));
        imageAddGridViewAdapter=new ImageAddGridViewAdapter(getActivity(), addPictureList);
        add_image_gridview.setAdapter(imageAddGridViewAdapter);
        //mProgress = (ProgressBar) root.findViewById(R.id.uploading_photo_progress);
        add_image_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                //这里是添加图片的按钮的回调
                if (position == 0) {
                    if (addPicCount == 10) {
                        CommonUtils.getUtilInstance().showToast(getActivity(), getString(R.string.no_more_than_9));
                    } else {
                        //点击添加图片
                        edit_photo_fullscreen_layout.setVisibility(View.VISIBLE);
                        get_photo_layout_in_from_down = AnimationUtils.loadAnimation(getActivity(), R.anim.search_layout_in_from_down);
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


        return root;

    }
    //加监听，等到view完全显示了再去做调整
    public void onViewCreated(final View view, Bundle saved) {
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
                big_picLayoutParam.bottomMargin=upload.getHeight();
                display_big_image_layout.setLayoutParams(big_picLayoutParam);
            }
        });
    }

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
        imagePagerAdapter=new ImagePagerAdapter(getActivity().getSupportFragmentManager(),urlList);
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
                ACache cache= ACache.get(getActivity());
                LoginDataModel userStr=(LoginDataModel)cache.getAsObject("loginModel");
                user=userStr.getUserModel();
                String usename=user.getPhone();
                String authKey=user.getAuth_key();
                if(authKey!=null&&authKey.equals("")){
                    CommonUtils.getUtilInstance().showToast(getActivity(),getString(R.string.publish_theme_after_login));
                    return;
                }

                String title=theme_title_edit.getText().toString();
                if(title.equals("")){
                    CommonUtils.getUtilInstance().showToast(getActivity(),getString(R.string.input_theme_comment_title));
                    return;
                }
                if(theme_desc_edit.getText().toString().length()==0){
                    CommonUtils.getUtilInstance().showToast(getActivity(),getString(R.string.input_theme_comment_desc));
                }
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

    //上传图片(每一张调用一次这个函数)
    public void saveThemeImgNew(final String themeId,final String picUrl,String tk,int num){
        UploadManager uploadmgr=new UploadManager();
        File data=new File(picUrl);
        String key=imgList.get(num);
        String token=tk;
        //mProgress.setVisibility(View.VISIBLE);
        progressDlg=ProgressDialog.show(getActivity(), "发布约拍", "正在上传图片", true, true, new DialogInterface.OnCancelListener() {
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
                new Thread(){
                    public void run(){
                        Map<String, Object>map=new HashMap<>();
                        /* map.put("themeId",themeId);
                        map.put("imgBody",UploadPhotoUtil.getInstance().getUploadBitmapZoomString(picUrl));
                        map.put("imgType",UploadPhotoUtil.getInstance().getFileType(picUrl));
                        map.put("type",1);*/
                        if (picToAdd==0){
                        Message msg=handler.obtainMessage();
                        msg.obj=map;
                        msg.what=SAVE_THEME_IMAGE;
                        handler.sendMessage(msg);
                        }//要上传的图片包装在msg后变成了消息发到handler
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
    }


    public void saveThemeInfo(String usrname,String auth_key,String title){//发第一次请求，仅请求约拍立项
        Map<String, Object>map=new HashMap<String, Object>();
        List<String> list=new ArrayList<>();
        imgList=new ArrayList<>();
        for (int i=0;i<uploadImgUrlList.size();i++){
            String[] ext=uploadImgUrlList.get(i).split("\\.");
            String extention="."+ext[ext.length-1];
            String filename=user.getPhone()+"/"+uploadImgUrlList.get(i).hashCode()+extention;
            imgList.add(filename);
            list.add(String.valueOf("\""+filename+"\""));
        }
        map.put("phone",usrname);
        map.put("auth_key",auth_key);
        map.put("title",title);
        map.put("type",YUEPAI_TYPE==1?StatusCode.REQUEST_CREATE_YUEPAIA:StatusCode.REQUEST_CREATE_YUEPAIB);
        map.put("imgs", list);
        requestFragment.httpRequest(map, CommonUrl.createYuePaiInfo);
    }

    //
    @Override
    public void requestFinish(final String result,String requestUrl) throws JSONException {

        if(requestUrl.equals(CommonUrl.createYuePaiInfo)){//约拍立项（第一次请求）完成的回调
            JSONObject object = new JSONObject(result);
            int code = Integer.valueOf(object.getString("code"));
            if (code == StatusCode.REQUEST_YUEPAI_SUCCESS) {
            final JSONObject content=object.getJSONObject("contents");
                        try {
                            apId = content.getInt("apId");
                            JSONArray auth_key_arr = content.getJSONArray("auth_key");
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
                Looper.prepare();CommonUtils.getUtilInstance().showToast(getActivity(), getString(R.string.publish_yuepai_sucess));Looper.loop();
                return;
        }
        if (code==StatusCode.REQUEST_YUEPAI_SUCCEED){
                progressDlg.dismiss();
                //Looper.prepare();CommonUtils.getUtilInstance().showToast(getActivity(), getString(R.string.publish_yuepai_succeed));Looper.loop();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("result", "发布约拍成功");
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                getActivity().finish();
                return;
        }else {
            progressDlg.dismiss();
            Looper.prepare();CommonUtils.getUtilInstance().showToast(getActivity(),object.getString("contents"));Looper.loop();
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
    public void exception(IOException e,String requestUrl){
        Log.d("logout", "--------------------------" + e.getMessage());
    }

    public void hideBigPhotoLayout(){
        display_big_image_layout.setVisibility(View.GONE);
        edit_photo_fullscreen_layout.setVisibility(View.GONE);
    }

    public void setYUEPAI_TYPE(int YUEPAI_TYPE) {
        this.YUEPAI_TYPE = YUEPAI_TYPE;
    }

    public RelativeLayout getEdit_big_photo_layout(){
        return display_big_image_layout;
    }

    public FrameLayout getdisplay_big_img(){
        return edit_photo_fullscreen_layout;
    }

    public void addTag(String tag) {
        if (!tag.equals(""))
        mTagContainerLayout.addTag(tag);
    }
}