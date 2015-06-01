package com.chanxa.wnb.bean.onLineShop;

/**
 * Created by CHANXA on 2015/1/14.
 */
public class GoodsHeader {
    private String name;
    private String imgPath;
    private String mark;
    private GoodsHeaderParent goodsHeaderParent;

    public GoodsHeaderParent getGoodsHeaderParent() {
        return goodsHeaderParent;
    }

    public void setGoodsHeaderParent(GoodsHeaderParent goodsHeaderParent) {
        this.goodsHeaderParent = goodsHeaderParent;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

}
