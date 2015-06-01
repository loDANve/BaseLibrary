package com.chanxa.wnb.fragment.childFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.chanxa.wnb.R;
import com.chanxa.wnb.activity.MainActivity;
import com.chanxa.wnb.activity.shop.StoreGoodsActivity;
import com.chanxa.wnb.bean.Store;
import com.chanxa.wnb.config.WnbApplication;
import com.chanxa.wnb.fragment.StoreFragment;
import com.chanxa.wnb.utils.baiduMap.Location;
import com.chanxa.wnb.utils.baiduMap.MapUtils;
import com.wtm.library.base.BaseFragment;
import com.wtm.library.bitmap.WBitmap;
import com.wtm.library.inject.ViewInject;

import java.util.ArrayList;

/**
 * Created by CHANXA on 2014/12/12.
 */
public class StoreChildMapFragment extends BaseFragment {

    private MapView bmapView;//百度地图view
    private BaiduMap baiduMap;//百度地图操作对象
    private BitmapDescriptor centerBitmap;//地图中心点位图资源
    private BitmapDescriptor storeBitmap;//地图店铺位图资源

    private Fragment parentFragment;

    private ArrayList<Marker> markerArrayList = new ArrayList<>();

    public void setParentFragment(Fragment parentFragment) {
        this.parentFragment = parentFragment;
    }

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_storechild_map, null, false);
    }

    @Override
    public void initView() {
        super.initView();
        bmapView = (MapView) rootView.findViewById(R.id.bmapView);
    }

    @Override
    public void onStart() {
        super.onStart();
        baiduMap = bmapView.getMap();
        baiduMap.setMyLocationEnabled(true);
        MapUtils.setMapzoom(baiduMap, 18);
    }

    @Override
    public void onResume() {
        super.onResume();
        bmapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        bmapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        baiduMap.clear();
        //回收位图资源
        if (centerBitmap != null) {
            centerBitmap.recycle();
        }
        if (storeBitmap != null) {
            centerBitmap.recycle();
        }
        bmapView.onDestroy();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (parentFragment != null) {
            ((StoreFragment) parentFragment).setPagerTouchMode(!hidden);
            ((MainActivity) parentFragment.getActivity()).setPagerTouchMode(!hidden);
        }
    }

    /**
     * 由父fragment调启,给出中心点,店铺列表,刷新当前view
     *
     * @param centerLalng
     * @param storeArrayList
     */
    public void refresh(final LatLng centerLalng, final ArrayList<Store> storeArrayList) {
        baiduMap.clear();
        markerArrayList.clear();
        setCenterPointMark(centerLalng);
        setStorePointMark(storeArrayList);

        baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                //创建InfoWindow展示的view
                Button button = new Button(getActivity());
                button.setBackgroundResource(R.drawable.popup);
                int position = markerArrayList.indexOf(marker);
                if (position == -1) {
                    return false;
                }
                if (0 == position) {
                    //是中心点
                    //ViewInject.toast("点击了中心点图片");
                    button.setText("我的位置");
                } else {
                    final Store store = storeArrayList.get(position - 1);
                    button.setText(store.getName());
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), StoreGoodsActivity.class);
                            intent.putExtra("storeMark", store.getMark());
                            startActivity(intent);
                        }
                    });
                }
                //创建InfoWindow , 传入 view， 地理坐标， y 轴偏移量
                InfoWindow mInfoWindow = new InfoWindow(button, marker.getPosition(), -52);
                //显示InfoWindow
                baiduMap.showInfoWindow(mInfoWindow);
                return false;
            }
        });
    }

    /**
     * 设置店铺与地图有关
     *
     * @param storeArrayList
     */
    private void setStorePointMark(ArrayList<Store> storeArrayList) {
        if (storeBitmap == null) {
            storeBitmap = BitmapDescriptorFactory
                    .fromResource(R.drawable.icon_location_store);
        }
        for (Store store : storeArrayList) {
            LatLng latLng = new LatLng(Double.parseDouble(store.getLat()), Double.parseDouble(store.getLng()));
            OverlayOptions option = new MarkerOptions()
                    .position(latLng)
                    .icon(storeBitmap);
            markerArrayList.add((Marker) baiduMap.addOverlay(option));
        }
    }

    /**
     * 设置地图中心点与中心点图标
     *
     * @param centerLalng
     */
    private void setCenterPointMark(final LatLng centerLalng) {
        MapUtils.setCenterPoint(baiduMap, centerLalng);
        if (centerBitmap == null) {
            centerBitmap = BitmapDescriptorFactory
                    .fromResource(R.drawable.icon_location_center);
        }
        OverlayOptions option = new MarkerOptions()
                .position(centerLalng)
                .icon(centerBitmap);
        markerArrayList.add((Marker) baiduMap.addOverlay(option));
    }
}
