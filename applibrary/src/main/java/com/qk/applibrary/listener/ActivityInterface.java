package com.qk.applibrary.listener;

/**
 * Created by acer on 2016-5-12.
 * activity接口
 */
public interface ActivityInterface {
    /**
     * 初始化组件
     */
    public void initViews();

    /**
     * 初始化数据
     */
    public void initData();

    /**
     * 给控件添加事件
     */
    public void addListeners();

    /**
     * 获取布局id
     * @return
     */
    public int getLayoutId();

}
