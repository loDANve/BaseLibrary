package com.chanxa.wnb.bean;

/**
 * Created by CHANXA on 2014/12/20.
 * <p/>
 * 卡  登陆后通过读卡接口来获取
 */
public class Card {

    private String memberMark;//会员标识
    private String name;//姓名
    private String appellation;//称谓
    private String birthDay;//生日
    private String phone;
    private String email;
    private String address;
    private String photoPath;
    private String personID;//身份证号码
    private String remarks;//备注
    private String regTime;
    private String cardConsumptionCount;//卡已消费次数
    private String cardConsumptionLastTime;//卡最后消费时间
    private String cardNumber;//卡号
    private String cardType;//卡类型
    private String cardLv;//卡级别
    private String cardLvImgPath;//卡级别图片路径
    private String consumptionMoney;//消费额
    private String balance;//可用储值
    private String canUseScore;//可用积分
    private String cardIssuanceTime;//卡发放时间
    private String cardVoidTime;//卡作废时间
    private String yearInterestRate;//年利率


    public String getYearInterestRate() {
        return yearInterestRate;
    }

    public void setYearInterestRate(String yearInterestRate) {
        this.yearInterestRate = yearInterestRate;
    }

    public String getMemberMark() {
        return memberMark;
    }

    public void setMemberMark(String memberMark) {
        this.memberMark = memberMark;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAppellation() {
        return appellation;
    }

    public void setAppellation(String appellation) {
        this.appellation = appellation;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
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

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRegTime() {
        return regTime;
    }

    public void setRegTime(String regTime) {
        this.regTime = regTime;
    }

    public String getCardConsumptionCount() {
        return cardConsumptionCount;
    }

    public void setCardConsumptionCount(String cardConsumptionCount) {
        this.cardConsumptionCount = cardConsumptionCount;
    }

    public String getCardConsumptionLastTime() {
        return cardConsumptionLastTime;
    }

    public void setCardConsumptionLastTime(String cardConsumptionLastTime) {
        this.cardConsumptionLastTime = cardConsumptionLastTime;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCardLv() {
        return cardLv;
    }

    public void setCardLv(String cardLv) {
        this.cardLv = cardLv;
    }

    public String getCardLvImgPath() {
        return cardLvImgPath;
    }

    public void setCardLvImgPath(String cardLvImgPath) {
        this.cardLvImgPath = cardLvImgPath;
    }

    public String getConsumptionMoney() {
        return consumptionMoney;
    }

    public void setConsumptionMoney(String consumptionMoney) {
        this.consumptionMoney = consumptionMoney;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getCanUseScore() {
        return canUseScore;
    }

    public void setCanUseScore(String canUseScore) {
        this.canUseScore = canUseScore;
    }

    public String getCardIssuanceTime() {
        return cardIssuanceTime;
    }

    public void setCardIssuanceTime(String cardIssuanceTime) {
        this.cardIssuanceTime = cardIssuanceTime;
    }

    public String getCardVoidTime() {
        return cardVoidTime;
    }

    public void setCardVoidTime(String cardVoidTime) {
        this.cardVoidTime = cardVoidTime;
    }
}
