package com.chanxa.wnb.bean.bean;

/**
 * Created by CHANXA on 2015/1/8.
 */
public class StaffStoreGoods {
    private String mark;//标识
    private String storeMark;//店面标识
    private String name;//名称
    private String reMark;//备注
    private String parent;//父级
    private String imgPath;//图片路径

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
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
}
