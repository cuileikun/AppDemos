package com.qk.applibrary.view;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 支持v4 碎片基类
 * Created by acer on 2015-10-20.
 */
public abstract class BaseV4Fragment extends Fragment {
    protected ProgressDialog loadingProgressDialog; //请求网络弹出加载框

    /**
     * 打开加载框,用于调用接口弹出的对话框
     * @param title
     * @param message
     * @return
     */
    public abstract ProgressDialog showProgressDialog(String title, String message);

    /**
     * 关闭对话框
     */
    public abstract void dissmissProgressDialog();

    /**
     * 初始化控件
     */
    public abstract void initViews(View view);

    /**
     * 初始化数据
     */
    public abstract void initData() ;

    /**
     * 添加事件
     */
    public abstract void addListeners();

    /**
     * 获取布局文件ID
     * @return
     */
    public abstract int getLayoutId();

    protected LayoutInflater mInflater;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.mInflater = inflater;
        View view = this.mInflater.inflate(getLayoutId(),container,false);
        initViews(view);
        initData();
        addListeners();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    protected View inflateView(int resId) {
        return this.mInflater.inflate(resId, null);
    }

    public boolean onBackPressed() {
        return false;
    }


}
