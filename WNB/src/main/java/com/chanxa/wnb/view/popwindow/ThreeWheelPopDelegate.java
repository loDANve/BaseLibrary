package com.chanxa.wnb.view.popwindow;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.chanxa.wnb.R;
import com.chanxa.wnb.bean.StaffStoreGoods;
import com.chanxa.wnb.bean.StaffStoreGoodsLV1;
import com.chanxa.wnb.bean.StaffStoreGoodsLV2;
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
import com.wtm.library.utils.LogUtils;

import java.util.ArrayList;

/**
 * Created by CHANXA on 2015/1/9.
 */
public class ThreeWheelPopDelegate implements View.OnClickListener {

    private Context context;


    private PopupWindow popupWindow;
    private WheelView wheelView_one;
    private WheelView wheelView_two;
    private WheelView wheelView_three;

    private ArrayList<StaffStoreGoodsLV1> staffStoreGoodsLV1ArrayList;

    ArrayList<TextInfo> provinceList = new ArrayList<TextInfo>();
    ArrayList<TextInfo> cityList = new ArrayList<TextInfo>();
    ArrayList<TextInfo> districtList = new ArrayList<TextInfo>();

    private OnAreaBtnClick onAreaBtnClick;

    int one = 0;
    int two = 0;
    int three = 0;

