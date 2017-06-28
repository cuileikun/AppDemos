package com.qk.applibrary.listener;

/**
 * Created by acer on 2016-5-12.
 * 调用api回调接口
 */
public interface ApiInterface {
    /**
     * 请求成功
     * @param result 后台返回来的json
     */
   void requestFinished(String result) ;

    /**
     * 请求失败
     * @param error 失败异常
     * @param content 错误信息
     */
   void requestFailed(Throwable error, String content) ;
}
