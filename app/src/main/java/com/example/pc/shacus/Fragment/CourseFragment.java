package com.example.pc.shacus.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.pc.shacus.APP;
import com.example.pc.shacus.Activity.CourseWebViewActivity;
import com.example.pc.shacus.Data.Cache.ACache;
import com.example.pc.shacus.Data.Model.CourseHomepageModel;
import com.example.pc.shacus.Data.Model.CourseHomepageRecommend;
import com.example.pc.shacus.Data.Model.LoginDataModel;
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

//LQ
//9.5
public class CourseFragment extends Fragment implements View.OnClickListener,NetworkCallbackInterface.NetRequestIterface{
    //推荐列表
    private LinearLayout recommendLine;
    //推荐图//六个小标签
    private TextView recommendText;
    private ImageView recommendImage;
    private List<TextView> tagsT=new ArrayList<>();
    private List<ImageView>tag=new ArrayList<>();
    private ImageButton myCourseImage;
    private NetRequest netRequest;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
                if(msg.what==StatusCode.REQUEST_COURSE_HOMEPAGE_SUCCESS){
                    //修改推荐图
                    LayoutInflater mInflater=LayoutInflater.from(CourseFragment.this.getActivity());
                    recommendLine.removeAllViews();
                    for (int i = 0; i < courseHomepageRecommend.size(); i++) {
                        View view=mInflater.inflate(R.layout.item_fragment_course,null);
                        recommendText= (TextView) view.findViewById(R.id.recommendCourse_Text);
                        recommendImage= (ImageView) view.findViewById(R.id.recommendCourse_image);
                        recommendText.setText(courseHomepageRecommend.get(i).getCtitle());
                        recommendImage.setOnClickListener(CourseFragment.this);
                        Glide.with(CourseFragment.this.getActivity())
                                .load(courseHomepageRecommend.get(i).getCimageUrl()).centerCrop()
                                .placeholder(R.drawable.holder)
                                .error(R.drawable.loading_error)
                                .into(recommendImage);
                        recommendLine.addView(view);
                    }//修改更多类型图
                    for (int i = 0; i < courseHomepageModel.size(); i++) {
                        ImageView image=tag.get(i);
                        Glide.with(CourseFragment.this.getActivity())
                                .load(courseHomepageModel.get(i).getCTimageurl()).centerCrop()
                                .placeholder(R.drawable.holder)
                                .error(R.drawable.loading_error)
                                .into(image);
                        image.setOnClickListener(CourseFragment.this);
                        tagsT.get(i).setText(courseHomepageModel.get(i).getCTname());
                    }
            }else if(msg.what==StatusCode.REQUEST_COURSE_HOMEPAGE_TAG_SUCCESS){

                }
        }
    };
    private List<CourseHomepageModel> courseHomepageModel;
    private List<CourseHomepageRecommend> courseHomepageRecommend;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //9.5缺少布局文件
        //9.6已更正
        View view=inflater.inflate(R.layout.frag_course, container, false);
        recommendLine= (LinearLayout) view.findViewById(R.id.recommendCourse_Linear);
        tag.add((ImageView) view.findViewById(R.id.btn_moreType1));
        tag.add((ImageView) view.findViewById(R.id.btn_moreType2));
        tag.add((ImageView) view.findViewById(R.id.btn_moreType3));
        tag.add((ImageView) view.findViewById(R.id.btn_moreType4));
        tag.add((ImageView) view.findViewById(R.id.btn_moreType5));
        tag.add((ImageView) view.findViewById(R.id.btn_moreType6));
        tagsT.add((TextView) view.findViewById(R.id.text_tag1));
        tagsT.add((TextView) view.findViewById(R.id.text_tag2));
        tagsT.add((TextView) view.findViewById(R.id.text_tag3));
        tagsT.add((TextView) view.findViewById(R.id.text_tag4));
        tagsT.add((TextView) view.findViewById(R.id.text_tag5));
        tagsT.add((TextView) view.findViewById(R.id.text_tag6));
        netRequest=new NetRequest(CourseFragment.this,CourseFragment.this.getActivity());

       // initImage();//添加初始图片，避免加载时破坏布局

        LayoutInflater itemInflater=LayoutInflater.from(CourseFragment.this.getActivity());
        View itemView=itemInflater.inflate(R.layout.item_fragment_course, null);
        recommendLine.addView(itemView);

        initNet();

        return view;
    }

    private void initImage() {
        LayoutInflater mInflater=LayoutInflater.from(CourseFragment.this.getActivity());

        View view=mInflater.inflate(R.layout.item_fragment_course, null);
        TextView initText= (TextView) view.findViewById(R.id.recommendCourse_Text);
        ImageView initImageView= (ImageView) view.findViewById(R.id.recommendCourse_image);
        initText.setText("Loading....");
        initImageView.setImageResource(R.drawable.ic_launcher);
        recommendLine.addView(view);
    }

    private void initNet() {
        ACache aCache=ACache.get(APP.context);
        LoginDataModel model= (LoginDataModel) aCache.getAsObject("loginModel");
        HashMap map=new HashMap();
        map.put("uid",model.getUserModel().getId());
        map.put("authkey",model.getUserModel().getAuth_key());
        map.put("type","11001");
        netRequest.httpRequest(map, CommonUrl.courseHomePage);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.recommendCourse_image:
                Intent intent=new Intent(CourseFragment.this.getActivity(), CourseWebViewActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_moreType1:
                break;
            case R.id.btn_moreType2:
                break;
            case R.id.btn_moreType3:
                break;
            case R.id.btn_moreType4:
                break;
            case R.id.btn_moreType5:
                break;
            case R.id.btn_moreType6:
                break;
            case R.id.btn_moreRecommend:
                break;
        }
    }

    @Override
    public void requestFinish(String result, String requestUrl) throws JSONException {
        if (requestUrl.equals(CommonUrl.courseHomePage)){
            JSONObject json=new JSONObject(result);
            String code=json.getString("code");
            Log.d("LQQQQQQQ", code);
            if(code.equals("11010")){
                courseHomepageModel=new ArrayList<>();
                courseHomepageRecommend=new ArrayList<>();
                JSONObject obj=json.getJSONObject("contents");
                JSONArray array=obj.getJSONArray("course");
                JSONArray tagArray=obj.getJSONArray("tag");
                Gson gson=new Gson();
                for (int i = 0; i < array.length(); i++) {
                    JSONObject info=array.getJSONObject(i);
                    CourseHomepageRecommend temp=gson.fromJson(info.toString(), CourseHomepageRecommend.class);
                    courseHomepageRecommend.add(temp);
                    Log.d("LQQQQQQ", "requestFinish:image ");
                }
                for (int i = 0; i < tagArray.length(); i++) {
                    JSONObject info=tagArray.getJSONObject(i);
                    CourseHomepageModel temp=gson.fromJson(info.toString(), CourseHomepageModel.class);
                    courseHomepageModel.add(temp);
                    Log.d("LQQQQQ", "requestFinish:tag ");
                }
                Message msg=handler.obtainMessage();
                msg.what= StatusCode.REQUEST_COURSE_HOMEPAGE_SUCCESS;
                handler.sendMessage(msg);
            }
            else if(code.equals("11000")){
                Log.d("LQQQQQQQQQ", "请求失败11000: ");
            }
        }
    }

    @Override
    public void exception(IOException e, String requestUrl) {

    }
}
