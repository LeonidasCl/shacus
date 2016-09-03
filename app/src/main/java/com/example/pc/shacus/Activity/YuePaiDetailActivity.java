package com.example.pc.shacus.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import com.example.pc.shacus.Adapter.JoinUserGridAdapter;
import com.example.pc.shacus.Data.Model.UserModel;
import com.example.pc.shacus.R;
import com.example.pc.shacus.View.FloatMenu.FilterMenu;
import com.example.pc.shacus.View.FloatMenu.FilterMenuLayout;
import java.util.ArrayList;
import java.util.List;
import cn.bingoogolapple.bgabanner.BGABanner;
import cn.bingoogolapple.bgabanner.BGABannerUtil;

public class YuePaiDetailActivity extends AppCompatActivity {

    HorizontalScrollView horizontalScrollView;
    GridView gridView;
    int dwidth;
    private int num_per_page = 5; // 每行显示个数
    FilterMenuLayout filterMenu;
    private int width;//每列宽度
    //private int total = 10;//列数
    private BGABanner mSideZoomBanner;
    FilterMenu.OnMenuChangeListener listener = new FilterMenu.OnMenuChangeListener() {
        @Override
        public void onMenuItemClick(View view, int position) {
            Toast.makeText(getApplicationContext(), "Touched position " + position, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onMenuCollapse() {

        }

        @Override
        public void onMenuExpand() {

        }
    };
    private TextView textName;
    private TextView textTitle;
    //private List<Drawable> userAvatarList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yue_pai_detail);

        horizontalScrollView = (HorizontalScrollView) findViewById(R.id.join_user_scroll);
        gridView = (GridView) findViewById(R.id.grid_join_user_scroll);

        horizontalScrollView.setHorizontalScrollBarEnabled(false);// 隐藏滚动条
        dwidth =(int)getScreenDen();

        width=100;
        num_per_page = dwidth / width;
        setValue();
        mSideZoomBanner = (BGABanner) findViewById(R.id.banner_detail_zoom);
        mSideZoomBanner.setViews(getPics());


        filterMenu= (FilterMenuLayout)findViewById(R.id.detail_filter_menu);
        attachMenu(filterMenu);

        textTitle=(TextView)findViewById(R.id.detail_toolbar_back);
        textTitle.setText(" < ");
        textTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        textName=(TextView)findViewById(R.id.detail_toolbar_title);
        textName.setText("标题标题标题标题标题标题标题");

    }

    private FilterMenu attachMenu(FilterMenuLayout filterMenu) {
        return new FilterMenu.Builder(this)
                .addItem(R.drawable.ic_action_info)
                .addItem(R.drawable.ic_action_info)
                .addItem(R.drawable.ic_action_info)
                .attach(filterMenu)
                .withListener(listener)
                .build();
    }

    public float getScreenDen() {
        //dwidth = new DisplayMetrics();
        //getWindowManager().getDefaultDisplay().getMetrics(dwidth);
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics ();
        display.getMetrics(outMetrics);

        float density  = getResources().getDisplayMetrics().density;

        return outMetrics.widthPixels / density;
    }

    private void setValue() {
        JoinUserGridAdapter adapter = new JoinUserGridAdapter(this, init(),true);

        gridView.setAdapter(adapter);
        LayoutParams params = new LayoutParams(adapter.getCount() * width, LayoutParams.WRAP_CONTENT);
        gridView.setLayoutParams(params);
        gridView.setColumnWidth(100);
        gridView.setStretchMode(GridView.NO_STRETCH);
        int count = adapter.getCount();
        gridView.setNumColumns(count);
    }

    private List<UserModel> init() {
        List<UserModel> list=new ArrayList<>();
        for (int i=0;i<10;i++)
        {
            UserModel user=new UserModel();
            user.setPhone("phone"+String.valueOf(i));
            user.setId("usrid" + i);
            list.add(user);
        }
        return  list;
    }

    public List<? extends View> getPics() {
        List<ImageView> views = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            views.add(BGABannerUtil.getItemImageView(this, R.drawable.holder));
        }
        return views;
    }
}