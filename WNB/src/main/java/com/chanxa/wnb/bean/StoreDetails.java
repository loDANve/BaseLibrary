package com.chanxa.wnb.bean;

/**
 * Created by chanxa on 2015/3/17.
 */
public class StoreDetails {
    private String mark;// 标识
    private String name;//店铺名称
    private String phone;//联系电话
    private String province;// 省
    private String city;// 市
    private String area;// 区县
    private String job;// 所属行业
    private String detailsAddress;// 详细地址
    private String imgPath;// 图片路径
    private String changePicture;//轮播图
    private String lat;// 纬度
    private String lng;// 经度
    private String reMark;//备注
    private String discountCoefficient;//折扣系数
    private String scoreCoefficient;//积分系数
    private String onlineShow;//是否线上显示
    private String onlineBuy;//是否线上订购
    private String onlineChange;//是否线上兑换
    private String onlineBook;//是否线上预约
    private String startLogistics;// 是否启用物流
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getDetailsAddress() {
        return detailsAddress;
    }

    public void setDetailsAddress(String detailsAddress) {
        this.detailsAddress = detailsAddress;
    }

    public String getStartLogistics() {
        return startLogistics;
    }

    public void setStartLogistics(String startLogistics) {
        this.startLogistics = startLogistics;
    }

    public String isOnlineBuy() {
        return onlineBuy;
    }

    public void setOnlineBuy(String onlineBuy) {
        this.onlineBuy = onlineBuy;
    }

    public String isOnlineShow() {
        return onlineShow;
    }

    public void setOnlineShow(String onlineShow) {
        this.onlineShow = onlineShow;
    }

    public String getChangePicture() {
        return changePicture;
    }

    public void setChangePicture(String changePicture) {
        this.changePicture = changePicture;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getReMark() {
        return reMark;
    }

    public void setReMark(String reMark) {
        this.reMark = reMark;
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


    public String isOnlineBook() {
        return onlineBook;
    }

    public void setOnlineBook(String onlineBook) {
        this.onlineBook = onlineBook;
    }

    public String isOnlineChange() {
        return onlineChange;
    }

    public void setOnlineChange(String onlineChange) {
        this.onlineChange = onlineChange;
    }


}
