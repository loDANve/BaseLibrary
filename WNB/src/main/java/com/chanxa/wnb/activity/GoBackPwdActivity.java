package com.chanxa.wnb.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.chanxa.wnb.R;
import com.chanxa.wnb.activity.base.DefaultTitleActivity;

import static android.view.View.OnClickListener;

public class GoBackPwdActivity extends DefaultTitleActivity implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_back_pwd);
    }

    @Override
    public String initTitleText() {
        return getString(R.string.gobackpwd);
    }

    @Override
    public void initView() {
        super.initView();
        findViewById(R.id.container_shortMessageVerification).setOnClickListener(this);
        findViewById(R.id.container_protectionVerification).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.container_shortMessageVerification:
                intent.setClass(this,MemberShortMessageVerificationActivity.class);
                break;
            case R.id.container_protectionVerification:
                intent.setClass(this,ProtectionVerificationActivity.class);
                break;
        }
        startActivity(intent);
    }
}
