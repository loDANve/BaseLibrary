package com.chanxa.wnb.bean;

/**
 * Created by CHANXA on 2015/1/8.
 * 提成
 */
public class Commission {
    private String mark;//标识
    private String storeMark;//店面标识
    private String changeQuota;//变化额度
    private String reMark;//备注
    private String time;//时间

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getStoreMark() {
        return storeMark;
    }

    public void setStoreMark(String storeMark) {
        this.storeMark = storeMark;
    }

    public String getChangeQuota() {
        return changeQuota;
    }

    public void setChangeQuota(String changeQuota) {
        this.changeQuota = changeQuota;
    }

    public String getReMark() {
        return reMark;
    }

    public void setReMark(String reMark) {
        this.reMark = reMark;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
