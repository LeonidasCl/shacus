package com.example.pc.shacus.Fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pc.shacus.Adapter.CardAdapter;
import com.example.pc.shacus.Data.Model.YuePaiDataModel;
import com.example.pc.shacus.R;
import com.example.pc.shacus.View.Custom.CardView;
import com.example.pc.shacus.View.Custom.MatrixView;
import com.example.pc.shacus.View.FloatMenu.FilterMenu;
import com.example.pc.shacus.View.FloatMenu.FilterMenuLayout;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 约拍子界面A（二级页面，约拍模特）
 * Created by pc on 2016/7/13.
 */
public class YuePaiFragmentAB extends Fragment implements CardView.OnCardClickListener {

    private ListView headerlist;
    public static final int HEADLIST_NUM=17;
    private int[] headerimg = new int[]{
            R.drawable.p0,
            R.drawable.p0,
            R.drawable.getnew,
            R.drawable.p1,
            R.drawable.p2,
            R.drawable.p2
            ,R.drawable.p3,
            R.drawable.p1,
            R.drawable.p2,
            R.drawable.p3,
            R.drawable.p1,
            R.drawable.p2,
            R.drawable.p3,
            R.drawable.getnew,
            R.drawable.p0,
            R.drawable.p0,
            R.drawable.p0};

    private Activity yuepai;
    List<YuePaiDataModel> yuepaiDatalist;
    private YuePaiDetailFragmentA frag;
    private View navibar;
    private int eventflag=0;//标识出此fragment的事件类型 1.约模特 2.约摄影师
    private ImageButton btn_previous;
    private ImageButton btn_next;
    private CardView cardView;

    FilterMenu.OnMenuChangeListener listener = new FilterMenu.OnMenuChangeListener() {
        @Override
        public void onMenuItemClick(View view, int position) {
            Toast.makeText(yuepai, "Touched position " + position, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onMenuCollapse() {

        }

        @Override
        public void onMenuExpand() {

        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        yuepai=this.getActivity();
        View view = inflater.inflate(R.layout.fragment_yue_pai_a, container, false);
        //FilterMenuLayout layout = (FilterMenuLayout) view.findViewById(R.id.filter_menu1);
        //attachMenu(layout);
        initUI(view);

        navibar=yuepai.findViewById(R.id.fragment_list);
        navibar.setVisibility(View.GONE);


        btn_previous=(ImageButton)view.findViewById(R.id.btn_previouscard);
        btn_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardView.goUp();
            }
        });


        btn_next=(ImageButton)view.findViewById(R.id.btn_nextcard);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardView.goDown();
            }
        });


        return view;
    }

//        private FilterMenu attachMenu(FilterMenuLayout layout){
//            return new FilterMenu.Builder(yuepai)
//                    .addItem(R.drawable.ic_action_info)
//                    .addItem(R.drawable.ic_action_info)
//                    .addItem(R.drawable.ic_action_info)
//                    .addItem(R.drawable.ic_action_info)
//                    .addItem(R.drawable.ic_action_info)
//                    .attach(layout)
//                    .withListener(listener)
//                    .build();
//        }

    private void initUI(View view) {

        //先把卡片绑定
        cardView = (CardView) view.findViewById(R.id.yuepai_cards_a);
        cardView.setOnCardClickListener(this);
        cardView.setItemSpace(20);
        //再设置好适配器（这里建了内部类来做适配器）
        YuePaiCardAdapter adapter = new YuePaiCardAdapter(yuepai);
        adapter.addAll(initData());
        cardView.setAdapter(adapter);

        FragmentManager manager = getActivity().getSupportFragmentManager();
        frag = new YuePaiDetailFragmentA();
        manager.beginTransaction().add(R.id.contentView, frag).commit();

        headerlist = (ListView) view.findViewById(R.id.yuepai_headerlist);
        headerlist.setAdapter(new HeaderAdapter());
        headerlist.setClipToPadding(false);
        headerlist.setClipChildren(false);
        headerlist.setVerticalScrollBarEnabled(true);//配合xml中滚动条null实现取消滚动条
        //XML中fadingedge去掉滑到顶端/底端的效果
        headerlist.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                for (int i = 0; i < headerlist.getChildCount(); i++) {
                    headerlist.getChildAt(i).invalidate();
                }
            }
        });
        headerlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position>2&&position<13)
                {
                    try {
                        if (cardView.scrollCards(position))
                                return;
                        else
                            Toast.makeText(yuepai,"这就是当前的用户",Toast.LENGTH_SHORT).show();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if(position==2||position==13)
                        Toast.makeText(yuepai,"重新获取网络数据",Toast.LENGTH_SHORT).show();
            }
        });

        headerlist.bringToFront();


    }


    //点中某张卡片后
    @Override
    public void onCardClick(final View view, final int position) {
        frag.show(view);
    }



    private List<YuePaiDataModel> initData() {
        yuepaiDatalist = new ArrayList<>();
        //模拟从网络获取数据
        for (int i=0;i<10;i++)
        {
            YuePaiDataModel model=new YuePaiDataModel();
            model.setUsername("用户" + i);
            model.setIntroduce("这是活动"+i+"的描述不能少于十五字不能多于一百五十字但是点开之前只能显示两行");
            model.setStartTime(new Date());
            model.setLocation("这是活动"+i+"的活动地点");
            yuepaiDatalist.add(model);
        }

        return yuepaiDatalist;
    }

    public void setEventflag(int eventflag) {
        this.eventflag = eventflag;
    }


    public class YuePaiCardAdapter extends CardAdapter<YuePaiDataModel> {

        public YuePaiCardAdapter(Context context) {
            super(context);
        }

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        protected View getCardView(int position,
                                   View convertView, ViewGroup parent) {
            if(convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(yuepai);
                convertView = inflater.inflate(R.layout.item_yuapai_layout, parent, false);
            }

            ImageView cardPic=(ImageView)convertView.findViewById(R.id.yuepai_card_pic);
            TextView username = (TextView) convertView.findViewById(R.id.yuepai_username);
            TextView introduce = (TextView) convertView.findViewById(R.id.yuepai_introduce);
            TextView time = (TextView) convertView.findViewById(R.id.yuepai_time);
            TextView location = (TextView) convertView.findViewById(R.id.yuepai_location);

            YuePaiDataModel model=getItem(position % yuepaiDatalist.size());
           //这里用Glide load一个imgview into到布局里，待写
            username.setText(model.getUsername());
            introduce.setText(model.getIntroduce());
            time.setText(model.getStartTime().toString());
            location.setText(model.getLocation());
            return convertView;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        navibar.setVisibility(View.VISIBLE);
    }

    class HeaderAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return HEADLIST_NUM;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                MatrixView m = (MatrixView) LayoutInflater.from(yuepai).inflate(R.layout.item_yuepai_header, null);
                m.setParentHeight(headerlist.getHeight());
                convertView = m;
            }
            ImageView imageView = (ImageView) convertView.findViewById(R.id.yuepai_header_img);
            imageView.setImageResource(headerimg[position % headerimg.length]);
            imageView.setTag(position);
            return convertView;
        }

    }

}
