package com.chanxa.wnb.activity;

import android.os.Bundle;

import com.chanxa.wnb.R;
import com.chanxa.wnb.activity.base.DefaultTitleActivity;

public class ResetPwdActivity extends DefaultTitleActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pwd);
    }

    @Override
    public String initTitleText() {
        return getString(R.string.setpwd);
    }
}