    public ThreeWheelPopDelegate(Context context, ArrayList<StaffStoreGoodsLV1> staffStoreGoodsLV1ArrayList) {
        this.staffStoreGoodsLV1ArrayList = staffStoreGoodsLV1ArrayList;
        this.context = context;
        initView();
    }


    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_wheel_ymd, null, false);
        int width = DeviceUtils.getScreenW(context);
        popupWindow = ViewInject.getInstance().createWindow(view, width, DeviceUtils.dip2px(context, 200), 0x070000);
        popupWindow.setAnimationStyle(R.style.popupAnimation);

        view.findViewById(R.id.btn_wheel_positive).setOnClickListener(this);
        view.findViewById(R.id.btn_wheel_negative).setOnClickListener(this);

        wheelView_one = (com.chanxa.wnb.view.wheelView.WheelView) view.findViewById(R.id.wheelView_year);
        wheelView_two = (com.chanxa.wnb.view.wheelView.WheelView) view.findViewById(R.id.wheelView_month);
        wheelView_three = (com.chanxa.wnb.view.wheelView.WheelView) view.findViewById(R.id.wheelView_day);

        wheelView_one.setAdapter(new WheelTextAdapter(context));
        wheelView_two.setAdapter(new WheelTextAdapter(context));
        wheelView_three.setAdapter(new WheelTextAdapter(context));

        wheelView_one.setOnItemSelectedListener(new TosAdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(TosAdapterView<?> parent, View view, int position, long id) {
                one = position;
                refreshCityListData(staffStoreGoodsLV1ArrayList.get(one).getStaffStoreGoodsLV2ArrayList());
                two = 0;
                wheelView_two.setSelection(0);
                if (staffStoreGoodsLV1ArrayList.get(one).getStaffStoreGoodsLV2ArrayList() != null) {
                    if (staffStoreGoodsLV1ArrayList.get(one).getStaffStoreGoodsLV2ArrayList().size() > two && staffStoreGoodsLV1ArrayList.get(one).getStaffStoreGoodsLV2ArrayList().get(two) != null) {
                        refreshDistrictListData(staffStoreGoodsLV1ArrayList.get(one).getStaffStoreGoodsLV2ArrayList().get(0).getStaffStoreGoodsArrayList());
                    }
                }
            }

            @Override
            public void onNothingSelected(TosAdapterView<?> parent) {
            }
        });
        wheelView_two.setOnItemSelectedListener(new TosAdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(TosAdapterView<?> parent, View view, int position, long id) {
                two = position;
                three = 0;
                wheelView_three.setSelection(0);
                if (staffStoreGoodsLV1ArrayList.get(one).getStaffStoreGoodsLV2ArrayList() != null) {
                    if (staffStoreGoodsLV1ArrayList.get(one).getStaffStoreGoodsLV2ArrayList().size() > two && staffStoreGoodsLV1ArrayList.get(one).getStaffStoreGoodsLV2ArrayList().get(two) != null) {
                        refreshDistrictListData(staffStoreGoodsLV1ArrayList.get(one).getStaffStoreGoodsLV2ArrayList().get(two).getStaffStoreGoodsArrayList());
                    }
                }

            }

            @Override
            public void onNothingSelected(TosAdapterView<?> parent) {
                districtList.clear();
                ((WheelTextAdapter) wheelView_three.getAdapter()).setData(districtList);
            }
        });

        wheelView_three.setOnItemSelectedListener(new TosAdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(TosAdapterView<?> parent, View view, int position, long id) {
                three = wheelView_three.getSelectedItemPosition();
            }

            @Override
            public void onNothingSelected(TosAdapterView<?> parent) {
            }
        });
        initData();
    }


    private void initData() {
        //ArrayList<StaffStoreGoodsLV2> staffStoreGoodsLV2ArrayList = staffStoreGoodsLV1ArrayList.get(0).getStaffStoreGoodsLV2ArrayList();
        if (staffStoreGoodsLV1ArrayList.size() <= 0) {
            return;
        }
        for (int i = 0; i < staffStoreGoodsLV1ArrayList.size(); i++) {
            StaffStoreGoodsLV1 staffStoreGoodsLV1 = staffStoreGoodsLV1ArrayList.get(i);
            provinceList.add(new TextInfo(i, staffStoreGoodsLV1.getName(), false));
        }
        refreshCityListData(staffStoreGoodsLV1ArrayList.get(0).getStaffStoreGoodsLV2ArrayList());
        if (staffStoreGoodsLV1ArrayList.get(0).getStaffStoreGoodsLV2ArrayList().size() > 0) {
            refreshDistrictListData(staffStoreGoodsLV1ArrayList.get(0).getStaffStoreGoodsLV2ArrayList().get(0).getStaffStoreGoodsArrayList());
        }
        ((WheelTextAdapter) wheelView_one.getAdapter()).setData(provinceList);
        ((WheelTextAdapter) wheelView_two.getAdapter()).setData(cityList);
        ((WheelTextAdapter) wheelView_three.getAdapter()).setData(districtList);

    }

    private void refreshCityListData(ArrayList<StaffStoreGoodsLV2> staffStoreGoodsLV2List) {
        cityList.clear();
        for (int i = 0; i < staffStoreGoodsLV2List.size(); i++) {
            cityList.add(new TextInfo(i, staffStoreGoodsLV2List.get(i).getName(), false));
        }
        ((WheelTextAdapter) wheelView_two.getAdapter()).setData(cityList);
    }

    /**
     * 根据城市刷新对应地区
     */
    private void refreshDistrictListData(ArrayList<StaffStoreGoods> staffStoreGoodses) {
        districtList.clear();
        for (int i = 0; i < staffStoreGoodses.size(); i++) {
            districtList.add(new TextInfo(i, staffStoreGoodses.get(i).getName(), false));
        }
        ((WheelTextAdapter) wheelView_three.getAdapter()).setData(districtList);
    }

    public void show() {
        popupWindow.showAtLocation(((ViewGroup) ((Activity) context).findViewById(android.R.id.content)).getChildAt(0), Gravity.BOTTOM, 0, 0);
    }

    public void disMiss() {
        popupWindow.dismiss();
    }

    @Override
    public void onClick(View v) {
//        Province mCurPro = area.getProvinceArrayList().get(mCurProvince);
//        City mCurCy = mCurPro.getCityArrayList().get(mCurCity);
//        District mCurdt = mCurCy.getDistrictArrayList().get(mCurDistrict);
        String curName = null;
        String curMark = null;
        if (provinceList.size() > 0) {
            curName = staffStoreGoodsLV1ArrayList.get(one).getName();
            curMark = staffStoreGoodsLV1ArrayList.get(one).getMark();
        }
        if (cityList.size() > 0) {
            curName = staffStoreGoodsLV1ArrayList.get(one).getStaffStoreGoodsLV2ArrayList().get(two).getName();
            curMark = staffStoreGoodsLV1ArrayList.get(one).getStaffStoreGoodsLV2ArrayList().get(two).getMark();
        }

        if (districtList.size() > 0) {
            curName = staffStoreGoodsLV1ArrayList.get(one).getStaffStoreGoodsLV2ArrayList().get(two).getStaffStoreGoodsArrayList().get(three).getName();
            curMark = staffStoreGoodsLV1ArrayList.get(one).getStaffStoreGoodsLV2ArrayList().get(two).getStaffStoreGoodsArrayList().get(three).getMark();
        }
        switch (v.getId()) {
            case R.id.btn_wheel_positive:
                if (onAreaBtnClick != null) {
                    onAreaBtnClick.onPositiveClick(curName, curMark);
                }
                break;
            case R.id.btn_wheel_negative:
                if (onAreaBtnClick != null) {
                    onAreaBtnClick.onNegativeClick(curName, curMark);
                }
                break;
        }
    }

    public interface OnAreaBtnClick {
        void onPositiveClick(String curName, String curMark);

        void onNegativeClick(String curName, String curMark);
    }

    public OnAreaBtnClick getOnAreaBtnClick() {
        return onAreaBtnClick;
    }

    public void setOnAreaBtnClick(OnAreaBtnClick onAreaBtnClick) {
        this.onAreaBtnClick = onAreaBtnClick;
    }
}
