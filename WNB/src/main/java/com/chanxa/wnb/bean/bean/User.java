package com.chanxa.wnb.bean.bean;

/**
 * Created by CHANXA on 2014/12/20.
 * 用户账户密码对象
 */
public class User {

    private String cardNumber;
    private String pwd;

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public User() {
    }

    public User(String cardNumber, String pwd) {
        this.cardNumber = cardNumber;
        this.pwd = pwd;
    }
}
