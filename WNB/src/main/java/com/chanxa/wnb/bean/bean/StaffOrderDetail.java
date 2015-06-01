package com.chanxa.wnb.bean.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class StaffOrderDetail implements Serializable{

    private String orderNum;     // 订单号
    private String isAuditing;   // 是否审核
    private String goodsAmount;  // 商品数量
    private String sumPrice;     // 总金额
    private String orderFinalSum;  // 订单总金额
    private String freight;      // 运费

    private ArrayList<Goods> goodsList;  //订单商品列表

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getIsAuditing() {
        return isAuditing;
    }

    public void setIsAuditing(String isAuditing) {
        this.isAuditing = isAuditing;
    }

    public String getGoodsAmount() {
        return goodsAmount;
    }

    public void setGoodsAmount(String goodsAmount) {
        this.goodsAmount = goodsAmount;
    }

    public String getSumPrice() {
        return sumPrice;
    }

    public void setSumPrice(String sumPrice) {
        this.sumPrice = sumPrice;
    }

    public String getOrderFinalSum() {
        return orderFinalSum;
    }

    public void setOrderFinalSum(String orderFinalSum) {
        this.orderFinalSum = orderFinalSum;
    }

    public String getFreight() {
        return freight;
    }

    public void setFreight(String freight) {
        this.freight = freight;
    }

    public ArrayList<Goods> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(ArrayList<Goods> goodsList) {
        this.goodsList = goodsList;
    }
}
