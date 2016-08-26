package com.example.pc.shacus.Fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
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
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pc.shacus.APP;
import com.example.pc.shacus.Adapter.ImageAddGridViewAdapter;
import com.example.pc.shacus.Adapter.ImagePagerAdapter;
import com.example.pc.shacus.Adapter.PhotoViewAttacher;
import com.example.pc.shacus.Adapter.UploadViewPager;
import com.example.pc.shacus.Network.NetRequest;
import com.example.pc.shacus.Network.NetworkCallbackInterface;
import com.example.pc.shacus.R;
import com.example.pc.shacus.Util.CommonUrl;
import com.example.pc.shacus.Util.CommonUtils;
import com.example.pc.shacus.Util.UploadPhotoUtil;
import com.example.pc.shacus.Util.UserInfoUtil;
import com.example.pc.shacus.View.DateTimePicker.SlideDateTimeListener;
import com.example.pc.shacus.View.DateTimePicker.SlideDateTimePicker;
import com.example.pc.shacus.View.TagView.TagContainerLayout;
import com.example.pc.shacus.View.TagView.TagView;

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

/**
 * Created by pc on 2016/7/26.
 */
public class FragmentCreateYuePaiB extends Fragment implements View.OnClickListener, NetworkCallbackInterface.NetRequestIterface,
            NetworkCallbackInterface.OnSingleTapDismissBigPhotoListener{

    private final int UPLOAD_TAKE_PICTURE=5;

    private final int NONE=0,TAKE_PICTURE=1,LOCAL_PICTURE=2;

    private final int SAVE_THEME_IMAGE=8;
    private final int SHOW_TAKE_PICTURE=9;
    private final int SHOW_LOCAL_PICTURE=10;
    private FrameLayout edit_photo_fullscreen_layout;
    private RelativeLayout edit_photo_outer_layout,
            uploading_photo_progress,display_big_image_layout,show_upload_pic_layout;
    private Animation get_photo_layout_out_from_up,get_photo_layout_in_from_down;
    private TextView take_picture,select_local_picture,position_in_total,upload;
    private ImageView delete_image;
    private TextView startTime;
    private boolean timeFlag=false;
    private Date startdate=new Date();
    private Date enddate=new Date();
    private TextView endTime;
    private String takePictureUrl,newThemeId;
    private Intent intent;
    private ListView lv;
    private SearchView search;
    private NetRequest requestFragment;
    private SimpleDateFormat mFormatter = new SimpleDateFormat("yyyy/MM/dd E HH:mm");
    private GridView add_image_gridview;
    private int addPicCount=1,addTakePicCount=1,viewpagerPosition;
    private List<String> uploadImgUrlList=new ArrayList<>();
    private List<Drawable>addPictureList=new ArrayList<>();
    private List<String>pictureUrlList=new ArrayList<>();
    private ImageAddGridViewAdapter imageAddGridViewAdapter;
    private ImagePagerAdapter imagePagerAdapter;
    private UploadViewPager image_viewpager;
    private boolean isBigImageShow=false,isShowUploadPic=false,addPic=false,clearFormerUploadUrlList=true;
    private EditText theme_title_edit,theme_desc_edit;
    private ListView theme_listview;
    private TagContainerLayout mTagContainerLayout;
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

    private Handler handler=new Handler(){

    // 选完图片后就是在这里处理的
    @Override
    public void handleMessage(Message msg){
            switch(msg.what){

            case SAVE_THEME_IMAGE://响应第二次msg，发送第二次请求：上传图片
            Map<String, Object> map=(Map<String, Object>)msg.obj;
            requestFragment.httpRequest(map, CommonUrl.saveThemeImgNew);//最后将图片在这里传出去
            break;
            case UPLOAD_TAKE_PICTURE://响应第一次msg，发送第二次msg：在本地把图片封装保存，准备发送图片

            if(uploadImgUrlList.size()>0){
            for(int i=0;i<uploadImgUrlList.size();i++){
            saveThemeImgNew(newThemeId,uploadImgUrlList.get(i));//逐张保存要上传的图片并发消息到发送的handle
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
            addPic=true;
            if(clearFormerUploadUrlList){
            if(uploadImgUrlList.size()>0){
            uploadImgUrlList.clear();
            }
            clearFormerUploadUrlList=false;
            }//获取到资源位置
            Uri uri=intent.getData();
            String photo_local_file_path=getPath_above19(getActivity().getApplicationContext(),uri);
            Bitmap bitmap2=UploadPhotoUtil.getInstance()
            .trasformToZoomBitmapAndLessMemory(photo_local_file_path);
            addPictureList.add(new BitmapDrawable(getResources(),
            bitmap2));
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

    public FrameLayout getEdit_photo_fullscreen_layout(){
        return edit_photo_fullscreen_layout;
    }

@Override
public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    View root=inflater.inflate(R.layout.fragment_create_yuepai_b, container,false);
    requestFragment=new NetRequest(this,getActivity());
    mTagContainerLayout = (TagContainerLayout) root.findViewById(R.id.tag_layout_yuepai);
    List<String> list1 = new ArrayList<String>();
    list1.add("Java");list1.add("C++");list1.add("C#");list1.add("PHP");
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
    lv = (ListView) root.findViewById(R.id.search_list);
    String[] mStrings = {"ad","dffa","uyiu","rqer","qwgt","afrgb","rtyr"};
    search = (SearchView) root.findViewById(R.id.search_tag);
    search.setIconifiedByDefault(false);
    search.setSubmitButtonEnabled(false);
    search.setQueryHint("查找或输入标签");
    final ArrayAdapter adapter=new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1, mStrings);
    lv.setAdapter(adapter);
    lv.setTextFilterEnabled(true);
    //暂时用这种办法让listview不占用空间
    adapter.getFilter().filter("clear");
    search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
        // 用户输入字符时激发该方法
        @Override
        public boolean onQueryTextChange(String newText) {
            if (TextUtils.isEmpty(newText)) {
                lv.clearTextFilter();
            } else {
                adapter.getFilter().filter(newText);
                //lv.setFilterText(newText);
            }
            return true;
        }

        @Override
        public boolean onQueryTextSubmit(String query) {
            return true;
        }
    });
    /*search.setOnFocusChangeListener(new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            //if (!hasFocus)
                //暂时用这种办法让listview不占用空间
                adapter.getFilter().filter("clear");
        }
    });*/
    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String str=(String)lv.getItemAtPosition(position);
            search.setQuery(str,false);
            //暂时用这种办法让listview不占用空间
            adapter.getFilter().filter("clear");
        }
    });
    Button btnAddTag = (Button) root.findViewById(R.id.btn_tag_add);
    btnAddTag.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String txt=search.getQuery().toString();
            if (!txt.equals(""))
            mTagContainerLayout.addTag(txt);
        }
    });
    edit_photo_fullscreen_layout=(FrameLayout)root.findViewById(R.id.edit_photo_fullscreen_layout);
    edit_photo_fullscreen_layout.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            hideEditPhotoLayout();
        }
    });
    edit_photo_outer_layout=(RelativeLayout)root.findViewById(R.id.edit_photo_outer_layout);
    TextView cancelEditPhoto=(TextView)edit_photo_outer_layout.findViewById(R.id.cancel);
    cancelEditPhoto.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            hideEditPhotoLayout();
        }
    });
    uploading_photo_progress=(RelativeLayout)root.findViewById(R.id.uploading_photo_progress);
    display_big_image_layout=(RelativeLayout)root.findViewById(R.id.display_big_image_layout);
    show_upload_pic_layout=(RelativeLayout)root.findViewById(R.id.show_upload_pic_layout);
    take_picture=(TextView)root.findViewById(R.id.take_picture);
    startTime=(TextView)root.findViewById(R.id.text_start_time);
    startTime.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (timeFlag)
            {
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
    endTime.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!timeFlag)
            {
                Toast.makeText(getContext(),"请先选择开始时间", Toast.LENGTH_SHORT).show();
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
    position_in_total=(TextView)root.findViewById(R.id.position_in_total);
    select_local_picture=(TextView)root.findViewById(R.id.select_local_picture);
    upload=(TextView)root.findViewById(R.id.upload);
    upload.setVisibility(View.VISIBLE);
    theme_title_edit=(EditText)root.findViewById(R.id.theme_title_edit);
    theme_desc_edit=(EditText)root.findViewById(R.id.theme_desc_edit);
    delete_image=(ImageView)root.findViewById(R.id.delete_image);
    add_image_gridview=(GridView)root.findViewById(R.id.add_image_gridview);
    image_viewpager=(UploadViewPager)root.findViewById(R.id.image_viewpager);
    theme_listview=(ListView)root.findViewById(R.id.theme_listview);
    upload.setOnClickListener(this);
    delete_image.setOnClickListener(this);

    ImageDisplayFragment.showNetImg=false;
    addPictureList.add(getResources().getDrawable(R.mipmap.theme_add_picture_icon));
    imageAddGridViewAdapter=new ImageAddGridViewAdapter(getActivity(), addPictureList);
    add_image_gridview.setAdapter(imageAddGridViewAdapter);
    add_image_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            //这里是添加图片的按钮的回调
            if (position == 0) {
                if (addPicCount == 9) {
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
            image_viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener(){
@Override
public void onPageScrollStateChanged(int state){

            }

@Override
public void onPageSelected(int position){

            viewpagerPosition=position;
            if(flag.equals("net")){
            position_in_total.setText((position+1)+"/"+pictureUrlList.size());
            }else{
            position_in_total.setText((position+1)+"/"+localUrlList.size());
            }

            }

@Override
public void onPageScrolled(int arg0,float arg1,int arg2){

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
            super.onActivityResult(requestCode,resultCode,intent);
            if(resultCode==NONE)
            return;
            //为什么不在这处理图片呢？因为处理图片比较耗时，如果在这里处理图片，从图库或者拍照Activity时会不流畅，很卡卡卡，试试就知道了
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
            case R.id.upload://第一次握手：按发表键后
            //检查用户是否登录
            if(UserInfoUtil.getInstance().getAuthKey()==null){
            CommonUtils.getUtilInstance().showToast(getActivity(),getString(R.string.publish_theme_after_login));
            return;
            }
            if(theme_title_edit.getText().toString().length()==0){
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
            saveThemeInfo();//发第一次请求


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

    //保存要上传的图片(每一张调用一次这个函数)
public void saveThemeImgNew(final String themeId,final String picUrl){
            new Thread(){
    public void run(){
            Map<String, Object>map=new HashMap<>();
            map.put("themeId",themeId);
            map.put("imgBody",UploadPhotoUtil.getInstance().getUploadBitmapZoomString(picUrl));
            map.put("imgType",UploadPhotoUtil.getInstance().getFileType(picUrl));
            map.put("type",1);
            Message msg=handler.obtainMessage();
            msg.obj=map;
            msg.what=SAVE_THEME_IMAGE;
            handler.sendMessage(msg);//要上传的图片包装在msg后变成了消息发到handler
            }
            }.start();
            }


public void saveThemeInfo(){
            Map<String, Object>map=new HashMap<String, Object>();
            map.put("themeTitle",theme_title_edit.getText().toString());
            map.put("themeDescr",theme_desc_edit.getText().toString());
            requestFragment.httpRequest(map, CommonUrl.saveThemeInfo);
            }

//
@Override
public void requestFinish(final String result,String requestUrl){

            if(requestUrl.equals(CommonUrl.saveThemeImgNew)){//上传图片完成的回调
            try{
            JSONObject object=new JSONObject(result);

            edit_photo_fullscreen_layout.setVisibility(View.GONE);
            addPic=false;
                    getActivity().finish();
            }catch(JSONException e){
            e.printStackTrace();
            }
            }
            if(requestUrl.equals(CommonUrl.saveThemeInfo)){//发表主题完成的回调
                    getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                    try {
                                            JSONObject gamesInfoObject = new JSONObject(result);
                                            int errorCode = gamesInfoObject.getInt("errorCode");

                                            if (errorCode == 0) {
                                                    CommonUtils.getUtilInstance().showToast(getActivity(), getString(R.string.publish_theme_sucess));
                                                    newThemeId = gamesInfoObject.getString("themeId");
                               /* UserInfoUtil.getInstance().setThemeNum(
                                        UserInfoUtil.getInstance().getThemeNum() + 1);*/
                                                    handler.sendEmptyMessageDelayed(UPLOAD_TAKE_PICTURE, 100);
                                            } else {
                               /* CommonUtils.getUtilInstance().showToast(UploadPicActivity.this,
                                        getString(R.string.publish_theme_failure));*/
                                            }
                                    } catch (JSONException e) {

                                            e.printStackTrace();
                                    }
                            }
                    });
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
            Log.d("发生错误", "--------------------------" + e.getMessage());
            }

    public void hideEditPhotoLayout(){
        edit_photo_fullscreen_layout.setVisibility(View.GONE);
    }

}