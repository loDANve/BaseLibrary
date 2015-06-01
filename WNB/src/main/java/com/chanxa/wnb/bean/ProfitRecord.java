package com.chanxa.wnb.bean;

/**
 * Created by CHANXA on 2014/12/26.
 */
public class ProfitRecord {

    private String storeMark;//店面标识
    private String canUserMoney;//可以储值
    private String yesterdayMoney;//结息金额
    private String time;//时间
    private String interestRate;//利率

    public String getStoreMark() {
        return storeMark;
    }

    public void setStoreMark(String storeMark) {
        this.storeMark = storeMark;
    }

    public String getCanUserMoney() {
        return canUserMoney;
    }

    public void setCanUserMoney(String canUserMoney) {
        this.canUserMoney = canUserMoney;
    }

    public String getYesterdayMoney() {
        return yesterdayMoney;
    }

    public void setYesterdayMoney(String yesterdayMoney) {
        this.yesterdayMoney = yesterdayMoney;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(String interestRate) {
        this.interestRate = interestRate;
    }
}
