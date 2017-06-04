package com.example.pc.shacus.Activity;


/**
 * Created by licl on 2017/2/4.
 * 个人照片详情页可用操作：
 * - 向个人照片添加图片：打开新activity添加图片后回到这个activity（相当于重新加载activity，要向服务器提交新上传的图片并下载新列表）
 * - 删除个人照片的图片：点击编辑后可以从现有列表中删除，再点击由编辑按钮变换而成的完成按钮可以重新加载（只需要向服务器提交新列表，不用下载新列表）
 * 需要intent传入：所属的用户id
 */


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bumptech.glide.Glide;
import com.example.pc.shacus.APP;
import com.example.pc.shacus.Adapter.FluidGridAdapter;
import com.example.pc.shacus.Adapter.ImagePagerAdapter;
import com.example.pc.shacus.Adapter.PhotoViewAttacher;
import com.example.pc.shacus.Adapter.UploadViewPager;
import com.example.pc.shacus.Data.Cache.ACache;
import com.example.pc.shacus.Data.Model.ImageData;
import com.example.pc.shacus.Data.Model.LoginDataModel;
import com.example.pc.shacus.Data.Model.UserModel;
import com.example.pc.shacus.Network.NetRequest;
import com.example.pc.shacus.Network.NetworkCallbackInterface;
import com.example.pc.shacus.Network.StatusCode;
import com.example.pc.shacus.R;
import com.example.pc.shacus.Util.CommonUrl;
import com.example.pc.shacus.Util.CommonUtils;
import com.google.gson.Gson;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class PhotoselfDetailActivity extends AppCompatActivity implements NetworkCallbackInterface.NetRequestIterface,NetworkCallbackInterface.OnSingleTapDismissBigPhotoListener{

    private TextView title,edit;
    private ImageButton back;
    private boolean isEditing=false;
    FluidGridAdapter fluidGridAdapter;
    ArrayList<ImageData> imageDatas;
    FrameLayout bottomMenu;
    private Animation get_photo_layout_in_from_down;
    private Button button_delete;
    private Button button_upload;
    private ImagePagerAdapter imagePagerAdapter;
    private UploadViewPager image_viewpager;
    private TextView position_in_total;
    private RelativeLayout display_big_image_layout;
    private Handler handler;
    private NetRequest request;
    private UserModel userModel;
    private ArrayList<String> imageBigDatas;
    private ArrayList<String> imgToDelete=new ArrayList<>();
    private int isself=1;//是否为自己，默认不是自己
    private ImageView theme_add_picture_icon;
    private boolean isBigImageShowing=false;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photoself_detail);
        request=new NetRequest(this,this);
        back= (ImageButton) findViewById(R.id.photoset_toolbar_back);
