package com.chanxa.wnb.activity.base;

import android.view.View;
import android.widget.TextView;

import com.chanxa.wnb.R;

/**
 * Created by CHANXA on 2014/12/13.
 */
public abstract class DefaultTitleActivity extends WnbBaseActivity {
    public void backOnclick(View v) {
        if (R.id.btn_title_back == v.getId()) finish();
    }
    public abstract String initTitleText();
    @Override
    public void initView() {
        super.initView();
        ((TextView)getRootView().findViewById(R.id.tv_title)).setText(initTitleText());
    }
}
