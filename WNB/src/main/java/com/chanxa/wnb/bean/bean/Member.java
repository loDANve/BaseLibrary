package com.chanxa.wnb.bean.bean;

import com.wtm.library.utils.StringUtils;

import java.util.ArrayList;

/**
 * 会员模型
 * Created by CHANXA on 2014/12/20.
 */
public class Member {

    private String name;//姓名
    private String memberMark;//标识
    private String appellation;//称谓
    private String phone;//手机
    private String email;//邮箱
    private String address;//联系地址
    private String photoPath;//照片路径

    private String birhday;//出生日期
    private String remarks;//备注

    private String personID;//身份证号码

    private ArrayList<MemberExpansion> memberExpansionArrayList;

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public String getBirhday() {
        return birhday;
    }

    public void setBirhday(String birhday) {
        this.birhday = birhday;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMemberMark() {
        return memberMark;
    }

    public void setMemberMark(String memberMark) {
        this.memberMark = memberMark;
    }

    public String getAppellation() {
        return appellation;
    }

    public void setAppellation(String appellation) {
        this.appellation = appellation;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public ArrayList<MemberExpansion> getMemberExpansionArrayList() {
        return memberExpansionArrayList;
    }

    public void setMemberExpansionArrayList(ArrayList<MemberExpansion> memberExpansionArrayList) {
        this.memberExpansionArrayList = memberExpansionArrayList;
    }
    /**
     * 姓名，称谓,地址,照片路径,备注,电话  不能为空
     * @return
     */
    public boolean verification() {
        boolean flag = true;
        if(StringUtils.isEmpty(this.name)
           ||StringUtils.isEmpty(this.appellation)
           ||StringUtils.isEmpty(this.address)
           ||StringUtils.isEmpty(this.photoPath)
           ||StringUtils.isEmpty(this.remarks)
           ||StringUtils.isEmpty(this.phone)
        ){
            flag = false;
        }
        return flag;
    }
}
