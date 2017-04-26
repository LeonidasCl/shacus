package com.example.pc.shacus.Activity;

/**
 * Created by licl on 2017/2/4.
 * 作品集概览页可用操作：
 * - 向作品集添加图片：打开新activity添加图片后回到这个activity（相当于重新加载activity，要向服务器提交新上传的图片并下载新列表）
 * - 删除作品集的图片：点击编辑后可以从现有列表中删除，再点击由编辑按钮变换而成的完成按钮可以重新加载（只需要向服务器提交新列表，不用下载新列表）
 * 需要intent传入：所属的用户id
 */


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.bumptech.glide.Glide;
import com.example.pc.shacus.APP;
import com.example.pc.shacus.Adapter.FluidGridAdapter;
import com.example.pc.shacus.Data.Cache.ACache;
import com.example.pc.shacus.Data.Model.ImageData;
import com.example.pc.shacus.Data.Model.LoginDataModel;
import com.example.pc.shacus.Data.Model.PhotosetModel;
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
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class PhotosetOverviewActivity extends AppCompatActivity implements NetworkCallbackInterface.NetRequestIterface{

    private TextView back,title,edit;
    private boolean isEditing=false;
    FluidGridAdapter fluidGridAdapter;
    ArrayList<ImageData> imageDatas=new ArrayList<>();
    FrameLayout bottomMenu;
    private Animation get_photo_layout_in_from_down;
    private Button button_delete;
    private Button button_upload;
    private Handler handler;
    private NetRequest request;
    private UserModel userModel;
    private ArrayList<String> deletingIds=new ArrayList<>();
    ArrayList<PhotosetModel> photoSets;
    private int isself=1;//是否为自己，默认不是自己
    private int uid=-1;//当前这个用户的id

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos_overview);
        request=new NetRequest(this,this);
        back=(TextView) findViewById(R.id.photoset_toolbar_back);
        back.setText("＜返回");
        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });

        title=(TextView)findViewById(R.id.photoset_toolbar_title);
        title.setText("作品集列表");
        edit=(TextView)findViewById(R.id.photoset_toolbar_edit);

        handler=new Handler(){
            @Override
            public void handleMessage(Message msg){
                super.handleMessage(msg);

                if (msg.what== StatusCode.REQUEST_FAILURE){
                    CommonUtils.getUtilInstance().showToast(APP.context,msg.obj.toString());
                    finish();
                }

                if (msg.what== StatusCode.PHOTOSET_BIGIMG){
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
                                    get_photo_layout_in_from_down = AnimationUtils.loadAnimation(PhotosetOverviewActivity.this, R.anim.search_layout_in_from_down);
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
                                    if (deletingIds.size()==0){//数组大小为1说明只有默认图片，不需要向服务器请求删除
                                        CommonUtils.getUtilInstance().showToast(PhotosetOverviewActivity.this,"您没有作出任何更改");
                                        return;
                                    }
                                    //删除作品集
                                    AlertDialog dialog = new AlertDialog.Builder(PhotosetOverviewActivity.this)
                                            .setTitle("警告")
                                            .setMessage("确定要保存更改吗？您刚才删除的作品集将无法恢复！")
                                            .setPositiveButton("保存更改", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Map map=new HashMap();
                                                    map.put("authkey",userModel.getAuth_key());
                                                    map.put("uid",userModel.getId());
                                                    map.put("ucid",deletingIds);
                                                    map.put("type",StatusCode.UPDATE_DELETE_PHOTOSET);
                                                    request.httpRequest(map, CommonUrl.imgSelfAndSets);
                                                }
                                            })
                                            .setNegativeButton("放弃更改", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    PhotosetOverviewActivity.this.finish();
                                                }
                                            })
                                            .create();
                                    dialog.show();
                                }
                            }
                        });

                        button_delete=(Button)findViewById(R.id.photoset_delete);
                        button_delete.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View v){
                                boolean hasChoosed=false;
                                ArrayList<ImageData> imgDatasToRemove=new ArrayList<ImageData>();
                                ArrayList<PhotosetModel> photoSetsToRemove=new ArrayList<PhotosetModel>();
                                //先找出本次要删除的URL加进准备发给服务器的待删数组，并给临时数组添加元素以准备处理本地数据，一组单循环
                                for (int index=0;index<imageDatas.size();index++){
                                    if (imageDatas.get(index).isChecked()){
                                        hasChoosed=true;
                                        deletingIds.add(String.valueOf(photoSets.get(index).getUCid()));
                                        imgDatasToRemove.add(imageDatas.get(index));
                                        photoSetsToRemove.add(photoSets.get(index));
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
                                for (int i=0;i<photoSetsToRemove.size();i++){
                                    int cid=photoSetsToRemove.get(i).getUCid();
                                    for (int j=0;j<photoSets.size();j++){
                                        if (photoSets.get(j).getUCid()==cid){
                                            photoSets.remove(j);
                                            break;
                                        }
                                    }
                                }
                                if (!hasChoosed){
                                    CommonUtils.getUtilInstance().showToast(PhotosetOverviewActivity.this,"您没有选择任何作品集");
                                    return;
                                }
                                //把新清单数据注入adapter
                                fluidGridAdapter.refresh(imageDatas,photoSets);
                                //清除图片完成后，把新注入的数据都设置为可选状态
                                fluidGridAdapter.setPhotosCheckable(true);
                                //通知adapter进行画面重绘
                                fluidGridAdapter.notifyDataSetChanged();
                                CommonUtils.getUtilInstance().showToast(PhotosetOverviewActivity.this,"删除成功");
                            }
                        });

                        button_upload=(Button)findViewById(R.id.photoset_upload);
                        button_upload.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View v){
                                if (deletingIds.size()!=0){
                                    CommonUtils.getUtilInstance().showToast(PhotosetOverviewActivity.this,"检测到您删除了作品集，请先保存更改");
                                    return;
                                }
                                //发布新作品集
                                Intent intent=new Intent(getApplicationContext(),PhotosAddActivity.class);
                                intent.putExtra("type",2);
                                startActivity(intent);
                                finish();
                            }
                        });

                    }else{//不是自己的作品集，不能编辑
                        edit.setVisibility(View.INVISIBLE);
                        edit.setClickable(false);
                    }
                    //将略缩图取回并存储
                    for (int i=0;i<photoSets.size();i++){
                        imageDatas.add(photoSets.get(i).getUCimg());
                    }
                    //修复略缩图最后一张不显示bug（暂时这样在最后加一张无用图）
                    //imageDatas.add(new ImageData("http://obdvl7z18.bkt.clouddn.com/gh-pages/img/20160804/p11.jpg", 435, 145));
                    //加载图片列表视图
                    setupFluidGrid();
                    return;
                }

                if (msg.what== StatusCode.UPDATE_DELETE_PHOTOSET){
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
        uid=getIntent().getIntExtra("uid",-1);
        Map map=new HashMap();
        map.put("authkey", authKey);
        map.put("uid",uid);
        map.put("type",StatusCode.PHOTOSET_BIGIMG);
        //获取作品集列表
        request.httpRequest(map, CommonUrl.imgSelfAndSets);

    }

    private void setupFluidGrid(){
        //final ArrayList<ImageData> imageDatas = loadDevicePhotos();
        fluidGridAdapter = new FluidGridAdapter(this, photoSets,imageDatas){
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
                    //打开作品集详情页
                    String img=imageData.getImageUrl();
                    int id=-1;
                    for (int i=0;i<photoSets.size();i++){
                        if (photoSets.get(i).getUCimg().getImageUrl().equals(img)){
                            id=photoSets.get(i).getUCid();
                            break;
                        }
                    }
                    Intent intent=new Intent(getApplicationContext(),PhotosetDetailActivity.class);
                    intent.putExtra("ucid",id);
                    intent.putExtra("uid",uid);
                    startActivity(intent);
                }

            }

            @Override
            protected void loadImageIntoView(String photoUrl, int cellWidth, int cellHeight, ImageView imageHolder){
                //Picasso.with(PhotosetDetailActivity.this).load(new File(photoUrl)).resize(cellWidth, cellHeight).into(imageHolder);
                Glide.with(PhotosetOverviewActivity.this).load(photoUrl).override(cellWidth,cellHeight).into(imageHolder);
            }
        };
        ListView listview = (ListView)findViewById(R.id.fluid_list);
        listview.setAdapter(fluidGridAdapter);
    }

    private void setSelectState(String tag, boolean state) {
        for (int index=0;index<imageDatas.size();index++){
            if (imageDatas.get(index).getImageUrl().equals(tag)){
                imageDatas.get(index).setChecked(state);
            }
        }
    }

    @Override
    public void requestFinish(String result, String requestUrl) throws JSONException {
        if (requestUrl.equals(CommonUrl.imgSelfAndSets)){
            JSONObject object = new JSONObject(result);
            int code = Integer.valueOf(object.getString("code"));
            if (code==StatusCode.PHOTOSET_BIGIMG){
                isself=object.getInt("isself");
                JSONArray bigImgs=object.getJSONArray("contents");
                Gson gson=new Gson();
                photoSets=new ArrayList<>();
                for (int i=0;i<bigImgs.length();i++){
                    photoSets.add(gson.fromJson(bigImgs.getJSONObject(i).toString(),PhotosetModel.class));
                }
                Message msg=handler.obtainMessage();
                msg.what= StatusCode.PHOTOSET_BIGIMG;
                //msg.obj=data;
                handler.sendMessage(msg);
                return;
            }
            if (code==StatusCode.UPDATE_DELETE_PHOTOSET){
                deletingIds.clear();
                Message msg=handler.obtainMessage();
                msg.what= StatusCode.UPDATE_DELETE_PHOTOSET;
                msg.obj="作品集删除成功";
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