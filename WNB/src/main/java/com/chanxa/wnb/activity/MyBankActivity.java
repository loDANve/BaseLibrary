package com.chanxa.wnb.activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.chanxa.wnb.R;
import com.chanxa.wnb.activity.base.WnbBaseActivity;
import com.wtm.library.base.BaseActivity;

public class MyBankActivity extends WnbBaseActivity {
    public void backOnclick(View v) {
        if (R.id.btn_title_back == v.getId()) finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bank);
    }
}
