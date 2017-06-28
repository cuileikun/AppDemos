package com.qk.safe;

/**
 * Created by acer on 2015-12-7.
 */
public class Safe {
    static {
        System.loadLibrary("safe");
    }
    public native String getSendLogFileEmailAccount();//获取发送日志文件邮箱账号
    public native String getReceiverLogFileEmailAccount();//获取接收日志文件邮箱账号

    /**
     * 获取阿里云accessKeyId
     * @return
     */
    public native String getAliyuncsAccessKeyId();
    /**
     * 获取阿里云accessKeySecret
     * @return
     */
    public native String getAliyuncsAccessKeySecret();
    /**
     * 获取阿里云OSS域名
     * @return
     */
    public native String getAliyuncsOosDomain();

    /**
     * 获取阿里云OSS房管员bucket
     * @return
     */
    public native String getAliyuncsOosHousekeeperBucket();
    /**
     * 获取阿里云OSS工地锁bucket
     * @return
     */
    public native String getAliyuncsOosSiteLockBucket();
    /**
     * 获取阿里云OSS青客宝bucket
     * @return
     */
    public native String getAliyuncsQkPayBucket();
    /**
     * 获取阿里云OSS青客测距仪
     * @return
     */
    public native String getAliyuncsQkRangefinderBucket();
    /**
     * 获取阿里云OSS青客在线
     * @return
     */
    public native String getAliyuncsQkOnlineBucket();
    /**
     * 获取阿里云OSS青客打扫
     * @return
     */
    public native String getAliyuncsQkBreamBucket();

    /**
     * 获取阿里云OSS青客远程验收
     * @return
     */
    public native String getAliyuncsQkRemoteaccpetanceBucket();
    /**
     * 获取阿里云OSS青客会员
     * @return
     */
    public native String getAliyuncsQkHuiyuanBucket();
    /**
     * 获取阿里云OSS智能公章
     * @return
     */
    public native String getAliyuncsSmartsealBucket();

}
