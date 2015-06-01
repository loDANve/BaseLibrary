package com.chanxa.wnb.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.chanxa.wnb.R;
import com.chanxa.wnb.activity.base.DefaultTitleActivity;

public class SafetyCenterActivity extends DefaultTitleActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safety_center);
    }

    @Override
    public String initTitleText() {
        return getString(R.string.safetycenter);
    }

    @Override
    public void initView() {
        super.initView();
        findViewById(R.id.container_changePwd).setOnClickListener(this);
        findViewById(R.id.container_goBackPwd).setOnClickListener(this);
        //findViewById(R.id.container_setUpProtectionVerification).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.container_changePwd:
                intent.setClass(this, ChangePwdActivity.class);
                break;
            case R.id.container_goBackPwd:
                intent.setClass(this, MemberShortMessageVerificationActivity.class);
                break;
           /* case R.id.container_setUpProtectionVerification:
                intent.setClass(this, SetUpProtectionVerificationActivity.class);
                break;*/
        }
        startActivity(intent);
    }
}
