package com.chanxa.wnb.activity.staff;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.chanxa.wnb.R;
import com.chanxa.wnb.fragment.staff.CommissionFragment;
import com.chanxa.wnb.fragment.staff.DrawingFragment;
import com.wtm.library.base.BaseActivity;


public class StaffLookRecordActivity extends BaseActivity {
    ViewPager viewpager_staff_lookRecord;
    ToggleButton tog_staffRecordtype;
    private Fragment[] fragments;
    private DrawingFragment drawingFragment;
    private CommissionFragment commissionFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_look_record);
    }

    @Override
    public void initView() {
        super.initView();
        fragments = new Fragment[2];
        commissionFragment = new CommissionFragment();
        drawingFragment = new DrawingFragment();
        fragments[0] = drawingFragment;
        fragments[1] = commissionFragment;
        tog_staffRecordtype = (ToggleButton) findViewById(R.id.tog_staffRecordtype);
        viewpager_staff_lookRecord = (ViewPager) findViewById(R.id.viewpager_staff_lookRecord);
        viewpager_staff_lookRecord.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments[position];
            }

            @Override
            public int getCount() {
                return fragments.length;
            }
        });
        viewpager_staff_lookRecord.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tog_staffRecordtype.setChecked(position == 0);
                if (position == 0) {
                    drawingFragment.loadData();
                } else {
                    commissionFragment.loadData();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tog_staffRecordtype.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    viewpager_staff_lookRecord.setCurrentItem(0);
                    return;
                }
                viewpager_staff_lookRecord.setCurrentItem(1);
            }
        });
        viewpager_staff_lookRecord.setCurrentItem(0);
        drawingFragment.loadData();
    }

    public void backOnclick(View v) {
        if (R.id.btn_title_back == v.getId()) finish();
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void initData() {
        super.initData();


    }
}
