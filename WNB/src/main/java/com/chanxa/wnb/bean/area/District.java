package com.chanxa.wnb.bean.area;

import java.io.Serializable;

/**
 * Created by CHANXA on 2014/12/27.
 * 区县
 */
public class District implements Serializable {

    private String mark;
    private String name;

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
}
