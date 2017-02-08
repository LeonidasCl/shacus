package com.example.pc.shacus.Activity;


/**
 * Created by licl on 2017/2/4.
 * 个人照片详情页可用操作：
 * - 向个人照片添加图片：打开新activity添加图片后回到这个activity（相当于重新加载activity，要向服务器提交新上传的图片并下载新列表）
 * - 删除个人照片的图片：点击编辑后可以从现有列表中删除，再点击由编辑按钮变换而成的完成按钮可以重新加载（只需要向服务器提交新列表，不用下载新列表）
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class PhotoselfDetailActivity extends AppCompatActivity implements NetworkCallbackInterface.NetRequestIterface,NetworkCallbackInterface.OnSingleTapDismissBigPhotoListener{

    private TextView back,title,edit;
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

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photoself_detail);
        request=new NetRequest(this,this);
        back=(TextView) findViewById(R.id.photoset_toolbar_back);
        back.setText("＜返回");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        title=(TextView)findViewById(R.id.photoset_toolbar_title);
        title.setText("个人照片");
        image_viewpager=(UploadViewPager)findViewById(R.id.photoset_detail_viewpager);
        edit=(TextView)findViewById(R.id.photoset_toolbar_edit);
        position_in_total=(TextView)findViewById(R.id.photoset_position_total);
        display_big_image_layout=(RelativeLayout)findViewById(R.id.display_photoset_image);

        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                if (msg.what==StatusCode.PHOTOSELF_SMALLIMG){

                    if (true){//如果是自己的作品集
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
                                    //发起更新图片请求完成删除
                                    Map map=new HashMap();
                                    map.put("authKey",userModel.getAuth_key());
                                    map.put("uid",userModel.getId());
                                    map.put("type",StatusCode.UPDATE_DELETE_IMG);
                                    request.httpRequest(map,CommonUrl.imgSelfAndSets);

                                }
                            }
                        });

                        button_delete=(Button)findViewById(R.id.photoset_delete);
                        button_delete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v){
                                boolean hasChoosed=false;
                                for (int index=0;index<imageDatas.size();index++){
                                    if (imageDatas.get(index).isChecked()){
                                        hasChoosed=true;
                                        imageDatas.remove(index);
                                        imageBigDatas.remove(index);//对应的大图url也一并删除
                                    }
                                }
                                if (!hasChoosed){
                                    CommonUtils.getUtilInstance().showToast(PhotoselfDetailActivity.this,"您没有选择任何图片");
                                    return;
                                }
                                //把新清单数据注入adapter
                                fluidGridAdapter.refresh(imageDatas);
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
                                //TODO 添加新图片
                                Intent intent=new Intent(getApplicationContext(),PhotosAddActivity.class);
                                intent.putExtra("type",3);
                                startActivity(intent);
                            }
                        });

                    }else{//不是自己的作品集，不能编辑
                        edit.setVisibility(View.INVISIBLE);
                        edit.setClickable(false);
                    }
                    //将略缩图取回并存储
                    imageDatas=new ArrayList<>();
                    ArrayList<String> obj= (ArrayList<String>) msg.obj;
                    for (int i=0;i<obj.size();i++){
                        imageDatas.add(new ImageData(obj.get(i),200,200));
                    }
                    //修复略缩图最后一张不显示bug（暂时这样在最后加一张无用图）
                    imageDatas.add(new ImageData("http://obdvl7z18.bkt.clouddn.com/gh-pages/img/20160804/p11.jpg", 435, 145));
                    //加载图片列表视图
                    setupFluidGrid();
                    return;
                }

                if (msg.what==StatusCode.PHOTOSELF_BIGIMG){
                    //将大图列表取回并存储
                    imageBigDatas =(ArrayList<String>) msg.obj;
                    return;
                }

                if (msg.what== StatusCode.UPDATE_DELETE_IMG){
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
        String uid=userModel.getId();
        Map map=new HashMap();
        map.put("authkey", authKey);
        map.put("uid",uid);
        map.put("type", StatusCode.PHOTOSELF_SMALLIMG);
        //获取略缩图列表
        request.httpRequest(map, CommonUrl.imgSelfAndSets);
        map.remove("type");
        map.put("type",StatusCode.PHOTOSELF_BIGIMG);
        //获取大图列表
        request.httpRequest(map, CommonUrl.imgSelfAndSets);

    }

    private void setupFluidGrid(){
        //final ArrayList<ImageData> imageDatas = loadDevicePhotos();
        fluidGridAdapter = new FluidGridAdapter(this, imageDatas){
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
                //Picasso.with(PhotosetDetailActivity.this).load(new File(photoUrl)).resize(cellWidth, cellHeight).into(imageHolder);
                Glide.with(PhotoselfDetailActivity.this).load(photoUrl).override(cellWidth,cellHeight).into(imageHolder);
            }
        };
        ListView listview = (ListView)findViewById(R.id.fluid_list);
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

    protected ArrayList<ImageData> loadDevicePhotos() {
        //测试数据
        imageDatas = new ArrayList<>();
        imageDatas.add(new ImageData("http://obdvl7z18.bkt.clouddn.com/faguangyepao.jpg", 800, 453));
        imageDatas.add(new ImageData("http://obdvl7z18.bkt.clouddn.com/image/excited/04.jpeg", 600, 1068));
        imageDatas.add(new ImageData("http://obdvl7z18.bkt.clouddn.com/gh-pages/img/20160804/p1.jpg", 999, 412));
        imageDatas.add(new ImageData("http://obdvl7z18.bkt.clouddn.com/gh-pages/img/20160804/p2.jpg", 501, 105));
        imageDatas.add(new ImageData("http://obdvl7z18.bkt.clouddn.com/image/excited/02.jpg", 480, 854));
        imageDatas.add(new ImageData("http://obdvl7z18.bkt.clouddn.com/gh-pages/img/20160804/p3.jpg", 541, 172));
        imageDatas.add(new ImageData("http://obdvl7z18.bkt.clouddn.com/gh-pages/img/20160804/p4.jpg", 431, 110));
        imageDatas.add(new ImageData("http://obdvl7z18.bkt.clouddn.com/image/excited/05.png", 600, 458));
        imageDatas.add(new ImageData("http://obdvl7z18.bkt.clouddn.com/image/excited/06.jpg",771, 561));
        imageDatas.add(new ImageData("http://obdvl7z18.bkt.clouddn.com/gh-pages/img/20160804/p5.jpg", 437, 123));
        imageDatas.add(new ImageData("http://obdvl7z18.bkt.clouddn.com/gh-pages/img/20160804/p6.jpg", 301, 266));
        imageDatas.add(new ImageData("http://obdvl7z18.bkt.clouddn.com/image/excited/01.jpg", 284, 325));
        imageDatas.add(new ImageData("http://obdvl7z18.bkt.clouddn.com/image/excited/03.jpg", 198, 191));
        imageDatas.add(new ImageData("http://obdvl7z18.bkt.clouddn.com/gh-pages/img/20160804/p7.jpg", 458, 418));
        imageDatas.add(new ImageData("http://obdvl7z18.bkt.clouddn.com/image/excited/07.jpg", 160,220));
        imageDatas.add(new ImageData("http://obdvl7z18.bkt.clouddn.com/image/excited/08.jpg", 480, 853));
        imageDatas.add(new ImageData("http://obdvl7z18.bkt.clouddn.com/gh-pages/img/20160804/p11.jpg", 435, 145));
        //修复最后一张不显示bug
        imageDatas.add(new ImageData("http://obdvl7z18.bkt.clouddn.com/gh-pages/img/20160804/p11.jpg", 435, 145));

        return imageDatas;
    }

    private void showImagePager(String startPositionUrl) {
        int position=-1;
        for (int index=0;index<imageDatas.size()-1;index++){
            if (imageDatas.get(index).getImageUrl().equals(startPositionUrl)){
                position=index;
                break;
            }
        }
        List<String> list=new ArrayList<>();
        final int size=imageDatas.size()-1;//在这里进行修复
        for (int i=0;i<size;i++){
            list.add(imageDatas.get(i).getImageUrl());
        }
        imagePagerAdapter=new ImagePagerAdapter(this.getSupportFragmentManager(),list);
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
        display_big_image_layout.setVisibility(View.GONE);
    }

    @Override
    public void requestFinish(String result, String requestUrl) throws JSONException {
        if (requestUrl.equals(CommonUrl.imgSelfAndSets)){
            JSONObject object = new JSONObject(result);
            int code = Integer.valueOf(object.getString("code"));
            if (code==StatusCode.PHOTOSELF_SMALLIMG){
                JSONArray smallImgs=object.getJSONArray("contents");
                ArrayList<String> data=new ArrayList<>();
                for (int i=0;i<smallImgs.length();i++){
                    data.add(smallImgs.get(i).toString());
                }
                Message msg=handler.obtainMessage();
                msg.what= StatusCode.PHOTOSELF_SMALLIMG;
                msg.obj=data;
                handler.sendMessage(msg);
                return;
            }
            if (code==StatusCode.PHOTOSELF_BIGIMG){
                JSONArray bigImgs=object.getJSONArray("contents");
                ArrayList<String> data=new ArrayList<>();
                for (int i=0;i<bigImgs.length();i++){
                    data.add(bigImgs.get(i).toString());
                }
                Message msg=handler.obtainMessage();
                msg.what= StatusCode.PHOTOSELF_BIGIMG;
                msg.obj=data;
                handler.sendMessage(msg);
                return;
            }
            if (code==StatusCode.UPDATE_DELETE_IMG){
                Message msg=handler.obtainMessage();
                msg.what= StatusCode.UPDATE_DELETE_IMG;
                msg.obj="已成功删除";
                handler.sendMessage(msg);
                return;
            }
        }
    }

    @Override
    public void exception(IOException e, String requestUrl) {

    }

}