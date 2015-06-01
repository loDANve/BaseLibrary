package com.chanxa.wnb.bean.bean;

/**
 * Created by CHANXA on 2014/12/23.
 * 手机充值-查询号段
 */
public class Segment {
    private String carrieroperator;//运营商
    private String province;       //省
    private String cityPostalCode; //城市区号
    private String description; //描述

    public String getCarrieroperator() {
        return carrieroperator;
    }

    public void setCarrieroperator(String carrieroperator) {
        this.carrieroperator = carrieroperator;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCityPostalCode() {
        return cityPostalCode;
    }

    public void setCityPostalCode(String cityPostalCode) {
        this.cityPostalCode = cityPostalCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
