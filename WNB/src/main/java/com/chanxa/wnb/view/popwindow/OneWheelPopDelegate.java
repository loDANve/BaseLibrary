package com.chanxa.wnb.view.popwindow;

import android.app.Activity;
import android.content.Context;
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
public class OneWheelPopDelegate {

    public PopupWindow popupWindow;
    private WheelView wheelView;
    private OnButtonClick onButtonClick;
    private Context context;

    public OneWheelPopDelegate(Context context, String[] data) {
        this.context = context;
        initPop(data);
    }

    public OneWheelPopDelegate(Activity activity) {
        this(activity,null);
    }

    private void initPop(String[] data) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_wheel, null, false);
        int width = DeviceUtils.getScreenW((Activity) context);
        popupWindow = ViewInject.getInstance().createWindow(view, width, DeviceUtils.dip2px(context, 200), 0x070000);
        popupWindow.setAnimationStyle(R.style.popupAnimation);
        wheelView = (WheelView) view.findViewById(R.id.wheelView);
        if (data != null) {
            initWheelView(data);
        }
        view.findViewById(R.id.btn_wheel_positive).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onButtonClick != null) {
                    onButtonClick.onPositivebtnClick(wheelView);
                }
                popupWindow.dismiss();
            }
        });
        view.findViewById(R.id.btn_wheel_negative).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onButtonClick != null) {
                    onButtonClick.onNegativebtnClick(wheelView);
                }
                popupWindow.dismiss();
            }
        });
    }

    public void initWheelView(String[] data) {

        WheelTextAdapter adapter = new WheelTextAdapter(context);
        wheelView.setAdapter(adapter);
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < data.length; i++) {
            TextInfo textInfo = new TextInfo(i, data[i], false);
            arrayList.add(textInfo);
        }
        adapter.setData(arrayList);
        //wheelView.setViewAdapter(new ArrayWheelAdapter<String>(activity, data));
        //wheelView.setCurrentItem(0);
        // wheelView.setCyclic(true);
        //wheelView.setInterpolator(new AnticipateOvershootInterpolator());
    }

    public void show() {
        popupWindow.showAtLocation(((ViewGroup) ((Activity) context).findViewById(android.R.id.content)).getChildAt(0), Gravity.BOTTOM, 0, 0);
    }

    public OnButtonClick getOnButtonClick() {
        return onButtonClick;
    }

    public void setOnButtonClick(OnButtonClick onButtonClick) {
        this.onButtonClick = onButtonClick;
    }

    public interface OnButtonClick {
        void onPositivebtnClick(WheelView wheelView1);

        void onNegativebtnClick(WheelView wheelView1);
    }

    public void dissMiss(){
        popupWindow.dismiss();
    }
}
