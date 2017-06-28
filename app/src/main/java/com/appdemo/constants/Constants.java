package com.appdemo.constants;

import com.qk.applibrary.util.CommonUtil;

/**
 * 作者：cuileikun
 * 时间：2017-6-13 13:22
 * 功能:demo常量类
 */
public class Constants {
    public static final int PAGE_SIZE = 10;//每页加载10条

    /**
     * 日常常量类
     */
    public static final class Log_Def {
        public static String LogDirectory = CommonUtil.getSDCardPath() + "/cuidemo/roomphoto/";
        public static String Api_Log_File = "house_api_log.txt";
        public static String Error_Log_File = "house_error_log.txt";
    }

    /**
     * 下载歌曲常量类
     */
    public static final class Song_Def {
        //下载歌曲的路径
        public static String DOWN_SONGS_SAVE_PATH = com.qk.applibrary.util.CommonUtil.getSDCardPath() + "/cuidemo/SONGS";

        //下载歌曲的路径
        public static String DOWN_SONGS_UPZIP_PATH = com.qk.applibrary.util.CommonUtil.getSDCardPath() + "/cuidemo/upzip";

        //下载歌曲的歌曲名字存放
        public static String DOWN_SONGS_SAVE_NAME = "songs.zip";
    }


}
