package com.qk.applibrary.presenter;

import android.content.Context;

import java.lang.ref.WeakReference;

/**
 * Created by acer on 2016-9-19.
 * 控制器基类
 */
public abstract class BasePresenter<V, M> {
    /**
     * 内存不足时释放内存,避免内存泄露
     */
    protected V v;//视图,显示数据
    protected M m;//处理业务逻辑
    public Context mContext;

    /**
     * 关联view和model
     * @param view
     * @param model
     * @param context
     */
    public void attachView(V view,M model,Context context) {
        v = view;
        m = model;
        mContext = context;
    }

    /**
     * 用于在activity销毁时释放资源
     */
    public void onDettach(){
        if(null != v){
            v = null;
        }
        if(null != m){
            m = null;
        }
     };

}
