package com.chanxa.wnb.view.popwindow;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.chanxa.wnb.R;
import com.chanxa.wnb.bean.area.Area;
import com.chanxa.wnb.bean.area.City;
import com.chanxa.wnb.bean.area.District;
import com.chanxa.wnb.bean.area.Province;
import com.chanxa.wnb.view.wheelView.TextInfo;
import com.chanxa.wnb.view.wheelView.TosAdapterView;
import com.chanxa.wnb.view.wheelView.TosGallery;
import com.chanxa.wnb.view.wheelView.WheelTextAdapter;
import com.chanxa.wnb.view.wheelView.WheelView;
import com.wtm.library.inject.ViewInject;
import com.wtm.library.utils.DeviceUtils;

import java.util.ArrayList;

/**
 * Created by CHANXA on 2014/12/27.
 */
public class AreaWheelPopDelegate implements View.OnClickListener {
    private Context context;


    private PopupWindow popupWindow;
    private WheelView wheelView_province;
    private WheelView wheelView_city;
    private WheelView wheelView_district;


    private Area area;
    ArrayList<TextInfo> provinceList = new ArrayList<TextInfo>();
    ArrayList<TextInfo> cityList = new ArrayList<TextInfo>();
    ArrayList<TextInfo> districtList = new ArrayList<TextInfo>();

    int mCurProvince = 0;
    int mCurCity = 0;
    int mCurDistrict = 0;


    private OnAreaBtnClick onAreaBtnClick;

    public AreaWheelPopDelegate(Context context, Area area) {
        this.context = context;
        this.area = area;
        initView();
    }


    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_wheel_ymd, null, false);
        int width = DeviceUtils.getScreenW((android.app.Activity) context);
        popupWindow = ViewInject.getInstance().createWindow(view, width, DeviceUtils.dip2px(context, 200), 0x070000);
        popupWindow.setAnimationStyle(R.style.popupAnimation);

        view.findViewById(R.id.btn_wheel_positive).setOnClickListener(this);
        view.findViewById(R.id.btn_wheel_negative).setOnClickListener(this);

        wheelView_province = (com.chanxa.wnb.view.wheelView.WheelView) view.findViewById(R.id.wheelView_year);
        wheelView_city = (com.chanxa.wnb.view.wheelView.WheelView) view.findViewById(R.id.wheelView_month);
        wheelView_district = (com.chanxa.wnb.view.wheelView.WheelView) view.findViewById(R.id.wheelView_day);

        wheelView_province.setAdapter(new WheelTextAdapter(context));
        wheelView_city.setAdapter(new WheelTextAdapter(context));
        wheelView_district.setAdapter(new WheelTextAdapter(context));

        wheelView_province.setOnItemSelectedListener(new TosAdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(TosAdapterView<?> parent, View view, int position, long id) {
                mCurProvince = position;
                refreshCityListData(area.getProvinceArrayList().get(mCurProvince));
                wheelView_city.setSelection(0);
                mCurCity = 0;
                refreshDistrictListData(area.getProvinceArrayList().get(mCurProvince).getCityArrayList().get(mCurCity));
            }

            @Override
            public void onNothingSelected(TosAdapterView<?> parent) {
            }
        });
        wheelView_city.setOnItemSelectedListener(new TosAdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(TosAdapterView<?> parent, View view, int position, long id) {
                mCurCity = position;
                refreshDistrictListData(area.getProvinceArrayList().get(mCurProvince).getCityArrayList().get(mCurCity));
                wheelView_district.setSelection(0);
                mCurDistrict = 0;
            }

            @Override
            public void onNothingSelected(TosAdapterView<?> parent) {

            }
        });

        wheelView_district.setOnItemSelectedListener(new TosAdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(TosAdapterView<?> parent, View view, int position, long id) {
                mCurDistrict = wheelView_district.getSelectedItemPosition();
            }

            @Override
            public void onNothingSelected(TosAdapterView<?> parent) {

            }
        });
        initData();
    }


    private void initData() {
        ArrayList<Province> provinceArrayList = area.getProvinceArrayList();
        for (int i = 0; i < provinceArrayList.size(); i++) {
            Province province = provinceArrayList.get(i);
            provinceList.add(new TextInfo(i, province.getName(), false));
        }
        refreshCityListData(provinceArrayList.get(0));
        refreshDistrictListData(provinceArrayList.get(0).getCityArrayList().get(0));
        ((WheelTextAdapter) wheelView_province.getAdapter()).setData(provinceList);
        ((WheelTextAdapter) wheelView_city.getAdapter()).setData(cityList);
        ((WheelTextAdapter) wheelView_district.getAdapter()).setData(districtList);

    }

    /**
     * 根据省份刷新对应城市
     *
     * @param province
     */
    private void refreshCityListData(Province province) {
        cityList.clear();
        ArrayList<City> cityArrayList = province.getCityArrayList();
        for (int i = 0; i < cityArrayList.size(); i++) {
            cityList.add(new TextInfo(i, cityArrayList.get(i).getName(), false));
        }
        ((WheelTextAdapter) wheelView_city.getAdapter()).setData(cityList);
    }

    /**
     * 根据城市刷新对应地区
     *
     * @param city
     */
    private void refreshDistrictListData(City city) {
        districtList.clear();
        ArrayList<District> districtArrayList = city.getDistrictArrayList();
        for (int i = 0; i < districtArrayList.size(); i++) {
            districtList.add(new TextInfo(i, districtArrayList.get(i).getName(), false));
        }
        ((WheelTextAdapter) wheelView_district.getAdapter()).setData(districtList);

    }

    public void show() {
        popupWindow.showAtLocation(((ViewGroup) ((Activity) context).findViewById(android.R.id.content)).getChildAt(0), Gravity.BOTTOM, 0, 0);
    }

    public void disMiss() {
        popupWindow.dismiss();
    }

    @Override
    public void onClick(View v) {
        Province mCurPro = area.getProvinceArrayList().get(mCurProvince);
        City mCurCy = mCurPro.getCityArrayList().get(mCurCity);
        District mCurdt = mCurCy.getDistrictArrayList().get(mCurDistrict);
        switch (v.getId()) {
            case R.id.btn_wheel_positive:
                if (onAreaBtnClick != null) {
                    onAreaBtnClick.onPositiveClick(mCurPro, mCurCy, mCurdt);
                }
                break;
            case R.id.btn_wheel_negative:
                if (onAreaBtnClick != null) {
                    onAreaBtnClick.onNegativeClick(mCurPro, mCurCy, mCurdt);
                }
                break;
        }
    }

    public interface OnAreaBtnClick {
        void onPositiveClick(Province province, City city, District district);

        void onNegativeClick(Province province, City city, District district);
    }

    public OnAreaBtnClick getOnAreaBtnClick() {
        return onAreaBtnClick;
    }

    public void setOnAreaBtnClick(OnAreaBtnClick onAreaBtnClick) {
        this.onAreaBtnClick = onAreaBtnClick;
    }
}
