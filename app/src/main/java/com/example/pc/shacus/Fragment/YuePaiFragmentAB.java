package com.example.pc.shacus.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
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

import com.bumptech.glide.Glide;
import com.example.pc.shacus.Activity.OtherUserActivity;
import com.example.pc.shacus.Adapter.CardAdapter;
import com.example.pc.shacus.Data.Cache.ACache;
import com.example.pc.shacus.Data.Model.LoginDataModel;
import com.example.pc.shacus.Data.Model.UserModel;
import com.example.pc.shacus.Data.Model.YuePaiDataModel;
import com.example.pc.shacus.Network.NetRequest;
import com.example.pc.shacus.Network.NetworkCallbackInterface;
import com.example.pc.shacus.Network.StatusCode;
import com.example.pc.shacus.R;
import com.example.pc.shacus.Util.CommonUrl;
import com.example.pc.shacus.View.Custom.CardView;
import com.example.pc.shacus.View.Custom.MatrixView;
import com.example.pc.shacus.View.FloatMenu.FilterMenu;
import com.example.pc.shacus.View.FloatMenu.FilterMenuLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 约拍子界面A（二级页面，约拍模特）
 * Created by pc on 2016/7/13.
 */
public class YuePaiFragmentAB extends Fragment implements NetworkCallbackInterface.NetRequestIterface{

    private ListView headerlist;
    public static final int HEADLIST_NUM=17;
    private String[] headerimg = new String[17];

    private Activity yuepai;
    List<YuePaiDataModel> yuepaiDatalist;
    private YuePaiDetailFragmentA frag;
    private View navibar;
    private int eventflag=0;//标识出此fragment的事件类型 1.约模特 2.约摄影师
    private ImageButton btn_previous;
    private ImageButton btn_next;
    private CardView cardView;
    ACache aCache;
    private NetRequest netRequest;
    String userId=null;
    String authkey=null;
    UserModel user = null;
    String imageurl;
    int id;

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

    View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        yuepai=this.getActivity();
        view = inflater.inflate(R.layout.fragment_yue_pai_a, container, false);
        netRequest = new NetRequest(this,getActivity());
        //FilterMenuLayout layout = (FilterMenuLayout) view.findViewById(R.id.filter_menu1);
        //attachMenu(layout);

        //获得约摸特还是摄影师
        yuepaiDatalist = new ArrayList<>();
        aCache = ACache.get(getActivity());
        LoginDataModel loginModel = (LoginDataModel)aCache.getAsObject("loginModel");
        user = loginModel.getUserModel();
        userId = user.getId();
        authkey = user.getAuth_key();
        initInfo();

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

    private void initInfo(){
        Map map=new HashMap();
        map.put("uid",userId);
        map.put("authkey", authkey);
        if(eventflag == 2){
            map.put("type", StatusCode.REQUEST_PAIHANG_PHOTOGRAPHER);
            netRequest.httpRequest(map, CommonUrl.paiHangbang);
        }else if(eventflag == 1){
            map.put("type", StatusCode.REQUEST_PAIHANG_MODEL);
            netRequest.httpRequest(map, CommonUrl.paiHangbang);
        }
    }

