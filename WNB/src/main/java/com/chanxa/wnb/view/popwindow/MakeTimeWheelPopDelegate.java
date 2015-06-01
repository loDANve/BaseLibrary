package com.chanxa.wnb.view.popwindow;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chanxa.wnb.R;
import com.chanxa.wnb.view.wheelView.TextInfo;
import com.chanxa.wnb.view.wheelView.TosGallery;
import com.chanxa.wnb.view.wheelView.WheelTextAdapter;
import com.chanxa.wnb.view.wheelView.WheelView;
import com.wtm.library.inject.ViewInject;
import com.wtm.library.utils.DeviceUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

/**
 * Created by CHANXA on 2014/12/16.
 */
public class MakeTimeWheelPopDelegate implements View.OnClickListener {

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


    ArrayList<TextInfo> mMonths = new ArrayList<TextInfo>();
    ArrayList<TextInfo> mYears = new ArrayList<TextInfo>();
    ArrayList<TextInfo> mDates = new ArrayList<TextInfo>();
    int mCurDate = 0;
    int mCurMonth = 0;
    int mCurYear = 0;

    public MakeTimeWheelPopDelegate(Context context) {
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


        wheelView_day = (WheelView) view.findViewById(R.id.wheelView_day);
        wheelView_hour = (WheelView) view.findViewById(R.id.wheelView_hour);
        wheelView_minute = (WheelView) view.findViewById(R.id.wheelView_minute);
        wheelView_day.setAdapter(new WheelTextAdapter(context));
        wheelView_hour.setAdapter(new WheelTextAdapter(context));
        wheelView_minute.setAdapter(new WheelTextAdapter(context));

        wheelView_month.setOnEndFlingListener(mListener);
        wheelView_year.setOnEndFlingListener(mListener);
        wheelView_day.setOnEndFlingListener(mListener);

        initWheelView(wheelView_hour, hourArr);
        initWheelView(wheelView_minute, minuteArr);
        prepareData();
    }

    private void initData() {
        monthArr = context.getResources().getStringArray(R.array.month);
        yearArr = context.getResources().getStringArray(R.array.year);
        dayArr = new String[31];
        for (int i = 0; i < dayArr.length; i++) {
            dayArr[i] = i + 1 + "日";
        }
        ArrayList<String> hourList = new ArrayList<>();
        // hourList.add("开始时间");
        for (int i = 0; i < 24; i++) {
            if (i < 10) {
                hourList.add("0" + i + ":00");
                hourList.add("0" + i + ":30");
            } else {
                hourList.add(i + ":00");
                hourList.add(i + ":30");
            }
        }
        hourArr = hourList.toArray(new String[hourList.size()]);
        //hourList.set(0, "结束时间");
        minuteArr = hourList.toArray(new String[hourList.size()]);
    }

    public void setHourData(String[] data) {
        initWheelView(wheelView_hour, data);
    }

    public void setMinuteData(String[] data) {
        initWheelView(wheelView_minute, data);
    }

