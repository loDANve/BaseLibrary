package com.chanxa.wnb.bean;

import java.util.ArrayList;

/**
 * Created by CHANXA on 2015/1/13.
 * 分类使用店面,只有基本信息和类别
 */
public class FenLeiStore {
    private String mark;
    private String name;
    private String discountCoefficient;//折扣系数
    private String scoreCoefficient;//积分系数
    private String reMark;
    private String storeMark;
    private ArrayList<StaffStoreGoodsLV1> staffStoreGoodsLV1ArrayList;

    public FenLeiStore() {
        staffStoreGoodsLV1ArrayList = new ArrayList<>();
    }

    public ArrayList<StaffStoreGoodsLV1> getStaffStoreGoodsLV1ArrayList() {
        return staffStoreGoodsLV1ArrayList;
    }

    public void setStaffStoreGoodsLV1ArrayList(ArrayList<StaffStoreGoodsLV1> staffStoreGoodsLV1ArrayList) {
        this.staffStoreGoodsLV1ArrayList = staffStoreGoodsLV1ArrayList;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDiscountCoefficient() {
        return discountCoefficient;
    }

    public void setDiscountCoefficient(String discountCoefficient) {
        this.discountCoefficient = discountCoefficient;
    }

    public String getScoreCoefficient() {
        return scoreCoefficient;
    }

    public void setScoreCoefficient(String scoreCoefficient) {
        this.scoreCoefficient = scoreCoefficient;
    }

    public String getReMark() {
        return reMark;
    }

    public void setReMark(String reMark) {
        this.reMark = reMark;
    }

    public void addStaffStoreGoodsLV1(StaffStoreGoodsLV1 staffStoreGoodsLV1) {
        staffStoreGoodsLV1ArrayList.add(staffStoreGoodsLV1);
    }
}
