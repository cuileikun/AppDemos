package com.appdemo.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by popular_cui on 17/06/19.
 * 实时榜初期显示接口列表
 */
public class RealTimeRankList implements Serializable {
    private List<RealTimeRankEntity> realTimeRankList;
    //add by clk on 从服务器获得时间(年月日)
    private String currentDay;

    //房源新接口add on
    private List<RealTimeRankEntity> list;

    public List<RealTimeRankEntity> getList() {
        return list;
    }

    public void setList(List<RealTimeRankEntity> list) {
        this.list = list;
    }

    private String curDate;

    public String getCurDate() {
        return curDate;
    }

    public void setCurDate(String curDate) {
        this.curDate = curDate;
    }

    //房源新接口add off
    public String getCurrentDay() {
        return currentDay;
    }

    public void setCurrentDay(String currentDay) {
        this.currentDay = currentDay;
    }

    //add by clk off 从服务器获得时间(年月日)
    public List<RealTimeRankEntity> getRealTimeRankList() {
        return realTimeRankList;
    }

    public void setRealTimeRankList(List<RealTimeRankEntity> realTimeRankList) {
        this.realTimeRankList = realTimeRankList;
    }
}
