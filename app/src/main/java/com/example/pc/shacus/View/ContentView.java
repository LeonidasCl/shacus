package com.example.pc.shacus.View;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.example.pc.shacus.Adapter.HuodongItemAdapter;
import com.example.pc.shacus.Data.Model.HuoDongItemModel;
import com.example.pc.shacus.Network.NetRequest;
import com.example.pc.shacus.Network.NetworkCallbackInterface;
import com.example.pc.shacus.Network.StatusCode;
import com.example.pc.shacus.R;
import com.example.pc.shacus.Util.CommonUrl;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 内容视图
 */
public class ContentView extends TouchMoveView implements NetworkCallbackInterface.NetRequestIterface{

    Context context;
    View parent;

    private SwipeRefreshLayout refreshLayout;
    private HuodongItemAdapter personAdapter;
    private ListView listView;
    private int bootCounter=0;
    private int maxRecords = 400;
    private boolean getHuodongFlag=false;

    private NetRequest netRequest;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case StatusCode.REQUEST_HUODONG_GET_HUODONG:
//                    List<HuoDongItemModel> persons = new ArrayList<>();
//                    for(int i=bootCounter;i<bootCounter+10;i++){
//                        HuoDongItemModel huodong = new HuoDongItemModel();
//                        huodong.setACcontent("活动描述信息加载中...不得少于十五字不得多于一百五十字");
//                        huodong.setACstartT(new Date());
//                        huodong.setACregistN((int) (Math.random() * 100) + 1);
//                        huodong.setAClikenumber((int) (Math.random() * 100) + 1);
//                        huodong.setUsrName("用户" + i);
//                        persons.add(huodong);
//                        Log.d("LQQQQQQQQ", "bootData"+i);
//                    }
//                    bootCounter+=10;
                    personAdapter.notifyDataSetChanged();
                    getHuodongFlag=false;
                    break;
                case StatusCode.REQUEST_HUODONG_MORE_HUODONG:
                    personAdapter.notifyDataSetChanged();
                    getHuodongFlag=false;
                    break;
            }
        }
    };


	public ContentView(Context context) {
		super(context);
        this.context=context;
        parent=this;
	}

	public ContentView(Context context, AttributeSet attrs) {
		super(context, attrs);
        this.context=context;
        parent=this;
	}

	public ContentView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
        this.context=context;
        //parent=this;
	}

    //这个init必须由父view在inflate完成后调用
	public void init(View view) {

        /*ptrClassicFrameLayout = (PtrClassicFrameLayout) this.findViewById(R.id.huodong_view_frame);
        mRecyclerView = (RecyclerView) this.findViewById(R.id.huodong_recycler_view);

        adapter = new RecyclerAdapter(context, mData);
        mAdapter = new RecyclerAdapterWithHF(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.setAdapter(mAdapter);
        ptrClassicFrameLayout.postDelayed(new Runnable() {

            @Override
            public void run() {
                ptrClassicFrameLayout.autoRefresh(true);
            }
        }, 150);

        ptrClassicFrameLayout.setPtrHandler(new PtrDefaultHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        page = 0;
                        mData.clear();
                        for (int i = 0; i < 17; i++) {
                            mData.add(new String("  RecyclerView item  -" + i));
                        }
                        mAdapter.notifyDataSetChanged();
                        ptrClassicFrameLayout.refreshComplete();
                        ptrClassicFrameLayout.setLoadMoreEnable(true);
                    }
                }, 2000);
            }
        });

        ptrClassicFrameLayout.setOnLoadMoreListener(new OnLoadMoreListener() {

            @Override
            public void loadMore() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mData.add(new String("  RecyclerView item  - add " + page));
                        mAdapter.notifyDataSetChanged();
                        ptrClassicFrameLayout.loadMoreComplete(true);
                        page++;
                        //Toast.makeText(RecyclerViewActivity.this, "load more complete", Toast.LENGTH_SHORT).show();
                    }
                }, 1000);
            }
        });*/


        List<HuoDongItemModel> list=new ArrayList<>();
        personAdapter = new HuodongItemAdapter(context,list);
        listView = (ListView) view.findViewById(R.id.huodong_list);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_huodong);
        listView.setAdapter(personAdapter);

        netRequest=new NetRequest(this,this.getContext());

        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                parent.onTouchEvent(event);
                return false;
            }
        });

        onInitHuodong();
        onScrollListener();
        onRefreshListener();

	}

    private void onInitHuodong() {
        HashMap map=new HashMap();
        map.put("type",10303);
        netRequest.httpRequest(map, CommonUrl.getHuodongList);
    }

    private void onRefreshListener() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);
                bootCounter = 0;
                List<HuoDongItemModel> list=new ArrayList<>();
                personAdapter.refresh(list);
                personAdapter.notifyDataSetChanged();
                refreshLayout.setRefreshing(false);
                onInitHuodong();
                Log.d("LQQQQQQQQ", "onRefresh: ");
            }
        });
    }

    private void onScrollListener() {
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                Log.d("LQQQQQQQQ", "onScrollChanged ");
            }


            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount){
//                Log.d("LQQQQQQQQ", "onScroll: ");
//                Log.d("LQQQQQQQQ", " f:"+firstVisibleItem+" v:"+visibleItemCount+" t:"+totalItemCount);
                if (firstVisibleItem + visibleItemCount > totalItemCount - 1 && totalItemCount < maxRecords && visibleItemCount!=0 &&!getHuodongFlag) {
                    HashMap map=new HashMap();
                    map.put("type", 10304);
                    map.put("acsended", bootCounter);
                    netRequest.httpRequest(map, CommonUrl.getHuodongList);
                    getHuodongFlag=true;
                    Log.d("LQQQQQQQQ", firstVisibleItem+"+"+visibleItemCount+">"+totalItemCount+"-2&&"+totalItemCount+"<"+maxRecords);
                }
            }
        });
    }


    public synchronized void onShowAnimation(float step) {

		if(isShowFinish()) {
			return;
		}
		updateMarginTop(-getShowMoveStep(step));
	}

	public synchronized void onHideAnimation(float step) {

		if(isHideFinish()) {
			return;
		}
		updateMarginTop(getHideMoveStep(step));
	}

	/**
	 * 获取当前视图在展示过程中已经滑离初始化位置的距离
	 * @return
	 */
	public int getShowOffset() {
		return mHideStopMarginTop - getMarginTop();
	}

	/**
	 * 获取当前视图在恢复过程中已经滑离展示停止位置的距离
	 * @return
	 */
	public int getHideOffset() {
        return getMarginTop() - mShowStopMarginTop;
    }

