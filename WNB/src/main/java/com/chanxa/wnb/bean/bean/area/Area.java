package com.chanxa.wnb.bean.bean.area;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by CHANXA on 2014/12/27.
 * 省市区县
 */
public class Area implements Serializable {

    private ArrayList<Province> provinceArrayList = new ArrayList<Province>();


    public ArrayList<Province> getProvinceArrayList() {
        return provinceArrayList;
    }

    public void setProvinceArrayList(ArrayList<Province> provinceArrayList) {
        this.provinceArrayList = provinceArrayList;
    }

    public void addProvince(Province province) {
        provinceArrayList.add(province);
    }
}
