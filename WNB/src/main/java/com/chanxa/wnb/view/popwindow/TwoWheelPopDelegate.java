package com.chanxa.wnb.view.popwindow;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.PopupWindow;

import com.chanxa.wnb.R;
import com.chanxa.wnb.view.wheel.adapter.ArrayWheelAdapter;
import com.chanxa.wnb.view.wheelView.TextInfo;
import com.chanxa.wnb.view.wheelView.WheelTextAdapter;
import com.chanxa.wnb.view.wheelView.WheelView;
import com.wtm.library.inject.ViewInject;
import com.wtm.library.utils.DeviceUtils;

import java.util.ArrayList;

/**
 * Created by CHANXA on 2014/12/16.
 */
public class TwoWheelPopDelegate {

    public PopupWindow popupWindow;
    private WheelView wheelView1;
    private WheelView wheelView2;
    private Activity activity;
    private OnButtonClick onButtonClick;

    public TwoWheelPopDelegate(Activity activity, String[] data1, String[] data2) {
        this.activity = activity;
        initPop(data1, data2);
    }

    private void initPop(String[] data1, String[] data2) {
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
                    onButtonClick.onPositivebtnClick(wheelView1,wheelView2);
                }
                popupWindow.dismiss();
            }
        });
        view.findViewById(R.id.btn_wheel_negative).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onButtonClick != null) {
                    onButtonClick.onNegativebtnClick(wheelView1,wheelView2);
                }
                popupWindow.dismiss();
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
        /*wheelView.setViewAdapter(new ArrayWheelAdapter<String>(activity, data));
        wheelView.setCurrentItem(0);
        //wheelView.setCyclic(true);
        wheelView.setInterpolator(new AnticipateOvershootInterpolator());*/
    }

    public void show() {
        popupWindow.showAtLocation(((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0), Gravity.BOTTOM, 0, 0);
    }

    public void setWheelisCyclic(boolean b) {
        wheelView1.setScrollCycle(b);
        wheelView2.setScrollCycle(b);
    }

    public interface OnButtonClick {
        void onPositivebtnClick(WheelView wheelView1,WheelView wheelView2);

        void onNegativebtnClick(WheelView wheelView1,WheelView wheelView2);
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
