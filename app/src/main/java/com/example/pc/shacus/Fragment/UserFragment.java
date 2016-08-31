package com.example.pc.shacus.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pc.shacus.APP;
import com.example.pc.shacus.Activity.ChatActivity;
import com.example.pc.shacus.Activity.CoursesActivity;
import com.example.pc.shacus.Activity.FavoritemActivity;
import com.example.pc.shacus.Activity.FollowActivity;
import com.example.pc.shacus.Activity.MainActivity;
import com.example.pc.shacus.Activity.OrdersActivity;
import com.example.pc.shacus.Activity.SettingsActivity;
import com.example.pc.shacus.Activity.ShareActivity;
import com.example.pc.shacus.Activity.SignInActivity;
import com.example.pc.shacus.Activity.WorksActiviy;
import com.example.pc.shacus.Network.NetworkCallbackInterface;
import com.example.pc.shacus.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//孙启凡 2016.08.29
//用户个人界面（一级）
public class UserFragment extends android.support.v4.app.ListFragment implements  NetworkCallbackInterface.NetRequestIterface {

    private ListView listView;
    private SimpleAdapter adapter;
    private com.example.pc.shacus.View.TagView.CircleImageView image3;
    SimpleAdapter adapter1;
    public int MID;
    LinearLayout button1;
    LinearLayout button2;
    LinearLayout button3;
    LinearLayout button4;
    ImageView button5;
    ImageView button6;
    TextView text1;
    TextView text2;
    String newName;
    //单击大图变量
    Bitmap bitmap=null;
    float scaleWidth;
    float scaleHeight;


    ContextMenu menu;
    String[] data = {"我的学习", "订单", "我的地图", "优惠券", "分享给好友得积分", "设置"};
    int[] image1 = {R.drawable.a, R.drawable.a, R.drawable.a, R.drawable.a, R.drawable.a, R.drawable.a,};

    /* 请求识别码 */
    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CODE_RESULT_REQUEST = 0xa2;

    // 裁剪后图片的宽(X)和高(Y),480 X 480的正方形。
    private static int output_X = 480;
    private static int output_Y = 480;


    private Bitmap head;
    private static String path = "/sdcard/";


    @Override
    public void requestFinish(String result, String requestUrl) {
        //在这里接收所有网络请求
    }

    @Override
    public void exception(IOException e, String requestUrl) {
        //处理网络请求的异常信息，一般用不到
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_user, container, false);
        listView = (ListView) view.findViewById(R.id.listView);
        button1 = (LinearLayout) view.findViewById(R.id.button1);
        button2 = (LinearLayout) view.findViewById(R.id.button2);
        button3 = (LinearLayout) view.findViewById(R.id.button3);
        button4 = (LinearLayout) view.findViewById(R.id.button4);
        button5 = (ImageView) view.findViewById(R.id.button5);
        button6 = (ImageView) view.findViewById(R.id.button6);
        text1 = (TextView) view.findViewById(R.id.text1);
        text2 = (TextView) view.findViewById(R.id.text2);
        
        
        image3 = (com.example.pc.shacus.View.TagView.CircleImageView) view.findViewById(R.id.image3);
        Bitmap bt = BitmapFactory.decodeFile(path + "head.jpg");

