package com.appdemo.base;

import android.app.ProgressDialog;

/**
 * 作者：popular cui
 * 时间：2017/6/7 17:00
 * 功能:碎片基类activity
 */

public abstract class HouseResourceBaseFragmentActivity extends com.qk.applibrary.view.BaseFragmentActivity {
    private ProgressDialog loadingProgressDialog;

    /**
     * 打开加载框,用于调用接口弹出的对话框
     *
     * @param title
     * @param message
     * @return
     */
    @Override
    public ProgressDialog showProgressDialog(String title, String message) {
        try {
            if (isFinishing() == false) {
                if (loadingProgressDialog == null) {
                    loadingProgressDialog = ProgressDialog.show(this, title,
                            message);

                    loadingProgressDialog.setCancelable(false);
                    loadingProgressDialog.setCanceledOnTouchOutside(false);
                } else {
                    loadingProgressDialog.setTitle(title);
                    loadingProgressDialog.setMessage(message);
                    if (loadingProgressDialog.isShowing() == false) {
                        loadingProgressDialog.show();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return loadingProgressDialog;
    }

    @Override
    public void dissmissProgressDialog() {
        try {
            if (isFinishing() == false && loadingProgressDialog != null) {
                loadingProgressDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
