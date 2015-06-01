package com.chanxa.wnb.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.chanxa.wnb.R;
import com.chanxa.wnb.activity.MainActivity;
import com.chanxa.wnb.bean.Advertising;
import com.chanxa.wnb.config.WnbApplication;
import com.chanxa.wnb.service.DataService;
import com.chanxa.wnb.service.ServiceCallBack;
import com.chanxa.wnb.view.ViewPagerCompat;
import com.wtm.library.base.BaseFragment;
import com.wtm.library.inject.ViewInject;
import com.wtm.library.utils.LogUtils;

import java.util.ArrayList;

/**
 * Created by CHANXA on 2015/1/5.
 */
public class MainFragment extends BaseFragment implements View.OnClickListener {

    private ViewPagerCompat fragment_viewPager;
    private Fragment[] fragArr = new Fragment[3];
    private StoreFragment storeFragment;
    private HomeFragment homeFragment;
    private ShopFragment shopFragment;
    private Button container_homeTab, container_shopTab, container_storeTab;
    private Button[] tabBtnArr = new Button[3];
    private int currentPager = 0;

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, null, false);
    }

    @Override
    public void initView() {
        super.initView();
        tabBtnArr[0] = container_homeTab = (Button) rootView.findViewById(R.id.container_homeTab);
        tabBtnArr[1] = container_shopTab = (Button) rootView.findViewById(R.id.container_shopTab);
        tabBtnArr[2] = container_storeTab = (Button) rootView.findViewById(R.id.container_storeTab);
        container_homeTab.setOnClickListener(this);
        container_shopTab.setOnClickListener(this);
        container_storeTab.setOnClickListener(this);
        fragment_viewPager = (ViewPagerCompat) rootView.findViewById(R.id.viewpager_fragment_main);
        fragment_viewPager.setAdapter(new FragmentPagerAdapter(getActivity().getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragArr[position];
            }

            @Override
            public int getCount() {
                return fragArr.length;
            }
        });
        fragment_viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position != 2) {
                    storeFragment.setCurrentFragment(1);
                }
                tabBtnArr[currentPager].setSelected(false);
                tabBtnArr[position].setSelected(true);
                currentPager = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        fragment_viewPager.setOffscreenPageLimit(2);
        fragment_viewPager.setCurrentItem(currentPager);
        container_homeTab.setSelected(true);
    }

    @Override
    public void initData() {
        super.initData();
        fragArr[0] = homeFragment = new HomeFragment();
        fragArr[1] = shopFragment = new ShopFragment();
        fragArr[2] = storeFragment = new StoreFragment();
        shopFragment.setMainParentFragment(this);
        storeFragment.setParentFragment(this);
        homeFragment.setParentFragment(this);
    }

    @Override
    public void onClick(View v) {
        fragment_viewPager.setViewTouchMode(false);
        switch (v.getId()) {
            case R.id.container_homeTab:
                fragment_viewPager.setCurrentItem(0);
                break;
            case R.id.container_shopTab:
                fragment_viewPager.setCurrentItem(1);
                break;
            case R.id.container_storeTab:
                fragment_viewPager.setCurrentItem(2);
                break;
        }
    }

    //刷新子fragment的广告
    public void refreshChildFragmentAdvertising() {
        new DataService().obtainConfig(new ServiceCallBack() {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(Exception e, int stateCode) {

            }

            @Override
            public void onEXECisFalse(String errroMsg) {

            }

            @Override
            public void onEXECSuccess(Object resultObj) {
                ArrayList<Advertising> advertisingArrayList = (ArrayList<Advertising>) resultObj;
                homeFragment.advertisingData2View(advertisingArrayList);
                shopFragment.advertisingData2View(advertisingArrayList);
            }

            @Override
            public void onEXECSuccess(Object resultObj, int pageSize, int dataSize) {
            }
        });
    }

    public void activityPagerIndex(int position) {
        if (position > 1) {
            return;
        }

        ((MainActivity) getActivity()).pagerIndex(position);
    }

    public void setPagerTouchMode(boolean mode) {
        fragment_viewPager.setViewTouchMode(mode);
    }
}