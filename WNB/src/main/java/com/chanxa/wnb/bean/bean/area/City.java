package com.chanxa.wnb.bean.bean.area;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by CHANXA on 2014/12/27.
 * 市
 */
public class City implements Serializable {
    private String mark;
    private String name;
    private ArrayList<District> districtArrayList = new ArrayList<>();

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public ArrayList<District> getDistrictArrayList() {
        return districtArrayList;
    }

    public void setDistrictArrayList(ArrayList<District> districtArrayList) {
        this.districtArrayList = districtArrayList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addDistrict(District district) {
        districtArrayList.add(district);
    }
}
