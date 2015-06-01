package com.chanxa.wnb.view.popwindow;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.chanxa.wnb.R;
import com.chanxa.wnb.bean.onLineShop.Distribution;
import com.chanxa.wnb.bean.onLineShop.Logistics;
import com.chanxa.wnb.view.wheelView.TextInfo;
import com.chanxa.wnb.view.wheelView.TosAdapterView;
import com.chanxa.wnb.view.wheelView.WheelTextAdapter;
import com.chanxa.wnb.view.wheelView.WheelView;
import com.wtm.library.inject.ViewInject;
import com.wtm.library.utils.DeviceUtils;

import java.util.ArrayList;

/**
 * Created by CHANXA on 2014/12/16.
 */
public class DistributionWheelPopDelegate {

    public PopupWindow popupWindow;
    private WheelView wheelView1;
    private WheelView wheelView2;
    private Activity activity;
    private OnButtonClick onButtonClick;
    private ArrayList<Distribution> data;
    private String curMoney = "";
    private String[] data1 = {};
    private String[] data2 = {};

    public DistributionWheelPopDelegate(Activity activity, ArrayList<Distribution> data) {
        this.activity = activity;
        this.data = data;
        initData1();
        initPop();
    }

    private void initData1() {
        data1 = new String[data.size()];
        for (int i = 0; i < data.size(); i++) {
            Distribution distribution = data.get(i);
            if ("null".equals(distribution.getProvince()) || distribution.getProvince() == null || "".equals(distribution.getProvince())) {
                data1[i] = "--";
                continue;
            }
            if ("null".equals(distribution.getCity()) || distribution.getCity() == null || "".equals(distribution.getCity())) {
                data1[i] = distribution.getProvince();
                continue;
            }
            if ("null".equals(distribution.getDistrict()) || distribution.getDistrict() == null || "".equals(distribution.getDistrict())) {
                data1[i] = distribution.getProvince() + distribution.getCity();
                continue;
            }
            data1[i] = distribution.getProvince() + distribution.getCity() + distribution.getDistrict();
        }
        if (data.size() > 0) {
            initData2(0);
        }
    }

    private void initData2(int index) {
        Distribution distribution = data.get(index);
        data2 = new String[distribution.getLogisticsArrayList().size()];
        for (int i = 0; i < data2.length; i++) {
            Logistics logistics = distribution.getLogisticsArrayList().get(i);
            data2[i] = logistics.getType();
        }
    }

    private void initPop() {
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_wheel_area, null, false);
        int width = DeviceUtils.getScreenW(activity);
        popupWindow = ViewInject.getInstance().createWindow(view, width, DeviceUtils.dip2px(activity, 200), 0x070000);
        popupWindow.setAnimationStyle(R.style.popupAnimation);
        wheelView1 = (WheelView) view.findViewById(R.id.wheelView1);
        wheelView2 = (WheelView) view.findViewById(R.id.wheelView2);
        initWheelView(wheelView1, data1);
        initWheelView(wheelView2, data2);
        view.findViewById(R.id.btn_wheel_positive).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onButtonClick != null) {
                    if (data.size() > 0 && wheelView1.getSelectedItemPosition() >=0 && wheelView1.getSelectedItemPosition() < data.size()) {
                        if (data.get(wheelView1.getSelectedItemPosition()).getLogisticsArrayList().size() > 0 && data.get(wheelView1.getSelectedItemPosition()).getLogisticsArrayList().size() > wheelView2.getSelectedItemPosition()) {
                            if (wheelView2.getSelectedItemPosition() >=0) {
                                curMoney = data.get(wheelView1.getSelectedItemPosition()).getLogisticsArrayList().get(wheelView2.getSelectedItemPosition()).getMoney();
                                onButtonClick.onPositivebtnClick(wheelView1, wheelView2, curMoney);
                            }
                        }

                    }
                }
                popupWindow.dismiss();
            }
        });
        view.findViewById(R.id.btn_wheel_negative).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onButtonClick != null) {
//                    curMoney = data.get(wheelView1.getSelectedItemPosition()).getLogisticsArrayList().get(wheelView2.getSelectedItemPosition()).getMoney();
//                    onButtonClick.onNegativebtnClick(wheelView1, wheelView2, curMoney);
                    if (data.size() > 0 && wheelView1.getSelectedItemPosition() > 0 && wheelView1.getSelectedItemPosition() < data.size()) {
                        if (data.get(wheelView1.getSelectedItemPosition()).getLogisticsArrayList().size() > 0 && data.get(wheelView1.getSelectedItemPosition()).getLogisticsArrayList().size() < wheelView2.getSelectedItemPosition()) {
                            if (wheelView2.getSelectedItemPosition() > 0) {
                                curMoney = data.get(wheelView1.getSelectedItemPosition()).getLogisticsArrayList().get(wheelView2.getSelectedItemPosition()).getMoney();
                                onButtonClick.onPositivebtnClick(wheelView1, wheelView2, curMoney);
                            }
                        }

                    }
                }
                popupWindow.dismiss();
            }
        });
        wheelView1.setOnItemSelectedListener(new TosAdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(TosAdapterView<?> parent, View view, int position, long id) {
                initData2(position);
                initWheelView(wheelView2, data2);
            }

            @Override
            public void onNothingSelected(TosAdapterView<?> parent) {

            }
        });
    }

    public void initWheelView(WheelView wheelView, String[] data) {

        WheelTextAdapter adapter = new WheelTextAdapter(activity);
        wheelView.setAdapter(adapter);
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < data.length; i++) {
            TextInfo textInfo = new TextInfo(i, data[i], false);
            arrayList.add(textInfo);
        }
        adapter.setData(arrayList);
    }

    public void show() {
        popupWindow.showAtLocation(((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0), Gravity.BOTTOM, 0, 0);
    }

    public void setWheelisCyclic(boolean b) {
        wheelView1.setScrollCycle(b);
        wheelView2.setScrollCycle(b);
    }

    public interface OnButtonClick {
        void onPositivebtnClick(WheelView wheelView1, WheelView wheelView2, String curMoney);

        void onNegativebtnClick(WheelView wheelView1, WheelView wheelView2, String curMoney);
    }

    public OnButtonClick getOnButtonClick() {
        return onButtonClick;
    }

    public void setOnButtonClick(OnButtonClick onButtonClick) {
        this.onButtonClick = onButtonClick;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }
}
