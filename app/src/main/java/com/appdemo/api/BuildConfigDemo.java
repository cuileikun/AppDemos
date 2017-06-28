package com.appdemo.api;

import com.qk.applibrary.api.BaseBuildConfig;
/**
 * 作者：popular cui
 * 时间：2017/6/7 13:40
 * 功能:配置各个环境服务器地址
 */
public class BuildConfigDemo extends BaseBuildConfig {
    private HouseResourceConnect connect;
    private static BuildConfigDemo instance;
    private int environmentType;//环境类型 1是正式 2是仿真 3是测试 4 云仿真 5 本地

    public static BuildConfigDemo getInstance() {
        if (instance == null) {
            instance = new BuildConfigDemo();
        }
        return instance;
    }

    private BuildConfigDemo() {
        /**
         * 默认切换到测试环境
         */
        environmentType = 3;
        connect = new HouseResourceConnect();
        changeEnvironment();
    }

    public HouseResourceConnect getConnect() {
        return connect;
    }

    /**
     * 切换环境
     */
    private void changeEnvironment() {
        switch (environmentType) {
            case 1:
                /**
                 * 切换正式环境
                 */
                switchOfficial();
                break;
            case 2:
                /**
                 * 切换仿真环境
                 */
                switchSimulation();
                break;
            case 3:
                /**
                 * 切换测试环境
                 */
                switchTest();
                break;
            case 4:
                /**
                 * 切换云仿真环境
                 */
                switchCloudSimulation();
                break;
            case 5:
                /**
                 * 切换本地环境
                 */
                switchLocal();

                break;
        }
    }


    /**
     * 切换正式环境
     */
    @Override
    public void switchOfficial() {
        connect.setApiSongUrl("http://58.215.175.244:8080/mdk");//房管员app 下载歌曲url
        connect.setApiUrl("http://fyapp.qk365.com");
        connect.setH5Url("http://fy2.qk365.com/admin/Houseinfo/showHouseinfo.do?houseid=");
    }

    /**
     * 切换测试环境
     */
    @Override
    public void switchTest() {
        connect.setApiSongUrl("http://192.168.1.72:89/mdk");//房管员app 下载歌曲url
        connect.setApiRankUrl ("http://192.168.4.19:8899/qk-web_interface");//房源 肖龙海本地电脑
        connect.setApiUrl("http://192.168.1.236:58806/qk-web_interface");
        connect.setH5Url("http://192.168.1.215:51006/qk-web/admin/Houseinfo/showHouseinfo.do?houseid=");
    }

    /**
     * 切换仿真环境
     */
    @Override
    public void switchSimulation() {
        connect.setApiSongUrl("http://139.219.198.123:8082/mdk");//房管员app 下载歌曲url
        connect.setApiUrl("http://192.168.1.15:58806");
        connect.setH5Url("http://192.168.1.15:51006/admin/Houseinfo/showHouseinfo.do?houseid=");
    }

    /**
     * 切换云仿真环境
     */
    private void switchCloudSimulation() {
        connect.setApiSongUrl("/mdk");//房管员app 下载歌曲url
//        connect.setApiUrl("http://139.219.236.183:58806");
//        connect.setH5Url("http://139.219.236.183:51006/admin/Houseinfo/showHouseinfo.do?houseid=");
        connect.setApiUrl("http://qingkesim.chinacloudapp.cn:58806");
        connect.setH5Url("http://qingkesim.chinacloudapp.cn:51006/admin/Houseinfo/showHouseinfo.do?houseid=");

    }

    /**
     * 切换本地环境
     */
    private void switchLocal() {
        connect.setApiSongUrl("http://192.168.1.74:41111/mdk");//房管员app 下载歌曲url
        connect.setApiUrl("http://192.168.2.224:9999/qk-web_interface");
        connect.setH5Url("http://192.168.2.224:8080/qk-web_interface/");

    }

}
