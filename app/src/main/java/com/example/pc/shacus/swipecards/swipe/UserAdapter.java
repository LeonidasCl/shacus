package com.example.pc.shacus.swipecards.swipe;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.os.Message;
import android.support.annotation.DrawableRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pc.shacus.Activity.OtherUserActivity;
import com.example.pc.shacus.Activity.OtherUserDisplayActivity;
import com.example.pc.shacus.Data.Model.RecommandModel;
import com.example.pc.shacus.Util.CommonUtils;
import com.example.pc.shacus.View.Custom.RoundImageView;
import com.example.pc.shacus.swipecards.util.CardEntity;
import com.example.pc.shacus.swipecards.util.ImageLoaderHandler;
import com.example.pc.shacus.swipecards.view.CardImageView;
import com.example.pc.shacus.swipecards.view.CardLayout;
import com.example.pc.shacus.swipecards.view.SwipeIndicatorView;
import java.util.ArrayList;
import android.os.Handler;
import java.util.logging.LogRecord;

import butterknife.ButterKnife;
import com.example.pc.shacus.R;

import static com.example.pc.shacus.R.drawable.holder;
import static com.example.pc.shacus.R.drawable.sex_man;

/**
 * card适配器
 *
 * @zc
 */
public class UserAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<RecommandModel> mList;
    private Handler mHandler;
    //static RoundImageView selfMainView;
    Bitmap bitmap;




    public UserAdapter(Context context, ArrayList<RecommandModel> list) {
        mInflater = LayoutInflater.from(context);
        this.mList = list;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mList.size();
    }


    @Override
    public RecommandModel getItem(int position) {
        if(position >= getCount()) return mList.get(position % getCount());
        else return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mList.get(position).hashCode();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.swipe_fling_item, parent, false);
            ViewHolder holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        final ViewHolder holder = (ViewHolder) convertView.getTag();


        final RecommandModel recommandModel = ((RecommandModel) getItem(position));
        holder.likeIndicator.reset();
        holder.unLikeIndicator.reset();
        holder.nameView.setText(recommandModel.getUserpublish().getNickName());
        holder.ageView.setText(recommandModel.getUserpublish().getAge());
        if(recommandModel.getUserpublish().getSex().equals("0")){//女
            holder.sexView.setBackgroundResource(R.drawable.sex_woman);
        }
        else{
            holder.sexView.setBackgroundResource((R.drawable.sex_man));
        }

        //开启一个线程来获取图像，获取成功之后传到handler中显示UI
//        new Thread(new Runnable(){
//            @Override
//            public void run(){
//                bitmap=CommonUtils.getHttpBitmap(recommandModel.getHeadimg());
//                mHandler.sendEmptyMessage(0);
//            }
//        }).start();
//
//        mHandler = new Handler() {  //放在开启线程的前面
//
//            public void handleMessage(android.os.Message msg) {
//                selfMainView.setImageBitmap(bitmap);
//            }
//
//        };


//        Bitmap bitmap = CommonUtils.getHttpBitmap(recommandModel.getHeadimg());
//        holder.selfMainView.setImageBitmap(bitmap);
        holder.img.reset();
        holder.img.setUser(recommandModel);
        holder.selfMainView.reset();
        holder.selfMainView.setUser(recommandModel);
        holder.selfMainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, OtherUserDisplayActivity.class);
                intent.putExtra("id", recommandModel.getUserpublish().getId());
                mContext.startActivity(intent);
            }
        });
        ImageLoaderHandler.get().loadCardImage((Activity) mContext, holder.img, null, recommandModel.getUcFirstimg(), false);
        ImageLoaderHandler.get().loadCardRoundedImage((Activity) mContext, holder.selfMainView, null, recommandModel.getHeadimg(), false);
        return convertView;
    }


    static class ViewHolder {
        CardLayout cardLayout;
        CardImageView img;
        TextView nameView;
        ImageView sexView;
        TextView ageView;
        SwipeIndicatorView likeIndicator;
        SwipeIndicatorView unLikeIndicator;
//        TextView mFriendCountTv;
//        TextView mInterestCountTv;
        ViewGroup mBottomLayout;
        CardImageView selfMainView;


        ViewHolder(View rootView) {
            cardLayout = (CardLayout) rootView;
            img = ButterKnife.findById(rootView, R.id.item_img);
            nameView = ButterKnife.findById(rootView, R.id.item_name);
            //这里要改成性别
            sexView = ButterKnife.findById(rootView, R.id.item_sex);
            ageView = ButterKnife.findById(rootView, R.id.item_age);
            likeIndicator = ButterKnife.findById(rootView, R.id.item_swipe_like_indicator);
            unLikeIndicator = ButterKnife.findById(rootView, R.id.item_swipe_unlike_indicator);
            selfMainView = ButterKnife.findById(rootView, R.id.self_main);
            //去掉点赞的人
            //mFriendCountTv = ButterKnife.findById(rootView, R.id.item_friend_count);
            //mInterestCountTv = ButterKnife.findById(rootView, R.id.item_interest_count);
            mBottomLayout = ButterKnife.findById(rootView, R.id.item_bottom_layout);
        }


        @Override
        public String toString() {
            return "[Card:" + nameView.getText() + "]";
        }
    }

}
