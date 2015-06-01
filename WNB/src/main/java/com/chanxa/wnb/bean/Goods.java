package com.chanxa.wnb.bean;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.wtm.library.utils.LogUtils;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by CHANXA on 2014/12/23.
 * 商品
 */
@Table(name = "Goods")
public class Goods extends Model implements Serializable {
    @Column(name = "mark")
    private String mark;//标识
    @Column(name = "storeMark")
    private String storeMark;//店面标识
    @Column(name = "goodsTypeMark")
    private String goodsTypeMark;//商品类别标识
    @Column(name = "name")
    private String name;//名称
    @Column(name = "barCode")
    private String barCode;//条码
    @Column(name = "money")
    private String money;// 售价
    @Column(name = "imgPath")
    private String imgPath;//图片路径
    @Column(name = "discountRatio")
    private String discountRatio;//折扣系数
    @Column(name = "scoreRatio")
    private String scoreRatio;//积分系数
    @Column(name = "remarks")
    private String remarks;//备注
    @Column(name = "isOriginalMoneySell")
    private String isOriginalMoneySell;//是否原价销售
    @Column(name = "cardNumber")
    private String cardNumber;//加入购物车对应的用户卡号
    @Column(name = "number")
    private int number;//数量

    private String panicBuyStartTime;//开始抢购时间
    private String panicBuyendTime;//截止抢购时间
    private String panicBuyNumber;//抢购数量
    private String details;//详情(HTML format)
    private String isOnLineOrder;//是否线上订购

    public String getIsOnLineOrder() {
        return isOnLineOrder;
    }

    public void setIsOnLineOrder(String isOnLineOrder) {
        this.isOnLineOrder = isOnLineOrder;
    }

    public String getPanicBuyStartTime() {
        return panicBuyStartTime;
    }

    public void setPanicBuyStartTime(String panicBuyStartTime) {
        this.panicBuyStartTime = panicBuyStartTime;
    }

    public String getPanicBuyendTime() {
        return panicBuyendTime;
    }

    public void setPanicBuyendTime(String panicBuyendTime) {
        this.panicBuyendTime = panicBuyendTime;
    }

    public String getPanicBuyNumber() {
        return panicBuyNumber;
    }

    public void setPanicBuyNumber(String panicBuyNumber) {
        this.panicBuyNumber = panicBuyNumber;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
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

    public String getGoodsTypeMark() {
        return goodsTypeMark;
    }

    public void setGoodsTypeMark(String goodsTypeMark) {
        this.goodsTypeMark = goodsTypeMark;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getDiscountRatio() {
        return discountRatio;
    }

    public void setDiscountRatio(String discountRatio) {
        this.discountRatio = discountRatio;
    }

    public String getScoreRatio() {
        return scoreRatio;
    }

    public void setScoreRatio(String scoreRatio) {
        this.scoreRatio = scoreRatio;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getIsOriginalMoneySell() {
        return isOriginalMoneySell;
    }

    public void setIsOriginalMoneySell(String isOriginalMoneySell) {
        this.isOriginalMoneySell = isOriginalMoneySell;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Goods)) {
            return false;
        }
        Goods gObj = (Goods) obj;
        if (this.mark.equals(gObj.getMark())) {
            return true;
        }
        return super.equals(obj);
    }

}
