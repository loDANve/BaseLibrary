package com.chanxa.wnb.activity.airticket;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.chanxa.wnb.R;
import com.chanxa.wnb.activity.base.WnbBaseActivity;
import com.chanxa.wnb.fragment.airticket.CalendarPriceFragment;
import com.chanxa.wnb.fragment.airticket.InternationalFragment;
import com.chanxa.wnb.fragment.airticket.OneWayFragment;
import com.chanxa.wnb.fragment.airticket.ReturnFragment;
import com.chanxa.wnb.view.ViewPagerCompat;
import com.wtm.library.base.BaseFragment;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class CalendarActivity extends WnbBaseActivity {
    @InjectView(R.id.btn_title_back)
    protected Button btn_title_back;
    @InjectView(R.id.tv_title)
    protected TextView tv_title;
    @InjectView(R.id.btn_oneway)
    protected ImageButton btn_oneway;
    @InjectView(R.id.btn_return)
    protected ImageButton btn_return;
    @InjectView(R.id.btn_international)
    protected ImageButton btn_international;
    @InjectView(R.id.vp_calendar_context)
    protected ViewPagerCompat viewpager_calendar;

    private CalendarPriceFragment calendarPriceFragment;
    private ArrayList<BaseFragment> fragmentList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_main);
    }

    @Override
    public void initView() {
        ButterKnife.inject(this);
        tv_title.setText("日历");
        btn_title_back.setOnClickListener(this);
        btn_oneway.setOnClickListener(this);
        btn_return.setOnClickListener(this);
        btn_international.setOnClickListener(this);

        calendarPriceFragment = CalendarPriceFragment.getInstance();
        fragmentList = new ArrayList<BaseFragment>();
        fragmentList.add(calendarPriceFragment);
        viewpager_calendar.setAdapter(new CalendarFragmentPagerAdapter(getSupportFragmentManager()));
        viewpager_calendar.setOnPageChangeListener(new CalendarPriceOnPageChangeListener());
        super.initView();
    }

    @Override
    public void initData() {
        super.initData();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_title_back:
                finish();
                break;
            case R.id.btn_oneway:
                viewpager_calendar.setCurrentItem(0);
                break;
            case R.id.btn_return:
                viewpager_calendar.setCurrentItem(1);
                break;
            case R.id.btn_international:
                viewpager_calendar.setCurrentItem(2);
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private class CalendarFragmentPagerAdapter extends FragmentPagerAdapter {

        public CalendarFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }

    private class CalendarPriceOnPageChangeListener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}