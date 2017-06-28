package com.qk.applibrary.listener;

/**
 * 作者：zhoubenhua
 * 时间：2017-3-22 14:46
 * 功能:视频压缩进度接口
 */
public interface VideoCompressionProgressListener {
    /**
     * 压缩成功
     */
    public void onCompressionSuccess(String sucess);

    /**
     * 压缩失败
     * @param reason 失败原因
     */
    public void onCompressionFail(String reason);

    /**
     * 压缩进度
     * @param message 进度
     */
    public void onCompressionProgress(String message);
}
