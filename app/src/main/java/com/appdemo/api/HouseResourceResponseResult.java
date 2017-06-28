package com.appdemo.api;

import com.qk.applibrary.api.BaseResponseResult;

/**
 * 作者：popular cui
 * 时间：2017/6/7 16:37
 * 功能:服务器返回结果，继承自框架里面的 BaseResponseResult
 */
public class HouseResourceResponseResult extends BaseResponseResult {
    public int code;//200成功 否则失败
    public String message;//提示信息
    public String data;//服务器返回的数据
    public static int SUCESS_CODE = 0;
    public static int FAILED_CODE = 1;
    public static int FAILED_CODE2 = 2;
}
