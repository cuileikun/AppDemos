package com.appdemo.dialog;

import com.appdemo.bean.RealTimeRankEntity;

import java.util.List;

public interface RealTimeChangeListener {

    //关闭弹窗
    void close();

    //开启弹窗
    void start(List<RealTimeRankEntity> list);

    //有弹窗 更新弹窗
    void reStart(List<RealTimeRankEntity> list);

}

