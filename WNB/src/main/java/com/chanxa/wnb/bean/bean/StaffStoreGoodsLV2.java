package com.chanxa.wnb.bean.bean;

import java.util.ArrayList;

/**
 * Created by CHANXA on 2015/1/9.
 */
public class StaffStoreGoodsLV2 {
    private ArrayList<StaffStoreGoods> staffStoreGoodsArrayList = new ArrayList<>();
    private String mark;//标识
    private String storeMark;//店面标识
    private String name;//名称
    private String reMark;//备注
    private String imgPath;//图片路径

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public ArrayList<StaffStoreGoods> getStaffStoreGoodsArrayList() {
        return staffStoreGoodsArrayList;
    }

    public void setStaffStoreGoodsArrayList(ArrayList<StaffStoreGoods> staffStoreGoodsArrayList) {
        this.staffStoreGoodsArrayList = staffStoreGoodsArrayList;
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

    public String getReMark() {
        return reMark;
    }

    public void setReMark(String reMark) {
        this.reMark = reMark;
    }

    public void addStaffStoreGoods(StaffStoreGoods staffStoreGoods) {
        staffStoreGoodsArrayList.add(staffStoreGoods);
    }
}
