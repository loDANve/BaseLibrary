package com.chanxa.wnb.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.chanxa.wnb.R;
import com.chanxa.wnb.activity.base.WnbBaseActivity;
import com.chanxa.wnb.bean.Industry;
import com.chanxa.wnb.bean.Store;
import com.chanxa.wnb.bean.area.Area;
import com.chanxa.wnb.bean.area.City;
import com.chanxa.wnb.bean.area.District;
import com.chanxa.wnb.bean.area.Province;
import com.chanxa.wnb.config.WnbApplication;
import com.chanxa.wnb.service.StoreService;
import com.chanxa.wnb.service.ServiceCallBack;
import com.chanxa.wnb.view.ProgressDialogBuilder;
import com.chanxa.wnb.view.popwindow.AreaWheelPopDelegate;
import com.chanxa.wnb.view.popwindow.OneWheelPopDelegate;
import com.chanxa.wnb.view.wheelView.WheelView;
import com.wtm.library.base.BaseActivity;
import com.wtm.library.inject.ViewInject;
import com.wtm.library.utils.FileUtils;
import com.wtm.library.utils.LogUtils;
import com.wtm.library.utils.StringUtils;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class SearchActivity extends WnbBaseActivity {

    private ProgressDialog progressDialog;
    private OneWheelPopDelegate oneWheelPopDelegate;
    private AreaWheelPopDelegate areaWheelPopDelegate;
    private ArrayList<Industry> industryArrayList;
    private TextView tv_quyu, tv_fenlei;
    private EditText edit_search;
    private String[] industryStrArr;

    private String mCurrIndustryMark;
    private String mCurrProvinceMark;
    private String mCurrCityMark;
    private String mCurrDistricyMark;


    public SearchActivity() {
        setHiddenActionBar(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
    }

    @Override
    public void initView() {
        tv_quyu = (TextView) findViewById(R.id.tv_quyu);
        tv_fenlei = (TextView) findViewById(R.id.tv_fenlei);

        edit_search = (EditText) findViewById(R.id.edit_search);
        findViewById(R.id.container_quyu).setOnClickListener(this);
        findViewById(R.id.btn_title_back).setOnClickListener(this);
        findViewById(R.id.container_fenlei).setOnClickListener(this);
        findViewById(R.id.btn_query).setOnClickListener(this);
        findViewById(R.id.tv_search).setOnClickListener(this);
        industryWheelInit();
        areaWheelInit();
        progressDialog = ProgressDialogBuilder.builderDialog(this);
        initData();
    }

    private void industryWheelInit() {
        oneWheelPopDelegate = new OneWheelPopDelegate(this);
        oneWheelPopDelegate.setOnButtonClick(new OneWheelPopDelegate.OnButtonClick() {
            @Override
            public void onPositivebtnClick(WheelView wheelView1) {
                mCurrIndustryMark = industryArrayList.get(wheelView1.getSelectedItemPosition()).getMark();
                tv_fenlei.setText(industryStrArr[wheelView1.getSelectedItemPosition()]);
            }

            @Override
            public void onNegativebtnClick(WheelView wheelView1) {

            }
        });

    }

    private void areaWheelInit() {
        Area area = WnbApplication.getInstance().getArea();
        if (area == null) {
            InputStream inputStream = null;
            ObjectInputStream objIn = null;
            try {
                inputStream = getAssets().open("area.obj");
                objIn = new ObjectInputStream(inputStream);
                area = (Area) objIn.readObject();
                WnbApplication.getInstance().setArea(area);
            } catch (Exception e) {
                ViewInject.toast("获取城市数据失败");
            } finally {
                FileUtils.closeIO(inputStream, objIn);
            }
        }
        areaWheelPopDelegate = new AreaWheelPopDelegate(this, area);
        areaWheelPopDelegate.setOnAreaBtnClick(new AreaWheelPopDelegate.OnAreaBtnClick() {
            @Override
            public void onPositiveClick(Province province, City city, District district) {
                mCurrProvinceMark = province.getMark();
                mCurrCityMark = city.getMark();
                mCurrDistricyMark = district.getMark();
                tv_quyu.setText(province.getName() + city.getName() + district.getName());
                areaWheelPopDelegate.disMiss();
            }

            @Override
            public void onNegativeClick(Province province, City city, District district) {
                areaWheelPopDelegate.disMiss();
            }
        });
    }

    @Override
    public void initData() {
        industryArrayList = WnbApplication.getInstance().getIndustryArrayList();
        if (industryArrayList == null) {//内存中没有 表示没有获取过 应该去网络获取
            obtainIndustry();
        } else {//已经获取过
            initIndustryWheelData();
        }
    }

    /**
     * 初始化行业wheel数据
     */
    private void initIndustryWheelData() {
        industryStrArr = new String[industryArrayList.size()];
        for (int i = 0; i < industryStrArr.length; i++) {
            industryStrArr[i] = industryArrayList.get(i).getName();
        }
        if (oneWheelPopDelegate == null) {
            industryWheelInit();
        }
        oneWheelPopDelegate.initWheelView(industryStrArr);
    }

    private void obtainIndustry() {
        new StoreService().obtainIndustry(new ServiceCallBack<ArrayList<Industry>>() {
            @Override
            public void onStart() {
                progressDialog.show();
            }

            @Override
            public void onError(Exception e, int stateCode) {
                progressDialog.dismiss();
                ViewInject.toast("获取行业信息失败");
            }

            @Override
            public void onEXECisFalse(String errroMsg) {
                progressDialog.dismiss();
                ViewInject.toast(errroMsg);
            }

            @Override
            public void onEXECSuccess(ArrayList<Industry> resultObj) {
                industryArrayList = resultObj;
                WnbApplication.getInstance().setIndustryArrayList(industryArrayList);
                initIndustryWheelData();
                progressDialog.dismiss();
            }

            @Override
            public void onEXECSuccess(ArrayList<Industry> resultObj, int pageSize, int dataSize) {

            }
        });
    }


    private void query(String storeName, String mCurrIndustryMark, String mCurrProvinceMark, String mCurrCityMark, String mCurrDistricyMark) {
        new StoreService().obtianStore(1, 10, storeName, mCurrIndustryMark, mCurrProvinceMark, mCurrCityMark, mCurrDistricyMark, new ServiceCallBack<ArrayList<Store>>() {
            @Override
            public void onStart() {
                progressDialog.show();
            }

            @Override
            public void onError(Exception e, int stateCode) {
                progressDialog.dismiss();
                ViewInject.toast("查询失败");
            }

            @Override
            public void onEXECisFalse(String errroMsg) {
                progressDialog.dismiss();
                ViewInject.toast(errroMsg);
            }

            @Override
            public void onEXECSuccess(ArrayList<Store> resultObj) {
                ArrayList<Store> storeList = (ArrayList<Store>) resultObj;
                if (storeList.size() == 0) {
                    ViewInject.toast("没有找到符合的结果");
                    progressDialog.dismiss();
                    return;
                }
                ViewInject.toast("找到了" + storeList.size() + "家店铺");
                Intent intent = new Intent();
                intent.putExtra("storeList", storeList);
                setResult(RESULT_OK, intent);
                progressDialog.dismiss();
                finish();
            }

            @Override
            public void onEXECSuccess(ArrayList<Store> resultObj, int pageSize, int dataSize) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_title_back:
                finish();
                break;
            case R.id.container_quyu:
                if (areaWheelPopDelegate!=null) {
                    areaWheelPopDelegate.show();
                }

                break;
            case R.id.container_fenlei:
                if (oneWheelPopDelegate != null) {
                    oneWheelPopDelegate.show();
                }
                break;
            case R.id.tv_search:
                if (!StringUtils.isEmpty(edit_search.getText().toString())) {
                    query(edit_search.getText().toString(),null,null,null,null);
                }
                break;
            case R.id.btn_query:
                //查询
                query(null,mCurrIndustryMark, mCurrProvinceMark, mCurrCityMark, mCurrDistricyMark);
                break;
        }
    }
}
