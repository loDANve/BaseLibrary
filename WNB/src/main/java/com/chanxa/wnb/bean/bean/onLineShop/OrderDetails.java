package com.chanxa.wnb.bean.bean.onLineShop;

import java.io.Serializable;

/**
 * Created by CHANXA on 2015/1/14.
 * 单据明细
 */
public class OrderDetails implements Serializable {
    private String mark;  //商品标识
    private String name;  //商品名称
    private String type;  //商品类别
    private String barCode;  //商品条码
    private String imgPath;  //商品图片路径
    private String number;   //订购数量

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
