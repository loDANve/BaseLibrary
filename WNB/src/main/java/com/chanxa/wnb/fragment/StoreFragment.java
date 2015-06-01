package com.chanxa.wnb.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.model.LatLng;
import com.chanxa.wnb.R;
import com.chanxa.wnb.activity.MainActivity;
import com.chanxa.wnb.activity.SearchActivity;
import com.chanxa.wnb.bean.Store;
import com.chanxa.wnb.config.WnbApplication;
import com.chanxa.wnb.fragment.base.MemberBaseFragment;
import com.chanxa.wnb.fragment.childFragment.StoreChildListFragment;
import com.chanxa.wnb.fragment.childFragment.StoreChildMapFragment;
import com.chanxa.wnb.service.StoreService;
import com.chanxa.wnb.service.ServiceCallBack;
import com.chanxa.wnb.utils.baiduMap.Location;
import com.wtm.library.base.BaseFragment;
import com.wtm.library.inject.ViewInject;
import com.wtm.library.utils.LogUtils;

import java.util.ArrayList;

/**
 * Created by wtm on 2014/12/12.
 */
public class StoreFragment extends MemberBaseFragment implements View.OnClickListener, BDLocationListener {

    private StoreChildListFragment storeChildListFragment;
    private StoreChildMapFragment storeChildMapFragment;
    private int currentShowChildFragmentIndex = 0;

    private Button btn_changeMaporList;
    private Location location;

    private ArrayList<Store> storeArrayList = new ArrayList<>();

    private LatLng centerLalng;

    private Fragment parentFragment;
    private int pageIndex = 0;
    private static boolean isLoadMore = false;
    private int maxPager = 0;
    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_store, null, false);
    }

    @Override
    public void initView() {
        super.initView();
        initChildFragment();
        btn_changeMaporList = (Button) rootView.findViewById(R.id.tv_search);
        btn_changeMaporList.setOnClickListener(this);
        rootView.findViewById(R.id.container_toSearch).setOnClickListener(this);
    }

    @Override
    public void initData() {
        super.initData();
        location = WnbApplication.getInstance().getLocation();
        location.setLocationListener(this).startLocation();
    }

    /**
     * 初始化子fragment
     */
    private void initChildFragment() {
        storeChildListFragment = new StoreChildListFragment();
        storeChildListFragment.setParentFragment(this);
        storeChildMapFragment = new StoreChildMapFragment();
        storeChildMapFragment.setParentFragment(this);
        getActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.container_storeFragment, storeChildListFragment)
                .add(R.id.container_storeFragment, storeChildMapFragment)
                .show(storeChildListFragment)
                .hide(storeChildMapFragment)
                .commit();
        currentShowChildFragmentIndex = 0;
    }

    /**
     * 切换子fragment
     */
    private void childFragmentSwitch() {
        setCurrentFragment(currentShowChildFragmentIndex);
    }

    public void setCurrentFragment(int index) {
        if (0 == index) {
            getActivity().getSupportFragmentManager().beginTransaction().show(storeChildMapFragment).hide(storeChildListFragment).commit();
            currentShowChildFragmentIndex = 1;
            btn_changeMaporList.setSelected(true);
            return;
        }
        getActivity().getSupportFragmentManager().beginTransaction().hide(storeChildMapFragment).show(storeChildListFragment).commit();
        currentShowChildFragmentIndex = 0;
        btn_changeMaporList.setSelected(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_search:
                childFragmentSwitch();
                break;
            case R.id.container_toSearch:
                startActivityForResult(new Intent(getActivity(), SearchActivity.class), 1);
                break;
        }
    }

    /**
     * 定位回调
     *
     * @param bdLocation
     */
    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        location.stopLocation();//定位一次后关闭定位
        //经度
        double lng = bdLocation.getLongitude();
        //纬度
        double lat = bdLocation.getLatitude();
        centerLalng = new LatLng(lat, lng);
        if (!isAdded()) {
            return;
        }
        new StoreService().obtianNearStore(getCardToken(), pageIndex, 10, centerLalng, new ServiceCallBack<ArrayList<Store>>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(Exception e, int stateCode) {
            }

            @Override
            public void onEXECisFalse(String errroMsg) {
                ViewInject.toast(errroMsg);
            }

            @Override
            public void onEXECSuccess(ArrayList<Store> resultObj) {
                setStoreArrayList(resultObj);
            }

            @Override
            public void onEXECSuccess(ArrayList<Store> resultObj, int pageSize, int dataSize) {
                maxPager=pageSize;
            }

        });
    }

    public void setStoreArrayList(ArrayList<Store> storeArrayList_new) {
        if (!isLoadMore){
            this.storeArrayList.clear();
        }

        this.storeArrayList.addAll(storeArrayList_new);
        for (int i = 0; i < storeArrayList.size(); i++) {
            LogUtils.fff(storeArrayList.get(i).getName() + "---"+storeArrayList.get(i).getDistance());
        }
        storeChildMapFragment.refresh(centerLalng, storeArrayList);//获取数据后 刷新子fragment
        storeChildListFragment.refresh(centerLalng, storeArrayList);
    }

    /**
     * 启动定位,由子fragment可以调启
     */
    public void startLocation() {
        this.pageIndex = 0;
        isLoadMore = false;
        location.startLocation();
    }

    /**
     *
     * 子fragment调用,加载更多
     */
    public void loadMore(int pageIndex){
        this.pageIndex = pageIndex;
        isLoadMore = true;
        location.startLocation();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            ArrayList<Store> storeArrayList = (ArrayList<Store>) data.getSerializableExtra("storeList");
            setStoreArrayList(storeArrayList);
            setCurrentFragment(1);
        }
    }

    public void setPagerTouchMode(boolean mode) {
        if (parentFragment != null) {
            ((MainFragment) parentFragment).setPagerTouchMode(mode);
        }
    }

    public int getMaxPager() {
        return maxPager;
    }

    public void setParentFragment(Fragment parentFragment) {
        this.parentFragment = parentFragment;
    }
}
