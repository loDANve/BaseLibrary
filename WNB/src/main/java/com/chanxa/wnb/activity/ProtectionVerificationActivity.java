package com.chanxa.wnb.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.chanxa.wnb.R;
import com.chanxa.wnb.activity.base.DefaultTitleActivity;

public class ProtectionVerificationActivity extends DefaultTitleActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_protection_verification);
    }

    @Override
    public String initTitleText() {
        return getString(R.string.protectionVerification);
    }

    @Override
    public void initView() {
        super.initView();
        findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProtectionVerificationActivity.this,ResetPwdActivity.class));
            }
        });
    }
}