//        back.setText("＜返回");
        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });

        title=(TextView)findViewById(R.id.photoset_toolbar_title);
        title.setText("个人照片");
        image_viewpager=(UploadViewPager)findViewById(R.id.photoset_detail_viewpager);
        edit=(TextView)findViewById(R.id.photoset_toolbar_edit);
        position_in_total=(TextView)findViewById(R.id.photoset_position_total);
        display_big_image_layout=(RelativeLayout)findViewById(R.id.display_photoset_image);
        theme_add_picture_icon=(ImageView)findViewById(R.id.btn_add_photo);


        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                if (msg.what== StatusCode.REQUEST_FAILURE){
                    CommonUtils.getUtilInstance().showToast(APP.context, msg.obj.toString());
                    finish();
                    return;
                }

                if (msg.what==StatusCode.PHOTOSELF_SMALLIMG){

                    if (isself==0){//如果是自己的照片集
                        bottomMenu=(FrameLayout)findViewById(R.id.photoset_bottom);
                        edit.setText("编辑  ");
                        edit.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View v){
                                if (!isEditing){//进入编辑模式
                                    edit.setText("完成  ");
                                    isEditing=true;
                                    //显示底部菜单
                                    bottomMenu.setVisibility(View.VISIBLE);
                                    get_photo_layout_in_from_down = AnimationUtils.loadAnimation(PhotoselfDetailActivity.this, R.anim.search_layout_in_from_down);
                                    bottomMenu.startAnimation(get_photo_layout_in_from_down);
                                    //显示勾选框
                                    fluidGridAdapter.setPhotosCheckable(true);
                                    fluidGridAdapter.notifyDataSetChanged();
                                }else {//退出编辑模式
                                    edit.setText("编辑  ");
                                    isEditing=false;
                                    //隐藏底部菜单
                                    bottomMenu.setVisibility(View.GONE);
                                    //隐藏勾选框
                                    fluidGridAdapter.setPhotosCheckable(false);
                                    fluidGridAdapter.notifyDataSetChanged();
                                    for (int index=0;index<imageDatas.size();index++){//还原选择状态
                                        imageDatas.get(index).setChecked(false);
                                    }
                                    if (imgToDelete.size()==0){//数组大小为1说明只有默认图片，不需要向服务器请求删除
                                        CommonUtils.getUtilInstance().showToast(PhotoselfDetailActivity.this,"您没有作出任何更改");
                                        return;
                                    }
                                    //发起更新图片请求完成删除
                                    Map map=new HashMap();
                                    map.put("authkey",userModel.getAuth_key());
                                    map.put("uid",userModel.getId());
                                    map.put("type",StatusCode.UPDATE_DELETE_SELFIMG);
                                    map.put("imgs",imgToDelete);
                                    request.httpRequest(map,CommonUrl.imgSelfAndSets);

                                }
                            }
                        });

                        button_delete=(Button)findViewById(R.id.photoset_delete);
                        button_delete.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View v){
                                boolean hasChoosed=false;
                                ArrayList<ImageData> imgDatasToRemove=new ArrayList<ImageData>();
                                ArrayList<String> imgBigDatasToRemove=new ArrayList<String>();
                                //先找出本次要删除的URL加进准备发给服务器的待删数组，并给临时数组添加元素以准备处理本地数据
                                for (int index=0;index<imageDatas.size();index++){
                                    if (imageDatas.get(index).isChecked()){
                                        hasChoosed=true;
                                        imgToDelete.add(String.valueOf("\""+imageBigDatas.get(index)+"\""));
                                        imgDatasToRemove.add(imageDatas.get(index));
                                        imgBigDatasToRemove.add(imageBigDatas.get(index));
                                    }
                                }
                                //再处理本地数据：按临时数组搜索，删除大图小图成员的特定数据
                                for (int i=0;i<imgDatasToRemove.size();i++){
                                    String str=imgDatasToRemove.get(i).getImageUrl();
                                    for (int j=0;j<imageDatas.size();j++){
                                        if (imageDatas.get(j).getImageUrl().equals(str)){
                                            imageDatas.remove(j);
                                            break;
                                        }
                                    }
                                }
                                for (int i=0;i<imgBigDatasToRemove.size();i++){
                                    String str=imgBigDatasToRemove.get(i);
                                    for (int j=0;j<imageBigDatas.size();j++){
                                        if (imageBigDatas.get(j).equals(str)){
                                            imageBigDatas.remove(j);
                                            break;
                                        }
                                    }
                                }
                                if (!hasChoosed){
                                    CommonUtils.getUtilInstance().showToast(PhotoselfDetailActivity.this,"您没有选择任何图片");
                                    return;
                                }
                                //把新清单数据注入adapter
                                fluidGridAdapter.refresh(imageDatas,null);
                                //清除图片完成后，把新注入的数据都设置为可选状态
                                fluidGridAdapter.setPhotosCheckable(true);
                                //通知adapter进行画面重绘
                                fluidGridAdapter.notifyDataSetChanged();
                                CommonUtils.getUtilInstance().showToast(PhotoselfDetailActivity.this,"删除成功");
                            }
                        });

                        button_upload=(Button)findViewById(R.id.photoset_upload);
                        button_upload.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View v) {
                                if (imgToDelete.size()!=0){
                                    AlertDialog dialog = new AlertDialog.Builder(PhotoselfDetailActivity.this)
                                            .setTitle("警告")
                                            .setMessage("检测到您删除了图片！添加新图片之前要保存更改吗？")
                                            .setPositiveButton("保存更改", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    //发起更新图片请求完成删除
                                                    Map map=new HashMap();
                                                    map.put("authkey",userModel.getAuth_key());
                                                    map.put("uid",userModel.getId());
                                                    map.put("type",StatusCode.UPDATE_DELETE_SELFIMG);
                                                    map.put("imgs",imgToDelete);
                                                    request.httpRequest(map,CommonUrl.imgSelfAndSets);
                                                    //给作品集添加新图片
                                                    Intent intent=new Intent(getApplicationContext(),PhotosAddActivity.class);
                                                    intent.putExtra("type",1);
                                                    startActivity(intent);
                                                    finish();// TODO 可能出现finish后因没有对象来处理网络请求返回导致app crash
                                                }
                                            })
                                            .setNegativeButton("放弃更改", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent intent=new Intent(getApplicationContext(),PhotosAddActivity.class);
                                                    intent.putExtra("type",1);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            })
                                            .create();
                                    dialog.show();
                                }else {
                                Intent intent=new Intent(getApplicationContext(),PhotosAddActivity.class);
                                intent.putExtra("type",1);
                                startActivity(intent);
                                finish();
                                }
                            }
                        });

                        theme_add_picture_icon.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent=new Intent(getApplicationContext(),PhotosAddActivity.class);
                                intent.putExtra("type",1);
                                startActivity(intent);
                                finish();
                            }
                        });

                    }else{//不是自己的作品集，不能编辑
                        theme_add_picture_icon.setVisibility(View.GONE);
                        edit.setVisibility(View.INVISIBLE);
                        edit.setClickable(false);
                    }
                    //修复略缩图最后一张不显示bug（暂时这样在最后加一张无用图）
                    //imageDatas.add(new ImageData("http://obdvl7z18.bkt.clouddn.com/gh-pages/img/20160804/p11.jpg", 435, 145));
                    //加载图片列表视图
                    setupFluidGrid();
                    return;
                }

                if (msg.what== StatusCode.UPDATE_DELETE_SELFIMG){
                    CommonUtils.getUtilInstance().showToast(APP.context, msg.obj.toString());
                    return;
                }

            }
        };

        //取出基本数据
        ACache cache=ACache.get(this);
        LoginDataModel loginModel=(LoginDataModel)cache.getAsObject("loginModel");
        userModel=loginModel.getUserModel();
        String authKey=userModel.getAuth_key();
        int uid=Integer.valueOf(getIntent().getStringExtra("uid"));
        Map map=new HashMap();
        map.put("authkey", authKey);
        map.put("uid",uid);
        map.put("type", StatusCode.PHOTOSELF_SMALLIMG);
        //获取个人照片的略缩图和大图列表
        request.httpRequest(map, CommonUrl.imgSelfAndSets);

    }

    private void setupFluidGrid(){
        //final ArrayList<ImageData> imageDatas = loadDevicePhotos();
        fluidGridAdapter = new FluidGridAdapter(this, null,imageDatas){
            @Override
            protected void onSingleCellTapped(ImageData imageData,View v){
                if (isEditing){//如果处于编辑模式
                    ImageView checkbox=(ImageView) v.findViewById(R.id.photo_check);
                    if (!imageData.isChecked()){//选择图片
                        checkbox.setImageResource(R.drawable.sel);
                        //同内部数据（已经存在于adapter中）绑定，备用
                        imageData.setChecked(true);
                        //同外部数据（本活动的成员，还未复制到adapter中）绑定，绑定外部数据才能实现刷新，实际使用
                        setSelectState(imageData.getImageUrl(),true);
                    }else {//取消选择
                        checkbox.setImageResource(R.drawable.def);
                        imageData.setChecked(false);
                        setSelectState(imageData.getImageUrl(),false);
                    }
                }else {//没有处于编辑模式
                    //打开看图模式
                    showImagePager(parseBigImgUrl(imageData.getImageUrl()));
                }

            }

            @Override
            protected void loadImageIntoView(String photoUrl, int cellWidth, int cellHeight, ImageView imageHolder){
                Glide.with(PhotoselfDetailActivity.this).load(photoUrl).override(cellWidth,cellHeight).into(imageHolder);
            }
        };
        ListView listview = (ListView)findViewById(R.id.fluid_list);
        fluidGridAdapter.setBuiltType(1);
        listview.setAdapter(fluidGridAdapter);
    }

    private String parseBigImgUrl(String imageUrl) {
        String ret="";
        for (int i=0;i<imageDatas.size();i++){
            if (imageDatas.get(i).getImageUrl().equals(imageUrl)){
                ret= imageBigDatas.get(i);
                break;
            }
        }
        return ret;
    }

    private void setSelectState(String tag, boolean state) {
        for (int index=0;index<imageDatas.size();index++){
            if (imageDatas.get(index).getImageUrl().equals(tag)){
                imageDatas.get(index).setChecked(state);
            }
        }
    }

    private void showImagePager(String startPositionUrl){
        isBigImageShowing=true;
        int position=-1;
        final int size=imageBigDatas.size();
        for (int index=0;index<size;index++){
            if (imageBigDatas.get(index).equals(startPositionUrl)){
                position=index;
                break;
            }
        }
        imagePagerAdapter=new ImagePagerAdapter(this.getSupportFragmentManager(),imageBigDatas);
        image_viewpager.setAdapter(imagePagerAdapter);
        display_big_image_layout.setVisibility(View.VISIBLE);
        imagePagerAdapter.notifyDataSetChanged();
        image_viewpager.setOffscreenPageLimit(imagePagerAdapter.getCount());
        image_viewpager.setCurrentItem(position,true);
        position_in_total.setText((position + 1) + "/" + size);
        image_viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                position_in_total.setText((position + 1) + "/" + size);
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
        PhotoViewAttacher.setOnSingleTapToPhotoViewListener(this);
    }

    @Override
    public void onDismissBigPhoto() {
        isBigImageShowing=false;
        display_big_image_layout.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        if (isBigImageShowing)
            onDismissBigPhoto();
        else
            finish();
    }

    @Override
    public void requestFinish(String result, String requestUrl) throws JSONException {
        if (requestUrl.equals(CommonUrl.imgSelfAndSets)){
            JSONObject object = new JSONObject(result);
            int code = Integer.valueOf(object.getString("code"));
            if (code==StatusCode.PHOTOSELF_SMALLIMG){
                JSONArray smallImgs=object.getJSONArray("contents");
                JSONArray bigImgs=object.getJSONArray("originurl");
                isself=object.getInt("isself");
                Gson gson=new Gson();
                imageBigDatas=new ArrayList<>();
                imageDatas=new ArrayList<>();
                for (int i=0;i<smallImgs.length();i++){
                    imageDatas.add(gson.fromJson(smallImgs.get(i).toString(),ImageData.class));
                    imageBigDatas.add(bigImgs.get(i).toString());
                }
                Message msg=handler.obtainMessage();
                msg.what= StatusCode.PHOTOSELF_SMALLIMG;
                //msg.obj=data; 数据已经在上面循环中设置，不需要再随消息发送了
                handler.sendMessage(msg);
                return;
            }
            if (code==StatusCode.UPDATE_DELETE_SELFIMG){
                imgToDelete.clear();
                Message msg=handler.obtainMessage();
                msg.what= StatusCode.UPDATE_DELETE_SELFIMG;
                msg.obj="已成功删除";
                handler.sendMessage(msg);
                return;
            }
            Message msg=handler.obtainMessage();
            msg.what= StatusCode.REQUEST_FAILURE;
            msg.obj=object.getString("contents");
            handler.sendMessage(msg);
        }
    }

    @Override
    public void exception(IOException e, String requestUrl) {

    }

}