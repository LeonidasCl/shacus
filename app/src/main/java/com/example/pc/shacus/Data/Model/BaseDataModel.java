package com.example.pc.shacus.Data.Model;

import android.util.Log;

import com.example.pc.shacus.APP;
import com.example.pc.shacus.Data.Cache.ACache;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.Serializable;

/**
 * Created by pc on 2016/3/7.
 */
public abstract class BaseDataModel implements Serializable {

    private String objectID;

    BaseDataModel(){
        this.mCache = ACache.get(APP.context);
    }

    private ACache mCache;

    /*这个方法完成自定义数据模型的缓存*/
    public boolean save(JSONObject jsonObject,int period){
        if (objectID.equals(null))
        {
            Log.i("Cache Error","该对象未设置唯一标识...是否忘记调用setObjectID()?");
            return false;
        }
        String classname=getClass().getName();
        if(mCache!=null){
            mCache.put(classname+getObjectID(), jsonObject,period);
            return true;
        }
        return false;
    }

    /*这个方法完成自定义数据模型的缓存*/
    public boolean save(BaseDataModel Object,int period){
        if (objectID.equals(null))
        {
            Log.i("Cache Error","该对象未设置唯一标识...是否忘记调用setObjectID()?");
            return false;
        }
        String classname=getClass().getName();
        if(mCache!=null){
            mCache.put(classname+getObjectID(), Object,period);
            return true;
        }
        return false;
    }

    public boolean delete(){
       return mCache.remove(getClass().getName());
    }

    /*获取子类数据模型的JSON包
    * 返回的JSON中包含该模型的所有键值对
    * 这个方法不能被外部调用
    * 开发者获取/建立数据模型后
    * 在本类的公共方法getString中直接按键名获取相应值
    * */
    private JSONObject getJson(){
        JSONObject jsonObject = mCache.getAsJSONObject(getClass().getName()+getObjectID());
        if (jsonObject==null)
        {}
        return jsonObject;
    }

    /*对建立、获取的数据模型实例
    * 直接调用这个函数
    * 可以获得相应键值
    * */
    public String getString(String varname) {
        JSONObject jsonObject=getJson();
        if(jsonObject!=null)
        {
            String str= null;
            try {
                str = jsonObject.getString(varname);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return str;
        }
        else return varname+" is null";
    }

    public String getObjectID() {
        return objectID;
    }

    public void setObjectID(String objectID) {
        this.objectID = objectID;
    }
}
