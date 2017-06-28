package com.qk.applibrary.listener;

import android.graphics.Bitmap;

/**
 * Created by acer on 2016-9-11.
 * 下载图片接口
 */
public interface DownloadPicInterface {
    /**
     * 下载成功
     * @param bitmap
     */
    public void downloadSucess(Bitmap bitmap);
    /**
     * 下载失败
     */
    public void downloadFailed();
}