    public void initWheelView(WheelView wheelView, String[] data) {

        WheelTextAdapter adapter = new WheelTextAdapter(context);
        wheelView.setAdapter(adapter);
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < data.length; i++) {
            TextInfo textInfo = new TextInfo(i, data[i], false);
            arrayList.add(textInfo);
        }
        adapter.setData(arrayList);
        wheelView.setSoundEffectsEnabled(true);
    }

    public void show() {
        popupWindow.showAtLocation(((ViewGroup) ((Activity) context).findViewById(android.R.id.content)).getChildAt(0), Gravity.BOTTOM, 0, 0);
    }

    @Override
    public void onClick(View v) {
        String year = yearArr[wheelView_year.getSelectedItemPosition()].substring(0, 4);
        int month = wheelView_month.getSelectedItemPosition() + 1;
        int day = wheelView_day.getSelectedItemPosition() + 1;
        int hour = wheelView_hour.getSelectedItemPosition();
        int minute = wheelView_minute.getSelectedItemPosition();
        if (R.id.btn_wheel_positive == v.getId()) {
            if (onButtonclick != null) {
                onButtonclick.onPositivebtnClick(year, month + "", day + "", hourArr[hour], minuteArr[minute]);
            }
            popupWindow.dismiss();
            return;
        }
        if (onButtonclick != null) {
            onButtonclick.onNegativebtnClick(year, month + "", day + "", hourArr[hour], minuteArr[minute]);
        }
        popupWindow.dismiss();
    }

    public onDateButtonclick getOnButtonclick() {
        return onButtonclick;
    }

    public void setOnButtonclick(onDateButtonclick onButtonclick) {
        this.onButtonclick = onButtonclick;
    }

    public interface onDateButtonclick {
        void onPositivebtnClick(String year, String month, String day, String hour, String minute);

        void onNegativebtnClick(String year, String month, String day, String hour, String minute);
    }

    public void setCurrentDate(String year, String month, String day, String hourStr, String minuteStr) {
        int currentYearItem = searchIndex(year, yearArr);
        int currentMonthItem = searchIndex(month, monthArr);
        int currentDayItem = searchIndex(day, dayArr);
        int minute = Integer.parseInt(minuteStr);
        int hour = Integer.parseInt(hourStr);
        if (minute > 0 && minute < 30) {
            minuteStr = "30";
        } else if (minute > 30 && minute < 60) {
            hour++;
            minuteStr = "00";
        }
        hourStr = hour + "";
        String hm = hourStr + ":" + minuteStr;
        int currentHourItem = searchIndex(hm, hourArr);
        wheelView_year.setSelection(currentYearItem);
        wheelView_month.setSelection(currentMonthItem);
        wheelView_day.setSelection(currentDayItem);
        wheelView_hour.setSelection(currentHourItem);
        wheelView_minute.setSelection(currentHourItem + 1);
    }

    private int searchIndex(String data, String[] array) {
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(data))
                return i;
        }
        return 0;
    }


    private void prepareData() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        int startYear = 2014;
        int endYear = 2030;

        mCurDate = day;
        mCurMonth = month;
        mCurYear = year;

        for (int i = 0; i < 12; i++) {
            mMonths.add(new TextInfo(i, i + 1 + "月", (i == month)));
        }

        for (int i = startYear; i <= endYear; i++) {
            mYears.add(new TextInfo(i, String.valueOf(i), (i == year)));
        }
        wheelView_month.setAdapter(new WheelTextAdapter(context));
        wheelView_year.setAdapter(new WheelTextAdapter(context));
        ((WheelTextAdapter) wheelView_month.getAdapter()).setData(mMonths);
        ((WheelTextAdapter) wheelView_year.getAdapter()).setData(mYears);
        prepareDayData(year, month, day);
    }

    private static final String[] MONTH_NAME = {"January", "February", "March", "April", "May", "June", "July",
            "August", "September", "October", "November", "December",};
    private static final int[] DAYS_PER_MONTH = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    private void prepareDayData(int year, int month, int curDate) {
        mDates.clear();

        int days = DAYS_PER_MONTH[month];

        if (1 == month) {
            days = isLeapYear(year) ? 29 : 28;
        }

        for (int i = 1; i <= days; ++i) {
            mDates.add(new TextInfo(i, String.valueOf(i), (i == curDate)));
        }
        ((WheelTextAdapter) wheelView_day.getAdapter()).setData(mDates);
    }

    private boolean isLeapYear(int year) {
        return ((0 == year % 4) && (0 != year % 100) || (0 == year % 400));
    }

    private TosGallery.OnEndFlingListener mListener = new TosGallery.OnEndFlingListener() {
        @Override
        public void onEndFling(TosGallery v) {
            int pos = v.getSelectedItemPosition();

            if (v == wheelView_day) {
                TextInfo info = mDates.get(pos);
                setDate(info.mIndex);
            } else if (v == wheelView_month) {
                TextInfo info = mMonths.get(pos);
                setMonth(info.mIndex);
            } else if (v == wheelView_year) {
                TextInfo info = mYears.get(pos);
                setYear(info.mIndex);
            }
        }
    };

    private void setDate(int date) {
        if (date != mCurDate) {
            mCurDate = date;
        }
    }

    private void setYear(int year) {
        if (year != mCurYear) {
            mCurYear = year;
        }
    }

    private void setMonth(int month) {
        if (month != mCurMonth) {
            mCurMonth = month;

            Calendar calendar = Calendar.getInstance();
            int date = calendar.get(Calendar.DATE);
            prepareDayData(mCurYear, month, date);
        }
    }

}
