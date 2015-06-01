package com.chanxa.wnb.utils.baiduMap;

import android.content.Context;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

/**
 * Created by android studio on 2014/12/16.
 */
public class Location{

    private LocationClient locationClient;

    private static Location instance;
    //监听函数
    private  BDLocationListener locationListener;
    //定位参数
    private LocationClientOption locationClientOption;

    private int timeout = 15000;
    //定位间隔
    private int timeSpan = 5000;

    private Location(Context context){
        locationClient = new LocationClient(context);//声明LocationClient类
        locationClientOption = new LocationClientOption();
        locationClientOption.setTimeOut(timeout);
        locationClientOption.setIsNeedAddress(true);
        locationClientOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        locationClientOption.setOpenGps(true);
        locationClientOption.setScanSpan(timeSpan);
        locationClientOption.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
        locationClient.setLocOption(locationClientOption);
    }

    public static Location getInstance(Context context) {
        if (instance == null) {
            instance = new Location(context);
        }
        return instance;
    }

    public BDLocationListener getLocationListener() {
        return locationListener;
    }

    public Location setLocationListener(BDLocationListener locationListener) {
        this.locationListener = locationListener;
        locationClient.registerLocationListener(locationListener);
        return instance;
    }

    public void startLocation() {
        if (locationClient.isStarted()) {
            return;
        }
        locationClient.start();
        locationClient.requestLocation();
    }

    public void stopLocation() {
        locationClient.stop();
    }

    public Location setLocationClientOption(LocationClientOption locationClientOption){
        this.locationClientOption = locationClientOption;
        locationClient.setLocOption(locationClientOption);
        return instance;
    }
}
