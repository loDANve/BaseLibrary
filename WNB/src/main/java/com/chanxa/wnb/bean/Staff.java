package com.chanxa.wnb.bean;

import com.wtm.library.utils.StringUtils;

/**
 * Created by CHANXA on 2015/1/6.
 * 员工
 */
public class Staff implements Cloneable {

    private String onlyCode;//唯一码
    private String proof;//凭据
    private String mark;//员工-标识
    private String name;//员工-姓名
    private String phone;//员工-电话
    private String personID;//员工-身份证号码
    private String personIDIsOk;//员工-身份证是否审核
    private String personIDIsExist;//员工-是否存在未审核身份证审核申请单据
    private String bankNumber;//员工-银行账号
    private String accountBalance;//员工-账户余额
    private String storeMark;//商家-标识

    private boolean canUpLoadGoods=false;


    public boolean isCanUpLoadGoods() {
        return canUpLoadGoods;
    }

    public void setCanUpLoadGoods(boolean canUpLoadGoods) {
        this.canUpLoadGoods = canUpLoadGoods;
    }

    public String getPersonIDIsExist() {
        return personIDIsExist;
    }

    public void setPersonIDIsExist(String personIDIsExist) {
        this.personIDIsExist = personIDIsExist;
    }

    public String getOnlyCode() {
        return onlyCode;
    }

    public void setOnlyCode(String onlyCode) {
        this.onlyCode = onlyCode;
    }

    public String getProof() {
        return proof;
    }

    public void setProof(String proof) {
        this.proof = proof;
    }

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public String getPersonIDIsOk() {
        return personIDIsOk;
    }

    public void setPersonIDIsOk(String personIDIsOk) {
        this.personIDIsOk = personIDIsOk;
    }

    public String getBankNumber() {
        return bankNumber;
    }

    public void setBankNumber(String bankNumber) {
        this.bankNumber = bankNumber;
    }

    public String getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(String accountBalance) {
        this.accountBalance = accountBalance;
    }

    public String getStoreMark() {
        return storeMark;
    }

    public void setStoreMark(String storeMark) {
        this.storeMark = storeMark;
    }

    public boolean verification() {
        if (StringUtils.isEmpty(this.onlyCode)||
                StringUtils.isEmpty(this.proof)||
                StringUtils.isEmpty(this.mark)||
                StringUtils.isEmpty(this.name)||
                StringUtils.isEmpty(this.phone)) {
            return false;
        }
        return true;
    }

    public Staff clone(){
        try {
            return (Staff) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
}
