package com.chanxa.wnb.bean.onLineShop;

import com.wtm.library.utils.StringUtils;

import java.util.ArrayList;

/**
 * Created by CHANXA on 2015/1/14.
 * 配送地信息
 * 获取物流费用获得
 */
public class Distribution {
    private String province="--";
    private String city="";
    private String district="";
    private ArrayList<Logistics> logisticsArrayList;

    public Distribution() {
        logisticsArrayList = new ArrayList<>();
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public ArrayList<Logistics> getLogisticsArrayList() {
        return logisticsArrayList;
    }

    public void setLogisticsArrayList(ArrayList<Logistics> logisticsArrayList) {
        this.logisticsArrayList = logisticsArrayList;
    }

    public void addLogistics(Logistics logistics) {
        logisticsArrayList.add(logistics);
    }
}
