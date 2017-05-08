package com.example.pc.shacus.Network;

import org.json.JSONException;

import java.io.IOException;

/**
 *  项目中用到的回调接口
 */
public class NetworkCallbackInterface {

    /**
     * 网络请求回调接口
     */
    public interface NetRequestIterface {
        void requestFinish(String result, String requestUrl) throws JSONException;
        void exception(IOException e, String requestUrl);
    }
    /**
     * 点击退出查看添加图片大图回调接口
     */
    public interface OnSingleTapDismissBigPhotoListener {
        void onDismissBigPhoto();
    }

}
