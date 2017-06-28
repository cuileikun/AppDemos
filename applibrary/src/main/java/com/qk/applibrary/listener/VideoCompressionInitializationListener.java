package com.qk.applibrary.listener;

/**
 * 作者：zhoubenhua
 * 时间：2017-3-22 14:44
 * 功能:视频压缩初始化接口
 */
public interface VideoCompressionInitializationListener {
    /**
     * 初始化成功
     */
    public void oninitSuccess();

    /**
     * 初始化失败
     * @param reason 失败原因
     */
    public void onInitFail(String reason);
}
