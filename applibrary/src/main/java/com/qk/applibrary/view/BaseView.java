package com.qk.applibrary.view;

import android.app.ProgressDialog;

/**
 * 作者：zhoubenhua
 * 时间：2017-2-8 15:07
 * 功能:
 */
public interface BaseView {
    /**
     * 打开加载框,用于调用接口弹出的对话框
     * @param title
     * @param message
     * @return
     */
    public abstract ProgressDialog openProgressDialog(String title, String message);

    /**
     * 关闭对话框
     */
    public abstract void closeProgressDialog();
}
