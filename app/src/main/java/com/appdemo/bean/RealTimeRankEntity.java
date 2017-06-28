package com.appdemo.bean;

import java.io.Serializable;

/**
 * Created by popular_cui on 17/06/19.
 * 实时榜初期显示实体类
 */
public class RealTimeRankEntity implements Serializable {

    private String userName;
    //播放的歌曲名称
    private String songName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    //add by clk on

    private String purchaserName;//采购员姓名  方亮
    private String departmentName;//部门名称   第一采购中心
    private String houseAddress;//房屋地址 新华悦都
    private String signTime;//签约时间 09:56
    private Boolean isNewSign;//是否新签
    private int ordinal;//序号 15

    public String getPurchaserName() {
        return purchaserName;
    }

    public void setPurchaserName(String purchaserName) {
        this.purchaserName = purchaserName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getHouseAddress() {
        return houseAddress;
    }

    public void setHouseAddress(String houseAddress) {
        this.houseAddress = houseAddress;
    }

    public String getSignTime() {
        return signTime;
    }

    public void setSignTime(String signTime) {
        this.signTime = signTime;
    }

    public boolean getIsNewSign() {
        return isNewSign;
    }

    public void setIsNewSign(boolean newSign) {
        isNewSign = newSign;
    }

    public int getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(int ordinal) {
        this.ordinal = ordinal;
    }

    @Override
    public String toString() {
        return "RealTimeRankEntity{" +
                "userName='" + userName + '\'' +
                ", songName='" + songName + '\'' +
                ", purchaserName='" + purchaserName + '\'' +
                ", departmentName='" + departmentName + '\'' +
                ", houseAddress='" + houseAddress + '\'' +
                ", signTime='" + signTime + '\'' +
                ", isNewSign=" + isNewSign +
                ", ordinal=" + ordinal +
                '}';
    }
}
