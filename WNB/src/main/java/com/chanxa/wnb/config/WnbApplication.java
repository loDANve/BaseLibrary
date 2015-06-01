package com.chanxa.wnb.config;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;
import com.baidu.mapapi.SDKInitializer;
import com.chanxa.wnb.R;
import com.chanxa.wnb.activity.WelcomeActivity;
import com.chanxa.wnb.bean.Advertising;
import com.chanxa.wnb.bean.Card;
import com.chanxa.wnb.bean.CardToken;
import com.chanxa.wnb.bean.FenLeiStore;
import com.chanxa.wnb.bean.Industry;
import com.chanxa.wnb.bean.Staff;
import com.chanxa.wnb.bean.StaffStoreGoods;
import com.chanxa.wnb.bean.StaffStoreGoodsLV1;
import com.chanxa.wnb.bean.area.Area;
import com.chanxa.wnb.bean.onLineShop.GoodsCart;
import com.chanxa.wnb.bean.onLineShop.MyCollection;
import com.chanxa.wnb.utils.baiduMap.Location;
import com.wtm.library.base.UncaughtHandler;
import com.wtm.library.base.WActivityManager;
import com.wtm.library.bitmap.WBitmap;

import java.util.ArrayList;


/**
 * Created by CHANXA on 2014/12/15.
 */
public class WnbApplication extends Application {
    private Location location;
    private static WnbApplication instance;
    private CardToken cardToken;
    private Card card;
    private Area area;
    private ArrayList<Industry> industryArrayList;//行业
    private ArrayList<Advertising> advertisingArrayList;//广告
    private Staff staff;
    private ArrayList<StaffStoreGoodsLV1> staffStoreGoodsLV1ArrayList;
    private ArrayList<FenLeiStore> fenleiArrayListArrayList;//分类界面使用
    private boolean isDownLoad = false;
    private WBitmap wBitmap;
    private MyCollection collection;
    private GoodsCart goodsCart = new GoodsCart();//购物车
    private static UncaughtHandler uncaughtHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        uncaughtHandler = UncaughtHandler.getInstance(this);
        ActiveAndroid.initialize(this);
        SDKInitializer.initialize(this);
        location = Location.getInstance(this);
        wBitmap = new WBitmap(this);
        instance = this;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public void clearCache() {
        setCard(null);
        setCardToken(null);
        setStaffStoreGoodsLV1ArrayList(null);
        setStaff(null);
        goodsCart = null;
        collection = null;
    }

    public GoodsCart getGoodsCart() {
        if (goodsCart == null) {
            goodsCart = new GoodsCart();
        }
        return goodsCart;
    }

    public void setGoodsCart(GoodsCart goodsCart) {
        this.goodsCart = goodsCart;
    }

    public static WnbApplication getInstance() {
        return instance;
    }

    public Location getLocation() {
        return location;
    }

    public CardToken getCardToken() {
        return cardToken;
    }

    public void setCardToken(CardToken cardToken) {
        this.cardToken = cardToken;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public WBitmap getwBitmap() {
        if (wBitmap == null) {
            wBitmap = new WBitmap(this);
        }
        return wBitmap;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public ArrayList<Industry> getIndustryArrayList() {
        return industryArrayList;
    }

    public void setIndustryArrayList(ArrayList<Industry> industryArrayList) {
        this.industryArrayList = industryArrayList;
    }

    public ArrayList<Advertising> getAdvertisingArrayList() {
        return advertisingArrayList;
    }

    public void setAdvertisingArrayList(ArrayList<Advertising> advertisingArrayList) {
        this.advertisingArrayList = advertisingArrayList;
    }

    public boolean isDownLoad() {
        return isDownLoad;
    }

    public void setDownLoad(boolean isDownLoad) {
        this.isDownLoad = isDownLoad;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public ArrayList<StaffStoreGoodsLV1> getStaffStoreGoodsLV1ArrayList() {
        return staffStoreGoodsLV1ArrayList;
    }

    public void setStaffStoreGoodsLV1ArrayList(ArrayList<StaffStoreGoodsLV1> staffStoreGoodsLV1ArrayList) {
        this.staffStoreGoodsLV1ArrayList = staffStoreGoodsLV1ArrayList;
    }

    public ArrayList<FenLeiStore> getFenleiArrayListArrayList() {
        return fenleiArrayListArrayList;
    }

    public void setFenleiArrayListArrayList(ArrayList<FenLeiStore> fenleiArrayListArrayList) {
        this.fenleiArrayListArrayList = fenleiArrayListArrayList;
    }

    public boolean verificationData() {
        if (cardToken == null || card == null) {
            if (staff != null || area != null) {
                return true;
            }
            return false;
        }else if (staff != null || area != null) {
            return true;
        }
        return true;
    }

    public MyCollection getCollection() {
        return collection;
    }

    public void setCollection(MyCollection collection) {
        this.collection = collection;
    }
}
