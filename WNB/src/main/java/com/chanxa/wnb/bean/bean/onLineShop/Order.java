package com.chanxa.wnb.bean.bean.onLineShop;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by CHANXA on 2015/1/14.
 * 订单（xml:单据）
 */
public class Order implements Serializable {

    private String mark;            //标识
    private String storeMark;       //店面标识
    private String orderNumber;     //单据号
    private String subTime;         //提交时间
    private String receiverPersonName;  //收货人
    private String receiverPersonPhone; //手机号
    private String receiverAddress; //收货地址
    private String receiverMoney;   //运费
    private String state;           //状态
    private String consumptionMoney;//消费总金额
    private String getTotalScore;   //获得总积分
    private String reviewingTime;   //审核时间
    private String deliveryTime;    //发货时间
    private String cancelTime;      //取消时间
    private String cancelReason;    //取消原因
    private String remark;          //备注
    private String waybillNum;      //运单号
    private String payCashonDelivery;   //邮费货到付款
    private String distributionTo;  //配送至
    private String logistics;       //物流方式
    private String postCode;        //邮编
    private ArrayList<OrderDetails> orderDetailsArrayList;  //订单里的货物列表

    public void addOrderDetails(OrderDetails orderDetails) {
        orderDetailsArrayList.add(orderDetails);
    }
    public Order() {
        orderDetailsArrayList = new ArrayList<>();
    }

    public ArrayList<OrderDetails> getOrderDetailsArrayList() {
        return orderDetailsArrayList;
    }

    public void setOrderDetailsArrayList(ArrayList<OrderDetails> orderDetailsArrayList) {
        this.orderDetailsArrayList = orderDetailsArrayList;
    }

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

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getSubTime() {
        return subTime;
    }

    public void setSubTime(String subTime) {
        this.subTime = subTime;
    }

    public String getReceiverPersonName() {
        return receiverPersonName;
    }

    public void setReceiverPersonName(String receiverPersonName) {
        this.receiverPersonName = receiverPersonName;
    }

    public String getReceiverPersonPhone() {
        return receiverPersonPhone;
    }

    public void setReceiverPersonPhone(String receiverPersonPhone) {
        this.receiverPersonPhone = receiverPersonPhone;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public String getReceiverMoney() {
        return receiverMoney;
    }

    public void setReceiverMoney(String receiverMoney) {
        this.receiverMoney = receiverMoney;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getConsumptionMoney() {
        return consumptionMoney;
    }

    public void setConsumptionMoney(String consumptionMoney) {
        this.consumptionMoney = consumptionMoney;
    }

    public String getGetTotalScore() {
        return getTotalScore;
    }

    public void setGetTotalScore(String getTotalScore) {
        this.getTotalScore = getTotalScore;
    }

    public String getReviewingTime() {
        return reviewingTime;
    }

    public void setReviewingTime(String reviewingTime) {
        this.reviewingTime = reviewingTime;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getCancelTime() {
        return cancelTime;
    }

    public void setCancelTime(String cancelTime) {
        this.cancelTime = cancelTime;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getWaybillNum() {
        return waybillNum;
    }

    public void setWaybillNum(String waybillNum) {
        this.waybillNum = waybillNum;
    }

    public String getPayCashonDelivery() {
        return payCashonDelivery;
    }

    public void setPayCashonDelivery(String payCashonDelivery) {
        this.payCashonDelivery = payCashonDelivery;
    }

    public String getDistributionTo() {
        return distributionTo;
    }

    public void setDistributionTo(String distributionTo) {
        this.distributionTo = distributionTo;
    }

    public String getLogistics() {
        return logistics;
    }

    public void setLogistics(String logistics) {
        this.logistics = logistics;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }
}
