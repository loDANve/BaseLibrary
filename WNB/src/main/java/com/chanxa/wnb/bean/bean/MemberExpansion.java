package com.chanxa.wnb.bean.bean;

/**
 * Created by CHANXA on 2014/12/31.
 */
public class MemberExpansion {

    private String eETV_eETP_Id;
    private String eETP_Id;
    private String eETP_FieldName;
    private String eETP_sValueType_Id;
    private String eETP_sInputControl_Id;
    private String eETP_AllowedNull;
    private String eETV_Value;

    public String geteETV_Value() {
        return eETV_Value;
    }

    public String geteETV_eETP_Id() {
        return eETV_eETP_Id;
    }

    public void seteETV_eETP_Id(String eETV_eETP_Id) {
        this.eETV_eETP_Id = eETV_eETP_Id;
    }

    public void seteETV_Value(String eETV_Value) {
        this.eETV_Value = eETV_Value;
    }

    public String geteETP_Id() {
        return eETP_Id;
    }
    public void seteETP_Id(String eETP_Id) {
        this.eETP_Id = eETP_Id;
    }

    public String geteETP_FieldName() {
        return eETP_FieldName;
    }

    public void seteETP_FieldName(String eETP_FieldName) {
        this.eETP_FieldName = eETP_FieldName;
    }

    public String geteETP_sValueType_Id() {
        return eETP_sValueType_Id;
    }

    public void seteETP_sValueType_Id(String eETP_sValueType_Id) {
        this.eETP_sValueType_Id = eETP_sValueType_Id;
    }

    public String geteETP_sInputControl_Id() {
        return eETP_sInputControl_Id;
    }

    public void seteETP_sInputControl_Id(String eETP_sInputControl_Id) {
        this.eETP_sInputControl_Id = eETP_sInputControl_Id;
    }

    public String geteETP_AllowedNull() {
        return eETP_AllowedNull;
    }

    public void seteETP_AllowedNull(String eETP_AllowedNull) {
        this.eETP_AllowedNull = eETP_AllowedNull;
    }
}