        SearchView search=(SearchView)view.findViewById(R.id.test_search_tag);
        search.setIconifiedByDefault(false);
        search.setSubmitButtonEnabled(false);
        search.setQueryHint("查找或输入标签");
        if (bt != null) {
            @SuppressWarnings("deprecation")
            Drawable drawable = new BitmapDrawable(bt);//转换成drawable
            image3.setImageDrawable(drawable);
        }

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent();
                intent1.putExtra("activity", "following");
                intent1.setClass(getActivity(), FollowActivity.class);
                startActivity(intent1);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent();
                intent2.putExtra("activity", "follower");
                intent2.setClass(getActivity(), FollowActivity.class);
                startActivity(intent2);
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(getActivity(), WorksActiviy.class);
                startActivity(intent3);
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent4 = new Intent(getActivity(), FavoritemActivity.class);
                startActivity(intent4);
            }
        });
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent5 = new Intent(getActivity(), SignInActivity.class);
                startActivity(intent5);
            }
        });
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent6 = new Intent(getActivity(), ChatActivity.class);
                startActivity(intent6);
            }
        });
        image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        image3.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                image3.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                    @Override
                    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                        menu.add(0, 0, 0, "拍照");
                        menu.add(0, 1, 0, "从本地选取");
                        menu.add(0, 2, 0, "取消");
                    }
                });
                return false;
            }
        });


        text1.setOnClickListener(new View.OnClickListener() {
                                     @Override
                                     public void onClick(View v) {

                                         final EditText et = new EditText(getActivity());

                                         new AlertDialog.Builder(getActivity()).setTitle("更改地点")
                                                 .setIcon(android.R.drawable.ic_dialog_info)
                                                 .setView(et)
                                                 .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                     @Override
                                                     public void onClick(DialogInterface dialog, int which) {
                                                         String input = et.getText().toString();
                                                         if (input.equals("")) {
                                                             Toast.makeText(getContext(), "地址不能为空！" + input, Toast.LENGTH_LONG).show();
                                                         } else {
                                                             newName = et.getText().toString();
                                                             text1.setText(newName);
                                                         }
                                                     }
                                                 })
                                                 .setNegativeButton("取消", null)
                                                 .show();
                                     }


                                 }
        );
        text2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText et = new EditText(getActivity());

                new AlertDialog.Builder(getActivity()).setTitle("更改个性签名")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setView(et)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String input = et.getText().toString();
                                if (input.equals("")) {

                                } else {
                                    newName = et.getText().toString();
                                    text2.setText(newName);
                                }
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });

        SimpleAdapter adapter1 = new SimpleAdapter(this.getActivity(), init(), R.layout.item_user_listview_layout, new String[]{"item1",
                "item2", "item3"}, new int[]{R.id.image1, R.id.content, R.id.image2});
        setListAdapter(adapter1);
        return view;
    }

    private List<Map<String, Object>> init() {
        List<Map<String, Object>> lst = new ArrayList<>();
        for (int i = 0; i < data.length; i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("item1", image1[i]);
            item.put("item2", data[i]);
            item.put("item3", R.drawable.a);
            lst.add(item);
        }
        return lst;
    }


    public void onListItemClick(ListView l, View v, int position, long id) {
        switch (position) {
            case 0:
                Intent intent4 = new Intent(getActivity(), CoursesActivity.class);
                startActivity(intent4);
                break;
            case 1:
                Intent intent5 = new Intent(getActivity(), OrdersActivity.class);
                startActivity(intent5);
                break;
            case 2:
                //TODO 地图
                break;
            case 3:
                //TODO 优惠券
                break;
            case 4:
                Intent intent8 = new Intent(getActivity(), ShareActivity.class);
                startActivity(intent8);
                break;
            case 5:
                Intent intent9 = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent9);
                break;
        }
    }


    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case 0:
                // 启动手机相机拍摄照片作为头像
                Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "head.jpg")));
                startActivityForResult(intentFromCapture, CODE_CAMERA_REQUEST);

                break;

            case 1:
                // 本地选取
                Intent intentFromGallery = new Intent(Intent.ACTION_PICK, null);
                // 设置文件类型
                intentFromGallery.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intentFromGallery, CODE_GALLERY_REQUEST);

//                Bundle extra = intentFromGallery.getExtras();
//                head = extra.getParcelable("data");
//                    setPicToView(head);
//                    image3.setImageBitmap(head);
                break;

            case 2:
                return super.onContextItemSelected(item);

            default:
                break;

        }

        return super.onContextItemSelected(item);


    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {


        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode) {
            case CODE_GALLERY_REQUEST:

               cropRawPhoto(intent.getData());
                break;

            case CODE_CAMERA_REQUEST:

                File tempFile = new File(
                        Environment.getExternalStorageDirectory() + "/head.jpg");
                cropRawPhoto(Uri.fromFile(tempFile));

                break;

            case CODE_RESULT_REQUEST:
                if (intent != null) {
                    Bundle extra = intent.getExtras();
                    head = extra.getParcelable("data");
                    if (head != null) {
                        setPicToView(head);
                        image3.setImageBitmap(head);

                    }
                }

                break;
        }

    }


    /**
     * 裁剪原始的图片
     */
    public void cropRawPhoto(Uri uri) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");

        // 设置裁剪
        intent.putExtra("crop", "true");

        // aspectX , aspectY :宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX , outputY : 裁剪图片宽高
        intent.putExtra("outputX", 80);
        intent.putExtra("outputY", 80);
        intent.putExtra("return-data", true);

        startActivityForResult(intent, CODE_RESULT_REQUEST);


    }



    private void setPicToView(Bitmap mBitmap) {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            return;
        }
        FileOutputStream b = null;
        File file = new File(path);
        file.mkdirs();// 创建文件夹
        String fileName = path + "head.jpg";//图片名字
        try {
            b = new FileOutputStream(fileName);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                //关闭流
                b.flush();
                b.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
