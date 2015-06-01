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
import com.chanxa.wnb.view.wheel.OnWheelChangedListener;
import com.chanxa.wnb.view.wheel.OnWheelScrollListener;
import com.chanxa.wnb.view.wheel.WheelView;
import com.chanxa.wnb.view.wheel.adapter.ArrayWheelAdapter;
import com.chanxa.wnb.view.wheel.adapter.NumericWheelAdapter;
import com.wtm.library.inject.ViewInject;
import com.wtm.library.utils.DeviceUtils;

import java.util.Arrays;

/**
 * Created by CHANXA on 2014/12/16.
 */
public class DateWheelPopDelegate implements OnWheelChangedListener,View.OnClickListener{

    private Context context;
    private String[] monthArr = null;
    private String[] yearArr = null;
    private String[] dayArr = null;
    private String[] hourArr = null;
    private String[] minuteArr = null;

    private PopupWindow popupWindow;

    private WheelView wheelView_year;
    private WheelView wheelView_month;
    private WheelView wheelView_day;
    private WheelView wheelView_hour;
    private WheelView wheelView_minute;

    private onDateButtonclick onButtonclick;

    public DateWheelPopDelegate(Context context) {
        this.context = context;
        initData();
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_wheel_date, null, false);
        int width = DeviceUtils.getScreenW((android.app.Activity) context);
        popupWindow = ViewInject.getInstance().createWindow(view, width, DeviceUtils.dip2px(context, 350), 0x070000);
        popupWindow.setAnimationStyle(R.style.popupAnimation);

        view.findViewById(R.id.btn_wheel_positive).setOnClickListener(this);
        view.findViewById(R.id.btn_wheel_negative).setOnClickListener(this);

        wheelView_year = (WheelView) view.findViewById(R.id.wheelView_year);
        wheelView_month = (WheelView) view.findViewById(R.id.wheelView_month);
        wheelView_month.addChangingListener(this);
        wheelView_year.addChangingListener(this);

        wheelView_day = (WheelView) view.findViewById(R.id.wheelView_day);
        wheelView_hour = (WheelView) view.findViewById(R.id.wheelView_hour);
        wheelView_minute = (WheelView) view.findViewById(R.id.wheelView_minute);
        initWheelView(wheelView_year, yearArr);
        initWheelView(wheelView_month, monthArr);
        initWheelView(wheelView_day, dayArr);
        initWheelView(wheelView_hour, hourArr);
        initWheelView(wheelView_minute, minuteArr);
    }

    private void initData() {
        monthArr = context.getResources().getStringArray(R.array.month);
        yearArr = context.getResources().getStringArray(R.array.year);
        dayArr = new String[31];
        for (int i = 0; i < dayArr.length; i++) {
            dayArr[i] = i+1 + "日";
        }
        hourArr = new String[24];
        for (int i = 0; i < hourArr.length; i++) {
            hourArr[i] = i + "时";
        }
        minuteArr = new String[60];
        for (int i = 0; i < minuteArr.length; i++) {
            minuteArr[i] = i + "分";
        }
    }

    public void setHourData(String[] data){
        initWheelView(wheelView_hour,data);
    }
    public void setMinuteData(String[] data){
        initWheelView(wheelView_minute,data);
    }

    public void initWheelView(WheelView wheelView, String[] data) {
        wheelView.setViewAdapter(new ArrayWheelAdapter<String>(context, data));
        wheelView.setCurrentItem(0);
        wheelView.setCyclic(true);
        wheelView.setInterpolator(new AnticipateOvershootInterpolator());
    }

    public void show() {
        popupWindow.showAtLocation(((ViewGroup) ((Activity)context).findViewById(android.R.id.content)).getChildAt(0), Gravity.BOTTOM, 0, 0);
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        int yearNumber =Integer.parseInt(yearArr[wheelView_year.getCurrentItem()].substring(0, 4));
        int monthNumber = wheelView_month.getCurrentItem()+1;
        if (monthNumber==4 ||monthNumber==6||monthNumber==9||monthNumber==11) {
            wheelView_day.setViewAdapter(new ArrayWheelAdapter<String>(context, Arrays.copyOfRange(dayArr, 0, dayArr.length - 1)));
            return;
        }
        if (monthNumber == 2) {
            if ((yearNumber % 4 == 0 && yearNumber % 100 != 0) || yearNumber % 400 == 0) {
                //是闰年
                wheelView_day.setViewAdapter(new ArrayWheelAdapter<String>(context, Arrays.copyOfRange(dayArr, 0, dayArr.length - 2)));
            } else {
                wheelView_day.setViewAdapter(new ArrayWheelAdapter<String>(context, Arrays.copyOfRange(dayArr, 0, dayArr.length - 3)));
            }
            return;
        }
        wheelView_day.setViewAdapter(new ArrayWheelAdapter<String>(context, dayArr));
    }

    @Override
    public void onClick(View v) {
        int year =  Integer.parseInt(yearArr[wheelView_year.getCurrentItem()].substring(0, 4));
        int month = wheelView_month.getCurrentItem()+1;
        int day = wheelView_day.getCurrentItem()+1;
        int hour = wheelView_hour.getCurrentItem();
        int minute = wheelView_minute.getCurrentItem();
        if (R.id.btn_wheel_positive == v.getId()) {
            if (onButtonclick != null) {
                onButtonclick.onPositivebtnClick(year,month,day,hour,minute);
            }
            popupWindow.dismiss();
            return;
        }
        if (onButtonclick != null) {
            onButtonclick.onNegativebtnClick(year, month, day, hour, minute);
        }
        popupWindow.dismiss();
    }

    public onDateButtonclick getOnButtonclick() {
        return onButtonclick;
    }

    public void setOnButtonclick(onDateButtonclick onButtonclick) {
        this.onButtonclick = onButtonclick;
    }

    public interface onDateButtonclick{
        void onPositivebtnClick(int year,int month,int day,int hour,int minute);
        void onNegativebtnClick(int year,int month,int day,int hour,int minute);
    }
}