//    private List<HuoDongItemModel> bootData(int Num){
//        List<HuoDongItemModel> persons = new ArrayList<>();
//        for(int i=bootCounter;i<bootCounter+Num;i++){
//            HuoDongItemModel huodong = new HuoDongItemModel();
//            huodong.setACcontent("活动描述信息加载中...不得少于十五字不得多于一百五十字");
//            huodong.setACstartT(new Date());
//            huodong.setACregistN((int) (Math.random() * 100) + 1);
//            huodong.setAClikenumber((int) (Math.random() * 100) + 1);
//            huodong.setUsrName("用户" + i);
//            persons.add(huodong);
//            Log.d("LQQQQQQQQ", "bootData"+i);
//        }
//        bootCounter+=Num;
//        return persons;
//    }

    @Override
    public void requestFinish(String result, String requestUrl) throws JSONException {
        if(requestUrl.equals(CommonUrl.getHuodongList)){
            JSONObject jsonObject=new JSONObject(result);
            String code=jsonObject.getString("code");
            JSONArray jsonArray=jsonObject.getJSONArray("contents");
            //Log.d("LQQQQQ", code);
            //Log.d("LQQQQQ", jsonObject.getString("contents"));
            if(code.equals("10303")){
                List<HuoDongItemModel> persons = new ArrayList<>();
                for(int i=bootCounter;i<bootCounter+jsonArray.length();i++){
                    JSONObject info=jsonArray.getJSONObject(i-bootCounter);
                    Gson gson=new Gson();
                    HuoDongItemModel huodong =gson.fromJson(info.toString(),HuoDongItemModel.class);
                    /*huodong.setACcontent(info.getString("ACcontent"));
                    huodong.setACstartT(info.getString("ACstartT"));
                    huodong.setACregistN(Integer.valueOf(info.getString("ACregistN")));
                    huodong.setAClikenumber(Integer.valueOf(info.getString("AClikenumber")));
                    huodong.setUsrName(info.getString("Ualais"));
                    huodong.setAClurl(info.getString("AClurl"));*/
//                    huodong.setUserimageurl();
                    persons.add(huodong);
//                    personAdapter.notifyDataSetChanged();
                    Log.d("LQQQQQQQQ", "bootData"+i);
                }
                personAdapter.add(persons);
                bootCounter+=jsonArray.length();
                Message msg=handler.obtainMessage();
                msg.what= StatusCode.REQUEST_HUODONG_GET_HUODONG;
                handler.sendMessage(msg);
            }
            else if(code.equals("10304")){
//                Message msg=handler.obtainMessage();
//                msg.what= StatusCode.REQUEST_HUODONG_MORE_HUODONG;
//                handler.sendMessage(msg);
                Log.d("LQQQQQQ", "10304: ");
                List<HuoDongItemModel> persons = new ArrayList<>();
                for(int i=bootCounter;i<bootCounter+jsonArray.length();i++){
                    JSONObject info=jsonArray.getJSONObject(i-bootCounter);
                    HuoDongItemModel huodong = new HuoDongItemModel();
                    huodong.setACcontent(info.getString("ACcontent"));
                    huodong.setACstartT(info.getString("ACstartT"));
                    huodong.setACregistN(Integer.valueOf(info.getString("ACregistN")));
                    huodong.setAClikenumber(Integer.valueOf(info.getString("AClikenumber")));
                    //huodong.setUsrName(info.getString("Ualais"));
                    huodong.setAClurl(info.getString("AClurl"));
//                    huodong.setUserimageurl();
                    persons.add(huodong);
                    Log.d("LQQQQQQQQ", "bootData" + i);
//                    personAdapter.notifyDataSetChanged();
                }
                personAdapter.add(persons);
                bootCounter+=jsonArray.length();
                Message msg=handler.obtainMessage();
                msg.what= StatusCode.REQUEST_HUODONG_MORE_HUODONG;
                handler.sendMessage(msg);
            }
        }
    }

    @Override
    public void exception(IOException e, String requestUrl) {

    }

    /*public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private List<String> datas;
        private LayoutInflater inflater;

        public RecyclerAdapter(Context context, List<String> data) {
            super();
            inflater = LayoutInflater.from(context);
            datas = data;
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
            ChildViewHolder holder = (ChildViewHolder) viewHolder;
            holder.itemTv.setText(datas.get(position));
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewHolder, int position) {
            View view = inflater.inflate(R.layout.item__huodong_layout2, null);
            return new ChildViewHolder(view);
        }

    }

    public class ChildViewHolder extends RecyclerView.ViewHolder {
        public TextView itemTv;

        public ChildViewHolder(View view) {
            super(view);
            itemTv = (TextView) view;
        }

    }*/
}
