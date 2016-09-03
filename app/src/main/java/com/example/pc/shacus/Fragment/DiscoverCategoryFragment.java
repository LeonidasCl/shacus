package com.example.pc.shacus.Fragment;


import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pc.shacus.Activity.SignInActivity;
import com.example.pc.shacus.R;
import com.example.pc.shacus.View.MyScrollView.MasonryAdapter;
import com.example.pc.shacus.View.MyScrollView.Product;
import com.example.pc.shacus.View.MyScrollView.RecycleItemClickListener;
import com.example.pc.shacus.View.MyScrollView.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DiscoverCategoryFragment extends Fragment {
    private List<Product> productList;
    RecyclerView recyclerView;
    public DiscoverCategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_discover_category, container, false);
       // initView();

        return view;
    }

    private void initData1() {
        productList = new ArrayList<Product>();
        Product p1 = new Product(R.drawable.a, R.mipmap.ic_launcher, "我是照片1");
        productList.add(p1);
        Product p2 = new Product(R.mipmap.ic_launcher, R.mipmap.ic_launcher, "我是照片2");
        productList.add(p2);
        Product p3 = new Product(R.mipmap.ic_launcher, R.mipmap.ic_launcher, "我是照片3");
        productList.add(p3);
        Product p4 = new Product(R.mipmap.ic_launcher, R.mipmap.ic_launcher, "我是照片4");
        productList.add(p4);
        Product p5 = new Product(R.mipmap.ic_launcher, R.mipmap.ic_launcher, "我是照片5");
        productList.add(p5);
        Product p6 = new Product(R.mipmap.ic_launcher, R.mipmap.ic_launcher, "我是照片6");
        productList.add(p6);
        Product p7 = new Product(R.drawable.a, R.mipmap.ic_launcher, "我是照片7");
        productList.add(p7);
        Product p8 = new Product(R.mipmap.personal_default_big_photo, R.mipmap.ic_launcher, "我是照片8");
        productList.add(p8);
        Product p9 = new Product(R.drawable.bg_circle_pressed1, R.mipmap.ic_launcher, "我是照片9");
        productList.add(p9);
        Product p10 = new Product(R.mipmap.ic_launcher, R.mipmap.ic_launcher, "我是照片10");
        productList.add(p10);
        Product p11 = new Product(R.mipmap.ic_launcher, R.mipmap.ic_launcher, "我是照片11");
        productList.add(p11);

    }

    private void initView() {

        recyclerView = (RecyclerView)recyclerView.findViewById(R.id.recyclerView1);
        //设置layoutManager
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        //设置adapter
        initData1();
        RecycleItemClickListener itemClickListener = new RecycleItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
//                Log.e("position","="+position);
//                Toast.makeText(MainActivity.this, productList.get(position).getTitle(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setClass(getActivity(), SignInActivity.class);
                startActivity(intent);
            }
        };
        MasonryAdapter adapter = new MasonryAdapter(productList, itemClickListener);
        recyclerView.setAdapter(adapter);
        SpacesItemDecoration decoration = new SpacesItemDecoration(16);
        recyclerView.addItemDecoration(decoration);
    }
}
