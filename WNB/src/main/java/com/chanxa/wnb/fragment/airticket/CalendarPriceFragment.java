package com.chanxa.wnb.fragment.airticket;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.chanxa.wnb.R;
import com.chanxa.wnb.view.ProgressDialogBuilder;
import com.chanxa.wnb.view.calendar.CalendarCellDecorator;
import com.chanxa.wnb.view.calendar.CalendarPickerView;
import com.wtm.library.base.BaseFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 *
 */
public class CalendarPriceFragment extends BaseFragment implements View.OnClickListener {
    @InjectView(R.id.calendar_view)
    protected CalendarPickerView calendar;
    private ProgressDialog progressDialog;
    private static CalendarPriceFragment calendarPriceFragment;

    public CalendarPriceFragment() {

    }

    public static CalendarPriceFragment getInstance() {
        if (calendarPriceFragment == null) {
            calendarPriceFragment = new CalendarPriceFragment();
        }
        return calendarPriceFragment;
    }

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        progressDialog = ProgressDialogBuilder.builderDialog(getActivity());
        return inflater.inflate(R.layout.fragment_airticket_calendar_price, null, false);
    }

    @Override
    public void initView() {
        super.initView();
        ButterKnife.inject(this, rootView);
        final Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);

        final Calendar lastYear = Calendar.getInstance();
        lastYear.add(Calendar.YEAR, -1);

        Calendar today = Calendar.getInstance();
        ArrayList<Date> dates = new ArrayList<Date>();
        today.add(Calendar.DATE, 3);
        dates.add(today.getTime());
        today.add(Calendar.DATE, 5);
        dates.add(today.getTime());
        calendar.setDecorators(Collections.<CalendarCellDecorator>emptyList());
        calendar.init(new Date(), nextYear.getTime()) //
                .inMode(CalendarPickerView.SelectionMode.RANGE) //
                .withSelectedDates(dates);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.rl_date:

                break;
        }
        startActivity(intent);
    }
}