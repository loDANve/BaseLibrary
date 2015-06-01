package com.chanxa.wnb.bean;

/**
 * Created by CHANXA on 2014/12/26.
 */
public class StoredValueRecord {

    private String storeMark;//店面标识
    private String changeNumber;//变化额度
    private String changeType;//变化类型
    private String thawTime;//解冻时间
    private String isFreezing;//是否冻结
    private String raMark;//备注
    private String time;//时间

    public String getStoreMark() {
        return storeMark;
    }

    public void setStoreMark(String storeMark) {
        this.storeMark = storeMark;
    }

    public String getChangeNumber() {
        return changeNumber;
    }

    public void setChangeNumber(String changeNumber) {
        this.changeNumber = changeNumber;
    }

    public String getChangeType() {
        return changeType;
    }

    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }

    public String getThawTime() {
        return thawTime;
    }

    public void setThawTime(String thawTime) {
        this.thawTime = thawTime;
    }

    public String getIsFreezing() {
        return isFreezing;
    }

    public void setIsFreezing(String isFreezing) {
        this.isFreezing = isFreezing;
    }

    public String getRaMark() {
        return raMark;
    }

    public void setRaMark(String raMark) {
        this.raMark = raMark;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
