package com.qk.applibrary.bean;

import com.qk.applibrary.listener.ResponseResultListener;

import java.util.HashMap;

/**
 * Created by acer on 2016-5-16.
 * 封装上传文件对像
 */
public class UploadFileBean {
    private String url;//连接地址
    private HashMap<String, Object> paramMap;//参数
    private HashMap<String,Object> headParamMap;//头部参数
    private int connectTimeOut;//连接超时
    private int readTimeOut;//请求超时时间
    private String fileParamterName;//文件参数名

    public String getFileParamterName() {
        return fileParamterName;
    }

    public void setFileParamterName(String fileParamterName) {
        this.fileParamterName = fileParamterName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public HashMap<String, Object> getParamMap() {
        return paramMap;
    }

    public void setParamMap(HashMap<String, Object> paramMap) {
        this.paramMap = paramMap;
    }

    public HashMap<String, Object> getHeadParamMap() {
        return headParamMap;
    }

    public void setHeadParamMap(HashMap<String, Object> headParamMap) {
        this.headParamMap = headParamMap;
    }

    public int getConnectTimeOut() {
        return connectTimeOut;
    }

    public void setConnectTimeOut(int connectTimeOut) {
        this.connectTimeOut = connectTimeOut;
    }

    public int getReadTimeOut() {
        return readTimeOut;
    }

    public void setReadTimeOut(int readTimeOut) {
        this.readTimeOut = readTimeOut;
    }

}
