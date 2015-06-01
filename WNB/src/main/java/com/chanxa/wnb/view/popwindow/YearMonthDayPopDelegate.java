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
import com.chanxa.wnb.view.wheelView.TextInfo;
import com.chanxa.wnb.view.wheelView.TosGallery;
import com.chanxa.wnb.view.wheelView.WheelTextAdapter;
import com.chanxa.wnb.view.wheelView.WheelView;
import com.wtm.library.inject.ViewInject;
import com.wtm.library.utils.DeviceUtils;
import com.wtm.library.utils.LogUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

/**
 * Created by CHANXA on 2014/12/20.
 */
public class YearMonthDayPopDelegate implements  View.OnClickListener {

    private Context context;
    private PopupWindow popupWindow;

    private WheelView wheelView_year;
    private WheelView wheelView_month;
    private WheelView wheelView_day;

    ArrayList<TextInfo> mMonths = new ArrayList<TextInfo>();
    ArrayList<TextInfo> mYears = new ArrayList<TextInfo>();
    ArrayList<TextInfo> mDates = new ArrayList<TextInfo>();
    int mCurDate = 0;
    int mCurMonth = 0;
    int mCurYear = 0;

    public YearMonthDayPopDelegate.onYMDButtonclick getOnYMDButtonclick() {
        return onYMDButtonclick;
    }

    public void setOnYMDButtonclick(YearMonthDayPopDelegate.onYMDButtonclick onYMDButtonclick) {
        this.onYMDButtonclick = onYMDButtonclick;
    }

    private onYMDButtonclick onYMDButtonclick;

    public YearMonthDayPopDelegate(Context context) {
        this.context = context;
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_wheel_ymd, null, false);
        int width = DeviceUtils.getScreenW((android.app.Activity) context);
        popupWindow = ViewInject.getInstance().createWindow(view, width, DeviceUtils.dip2px(context, 200), 0x070000);
        popupWindow.setAnimationStyle(R.style.popupAnimation);

        view.findViewById(R.id.btn_wheel_positive).setOnClickListener(this);
        view.findViewById(R.id.btn_wheel_negative).setOnClickListener(this);

        wheelView_year = (WheelView) view.findViewById(R.id.wheelView_year);
        wheelView_month = (WheelView) view.findViewById(R.id.wheelView_month);
        wheelView_day = (WheelView) view.findViewById(R.id.wheelView_day);

        wheelView_day.setAdapter(new WheelTextAdapter(context));
        wheelView_year.setAdapter(new WheelTextAdapter(context));
        wheelView_month.setAdapter(new WheelTextAdapter(context));
        wheelView_month.setOnEndFlingListener(mListener);
        wheelView_year.setOnEndFlingListener(mListener);
        wheelView_day.setOnEndFlingListener(mListener);
        prepareData();
    }

    public void show() {
        popupWindow.showAtLocation(((ViewGroup) ((Activity)context).findViewById(android.R.id.content)).getChildAt(0), Gravity.BOTTOM, 0, 0);
    }

    @Override
    public void onClick(View v) {
        int year =  Integer.parseInt(mYears.get(wheelView_year.getSelectedItemPosition()).mText);
                //[wheelView_year.getSelectedItemPosition()].substring(0, 4));
        int month = wheelView_month.getSelectedItemPosition()+1;
        int day = wheelView_day.getSelectedItemPosition()+1;
        if (R.id.btn_wheel_positive == v.getId()) {
            if (onYMDButtonclick != null) {
                onYMDButtonclick.onPositivebtnClick(year,month,day);
            }
            popupWindow.dismiss();
            return;
        }
        if (onYMDButtonclick != null) {
            onYMDButtonclick.onNegativebtnClick(year, month, day);
        }
        popupWindow.dismiss();
    }

    public interface onYMDButtonclick{
        void onPositivebtnClick(int year,int month,int day);
        void onNegativebtnClick(int year,int month,int day);
    }

    public void setCurrentYearMonth(String year, String month,String day) {
        int yearIndex = searchIndex(year, mYears);
        int monthIndex = searchIndex(month+"月", mMonths);
        if (day.startsWith("0")) {
            day = day.substring(1, 2);
        }
        int dayIndex = searchIndex(day, mDates);
        wheelView_year.setSelection(yearIndex);
        wheelView_month.setSelection(monthIndex);
        wheelView_day.setSelection(dayIndex);
    }

    private int searchIndex(String data,ArrayList<TextInfo> textInfos) {
        for (int i = 0; i < textInfos.size(); i++) {
            if (textInfos.get(i).mText.equals(data))
                return i;
        }
        return 0;
    }

    private void prepareData() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        int startYear = 1900;
        int endYear = 2030;
        mCurDate = day;
        mCurMonth = month;
        mCurYear = year;
        for (int i = 0; i < 12; i++) {
            mMonths.add(new TextInfo(i, i+1+"月", false));
        }
        for (int i = startYear; i <= endYear; i++) {
            mYears.add(new TextInfo(i, String.valueOf(i), false));
        }
        wheelView_month.setAdapter(new WheelTextAdapter(context));
        wheelView_year.setAdapter(new WheelTextAdapter(context));
        ((WheelTextAdapter) wheelView_month.getAdapter()).setData(mMonths);
        ((WheelTextAdapter) wheelView_year.getAdapter()).setData(mYears);
        prepareDayData(year, month, day);
    }

    private static final String[] MONTH_NAME = { "January", "February", "March", "April", "May", "June", "July",
            "August", "September", "October", "November", "December", };
    private static final int[] DAYS_PER_MONTH = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

    private void prepareDayData(int year, int month, int curDate) {
        mDates.clear();

        int days = DAYS_PER_MONTH[month];

        // The February.
        if (1 == month) {
            days = isLeapYear(year) ? 29 : 28;
        }

        for (int i = 1; i <= days; ++i) {
            mDates.add(new TextInfo(i, String.valueOf(i), false));
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
            prepareDayData(year, mCurMonth, mCurDate);
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
