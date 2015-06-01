package com.chanxa.wnb.bean;

import com.activeandroid.Model;
import com.wtm.library.utils.StringUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * 店铺维护类
 */
public class ContactDetails extends Model{
    //联系人
    private String contactPerson;
    //联系电话
    private String contactPhone;
    //店铺Logo路径
    private String logoPath;
    //店铺详情路径集合
    private ArrayList<String> storeDetails;

    public void  setContactPerson(String contactPerson){
        this.contactPerson=contactPerson;
    }

    public String getContactPerson(){
        return contactPerson;
    }

    public void setContactPhone(String contactPhone){
        this.contactPhone=contactPhone;
    }

    public String getContactPhone(){return contactPhone;}

    public void setLogoPath(String logoPath){
        this.logoPath=logoPath;
    }

    public String getLogoPath(){
        return logoPath;
    }

    public void setStoreDetails(ArrayList<String> storeDetails){
        this.storeDetails=storeDetails;
    }

    public ArrayList<String> getStoreDetails(){
        return  storeDetails;
    }
    public ContactDetails(String contactPerson,String contactPhone,String logoPath, ArrayList<String> storeDetails){
        super();
        this.contactPerson=contactPerson;
        this.contactPhone=contactPerson;
        this.logoPath=logoPath;
        this.storeDetails=storeDetails;
    }

    public boolean verification() {
        if (StringUtils.isEmpty(contactPerson)) {
            return false;
        }
        if (StringUtils.isEmpty(contactPhone)) {
            return false;
        }
        if (StringUtils.isEmpty(logoPath)) {
            return false;
        }
//        if (StringUtils.isEmpty(storeDetails)) {
//            return false;
//        }

        return true;
    }

    public ContactDetails(){
        super();
    }
}
