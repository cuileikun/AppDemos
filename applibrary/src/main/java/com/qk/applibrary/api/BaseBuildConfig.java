package com.qk.applibrary.api;

/**
 * 切换环境基类。所有业务模块必须继承实现该类，完成环境切换。并在加载业务模块的时候，完成初始化操作。
 * @zhoubenhua
 */
public abstract class BaseBuildConfig {
       /**
     * 切换为正式环境
     */
    public abstract void switchOfficial() ;

    /**
     * 切换为测试环境
     */
    public abstract void switchTest () ;

    /**
     * 切换为仿真环境
     */
    public abstract void switchSimulation () ;

}
