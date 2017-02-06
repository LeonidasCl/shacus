package com.example.pc.shacus.Activity;

/**
 * Created by licl on 2017/2/4.
 * 作品集详情页可用操作：
 * - 向作品集添加图片：打开新activity添加图片后回到这个activity（相当于重新加载activity，要向服务器提交新上传的图片并下载新列表）
 * - 删除作品集的图片：点击编辑后可以从现有列表中删除，再点击由编辑按钮变换而成的完成按钮可以重新加载（只需要向服务器提交新列表，不用下载新列表）
 */


import java.util.ArrayList;
import com.bumptech.glide.Glide;
import com.example.pc.shacus.Adapter.FluidGridAdapter;
import com.example.pc.shacus.Data.Model.ImageData;
import com.example.pc.shacus.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class PhotosetDetailActivity extends Activity {

    private TextView back,title,edit;
    private boolean isEditing=false;
    FluidGridAdapter fluidGridAdapter;
    ArrayList<ImageData> imageDatas;
    FrameLayout bottomMenu;
    private Animation get_photo_layout_in_from_down;
    private Button button_delete;
    private Button button_upload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos_detail);

        back=(TextView) findViewById(R.id.photoset_toolbar_back);
        back.setText("＜返回");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        title=(TextView)findViewById(R.id.photoset_toolbar_title);
        title.setText("作品集标题");

        edit=(TextView)findViewById(R.id.photoset_toolbar_edit);

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
                    }
                }
            });

            button_delete=(Button)findViewById(R.id.photoset_delete);
            button_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    for (int index=0;index<imageDatas.size();index++){
                        if (imageDatas.get(index).isChecked())
                            imageDatas.remove(index);
                    }
                    //把新清单数据注入adapter
                    fluidGridAdapter.refresh(imageDatas);
                    //清除图片完成后，把新注入的数据都设置为可选状态
                    fluidGridAdapter.setPhotosCheckable(true);
                    //通知adapter进行画面重绘
                    fluidGridAdapter.notifyDataSetChanged();
                }
            });

            button_upload=(Button)findViewById(R.id.photoset_upload);
            button_upload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO 添加新图片
                }
            });

        }else{//不是自己的作品集，不能编辑
            edit.setVisibility(View.INVISIBLE);
            edit.setClickable(false);
        }

        setupFluidGrid();
    }

    private void setupFluidGrid(){
        final ArrayList<ImageData> imageDatas = loadDevicePhotos();
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
                    //TODO 打开看图模式
                    Toast.makeText(PhotosetDetailActivity.this,"打开看图模式",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            protected void loadImageIntoView(String photoUrl, int cellWidth, int cellHeight, ImageView imageHolder){
                //Picasso.with(PhotosetDetailActivity.this).load(new File(photoUrl)).resize(cellWidth, cellHeight).into(imageHolder);
                Glide.with(PhotosetDetailActivity.this).load(photoUrl).override(cellWidth,cellHeight).into(imageHolder);
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

    protected ArrayList<ImageData> loadDevicePhotos() {
        //String[] projection = { MediaStore.Images.Thumbnails._ID, MediaStore.Images.Thumbnails.DATA, MediaStore.Images.Thumbnails.HEIGHT,
//                MediaStore.Images.Thumbnails.WIDTH };
//        Cursor cursor = getContentResolver().query(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, projection, null, null, null);
//        ArrayList<ImageData> imageDatas = new ArrayList<ImageData>();
//        int photoHeightIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.HEIGHT);
//        int photoWidthIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.WIDTH);
//        int fileLocationIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//        while(cursor.moveToNext()) {
//
//            int photoHeight = cursor.getInt(photoHeightIndex);
//            int photoWidth = cursor.getInt(photoWidthIndex);
//            String fileLocation = cursor.getString(fileLocationIndex);
//
//            if(photoWidth > 0 && photoHeight > 0) {
//                ImageData imageData = new ImageData(fileLocation, photoWidth, photoHeight);
//                imageDatas.add(imageData);
//            }
//        }
//        cursor.close();
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
        imageDatas.add(new ImageData("http://obdvl7z18.bkt.clouddn.com/gh-pages/img/20160804/p11.jpg", 435, 145));
        imageDatas.add(new ImageData("http://obdvl7z18.bkt.clouddn.com/image/excited/08.jpg", 480, 853));

        return imageDatas;
    }

}