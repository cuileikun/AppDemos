package com.qk.applibrary.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 作者：zhoubenhua
 * 时间：2016-10-31 11:11
 * 功能:网络工具
 */
public class NetworkUtil {
    /**
     * 检查是否有网络
     *
     * @param mContext
     * @return
     */
    public static boolean checkNetwork(Context mContext) {
        ConnectivityManager cwjManager = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cwjManager.getActiveNetworkInfo();
        boolean flag = info != null && info.isAvailable() ? true : false;
        if (!flag) {
            CommonUtil.sendToast(mContext, "请检查你的网络");
        }
        return flag;
    }
}
