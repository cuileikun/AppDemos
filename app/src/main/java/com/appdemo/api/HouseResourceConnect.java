package com.appdemo.api;

import com.qk.applibrary.api.BaseConnect;

/**
 * 作者：popular cui
 * 时间：2017/6/19 16:38
 * 功能:房源采购后台服务器地址
 */
public class HouseResourceConnect extends BaseConnect {
    private String apiRankUrl;
    private String apiSongUrl;

    public String getApiSongUrl() {
        return apiSongUrl;
    }

    public void setApiSongUrl(String apiSongUrl) {
        this.apiSongUrl = apiSongUrl;
    }

    public String getApiRankUrl() {
        return apiRankUrl;
    }

    public void setApiRankUrl(String apiRankUrl) {
        this.apiRankUrl = apiRankUrl;
    }
}
