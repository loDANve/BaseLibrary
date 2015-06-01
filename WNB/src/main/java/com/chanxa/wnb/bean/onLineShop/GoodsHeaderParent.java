package com.chanxa.wnb.bean.onLineShop;

import java.util.ArrayList;

/**
 * Created by CHANXA on 2015/1/14.
 */
public class GoodsHeaderParent {
    private long hearId;
    private String name;
    private String mark;
    private ArrayList<GoodsHeader> goodsHeaderArrayList=new ArrayList<>();

    public ArrayList<GoodsHeader> getGoodsHeaderArrayList() {
        return goodsHeaderArrayList;
    }

    public void setGoodsHeaderArrayList(ArrayList<GoodsHeader> goodsHeaderArrayList) {
        this.goodsHeaderArrayList = goodsHeaderArrayList;
    }

    public void addGoodsHeader(GoodsHeader goodsHeader) {
        goodsHeaderArrayList.add(goodsHeader);
    }

    public long getHearId() {
        return hearId;
    }

    public void setHearId(long hearId) {
        this.hearId = hearId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }
}
