package com.qk.applibrary.api;

/**
 * 作者：zhoubenhua
 * 时间：2017-3-23 13:31
 * 功能:后台服务器地址
 */
public class BaseConnect{
    private String apiUrl;
    private String host;
    private String h5Url;


    public String getH5Url() {
        return h5Url;
    }

    public void setH5Url(String h5Url) {
        this.h5Url = h5Url;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }
}
