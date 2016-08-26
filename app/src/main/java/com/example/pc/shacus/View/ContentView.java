package com.example.pc.shacus.View;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.example.pc.shacus.Adapter.HuodongItemAdapter;
import com.example.pc.shacus.Data.Model.HuoDongItemModel;
import com.example.pc.shacus.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 内容视图
 */
public class ContentView extends TouchMoveView {

    Context context;
    View parent;

    /*PtrClassicFrameLayout ptrClassicFrameLayout;
    RecyclerView mRecyclerView;
    private List<String> mData = new ArrayList<String>();
    private RecyclerAdapter adapter;
    private RecyclerAdapterWithHF mAdapter;
    Handler handler = new Handler();
    int page = 0;*/

    private SwipeRefreshLayout refreshLayout;
    private HuodongItemAdapter personAdapter;
    private ListView listView;
    private int bootCounter=0;
    private int maxRecords = 400;


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



        personAdapter = new HuodongItemAdapter(context,bootData(0));
        listView = (ListView) view.findViewById(R.id.huodong_list);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_huodong);
        listView.setAdapter(personAdapter);

        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                parent.onTouchEvent(event);
                return false;
            }
        });

        onScrollListener();
        onRefreshListener();

	}

    private void onRefreshListener() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);
                bootCounter = 0;
                personAdapter.refresh(bootData(0));
                personAdapter.notifyDataSetChanged();
                refreshLayout.setRefreshing(false);
            }
        });
    }

    private void onScrollListener() {
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount > totalItemCount - 2 && totalItemCount < maxRecords) {
                    personAdapter.add(bootData(0));
                    personAdapter.notifyDataSetChanged();
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

    private List<HuoDongItemModel> bootData(int Type){
        List<HuoDongItemModel> persons = new ArrayList<>();
        for(int i=bootCounter;i<bootCounter+20;i++){
            HuoDongItemModel huodong = new HuoDongItemModel();
            huodong.setDescribe("活动描述信息加载中...不得少于十五字不得多于一百五十字");
            huodong.setEndtime(new Date());
            huodong.setStarttime(new Date());
            huodong.setSetTime(new Date());
            huodong.setJoinNum((int) (Math.random() * 100)+1);
            huodong.setPraiseNum((int) (Math.random() * 100) + 1);
            huodong.setUsrName("用户" + i);
            huodong.setLocation("这是活动地点");
            huodong.setPrice(1234 * i);
            persons.add(huodong);
        }
        bootCounter+=20;
        return persons;
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
