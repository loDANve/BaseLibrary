package com.chanxa.wnb.activity.staff;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chanxa.wnb.R;
import com.chanxa.wnb.activity.base.DefaultTitleActivity;
import com.chanxa.wnb.config.WnbApplication;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class StaffDataActivity extends DefaultTitleActivity {


    @InjectView(R.id.tv_staff_camera_iscan)
    TextView mTvStaffCameraIscan;

    @Override
    public String initTitleText() {
        return "个人信息";
    }

    @InjectView(R.id.container_staff_changeData)
    RelativeLayout mContainerStaffChangeData;
    @InjectView(R.id.container_staff_Identity_Authentication)
    RelativeLayout mContainerStaffIdentityAuthentication;
    @InjectView(R.id.container_staff_changePwd)
    RelativeLayout mContainerStaffChangePwd;
    @InjectView(R.id.tv_staff_name)
    TextView mTvStaffName;
    @InjectView(R.id.tv_staff_number)
    TextView mTvStaffNumber;
    private WnbApplication wnbApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_data);
    }

    @Override
    public void initView() {
        ButterKnife.inject(this);
        super.initView();
        mContainerStaffChangeData.setOnClickListener(this);
        mContainerStaffIdentityAuthentication.setOnClickListener(this);
        mContainerStaffChangePwd.setOnClickListener(this);
        wnbApplication = WnbApplication.getInstance();
        refreshView();
    }

    private void refreshView() {
        mTvStaffName.setText(wnbApplication.getStaff().getName() + "");
        mTvStaffNumber.setText(wnbApplication.getStaff().getPhone());
        if (wnbApplication.getStaff().getPersonIDIsExist().equals("1")) {
            mTvStaffCameraIscan.setVisibility(View.VISIBLE);
        } else {
            mTvStaffCameraIscan.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.container_staff_changeData:
                intent.setClass(this, StaffChangeDataActivity.class);
                break;
            case R.id.container_staff_Identity_Authentication:
                if (mTvStaffCameraIscan.getVisibility() == View.VISIBLE) {
                    return;
                }
                intent.setClass(this, StaffIdentityActivity.class);
                break;
            case R.id.container_staff_changePwd:
                intent.setClass(this, StaffChangePwdActivity.class);
                break;
        }
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshView();
    }
}
