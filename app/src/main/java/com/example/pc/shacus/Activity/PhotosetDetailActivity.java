package com.example.pc.shacus.Activity;

/**
 * Created by licl on 2017/2/4.
 * 作品集详情页可用操作：
 * - 向作品集添加图片：打开新activity添加图片后回到这个activity（相当于重新加载activity，要向服务器提交新上传的图片并下载新列表）
 * - 删除作品集的图片：点击编辑后可以从现有列表中删除，再点击由编辑按钮变换而成的完成按钮可以重新加载（只需要向服务器提交新列表，不用下载新列表）
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
import com.example.pc.shacus.Adapter.JoinUserGridAdapter;
import com.example.pc.shacus.Adapter.PhotoViewAttacher;
import com.example.pc.shacus.Adapter.UploadViewPager;
import com.example.pc.shacus.Data.Cache.ACache;
import com.example.pc.shacus.Data.Model.ImageData;
import com.example.pc.shacus.Data.Model.LoginDataModel;
import com.example.pc.shacus.Data.Model.PhotosetDetailModel;
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
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class PhotosetDetailActivity extends AppCompatActivity implements NetworkCallbackInterface.NetRequestIterface,NetworkCallbackInterface.OnSingleTapDismissBigPhotoListener{

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
    private ArrayList<String> imgToDelete=new ArrayList<>();
    private Handler handler;
    private NetRequest request;
    private UserModel userModel;
    private ArrayList<String> imageBigDatas;
    private int isself=1;
    private PhotosetDetailModel detailData;
    private int ucid=-1;
    private TextView describtion;
    private TextView time;

    private HorizontalScrollView photoset_like_user_scroll;
    private GridView photoset_grid_join_user_scroll;
    private TextView btn_photoset_likecount;
    private ImageButton btn_photoset_addlike;

    private ImageView theme_add_picture_icon;

    private boolean isBigImageShowing=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos_detail);
        request=new NetRequest(this,this);
        back=(ImageButton) findViewById(R.id.photoset_toolbar_back);
//        back.setText("＜返回");
        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        theme_add_picture_icon=(ImageView)findViewById(R.id.btn_add_photo);

        title=(TextView)findViewById(R.id.photoset_toolbar_title);
        title.setText("作品集标题");
        image_viewpager=(UploadViewPager)findViewById(R.id.photoset_detail_viewpager);
        edit=(TextView)findViewById(R.id.photoset_toolbar_edit);
        position_in_total=(TextView)findViewById(R.id.photoset_position_total);
        display_big_image_layout=(RelativeLayout)findViewById(R.id.display_photoset_image);
        describtion=(TextView)findViewById(R.id.photoset_detail_describtion);
        time=(TextView)findViewById(R.id.photoset_detail_time);

        photoset_like_user_scroll=(HorizontalScrollView)findViewById(R.id.photoset_like_user_scroll);
        photoset_like_user_scroll.setHorizontalScrollBarEnabled(false);// 隐藏滚动条
        photoset_grid_join_user_scroll=(GridView)findViewById(R.id.photoset_grid_join_user_scroll);
        btn_photoset_likecount=(TextView)findViewById(R.id.btn_photoset_likecount);
        btn_photoset_addlike=(ImageButton)findViewById(R.id.btn_photoset_addlike);

        handler=new Handler(){
            @Override
            public void handleMessage(Message msg){
                super.handleMessage(msg);

                if (msg.what== StatusCode.REQUEST_FAILURE||msg.what== StatusCode.UPDATE_DELETE_PHOTOSET){
                    CommonUtils.getUtilInstance().showToast(PhotosetDetailActivity.this,msg.obj.toString());
                    return;
                }

                if (msg.what== StatusCode.PHOTOSET_SMALLIMG){
                    title.setText(detailData.getTitle());
                    describtion.setText(detailData.getUCcontent());
                    time.setText(detailData.getUCcreateT());

                    if (isself==0){//如果是自己的作品集
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
                                    get_photo_layout_in_from_down = AnimationUtils.loadAnimation(PhotosetDetailActivity.this, R.anim.search_layout_in_from_down);
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
                                    if (imgToDelete.size()==0){
                                        CommonUtils.getUtilInstance().showToast(PhotosetDetailActivity.this,"您没有作出任何更改");
                                        return;
                                    }
                                    if (imageDatas.size()==0){
                                        AlertDialog dialog = new AlertDialog.Builder(PhotosetDetailActivity.this)
                                                .setTitle("警告")
                                                .setMessage("您清空了这个作品集！这个作品集将被删除，确定吗？")
                                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        //发起更新图片请求完成删除
                                                        Map map=new HashMap();
                                                        map.put("authkey",userModel.getAuth_key());
                                                        map.put("uid",userModel.getId());
                                                        map.put("type",StatusCode.UPDATE_DELETE_PHOTOSET);
                                                        map.put("ucid",ucid);
                                                        map.put("imgs",imgToDelete);
                                                        request.httpRequest(map,CommonUrl.imgSelfAndSets);
                                                        finish();// TODO 可能出现finish后因没有对象来处理网络请求返回导致app crash
                                                    }
                                                })
                                                .setNegativeButton("放弃更改", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        finish();
                                                    }
                                                })
                                                .create();
                                        dialog.show();
                                        return;
                                    }
                                    //发起更新图片请求完成删除
                                    Map map=new HashMap();
                                    map.put("authkey",userModel.getAuth_key());
                                    map.put("uid",userModel.getId());
                                    map.put("type",StatusCode.UPDATE_DELETE_SETIMG);
                                    map.put("ucid",ucid);
                                    map.put("imgs",imgToDelete);
                                    request.httpRequest(map, CommonUrl.imgSelfAndSets);
                                }
                            }
                        });

                        button_delete=(Button)findViewById(R.id.photoset_delete);
                        button_delete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v){
                                boolean hasChoosed=false;
                                ArrayList<ImageData> imgDatasToRemove=new ArrayList<ImageData>();
                                ArrayList<String> imgBigDatasToRemove=new ArrayList<String>();
                                //先找出本次要删除的URL加进准备发给服务器的待删数组，并给临时数组添加元素以准备处理本地数据，一组单循环
                                for (int index=0;index<imageDatas.size();index++){
                                    if (imageDatas.get(index).isChecked()){
                                        hasChoosed=true;
                                        imgToDelete.add(String.valueOf("\""+imageBigDatas.get(index)+"\""));
                                        imgDatasToRemove.add(imageDatas.get(index));
                                        imgBigDatasToRemove.add(imageBigDatas.get(index));
                                    }
                                }
                                //再处理本地数据：按临时数组搜索，删除大图小图成员的特定数据，两组双重循环
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
                                    CommonUtils.getUtilInstance().showToast(PhotosetDetailActivity.this,"您没有选择任何图片");
                                    return;
                                }
                                //把新清单数据注入adapter
                                fluidGridAdapter.refresh(imageDatas,null);
                                //清除图片完成后，把新注入的数据都设置为可选状态
                                fluidGridAdapter.setPhotosCheckable(true);
                                //通知adapter进行画面重绘
                                fluidGridAdapter.notifyDataSetChanged();
                                CommonUtils.getUtilInstance().showToast(PhotosetDetailActivity.this,"删除成功");
                            }
                        });

                        button_upload=(Button)findViewById(R.id.photoset_upload);
                        button_upload.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View v) {
                                if (imgToDelete.size()!=0){
                                    AlertDialog dialog = new AlertDialog.Builder(PhotosetDetailActivity.this)
                                            .setTitle("警告")
                                            .setMessage("检测到您删除了图片！添加新图片之前要保存更改吗？")
                                            .setPositiveButton("保存更改", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    //发起更新图片请求完成删除
                                                    Map map=new HashMap();
                                                    map.put("authkey",userModel.getAuth_key());
                                                    map.put("uid",userModel.getId());
                                                    map.put("type",StatusCode.UPDATE_DELETE_SETIMG);
                                                    map.put("ucid",ucid);
                                                    map.put("imgs",imgToDelete);
                                                    request.httpRequest(map,CommonUrl.imgSelfAndSets);
                                                    //给作品集添加新图片
                                                    Intent intent=new Intent(getApplicationContext(),PhotosAddActivity.class);
                                                    intent.putExtra("type",3);
                                                    intent.putExtra("content",detailData.getUCcontent());
                                                    intent.putExtra("title",detailData.getTitle());
                                                    intent.putExtra("ucid",detailData.getUCid());
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            })
                                            .setNegativeButton("放弃更改", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent intent=new Intent(getApplicationContext(),PhotosAddActivity.class);
                                                    intent.putExtra("type",3);
                                                    intent.putExtra("content",detailData.getUCcontent());
                                                    intent.putExtra("title",detailData.getTitle());
                                                    intent.putExtra("ucid",detailData.getUCid());
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            })
                                            .create();
                                    dialog.show();
                                }else {
                                // 添加新图片
                                Intent intent=new Intent(getApplicationContext(),PhotosAddActivity.class);
                                intent.putExtra("type",3);
                                intent.putExtra("content",detailData.getUCcontent());
                                intent.putExtra("title",detailData.getTitle());
                                intent.putExtra("ucid",detailData.getUCid());
                                startActivity(intent);
                                finish();
                                }
                            }
                        });

                        theme_add_picture_icon.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // 添加新图片
                                Intent intent=new Intent(getApplicationContext(),PhotosAddActivity.class);
                                intent.putExtra("type",3);
                                intent.putExtra("content",detailData.getUCcontent());
                                intent.putExtra("title",detailData.getTitle());
                                intent.putExtra("ucid",detailData.getUCid());
                                startActivity(intent);
                                finish();
                            }
                        });

                    }else{//不是自己的作品集，不能编辑
                        theme_add_picture_icon.setVisibility(View.GONE);
                        edit.setVisibility(View.INVISIBLE);
                        edit.setClickable(false);
                    }

                    //处理点赞逻辑
                    String isLiked=detailData.getUserIsLiked();
                    if (isLiked.equals("1")){
                        btn_photoset_addlike.setSelected(true);
                    }
                    else{
                        btn_photoset_addlike.setSelected(false);
                    }
                    btn_photoset_addlike.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v){
                            //发起点赞请求
                            NetRequest netRequest = new NetRequest(new NetworkCallbackInterface.NetRequestIterface() {
                                @Override
                                public void requestFinish(String result, String requestUrl) throws JSONException {
                                    if (requestUrl.equals(CommonUrl.praisePhotoset)) {
                                        JSONObject object = new JSONObject(result);
                                        int code = Integer.valueOf(object.getString("code"));
                                        if (code == StatusCode.PRAISE_PHOTOSET_SUCCESS) {
                                            detailData.setUserIsLiked("1");
                                            Message msg=handler.obtainMessage();
                                            msg.what= StatusCode.PRAISE_PHOTOSET_SUCCESS;
                                            detailData.setUserlikeNum(Integer.valueOf(detailData.getUserlikeNum()) + 1 + "");
                                            handler.sendMessage(msg);
                                            return;
                                        }
                                        if (code == StatusCode.CANCEL_PRAISE_PHOTOSET_SUCCESS){
                                            detailData.setUserIsLiked("0");
                                            List<UserModel> list=detailData.getUserlikeList();
                                            for (int i=0;i<list.size();i++){
                                                UserModel model=list.get(i);
                                                if (model.getId().equals(userModel.getId())){
                                                    list.remove(i);
                                                }
                                            }
                                            detailData.setUserlikeList(list);
                                            Message msg=handler.obtainMessage();
                                            msg.what= StatusCode.CANCEL_PRAISE_PHOTOSET_SUCCESS;
                                            detailData.setUserlikeNum(Integer.valueOf(detailData.getUserlikeNum()) - 1 + "");
                                            handler.sendMessage(msg);
                                            return;
                                        }
                                    }
                                }

                                @Override
                                public void exception(IOException e, String requestUrl) {}
                            }, PhotosetDetailActivity.this);
                            btn_photoset_addlike.setClickable(false);
                            Map map = new HashMap();
                            if (detailData.getUserIsLiked().equals("1"))
                                map.put("type", StatusCode.CANCEL_PRAISE_PHOTOSET);
                            else
                                map.put("type", StatusCode.PRAISE_PHOTOSET);
                            ACache cache = ACache.get(PhotosetDetailActivity.this);
                            LoginDataModel model = (LoginDataModel) cache.getAsObject("loginModel");
                            map.put("ucid", detailData.getUCid());
                            map.put("authkey", model.getUserModel().getAuth_key());
                            netRequest.httpRequest(map, CommonUrl.praisePhotoset);

                        }
                    });
                    //处理点赞人水平滚动条
                    List<UserModel> userlike=detailData.getUserlikeList();
                    JoinUserGridAdapter adapter = new JoinUserGridAdapter(PhotosetDetailActivity.this, userlike,true);
                    photoset_grid_join_user_scroll.setAdapter(adapter);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(adapter.getCount() * 130, LinearLayout.LayoutParams.WRAP_CONTENT);
                    photoset_grid_join_user_scroll.setLayoutParams(params);
                    photoset_grid_join_user_scroll.setColumnWidth(120);
                    photoset_grid_join_user_scroll.setStretchMode(GridView.NO_STRETCH);
                    int itemCount = adapter.getCount();
                    photoset_grid_join_user_scroll.setNumColumns(itemCount);
                    //处理点赞人数量
                    int likeCount=detailData.getUserlikeList().size();
                    String likeStr="共"+String.valueOf(likeCount)+"赞";
                    btn_photoset_likecount.setText(likeStr);

                    //加载图片列表视图
                    setupFluidGrid();
                    return;
                }
                if (msg.what== StatusCode.UPDATE_DELETE_SETIMG){
                    CommonUtils.getUtilInstance().showToast(APP.context, msg.obj.toString());
                    return;
                }
                if (msg.what== StatusCode.PRAISE_PHOTOSET_SUCCESS){
                    btn_photoset_addlike.setClickable(true);
                    List<UserModel> list=detailData.getUserlikeList();
                    boolean add=true;
                    for (int i=0;i<list.size();i++){
                        UserModel model=list.get(i);
                        if (model.getId().equals(userModel.getId())){
                            add=false;
                        }
                    }
                    if (add)
                        detailData.getUserlikeList().add(userModel);
                    JoinUserGridAdapter adapter = new JoinUserGridAdapter(PhotosetDetailActivity.this, detailData.getUserlikeList(),true);
                    photoset_grid_join_user_scroll.setAdapter(adapter);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(adapter.getCount() * 130, LinearLayout.LayoutParams.WRAP_CONTENT);
                    photoset_grid_join_user_scroll.setLayoutParams(params);
                    photoset_grid_join_user_scroll.setColumnWidth(120);
                    photoset_grid_join_user_scroll.setStretchMode(GridView.NO_STRETCH);
                    int itemCount = adapter.getCount();
                    photoset_grid_join_user_scroll.setNumColumns(itemCount);

                    btn_photoset_addlike.setSelected(true);
                    btn_photoset_likecount.setText(detailData.getUserlikeNum());
                }
                if (msg.what == StatusCode.CANCEL_PRAISE_PHOTOSET_SUCCESS){
                    btn_photoset_addlike.setClickable(true);
                    JoinUserGridAdapter adapter = new JoinUserGridAdapter(PhotosetDetailActivity.this, detailData.getUserlikeList(),true);
                    photoset_grid_join_user_scroll.setAdapter(adapter);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(adapter.getCount() * 130, LinearLayout.LayoutParams.WRAP_CONTENT);
                    photoset_grid_join_user_scroll.setLayoutParams(params);
                    photoset_grid_join_user_scroll.setColumnWidth(120);
                    photoset_grid_join_user_scroll.setStretchMode(GridView.NO_STRETCH);
                    int itemCount = adapter.getCount();
                    photoset_grid_join_user_scroll.setNumColumns(itemCount);

                    btn_photoset_addlike.setSelected(false);
                    btn_photoset_likecount.setText(detailData.getUserlikeNum());
                }
            }
        };

        //取出基本数据
        ACache cache=ACache.get(this);
        LoginDataModel loginModel=(LoginDataModel)cache.getAsObject("loginModel");
        userModel=loginModel.getUserModel();
        String authKey=userModel.getAuth_key();
        int uid=Integer.valueOf(getIntent().getStringExtra("uid"));
        ucid=Integer.valueOf(getIntent().getStringExtra("ucid"));
        Map map=new HashMap();
        map.put("authkey", authKey);
        map.put("uid",uid);
        map.put("ucid",ucid);
        map.put("type", StatusCode.PHOTOSET_SMALLIMG);
        //获取略缩图列表
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
                //Picasso.with(PhotosetDetailActivity.this).load(new File(photoUrl)).resize(cellWidth, cellHeight).into(imageHolder);
                Glide.with(PhotosetDetailActivity.this).load(photoUrl).override(cellWidth,cellHeight).into(imageHolder);
            }
        };
        ListView listview = (ListView)findViewById(R.id.fluid_list);
        fluidGridAdapter.setBuiltType(3);
        listview.setAdapter(fluidGridAdapter);
    }

    private void setSelectState(String tag, boolean state) {
        for (int index=0;index<imageDatas.size();index++){
            if (imageDatas.get(index).getImageUrl().equals(tag)){
                imageDatas.get(index).setChecked(state);
            }
        }
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

    private void showImagePager(String startPositionUrl) {
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
            if (code==StatusCode.PHOTOSET_SMALLIMG){
                JSONObject model=object.getJSONObject("contents");
                isself=object.getInt("isself");
                Gson gson=new Gson();
                imageBigDatas=new ArrayList<>();
                imageDatas=new ArrayList<>();
                detailData=gson.fromJson(model.toString(),PhotosetDetailModel.class);
                for (int i=0;i<detailData.getUCsimpleimg().size();i++){
                    imageDatas.add(detailData.getUCsimpleimg().get(i));
                    imageBigDatas.add(detailData.getUCimg().get(i));
                }
                Message msg=handler.obtainMessage();
                msg.what= StatusCode.PHOTOSET_SMALLIMG;
                //msg.obj=data;数据已经在上面循环中设置，不需要再随消息发送了
                handler.sendMessage(msg);
                return;
            }

            if (code==StatusCode.UPDATE_DELETE_SETIMG){
                imgToDelete.clear();
                Message msg=handler.obtainMessage();
                msg.what= StatusCode.UPDATE_DELETE_SETIMG;
                msg.obj="图片删除成功";
                handler.sendMessage(msg);
                return;
            }

            if (code==StatusCode.UPDATE_DELETE_PHOTOSET){
                Message msg=handler.obtainMessage();
                msg.what= StatusCode.UPDATE_DELETE_PHOTOSET;
                msg.obj="作品集已删除";
                handler.sendMessage(msg);
                return;
            }

            Message msg=handler.obtainMessage();
            msg.what= StatusCode.REQUEST_FAILURE;
            msg.obj="操作失败";
            handler.sendMessage(msg);
        }
    }

    @Override
    public void exception(IOException e, String requestUrl) {

    }

}