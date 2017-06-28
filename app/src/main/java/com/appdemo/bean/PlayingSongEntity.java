package com.appdemo.bean;

/**
 * Created by popular_cui on 17/06/19.
 */

public class PlayingSongEntity {

    private String payingSongName;

    private String payingSongPath;

    private String payingSongToWho;

    public String getPayingSongName() {
        return payingSongName;
    }

    public void setPayingSongName(String payingSongName) {
        this.payingSongName = payingSongName;
    }

    public String getPayingSongPath() {
        return payingSongPath;
    }

    public void setPayingSongPath(String payingSongPath) {
        this.payingSongPath = payingSongPath;
    }

    public String getPayingSongToWho() {
        return payingSongToWho;
    }

    public void setPayingSongToWho(String payingSongToWho) {
        this.payingSongToWho = payingSongToWho;
    }

    @Override
    public String toString() {
        return "PlayingSongEntity{" +
                "payingSongName='" + payingSongName + '\'' +
                ", payingSongPath='" + payingSongPath + '\'' +
                ", payingSongToWho='" + payingSongToWho + '\'' +
                '}';
    }
}
