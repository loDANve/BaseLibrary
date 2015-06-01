package com.chanxa.wnb.bean.onLineShop;

/**
 * Created by CHANXA on 2015/1/14.
 * 物流： 对应xml节点名：物流
 */
public class Logistics {
    private String type;//方式
    private String money;//费用

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }
}