    private void initUI(View view) {

        //先把卡片绑定
        cardView = (CardView) view.findViewById(R.id.yuepai_cards_a);
       // cardView.setOnCardClickListener(this);
        cardView.setItemSpace(20);
        //再设置好适配器（这里建了内部类来做适配器）
        YuePaiCardAdapter adapter = new YuePaiCardAdapter(yuepai);
        Log.d("aaaaaaaaaa",yuepaiDatalist.toString());
        adapter.addAll(initData(yuepaiDatalist));
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
                        initInfo();
            }
        });

        headerlist.bringToFront();

    }

    //点中某张卡片后
 /*   @Override
    public void onCardClick(final View view, final int position) {
//        frag.show(view);
        Intent intent = new Intent(getActivity(), OtherUserActivity.class);
        intent.putExtra("id",String.valueOf(yuepaiDatalist.get(position).getAPid()));
        startActivity(intent);
    }*/



    private List<YuePaiDataModel> initData(List<YuePaiDataModel> temp) {
        return temp;
    }

    public void setEventflag(int eventflag) {
        this.eventflag = eventflag;
    }

    private Handler handler=new Handler(){

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case StatusCode.REQUEST_PAIHANG_SUCCESS:
                {
                    initData(yuepaiDatalist);
                    initUI(view);
                }
            }
        }
    };
    @Override
    public void requestFinish(String result, String requestUrl) throws JSONException {
        if(requestUrl.equals(CommonUrl.paiHangbang)) {//返回排行榜
            JSONObject object = new JSONObject(result);
            int code = Integer.valueOf(object.getString("code"));
            Message msg = new Message();
            Log.d("yyyy",String.valueOf(code));
            switch (code) {
                case StatusCode.REQUEST_PAIHANG_SUCCESS://请求排行榜成功
                {
                    JSONArray content = object.getJSONArray("content");
                    Log.d("eeeeeeeeeeeeeeeeee", content.toString());
                    for (int i = 0; i < content.length(); i++) {
                        JSONObject data = content.getJSONObject(i);
                        Log.d("eeeeeeeeeeee", data.toString());
                        YuePaiDataModel yuePaiDataModel = new YuePaiDataModel();
                        yuePaiDataModel.setAPtitle(data.getString("nickName"));
                        yuePaiDataModel.setAPid(data.getInt("id"));
                        yuePaiDataModel.setAPcontent(data.getString("sign"));
                       // yuePaiDataModel.setAPlocation(data.getString("location"));
                        yuePaiDataModel.setImage(data.getString("headImage"));
                        yuePaiDataModel.setImagecard(data.getString("image"));
                        yuePaiDataModel.setRank(data.getInt("rank"));
                        Log.d("eeeeeeeeeeeeeeeeee", yuePaiDataModel.getImage().toString());
                        headerimg[i+3] = data.getString("headImage");
                        yuepaiDatalist.add(yuePaiDataModel);
                    }
                    Collections.sort(yuepaiDatalist, new Comparator<YuePaiDataModel>() {
                        @Override
                        public int compare(YuePaiDataModel o1, YuePaiDataModel o2) {
                            if(o1.getRank()>o2.getRank()){
                                return 1;
                            }
                            if (o1.getRank()==o2.getRank()){
                                return 0;
                            }
                            else{
                                return -1;
                            }



                        }
                    });
                    msg.what = StatusCode.REQUEST_PAIHANG_SUCCESS;
                    Log.d("jjjjjjjjjjjjjj",headerimg[3]);
                    handler.sendMessage(msg);

                    break;
                }
            }
        }
    }
    @Override
    public void exception(IOException e, String requestUrl) {

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
        protected View getCardView(final int position, View convertView, ViewGroup parent) {
            if(convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(yuepai);
                convertView = inflater.inflate(R.layout.item_yuapai_layout, parent, false);
            }

            ImageView cardPic=(ImageView)convertView.findViewById(R.id.yuepai_card_pic);
            TextView username = (TextView) convertView.findViewById(R.id.yuepai_username);
            TextView introduce = (TextView) convertView.findViewById(R.id.yuepai_introduce);
           // TextView time = (TextView) convertView.findViewById(R.id.yuepai_time);
            TextView location = (TextView) convertView.findViewById(R.id.yuepai_location);
            cardPic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), OtherUserActivity.class);
                    intent.putExtra("id",String.valueOf(yuepaiDatalist.get(position% yuepaiDatalist.size()).getAPid()));
                    startActivity(intent);
                }
            });
            Log.d("aaaaaaaaaa",String.valueOf(yuepaiDatalist.size()));
            Log.d("qqqqqqqqqqqq",String.valueOf(position));
            YuePaiDataModel model = getItem(position % yuepaiDatalist.size());
           //这里用Glide load一个imgview into到布局里，待写
            username.setText(model.getAPtitle());
            introduce.setText(model.getAPcontent());
//            time.setText(model.getAPcreateT().toString());
            location.setText(model.getAPlocation());
            Picasso.with(getContext())
                    .load(yuepaiDatalist.get(position%yuepaiDatalist.size()).getImagecard())
                    .error(R.drawable.p0)
                    .into(cardPic);
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
         //   return Integer.MAX_VALUE;//无限加载
        }

        @Override
        public Object getItem(int position) {
            return yuepaiDatalist.get(position%yuepaiDatalist.size());
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                MatrixView m = (MatrixView) LayoutInflater.from(yuepai).inflate(R.layout.item_yuepai_header, null);
                m.setParentHeight(headerlist.getHeight());
                convertView = m;
            }
            ImageView imageView = (ImageView) convertView.findViewById(R.id.yuepai_header_img);
            if (position<3||position>12){
                imageView.setImageResource(R.drawable.p0);
            }else {
               // Picasso.with(getContext()).load(yuepaiDatalist.get((position)%yuepaiDatalist.size()).getImage()).into(imageView);
               Picasso.with(getContext()).load(yuepaiDatalist.get((position+7) % yuepaiDatalist.size()).getImage()).into(imageView);
              /*  Glide.with(getContext())
                        .load(yuepaiDatalist.get(position%yuepaiDatalist.size()).getImage()).into(imageView);*/
              //\  Log.d("hhhhhh",headerimg[position % headerimg.length]);
            }
           // imageView.setTag(position);
            return convertView;
        }

    }

}
