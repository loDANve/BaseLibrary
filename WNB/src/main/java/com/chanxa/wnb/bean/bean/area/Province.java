package com.chanxa.wnb.bean.bean.area;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by CHANXA on 2014/12/27.
 * 省
 */
public class Province implements Serializable {

    private String name;
    private String mark;
    private ArrayList<City> cityArrayList =new ArrayList<>();

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public ArrayList<City> getCityArrayList() {
        return cityArrayList;
    }

    public void setCityArrayList(ArrayList<City> cityArrayList) {
        this.cityArrayList = cityArrayList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addCity(City city){
        cityArrayList.add(city);
    }
}
