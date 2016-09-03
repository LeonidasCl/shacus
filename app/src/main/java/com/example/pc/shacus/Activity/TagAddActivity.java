package com.example.pc.shacus.Activity;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.pc.shacus.R;

public class TagAddActivity extends Activity {

    private ListView lv;
    private SearchView search;
    private Button selected;
    private int type;//父活动的种类，1为约拍，2为活动

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_add);
        setTitle("标签添加与选择");
        lv = (ListView) findViewById(R.id.a_search_list);
        String[] mStrings = {"ad","dffa","uyiu","rqer","qwgt","afrgb","rtyr"};
        search = (SearchView) findViewById(R.id.a_search_tag);
        search.setIconifiedByDefault(false);
        search.setSubmitButtonEnabled(false);
        final ArrayAdapter adapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, mStrings);
        lv.setAdapter(adapter);
        lv.setTextFilterEnabled(true);
        //暂时用这种办法让listview不占用空间
        adapter.getFilter().filter("clear");
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 用户输入字符时激发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                    lv.clearTextFilter();
                } else {
                    adapter.getFilter().filter(newText);
                    //lv.setFilterText(newText);
                }
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String str = (String) lv.getItemAtPosition(position);
                search.setQuery(str, false);
                //暂时用这种办法让listview不占用空间
                adapter.getFilter().filter("clear");
            }
        });

        selected=(Button)findViewById(R.id.a_btn_selected_tag);
        selected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tag = search.getQuery().toString();
                Intent intent = new Intent(getApplicationContext(), CreateYuePaiActivity.class);
                intent.putExtra("tag", tag);
                intent.putExtra("type", type==1?"tagAdd1":"tagAdd2");
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                finish();
                return;
            }
        });

        setType(getIntent().getIntExtra("type",0));
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
