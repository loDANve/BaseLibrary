package com.chanxa.wnb.bean;

/**
 * Created by CHANXA on 2014/12/23.
 */
public class Profit {

    private String countProfit;//总收益
    private String lastMonthProfit;//上月收益
    private String lastWeekProfit;//上周收益

    public String getCountProfit() {
        return countProfit;
    }

    public void setCountProfit(String countProfit) {
        this.countProfit = countProfit;
    }

    public String getLastMonthProfit() {
        return lastMonthProfit;
    }

    public void setLastMonthProfit(String lastMonthProfit) {
        this.lastMonthProfit = lastMonthProfit;
    }

    public String getLastWeekProfit() {
        return lastWeekProfit;
    }

    public void setLastWeekProfit(String lastWeekProfit) {
        this.lastWeekProfit = lastWeekProfit;
    }
}
