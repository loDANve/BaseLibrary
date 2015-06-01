package com.chanxa.wnb.utils.baiduMap;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.model.LatLng;

/**
 * Created by CHANXA on 2014/12/16.
 */
public class MapUtils {
    /**
     * 设置地图中心点
     *
     * @param baiduMap
     * @param centerPoint
     */
    public static void setCenterPoint(BaiduMap baiduMap, LatLng centerPoint) {
        baiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(centerPoint));
    }

    /**
     * 设置地图的缩放级别
     * @param baiduMap
     * @param zoom
     */
    public static void setMapzoom(BaiduMap baiduMap, float zoom) {
        baiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(zoom));
    }
}
